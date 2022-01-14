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
}
