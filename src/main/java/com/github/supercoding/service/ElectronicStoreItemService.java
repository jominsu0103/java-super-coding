package com.github.supercoding.service;

import com.github.supercoding.repository.items.ElectronalStoreItemRepository;
import com.github.supercoding.repository.items.ElectronicStoreItemJpaRepository;
import com.github.supercoding.repository.items.ItemEntity;
import com.github.supercoding.repository.storeSales.StoreSales;
import com.github.supercoding.repository.storeSales.StoreSalesJpaRepository;
import com.github.supercoding.repository.storeSales.StoreSalesRepository;
import com.github.supercoding.service.exceptions.NotAcceptException;
import com.github.supercoding.service.exceptions.NotFoundException;
import com.github.supercoding.service.mapper.ItemMapper;
import com.github.supercoding.web.dto.BuyOrder;
import com.github.supercoding.web.dto.Item;
import com.github.supercoding.web.dto.ItemBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElectronicStoreItemService {
    private final ElectronicStoreItemJpaRepository electronicStoreItemJpaRepository;
    private final StoreSalesJpaRepository storeSalesJpaRepository;

    public List<Item> findAllItem() {
        List<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findAll();
        return itemEntities.stream().map(ItemMapper.INSTANCE::itemEntityToItem).collect(Collectors.toList());
    }

    public Integer saveItem(ItemBody itemBody) {
        ItemEntity itemEntity = ItemMapper.INSTANCE.idAndItemBodyToItemEntity(null, itemBody);
        ItemEntity itemEntityCreated;

        try {
            itemEntityCreated = electronicStoreItemJpaRepository.save(itemEntity);
        }catch (RuntimeException e){
            throw new NotAcceptException("Item을 저장하는 도중에 Error가 발생");
        }
        return itemEntityCreated.getId();
    }

    public Item findItemById(String id) {
        Integer idInt = Integer.parseInt(id);
        ItemEntity itemEntity = electronicStoreItemJpaRepository.findById(idInt).orElseThrow(()-> new NotFoundException("해당 아이디로 조회할 수 없습니다."));
        Item item = ItemMapper.INSTANCE.itemEntityToItem(itemEntity);
        return item;
    }

    public List<Item> findItemsByIds(List<String> ids) {
        List<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findAll();
        return itemEntities.stream()
                .map(ItemMapper.INSTANCE::itemEntityToItem)
                .filter((item -> ids.contains(item.getId())))
                .collect(Collectors.toList());
    }

    public void deleteItem(String id) {
        Integer idInt = Integer.parseInt(id);
        electronicStoreItemJpaRepository.deleteById(idInt);
    }
    @Transactional(transactionManager = "tmJpa1")
    public Item updateItem(String id, ItemBody itemBody) {
        Integer idInt = Integer.valueOf(id);
        ItemEntity itemEntityUpdated = electronicStoreItemJpaRepository.findById(idInt)
                .orElseThrow(()-> new NotFoundException("해당 아이디로 조회할 수 없습니다."));
        itemEntityUpdated.setItemBody(itemBody);
        return ItemMapper.INSTANCE.itemEntityToItem(itemEntityUpdated);
    }

@Transactional(transactionManager = "tmJpa1")
    public Integer buyItems(BuyOrder buyOrder) {
        Integer itemId = buyOrder.getItemId();
        Integer itemNums = buyOrder.getItemNums();

        ItemEntity itemEntity = electronicStoreItemJpaRepository.findById(itemId)
                .orElseThrow(()-> new NotFoundException("해당 아이디로 조회할 수 없습니다."));
        if (itemEntity.getStoreId() == null) throw new RuntimeException("매장을 찾을 수 없음");
        if (itemEntity.getStock() <=0) throw new RuntimeException("상품 재고 없음");

        Integer successBuyItemNums;
        if(itemNums >= itemEntity.getStock()) successBuyItemNums = itemEntity.getStock();
        else successBuyItemNums = itemNums;

        Integer totalPrice = successBuyItemNums * itemEntity.getPrice();

        itemEntity.setStock(itemEntity.getStock() - successBuyItemNums);

        if(successBuyItemNums == 5) {
            log.error("5개를 구매하는건 허락하지 않습니다.");
            throw new RuntimeException("5개는 구매안돼");
        }

        StoreSales storeSales = storeSalesJpaRepository.findById(itemEntity.getStoreId())
                .orElseThrow(()-> new NotFoundException("해당 아이디로 조회할 수 없습니다."));
        storeSales.setAmount(storeSales.getAmount()+totalPrice);
        return successBuyItemNums;
    }

    public List<Item> findItemsByTypes(List<String> types) {
        List<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findItemEntitiesByTypeIn(types);
        return itemEntities.stream().map(ItemMapper.INSTANCE::itemEntityToItem).collect(Collectors.toList());
    }

    public List<Item> findItemsByOrderByPrices(Integer maxValue) {
        List<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findItemEntitiesByPriceLessThanEqualOrderByPriceAsc(maxValue);
        return itemEntities.stream().map(ItemMapper.INSTANCE::itemEntityToItem).collect(Collectors.toList());
    }

    public Page<Item> findAllWithPageable(Pageable pageable) {
        Page<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findAll(pageable);
        return itemEntities.map(ItemMapper.INSTANCE::itemEntityToItem);
    }

    public Page<Item> findAllWithPageable(List<String> types, Pageable pageable) {
        Page<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findAllByTypeIn(types,pageable);
        return itemEntities.map(ItemMapper.INSTANCE::itemEntityToItem);
    }
}
