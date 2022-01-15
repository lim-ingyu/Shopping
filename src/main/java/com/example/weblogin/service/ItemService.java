package com.example.weblogin.service;

import com.example.weblogin.domain.Item;
import com.example.weblogin.domain.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    // 상품 등록
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    // 상품 불러오기
    public Item itemView(Integer id) {
        Item item = itemRepository.findById(id).get();
        return item;
    }
}
