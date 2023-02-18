package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository //  [Persistence Layer에서 DAO를 명시하기 위해 사용]

/**
 * DAO란?
 * 데이터베이스 관련 작업을 전담하는 클래스
 * 데이터베이스에 연결하여, 입력 , 수정, 삭제, 조회 등의 작업을 수행
 */
public class UserDao {

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************

    private JdbcTemplate jdbcTemplate;

    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    // ******************************************************************************

    /**
     * DAO관련 함수코드의 전반부는 크게 String ~~~Query와 Object[] ~~~~Params, jdbcTemplate함수로 구성되어 있습니다.(보통은 동적 쿼리문이지만, 동적쿼리가 아닐 경우, Params부분은 없어도 됩니다.)
     * Query부분은 DB에 SQL요청을 할 쿼리문을 의미하는데, 대부분의 경우 동적 쿼리(실행할 때 값이 주입되어야 하는 쿼리) 형태입니다.
     * 그래서 Query의 동적 쿼리에 입력되어야 할 값들이 필요한데 그것이 Params부분입니다.
     * Params부분은 클라이언트의 요청에서 제공하는 정보(~~~~Req.java에 있는 정보)로 부터 getXXX를 통해 값을 가져옵니다. ex) getEmail -> email값을 가져옵니다.
     *      Notice! get과 get의 대상은 카멜케이스로 작성됩니다. ex) item -> getItem, password -> getPassword, email -> getEmail, userIdx -> getUserIdx
     * 그 다음 GET, POST, PATCH 메소드에 따라 jabcTemplate의 적절한 함수(queryForObject, query, update)를 실행시킵니다(DB요청이 일어납니다.).
     *      Notice!
     *      POST, PATCH의 경우 jdbcTemplate.update
     *      GET은 대상이 하나일 경우 jdbcTemplate.queryForObject, 대상이 복수일 경우, jdbcTemplate.query 함수를 사용합니다.
     * jdbcTeplate이 실행시킬 때 Query 부분과 Params 부분은 대응(값을 주입)시켜서 DB에 요청합니다.
     * <p>
     * 정리하자면 < 동적 쿼리문 설정(Query) -> 주입될 값 설정(Params) -> jdbcTemplate함수(Query, Params)를 통해 Query, Params를 대응시켜 DB에 요청 > 입니다.
     * <p>
     * <p>
     * DAO관련 함수코드의 후반부는 전반부 코드를 실행시킨 후 어떤 결과값을 반환(return)할 것인지를 결정합니다.
     * 어떠한 값을 반환할 것인지 정의한 후, return문에 전달하면 됩니다.
     * ex) return this.jdbcTemplate.query( ~~~~ ) -> ~~~~쿼리문을 통해 얻은 결과를 반환합니다.
     */

    /**
     * 참고 링크)
     * https://jaehoney.tistory.com/34 -> JdbcTemplate 관련 함수에 대한 설명
     * https://velog.io/@seculoper235/RowMapper%EC%97%90-%EB%8C%80%ED%95%B4 -> RowMapper에 대한 설명
     */

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

    public String patchProfile(int profileIdx) {
        String deleteProfileQuery = "update Profile set staus = 'INACTIVE' where profileIdx = ?";

        this.jdbcTemplate.update(deleteProfileQuery, profileIdx);

        return "프로파일 삭제에 성공했습니다.";
    }
}
