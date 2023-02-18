package com.example.demo.src.Video.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetVideoListPageRes {
    private int videoListIdx;
    private String videoListName;
    private String videoListPlot;
    private String videoListCharater;
    private String videoListSince;
    private String videoListTeaser;
    private int ageLimit;
    private String releaseDate;
    private String language;
    private List<String> actors;
    private List<String> creators;
    private List<String> episodeNum;
    private List<String> episodeName;
    private List<String>episodeURL;
    private List<String> episodePlot;
    private int episodeCount;
}
