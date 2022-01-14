package com.example.weblogin.domain;

import com.example.weblogin.constant.ItemStatus;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String itemName;
    private String itemText; // 물건에 대한 상세설명
    private int itemPrice;
    private int sellCount; // 판매 개수
    private int stock; // 재고

    @Enumerated(EnumType.STRING)
    private ItemStatus isSoldout; // 상품 상태 (판매중 / 품절)

    @ManyToOne
    private User sellerId; // 판매자 아이디

    private String itemImage; // 상품 사진


}
