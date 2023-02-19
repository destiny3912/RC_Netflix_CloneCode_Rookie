package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatchProfileReq {
    int profileIdx;
    int userIdx;
    int ageLimit;
    String profileName;
    String lang;
}
