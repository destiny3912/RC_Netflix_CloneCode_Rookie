package com.example.demo.src.Video;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.Video.Model.GetVideoLinkRes;
import com.example.demo.src.Video.Model.GetVideoListPageRes;
import com.example.demo.src.Video.Model.GetVideoListPicRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final VideoDao videoDao;
    @Autowired
    private final JwtService jwtService;

    public VideoProvider(VideoDao videoDao, JwtService jwtService) {
        this.videoDao = videoDao;
        this.jwtService = jwtService;
    }

    public GetVideoListPageRes getVideoListPage(int videoListIdx) throws BaseException{
        try{
            return videoDao.getVideoListPage(videoListIdx);
        }catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public List<GetVideoListPicRes> getVideoListPic() throws BaseException{
        try{
            return videoDao.getVideoListPic();
        }catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public GetVideoLinkRes getPlayVideoLink(int videoIdx) throws BaseException {
        try{
            return videoDao.getPlayVideoLink(videoIdx);
        }catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public GetVideoLinkRes getDownVideoLink(int videoIdx) throws BaseException{
        try{
            return videoDao.getDownVideoLink(videoIdx);
        }catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public List<GetVideoLinkRes> getDownVideoListLink(int videoListIdx) throws BaseException{
        try{
            return videoDao.getDownVideoListLink(videoListIdx);
        }catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}

