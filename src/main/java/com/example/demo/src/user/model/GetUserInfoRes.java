package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserInfoRes {
    private int userIdx;
    private String userId;
    private String userPw;
    private String phoneNum;
    private String payType;
    private String payNum;
    private String lastPaidDate;
    private String membershipName;
}
