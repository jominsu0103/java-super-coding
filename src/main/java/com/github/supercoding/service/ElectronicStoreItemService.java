package com.github.supercoding.service;

import com.github.supercoding.repository.items.ElectronalStoreItemRepository;
import com.github.supercoding.repository.items.ItemEntity;
import com.github.supercoding.repository.storeSales.StoreSales;
import com.github.supercoding.repository.storeSales.StoreSalesRepository;
import com.github.supercoding.web.dto.BuyOrder;
import com.github.supercoding.web.dto.Item;
import com.github.supercoding.web.dto.ItemBody;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElectronicStoreItemService {
    private ElectronalStoreItemRepository electronalStoreItemRepository;
    private StoreSalesRepository storeSalesRepository;

    public ElectronicStoreItemService(ElectronalStoreItemRepository electronalStoreItemRepository, StoreSalesRepository storeSalesRepository) {
        this.electronalStoreItemRepository = electronalStoreItemRepository;
        this.storeSalesRepository = storeSalesRepository;
    }

    public List<Item> findAllItem() {
        List<ItemEntity> itemEntities = electronalStoreItemRepository.findAllItems();
        return itemEntities.stream().map(Item::new).collect(Collectors.toList());
    }

    public Integer saveItem(ItemBody itemBody) {
        ItemEntity itemEntity = new ItemEntity(null, itemBody.getName(), itemBody.getType(),
                itemBody.getPrice(), itemBody.getSpec().getCpu(), itemBody.getSpec().getCapacity()

        );
        return electronalStoreItemRepository.saveItem(itemEntity);
    }

    public Item findItemById(String id) {
        Integer idInt = Integer.parseInt(id);
        ItemEntity itemEntity = electronalStoreItemRepository.findItemById(idInt);
        Item item = new Item(itemEntity);
        return item;
    }

    public List<Item> findItemsByIds(List<String> ids) {
        List<ItemEntity> itemEntities = electronalStoreItemRepository.findAllItems();
        return itemEntities.stream()
                .map(Item::new)
                .filter((item -> ids.contains(item.getId())))
                .collect(Collectors.toList());
    }

    public void deleteItem(String id) {
        Integer idInt = Integer.parseInt(id);
        electronalStoreItemRepository.deleteItem(idInt);
    }

    public Item updateItem(String id, ItemBody itemBody) {
        Integer idInt = Integer.valueOf(id);
        ItemEntity itemEntity = new ItemEntity(idInt,itemBody.getName(),
                itemBody.getType(),itemBody.getPrice(),
                itemBody.getSpec().getCpu(),itemBody.getSpec().getCapacity()
                );
        ItemEntity itemEntityUpdated = electronalStoreItemRepository.updateItemEntity(idInt,itemEntity);
        return new Item(itemEntityUpdated);

    }

@Transactional(transactionManager = "tm1")
    public Integer buyItems(BuyOrder buyOrder) {
        Integer itemId = buyOrder.getItemId();
        Integer itemNums = buyOrder.getItemNums();

        ItemEntity itemEntity = electronalStoreItemRepository.findItemById(itemId);
        if (itemEntity.getStoreId() == null) throw new RuntimeException("매장을 찾을 수 없음");
        if (itemEntity.getStock() <=0) throw new RuntimeException("상품 재고 없음");

        Integer successBuyItemNums;
        if(itemNums >= itemEntity.getStock()) successBuyItemNums = itemEntity.getStock();
        else successBuyItemNums = itemNums;

        Integer totalPrice = successBuyItemNums * itemEntity.getPrice();

        electronalStoreItemRepository.updateItemStock(itemId, itemEntity.getStock() - successBuyItemNums);

        if(successBuyItemNums == 5) throw new RuntimeException("5개는 구매안돼");

        StoreSales storeSales = storeSalesRepository.findStoreSalesById(itemEntity.getStoreId());
        storeSalesRepository.updateSalesAmount(itemEntity.getStoreId(), storeSales.getAmount() + totalPrice);

        return successBuyItemNums;
    }
}
