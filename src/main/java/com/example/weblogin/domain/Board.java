package com.example.weblogin.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String reviewTitle; // 후기 제목
    private String reviewText; // 후기 내용
    private String reviewWriter; // 후기 작성자
    private String reviewImage; // 후기 사진
}
