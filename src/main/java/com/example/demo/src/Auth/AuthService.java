package com.example.demo.src.Auth;

import com.example.demo.config.BaseResponseStatus;
import com.example.demo.config.BaseException;
import com.example.demo.src.Auth.Model.PostLoginReq;
import com.example.demo.src.Auth.Model.PostLoginRes;
import com.example.demo.src.Auth.Model.User;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final AuthDao authDao;
    @Autowired
    private final JwtService jwtService;

    public AuthService(AuthDao authDao, JwtService jwtService) {
        this.authDao = authDao;
        this.jwtService = jwtService;
    }

    public PostLoginRes postLogin(PostLoginReq postLoginReq) throws BaseException {
        try{
            User user = authDao.postLogin(postLoginReq);
            String jwt = jwtService.createJwt(user.getUserIdx());

            return new PostLoginRes(user.getUserIdx(), jwt);
        }catch (Exception exception) {
            logger.error("Error In Login", exception);
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
