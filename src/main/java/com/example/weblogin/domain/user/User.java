package com.example.weblogin.domain.user;

import com.example.weblogin.domain.cart.Cart;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.order.Order;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// @RequiredArgsConstructor final이나 @NonNull인 필드 값만 파라미터로 받는 생성자를 만들어줌
@Builder
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자를 만들어줌
@NoArgsConstructor // 파라미터가 없는 기본 생성자를 만들어줌
@Getter
@Setter
@Entity // DB에 테이블 자동 생성
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true) // 닉네임 중복 안됨
    private String username;
    private String password;
    private String name;
    private String email;
    private String address;
    private String phone;
    private String grade; // 회원 등급
    private String role; // 권한 (회원 / 관리자)

    private int coin; // 충전한 돈 - 코인

    @OneToMany(mappedBy = "user")
    private List<Item> items = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private Cart cart;

    // 구매자 주문
    @OneToMany(mappedBy = "user")
    private List<Order> userOrder = new ArrayList<>();;

    // 판매자 판매 (구매자의 주문)
    @OneToMany(mappedBy = "seller")
    private List<Order> sellerOrder = new ArrayList<>();;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate createDate; // 날짜

    @PrePersist // DB에 INSERT 되기 직전에 실행. 즉 DB에 값을 넣으면 자동으로 실행됨
    public void createDate() {
        this.createDate = LocalDate.now();
    }
}
