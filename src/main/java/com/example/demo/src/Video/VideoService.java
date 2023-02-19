package com.example.demo.src.Video;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.Video.Model.PatchVideoCriticReq;
import com.example.demo.src.Video.Model.PostVideoCriticReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final VideoDao videoDao;
    @Autowired
    private final JwtService jwtService;

    public VideoService(VideoDao videoDao, JwtService jwtService) {
        this.videoDao = videoDao;
        this.jwtService = jwtService;
    }

    public String postScrapContent(int videoListIdx, int profileIdx) throws BaseException {
        try{
            return videoDao.postScrapContent(videoListIdx, profileIdx);
        }catch (Exception exception) {
            logger.error("Error In PostScrapContent", exception);
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public String patchScrapContent(int videoListIdx, int profileIdx) throws BaseException{
        try{
            return videoDao.patchScrapContent(videoListIdx, profileIdx);
        }catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public String postCritic(PostVideoCriticReq postVideoCriticReq) throws BaseException{
        try{
            return videoDao.postCritic(postVideoCriticReq);
        }catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public String patchCritic(PatchVideoCriticReq patchVideoCriticReq) throws BaseException{
        try{
            return videoDao.patchCritic(patchVideoCriticReq);
        }catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
