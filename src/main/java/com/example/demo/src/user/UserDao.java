package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {


    private JdbcTemplate jdbcTemplate;

    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public User postSignIn(PostSignInReq postSignInReq) {
        String insertUserQuery = "insert into User (membershipIdx, ID, PW, userName, phone, birthday) values(?, ?, ?, ?, ?, ?)";
        String insertPayQuery = "insert into Payment (userIdx, payType, payNumber, payDesc, isSub) values(?, ?, ?, ?, ?);";
        String updatePayIdxQuery = "update User set paymentIdx = ? where userIdx = ?";
        String getLastInsertedUserQuery = "select max(userIdx) from User";
        String getLastInsertedPayQuery = "select max(paymentIdx) from Payment";
        String getUserInfoQuery = "select userIdx, ID, PW, userName from User where userIdx = ?";

        Object [] insertUserParams = new Object[] {
                postSignInReq.getMembershipIdx(),
                postSignInReq.getId(),
                postSignInReq.getPw(),
                postSignInReq.getUserName(),
                postSignInReq.getPhoneNum(),
                postSignInReq.getBirthday()
        };
        //초기 유저정보 삽입
        this.jdbcTemplate.update(insertUserQuery, insertUserParams);
        //방금 삽입한 유저 인덱스 가져오기
        int lastInsertedIdx = this.jdbcTemplate.queryForObject(getLastInsertedUserQuery, int.class);

        Object [] insertPayParams = new Object[] {
                lastInsertedIdx,
                postSignInReq.getPayType(),
                postSignInReq.getPayNumber(),
                postSignInReq.getPayDesc(),
                postSignInReq.getIsSub()
        };

        //결재정보 삽입
        this.jdbcTemplate.update(insertPayQuery, insertPayParams);
        //방금 삽입한 지불 인덱스 가져오기
        int lastInsertedPayIdx = this.jdbcTemplate.queryForObject(getLastInsertedPayQuery, int.class);

        Object [] updatePayIdxParams = new Object[] {
                lastInsertedPayIdx,
                lastInsertedIdx
        };

        //유저와 지불 수단 연결
        this.jdbcTemplate.update(updatePayIdxQuery, updatePayIdxParams);

        //유저 정보 리턴
        return this.jdbcTemplate.queryForObject(getUserInfoQuery, (rs, rowNum) -> new User(
                rs.getInt("userIdx"),
                rs.getString("ID"),
                rs.getString("PW"),
                rs.getString("userName")
        ), lastInsertedIdx);
    }

    public String deleteUser(int userIdx) {
        String deleteUserQuery = "update User set staus = 'INACTIVE' where userIdx = ?;";
        String deleteProfileQuery = "update Profile set staus = 'INACTIVE' where userIdx = ?;";
        String deletePaymentQuery = "update Payment set staus = 'INACTIVE' where userIdx = ?;";

        this.jdbcTemplate.update(deleteUserQuery, userIdx);
        this.jdbcTemplate.update(deleteProfileQuery, userIdx);
        this.jdbcTemplate.update(deletePaymentQuery, userIdx);

        return "삭제에 성공하였습니다.";
    }

    public String patchPlan(int userIdx, int planIdx) {
        String changePlanQuery = "update User set membershipIdx = ? where userIdx = ?";

        Object [] changePlanParams = new Object[] {
                planIdx,
                userIdx
        };

        this.jdbcTemplate.update(changePlanQuery, changePlanParams);

        return "맴버십 변경에 성공하였습니다.";
    }

    public GetUserInfoRes getUserInfo(int userIdx) {
        String getUserInfoQuery =
                "select T.*,M.membershipName from\n" +
                "(select U.*, P.payType, P.payNumber, P.lastPayDate from\n" +
                "(select userIdx, ID, PW, phone from User where userIdx = ?) as U\n" +
                "left join\n" +
                "(select userIdx, payType, payNumber, lastPayDate from Payment where userIdx = ?) as P on U.userIdx = P.userIdx) as T\n" +
                "left join\n" +
                "(select (select userIdx from User where userIdx = ?) as userIdx, membershipName from Membership\n" +
                "WHERE membershipIdx = (select membershipIdx from User where userIdx = ?)) as M on T.userIdx = M.userIdx;";

        Object [] getUserInfoParams = new Object[] {userIdx, userIdx, userIdx, userIdx};

        return this.jdbcTemplate.queryForObject(getUserInfoQuery, (rs, rowNum) ->
                new GetUserInfoRes(
                        rs.getInt("userIdx"),
                        rs.getString("ID"),
                        rs.getString("PW"),
                        rs.getString("phone"),
                        rs.getString("payType"),
                        rs.getString("payNumber"),
                        rs.getString("lastPayDate"),
                        rs.getString("membershipName")
        ), getUserInfoParams);

    }

    public PostProfileRes postProfile(int userIdx, PostProfileReq postProfileReq) {
        String insertProfileQuery = "insert into Profile (userIdx, profileName, profilePictureURL, kids) values(?, ?, ?, ?);";
        String getInsertedQuery = "select max(profileIdx) from Profile;";
        String getProfileQuery = "select profileIdx, profileName, profilePictureURL from Profile where profileIdx = ?;";
        Object [] insertProfileParams = new Object [] {
                userIdx,
                postProfileReq.getProfileName(),
                postProfileReq.getProfileImgURL(),
                postProfileReq.getKid()
        };

        //프로필 정보 삽입
        this.jdbcTemplate.update(insertProfileQuery, insertProfileParams);

        //방금 삽입한 인덱스 가져오기
        int insertedIdx = this.jdbcTemplate.queryForObject(getInsertedQuery, int.class);

        //방금 삽입한 프로필 내용 리턴
        return this.jdbcTemplate.queryForObject(getProfileQuery, (rs, rowNum) ->
                new PostProfileRes(
                        rs.getInt("profileIdx"),
                        rs.getString("profileName"),
                        rs.getString("profilePictureURL")
                ), insertedIdx);
    }

    public String deleteProfile(int profileIdx) {
        String deleteProfileQuery = "update Profile set staus = 'INACTIVE' where profileIdx = ?";

        this.jdbcTemplate.update(deleteProfileQuery, profileIdx);

        return "프로파일 삭제에 성공했습니다.";
    }

    public String patchProfile(PatchProfileReq patchProfileReq) {
        String patchProfileQuery = "update Profile set ageLimit = ?, profileName = ?, language = ? where profileIdx = ? and staus = 'ACTIVE';";

        Object[] queryParams = new Object[]{
                patchProfileReq.getAgeLimit(),
                patchProfileReq.getProfileName(),
                patchProfileReq.getLang()
        };

        this.jdbcTemplate.update(patchProfileQuery, queryParams);

        return "프로파일 수정에 성공하였습니다.";
    }

    public String postGameNickname(PostGameNickReq postGameNickReq) {
        String postQuery = "update Profile set gameNickname = ? where profileIdx = ? and staus = 'ACTIVE";

        Object[] queryParams = new Object[]{
                postGameNickReq.getGameNickname(),
                postGameNickReq.getProfileIdx()
        };

        this.jdbcTemplate.update(postQuery, queryParams);

        return "게임닉네임 설정에 성공하였습니다.";


    }

    public String patchUserPassword(PatchUserPasswordReq patchUserPasswordReq) {
        String patchQuery = "update User set PW = ? where userIdx = ? and staus = 'ACTIVE';";

        Object[] queryParams = new Object[]{
                patchUserPasswordReq.getPassword(),
                patchUserPasswordReq.getUserIdx()
        };

        this.jdbcTemplate.update(patchQuery, queryParams);

        return "비밀번호 변경에 성공하였습니다.";
    }

    public String patchUserEmail(PatchUserEmailReq patchUserEmailReq) {
        String patchQuery = "update User set email = ? where userIdx = ? and staus = 'ACTIVE';";

        Object[] queryParams = new Object[]{
                patchUserEmailReq.getEmail(),
                patchUserEmailReq.getUserIdx()
        };

        this.jdbcTemplate.update(patchQuery, queryParams);

        return "이메일 변경에 성공하였습니다.";
    }

    public String patchuserPhone(PatchUserPhoneReq patchUserPhoneReq) {
        String patchQuery = "update User set phone = ? where userIdx = ? and staus = 'ACTIVE';";

        Object[] queryParams = new Object[]{
                patchUserPhoneReq.getPhone(),
                patchUserPhoneReq.getUserIdx()
        };

        this.jdbcTemplate.update(patchQuery, queryParams);

        return "휴대폰 변경에 성공하였습니다.";
    }
}
