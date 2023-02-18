package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostProfileRes {
    private int profileIdx;
    private String profileName;
    private String profileImgURL;
}
