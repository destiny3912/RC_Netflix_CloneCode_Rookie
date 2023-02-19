package com.example.demo.src.Video.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostVideoCriticReq {
    int videoListIdx;
    int profileIdx;
    int userIdx;
    int criticIdx;
}
