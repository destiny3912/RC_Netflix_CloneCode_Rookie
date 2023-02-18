package com.example.demo.src.Video;

import com.example.demo.src.Video.Model.GetVideoLinkRes;
import com.example.demo.src.Video.Model.GetVideoListPageRes;
import com.example.demo.src.Video.Model.GetVideoListPicRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Repository
public class VideoDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String postScrapContent(int videoListIdx, int profileIdx) {
        String checkRowExistQuery = "select exists(select scrapIdx from VideoListScrap where videoListIdx = ? and profileIdx = ?);";
        String insertScrapQuery = "insert into VideoListScrap (videoListIdx, profileIdx) values (?, ?);";
        String updateScrapQuery = "update VideoListScrap set staus = 'ACTIVE' where videoListIdx = ? and profileIdx = ?;";

        Object[] checkRowExistParams = new Object[] {videoListIdx, profileIdx};

        boolean checkExist = this.jdbcTemplate.queryForObject(checkRowExistQuery, int.class, checkRowExistParams) == 1;

        if(checkExist) this.jdbcTemplate.update(updateScrapQuery, checkRowExistParams);
        else this.jdbcTemplate.update(insertScrapQuery, checkRowExistParams);

        return "내가 찜한 컨텐츠 추가 성공";
    }

    public String patchScrapContent(int videoListIdx, int profileIdx) {
        String updateScrapStatusQuery = "update VideoListScrap set staus = 'INACTIVE' where videoListIdx = ? and profileIdx = ?;";
        Object[] updateScrapStatusParams = new Object[] {videoListIdx, profileIdx};

        this.jdbcTemplate.update(updateScrapStatusQuery, updateScrapStatusParams);

        return "내가 찜한 컨텐츠 삭제 성공";
    }

    public String postLike(int videoListIdx, int profileIdx) {
        String checkRowExistQuery = "select exists(select profileIdx, videoListIdx from VideoCriticGood where profileIdx = ? and videoListIdx = ? )";
        String insertQuery = "insert into VideoCriticGood (profileIdx, videoListIdx) values(?, ?);";
        String updateQuery = "update VideoCriticGood set status = 'ACTIVE' where profileIdx = ? and videoListIdx = ?;";

        Object[] queryParams = new Object[] {profileIdx, videoListIdx};

        boolean checkExist = this.jdbcTemplate.queryForObject(checkRowExistQuery, int.class, queryParams) == 1;

        if(checkExist) this.jdbcTemplate.update(updateQuery, queryParams);
        else this.jdbcTemplate.update(insertQuery, queryParams);

        return "시리즈 좋아요 설정 성공";
    }

    public String patchLike(int videoListIdx, int profileIdx) {
        String updateQuery = "update VideoCriticGood set status = 'INACTIVE' where profileIdx = ? and videoListIdx = ?;";
        Object[] queryParams = new Object[] {profileIdx, videoListIdx};

        this.jdbcTemplate.update(updateQuery, queryParams);

        return "시리즈 좋아요 삭제 성공";
    }

    public String postBest(int videoListIdx, int profileIdx) {
        String checkRowExistQuery = "select exists(select profileIdx, videoListIdx from VideoCriticBest where profileIdx = ? and videoListIdx = ? )";
        String insertQuery = "insert into VideoCriticBest (profileIdx, videoListIdx) values(?, ?);";
        String updateQuery = "update VideoCriticBest set staus = 'ACTIVE' where profileIdx = ? and videoListIdx = ?;";

        Object[] queryParams = new Object[] {profileIdx, videoListIdx};

        boolean checkExist = this.jdbcTemplate.queryForObject(checkRowExistQuery, int.class, queryParams) == 1;

        if(checkExist) this.jdbcTemplate.update(updateQuery, queryParams);
        else this.jdbcTemplate.update(insertQuery, queryParams);

        return "시리즈 최고에요 설정 성공";
    }

    public String patchBest(int videoListIdx, int profileIdx) {
        String updateQuery = "update VideoCriticBest set staus = 'INACTIVE' where profileIdx = ? and videoListIdx = ?;";
        Object[] queryParams = new Object[] {profileIdx, videoListIdx};

        this.jdbcTemplate.update(updateQuery, queryParams);

        return "시리즈 최고에요 삭제 성공";
    }

    public String postWorst(int videoListIdx, int profileIdx) {
        String checkRowExistQuery = "select exists(select profileIdx, videoListIdx from VideoCriticWorst where profileIdx = ? and videoListIdx = ? )";
        String insertQuery = "insert into VideoCriticWorst (profileIdx, videoListIdx) values(?, ?);";
        String updateQuery = "update VideoCriticWorst set status = 'ACTIVE' where profileIdx = ? and videoListIdx = ?;";

        Object[] queryParams = new Object[] {profileIdx, videoListIdx};

        boolean checkExist = this.jdbcTemplate.queryForObject(checkRowExistQuery, int.class, queryParams) == 1;

        if(checkExist) this.jdbcTemplate.update(updateQuery, queryParams);
        else this.jdbcTemplate.update(insertQuery, queryParams);

        return "시리즈 싫어요 설정 성공";
    }

    public String patchWorst(int videoListIdx, int profileIdx) {
        String updateQuery = "update VideoCriticWorst set status = 'INACTIVE' where profileIdx = ? and videoListIdx = ?;";
        Object[] queryParams = new Object[] {profileIdx, videoListIdx};

        this.jdbcTemplate.update(updateQuery, queryParams);

        return "시리즈 싫어요 삭제 성공";
    }

    public GetVideoListPageRes getVideoListPage(int videoListIdx) {
        String getPageQuery =
                "SELECT VL.videoListIdx, VL.videoListName, VL.videoListPlot, VL.videoListCharater,\n" +
                        "       VL.videoListSince, VL.videoListTeaser, VL.ageLimit,\n" +
                        "       VL.releaseDate, VL.language,\n" +
                        "       group_concat(distinct Actors.actorName) as actors,\n" +
                        "       group_concat(distinct Creators.creatorName) as creators,\n" +
                        "       group_concat(distinct V.episode) as episodeNum,\n" +
                        "       group_concat(distinct V.videoName) as episodeName,\n" +
                        "       group_concat(distinct V.videoURL) as episodeURL,\n" +
                        "       group_concat(distinct V.videoPlot) as episodePlot,\n" +
                        "       count(distinct V.videoIdx) as episodeCount\n" +
                        "FROM\n" +
                        "    VideoList as VL\n" +
                        "        left join\n" +
                        "    (SELECT videoListIdx, Actor.actorIdx, actorName\n" +
                        "     FROM Actor left join ActorVideoListMatching AVLM\n" +
                        "                          on Actor.actorIdx = AVLM.actorIdx WHERE videoListIdx = ? order by actorIdx) as Actors\n" +
                        "    on Actors.videoListIdx = VL.videoListIdx\n" +
                        "        left join\n" +
                        "    (SELECT videoListIdx, Creator.creatorIdx, creatorName\n" +
                        "     FROM Creator left join CreatorVideoListMatching CVLM\n" +
                        "                            on Creator.creatorIdx = CVLM.creatorIdx WHERE videoListIdx = ? order by creatorIdx) as Creators\n" +
                        "    on Creators.videoListIdx = VL.videoListIdx\n" +
                        "        left join\n" +
                        "    (SELECT videoListIdx, videoIdx, episode, videoName, videoPlot, videoURL\n" +
                        "     FROM Video WHERE videoListIdx = ? order by videoIdx) as V\n" +
                        "    on V.videoListIdx = VL.videoListIdx\n" +
                        "WHERE V.videoListIdx = ?;";

        Object[] getPageParams = new Object[] { videoListIdx, videoListIdx, videoListIdx, videoListIdx };

        return this.jdbcTemplate.queryForObject(getPageQuery, (rs, rowNum)
                -> new GetVideoListPageRes(
                        rs.getInt("videoListIdx"),
                        rs.getString("videoListName"),
                        rs.getString("videoListPlot"),
                        rs.getString("videoListCharater"),
                        rs.getString("videoListSince"),
                        rs.getString("videoListTeaser"),
                        rs.getInt("ageLimit"),
                        rs.getString("releaseDate"),
                        rs.getString("language"),
                        Arrays.asList(rs.getString("actors").split(",")),
                        Arrays.asList(rs.getString("creators").split(",")),
                        Arrays.asList(rs.getString("episodeNum").split(",")),
                        Arrays.asList(rs.getString("episodeName").split(",")),
                        Arrays.asList(rs.getString("episodeURL").split(",")),
                        Arrays.asList(rs.getString("episodePlot").split(",")),
                        rs.getInt("episodeCount")
        ), getPageParams);
    }

    public List<GetVideoListPicRes> getVideoListPic() {
        String getListPicQuery = "SELECT videoListIdx, videoListCharater, videoListThumbnail FROM VideoList LIMIT 100;";

        return this.jdbcTemplate.query(getListPicQuery, (rs, rowNum)
                -> new GetVideoListPicRes(
                        rs.getInt("videoListIdx"),
                rs.getString("videoListCharater"),
                rs.getString("videoListThumbnail")
        ));
    }

    public GetVideoLinkRes getPlayVideoLink(int videoIdx) {
        String getLinkQuery = "select videoURL from Video where videoIdx = ?";


        return this.jdbcTemplate.queryForObject(getLinkQuery, (rs, rowNum)
                -> new GetVideoLinkRes(
                        rs.getString("videoURL")
        ), videoIdx);
    }

    public GetVideoLinkRes getDownVideoLink(int videoIdx) {
        String getLinkQuery = "select videoURL from Video where videoIdx = ?";

        return this.jdbcTemplate.queryForObject(getLinkQuery, (rs, rowNum)
                -> new GetVideoLinkRes(
                rs.getString("videoURL")
        ), videoIdx);
    }

    public List<GetVideoLinkRes> getDownVideoListLink(int videoListIdx) {
        String getLinkQuery = "select videoURL from Video where videoListIdx = ?";

        return this.jdbcTemplate.query(getLinkQuery, (rs, rowNum)
                -> new GetVideoLinkRes(
                        rs.getString("videoURL")
        ), videoListIdx);
    }


}
