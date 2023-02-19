package com.example.demo.src.Video.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatchVideoCriticReq {
    int videoListIdx;
    int profileIdx;
    int userIdx;
    int criticIdx;
}
