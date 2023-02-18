package com.example.demo.src.Video.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetVideoListPicRes {
    private int videoListIdx;
    private String videoListCharater;
    private String videoListPicture;
}
