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

@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired //readme 참고
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

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
