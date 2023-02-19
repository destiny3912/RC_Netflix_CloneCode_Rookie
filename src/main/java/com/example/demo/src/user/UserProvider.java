package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {


    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired //readme 참고
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public GetUserInfoRes getUserInfo(int userIdx) throws BaseException{
        try {
            return userDao.getUserInfo(userIdx);
        }catch (Exception exception){
            logger.error("Error in get user info", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
