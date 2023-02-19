package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.ValidationRegex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.example.demo.config.BaseResponseStatus.*;

/**
 * Service란?
 * Controller에 의해 호출되어 실제 비즈니스 로직과 트랜잭션을 처리: Create, Update, Delete 의 로직 처리
 * 요청한 작업을 처리하는 관정을 하나의 작업으로 묶음
 * dao를 호출하여 DB CRUD를 처리 후 Controller로 반환
 */
@Service    // [Business Layer에서 Service를 명시하기 위해서 사용] 비즈니스 로직이나 respository layer 호출하는 함수에 사용된다.
            // [Business Layer]는 컨트롤러와 데이터 베이스를 연결
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log 처리부분: Log를 기록하기 위해 필요한 함수입니다.

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************
    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!


    @Autowired //readme 참고
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    }

    public PostSignInRes postSignIn(PostSignInReq postSignInReq) throws BaseException {

        try{
            User user = userDao.postSignIn(postSignInReq);

            return new PostSignInRes(user.getUserIdx(), user.getID(), user.getPassword(), user.getName());
        }catch (Exception exception){
            logger.error("Error in user signin API", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String deleteUser(int userIdx) throws BaseException{
        try{
            return userDao.deleteUser(userIdx);
        }catch (Exception exception){
            logger.error("Error in user delete API", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String patchPlan(int userIdx, int planIdx) throws BaseException {
        try{
            return userDao.patchPlan(userIdx, planIdx);
        }catch (Exception exception){
            logger.error("Error in plan change API", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostProfileRes postProfile(int userIdx, PostProfileReq postProfileReq) throws BaseException{
        try{
            return userDao.postProfile(userIdx, postProfileReq);
        }catch (Exception exception){
            logger.error("Error in postProfile() in userService", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String deleteProfile(int profileIdx) throws BaseException{
        try{
            return userDao.deleteProfile(profileIdx);
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String patchProfile(PatchProfileReq patchProfileReq) throws BaseException{
        try{
            return userDao.patchProfile(patchProfileReq);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String postGameNickname(PostGameNickReq postGameNickReq) throws BaseException{
        try{
            return userDao.postGameNickname(postGameNickReq);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String patchUserPassword(PatchUserPasswordReq patchUserPasswordReq) throws BaseException{
        if(!ValidationRegex.isPasswordFit(patchUserPasswordReq.getPassword())) throw new BaseException(INVALID_PW);

        try{
            return userDao.patchUserPassword(patchUserPasswordReq);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String patchUserEmail(PatchUserEmailReq patchUserEmailReq) throws BaseException{
        try{
            return userDao.patchUserEmail(patchUserEmailReq);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String patchUserPhone(PatchUserPhoneReq patchUserPhoneReq) throws BaseException{
        try{
            return userDao.patchuserPhone(patchUserPhoneReq);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
