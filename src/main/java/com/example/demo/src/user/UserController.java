package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.ValidationRegex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.*;
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
     * [DELETE] /user/profile/{profileIdx}
     * */
    @ResponseBody
    @DeleteMapping ("/profile/{profileIdx}")
    public BaseResponse<String> deleteProfile(@PathVariable("profileIdx") int profileIdx, @RequestParam int userIdx) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            return new BaseResponse<>(userService.deleteProfile(profileIdx));
        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 프로필 수정 API
     * [PATCH]/user/profile/{profileIdx}
     * */
    @ResponseBody
    @PatchMapping("/profile")
    public BaseResponse<String> patchProfile(@RequestBody PatchProfileReq patchProfileReq){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(patchProfileReq.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            return new BaseResponse<>(userService.patchProfile(patchProfileReq));

        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 게임 닉네임 설정 API
     * [POST] /user/game/nickname
     * */
    @ResponseBody
    @PostMapping("/game/nickname")
    public BaseResponse<String> postGameNickname(@RequestBody PostGameNickReq postGameNickReq){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(postGameNickReq.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            return new BaseResponse<>(userService.postGameNickname(postGameNickReq));
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 비밀번호 변경 API
     * [PATCH] /user/password
     * */
    @ResponseBody
    @PatchMapping("/password")
    public BaseResponse<String> patchUserPassword(@RequestBody PatchUserPasswordReq patchUserPasswordReq){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(patchUserPasswordReq.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            if(patchUserPasswordReq.getPassword().length() < 8) throw new BaseException(PW_TOO_SHORT);
            else if (patchUserPasswordReq.getPassword().length() > 20) throw new BaseException(PW_TOO_LONG);

            return new BaseResponse<>(userService.patchUserPassword(patchUserPasswordReq));
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 이메일 변경 API
     * [PATCH] /user/email
     * */
    @ResponseBody
    @PatchMapping("/email")
    public BaseResponse<String> patchUserEmail(@RequestBody PatchUserEmailReq patchUserEmailReq){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(patchUserEmailReq.getUserIdx() != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(!ValidationRegex.isRegexEmail(patchUserEmailReq.getEmail())) throw new BaseException(POST_USERS_INVALID_EMAIL);

            return new BaseResponse<>(userService.patchUserEmail(patchUserEmailReq));

        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 전화번호 변경 API
     * [PATCH] /user/phone
     * */
    @ResponseBody
    @PatchMapping("/phone")
    public BaseResponse<String> patchUserPhone(@RequestBody PatchUserPhoneReq patchUserPhoneReq){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(patchUserPhoneReq.getUserIdx() != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            return new BaseResponse<>(userService.patchUserPhone(patchUserPhoneReq));
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
