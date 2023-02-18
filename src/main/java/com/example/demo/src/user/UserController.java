package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;
import static com.example.demo.config.Constant.BASIC_PROFILE_IMG_URL;

@RestController
@RequestMapping("/api/user")
public class UserController {

    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log를 남기기: 일단은 모르고 넘어가셔도 무방합니다.

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;


    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService) {
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원가입 API
     * [POST] /user
     * */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostSignInRes> postSignIn(@RequestBody PostSignInReq postSignInReq) {
        try{
            return new BaseResponse<>(userService.postSignIn(postSignInReq));
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 삭제 API
     * [PATCH] /user/{userIdx}
     * */
    @ResponseBody
    @PatchMapping("/{userIdx}")
    public BaseResponse<String> deleteUser(@PathVariable int userIdx) {

        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            return new BaseResponse<>(userService.deleteUser(userIdx));

        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 맴버십 변경 API
     * [PATCH] /user/changePlan/{userIdx}
     * */
    @ResponseBody
    @PatchMapping("/changePlan/{userIdx}")
    public BaseResponse<String> patchPlan(@PathVariable("userIdx") int userIdx, @RequestParam int planIdx) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            return new BaseResponse<>(userService.patchPlan(userIdx, planIdx));
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 정보 조회 API
     * [GET] /user/info/{userIdx}
     * */
    @ResponseBody
    @GetMapping("/info/{userIdx}")
    public BaseResponse<GetUserInfoRes> getUserInfo(@PathVariable("userIdx") int userIdx) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            return new BaseResponse<>(userProvider.getUserInfo(userIdx));
        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 프로필 생성 API
     * [POST] /user/profile/{userIdx}
     * */
    @ResponseBody
    @PostMapping("/profile/{userIdx}")
    public BaseResponse<PostProfileRes> postProfile(@PathVariable("userIdx") int userIdx,@RequestBody PostProfileReq postProfileReq) {
        if(postProfileReq.getProfileImgURL() == null) postProfileReq.setProfileImgURL(BASIC_PROFILE_IMG_URL);

        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            return new BaseResponse<>(userService.postProfile(userIdx, postProfileReq));
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 프로필 삭제 API
     * [PATCH] /user/profile/{profileIdx}
     * */
    @ResponseBody
    @PatchMapping("/profile/{profileIdx}")
    public BaseResponse<String> patchProfile(@PathVariable("profileIdx") int profileIdx, @RequestParam int userIdx) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            return new BaseResponse<>(userService.patchProfile(profileIdx));
        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
