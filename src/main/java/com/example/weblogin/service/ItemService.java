package com.example.weblogin.service;

import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    // 상품 등록
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    // 상품 개별 불러오기
    public Item itemView(Integer id) {
        return itemRepository.findById(id).get();
    }

    // 아이템 리스트 불러오기
    public List<Item> allItemView() {
        return itemRepository.findAll();
    }

    // 아이템 수정
    public void itemModify(Item item, Integer id) {
        Item before = itemRepository.findItemById(id);
        before.setName(item.getName());
        before.setText(item.getText());
        before.setPrice(item.getPrice());
        before.setStock(item.getStock());
        itemRepository.save(before);

    }
}
