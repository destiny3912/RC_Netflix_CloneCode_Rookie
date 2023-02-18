package com.example.demo.src.user.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSignInReq {
    private int membershipIdx;
    private String payType;
    private String payNumber;
    private String payDesc;
    private String isSub;
    private String userName;
    private String id;
    private String pw;
    private String birthday;
    private String phoneNum;
}
