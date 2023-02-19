package com.example.demo.src.Video;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Video.Model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("api/video")
public class VideoController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final VideoProvider videoProvider;
    @Autowired
    private final VideoService videoService;
    @Autowired
    private final JwtService jwtService;

    public VideoController(VideoProvider videoProvider, VideoService videoService, JwtService jwtService) {
        this.videoProvider = videoProvider;
        this.videoService = videoService;
        this.jwtService = jwtService;
    }
    /**
     * 내가 찜한 컨텐츠 설정 API
     * [POST] video/scrap/{videoListIdx}
     * */
    @ResponseBody
    @PostMapping("/scrap/{videoListIdx}")
    public BaseResponse<String> postScrapContent(@PathVariable("videoListIdx") int videoListIdx, @RequestParam int profileIdx, int userIdx) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            return new BaseResponse<>(videoService.postScrapContent(videoListIdx, profileIdx));
        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 내가 찜한 컨텐츠 취소 API
     * [PATCH] video/scrap/{videoList}
     * */
    @ResponseBody
    @PatchMapping("/scrap/{videoListIdx}")
    public BaseResponse<String> patchScrapContent(@PathVariable("videoListIdx") int videoListIdx, @RequestParam int profileIdx, int userIdx) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            return new BaseResponse<>(videoService.patchScrapContent(videoListIdx, profileIdx));
        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 시리즈 평가 생성 API
     * [POST] video/critic/{videoListIdx}
     * */
    @ResponseBody
    @PostMapping("/critic/{videoListIdx}")
    public BaseResponse<String> postCritic(@RequestBody PostVideoCriticReq postVideoCriticReq) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(postVideoCriticReq.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            return new BaseResponse<>(videoService.postCritic(postVideoCriticReq));
        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 시리즈 평가 삭제 API
     * [PATCH] video/critic/{videoListIdx}
     * */
    @ResponseBody
    @PatchMapping("/critic/{videoListIdx}")
    public BaseResponse<String> patchCritic(@RequestBody PatchVideoCriticReq patchVideoCriticReq) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(patchVideoCriticReq.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            return new BaseResponse<>(videoService.patchCritic(patchVideoCriticReq));
        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 시리즈 화면정보 조회 API
     * [GET] video/list/{videoListIdx}
     * */
    @ResponseBody
    @GetMapping("/list/{videoListIdx}")
    public BaseResponse<GetVideoListPageRes> getVideoListPage(@PathVariable("videoListIdx") int videoListIdx) {
        try{
            return new BaseResponse<>(videoProvider.getVideoListPage(videoListIdx));
        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 시리즈 리스트 썸네일 정보 조회 API
     * [GET] video/listPicture
     * */
    @ResponseBody
    @GetMapping("/listPicture")
    public BaseResponse<List<GetVideoListPicRes>> getVideoListPic() {
        try{
            return new BaseResponse<>(videoProvider.getVideoListPic());
        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 비디오 재생 요청 API
     * [GET] video/play/{videoIdx}
     * */
    @ResponseBody
    @GetMapping("/play/{videoIdx}")
    public BaseResponse<GetVideoLinkRes> getPlayVideoLink(@PathVariable("videoIdx") int videoIdx, int userIdx) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            return new BaseResponse<>(videoProvider.getPlayVideoLink(videoIdx));
        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 개별 영상 다운로드 요청 API
     * [GET] video/download/{videoIdx}
     * */
    @ResponseBody
    @GetMapping("/download/{videoIdx}")
    public BaseResponse<GetVideoLinkRes> getDownVideoLink(@PathVariable("videoIdx") int videoIdx, int userIdx) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            return new BaseResponse<>(videoProvider.getDownVideoLink(videoIdx));
        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 시리즈 전체 영상 다운로드 요청 API
     * [GET] video/downloadList/{videoListIdx}
     * */
    @ResponseBody
    @GetMapping("/downloadList/{videoListIdx}")
    public BaseResponse<List<GetVideoLinkRes>> getDownVideoListLink(@PathVariable("videoListIdx") int videoListIdx, int userIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            return new BaseResponse<>(videoProvider.getDownVideoListLink(videoListIdx));
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
