package com.github.supercoding.web.controller;

import com.github.supercoding.service.ElectronicStoreItemService;
import com.github.supercoding.web.dto.items.BuyOrder;
import com.github.supercoding.web.dto.items.Item;
import com.github.supercoding.web.dto.items.ItemBody;
import com.github.supercoding.web.dto.items.StoreInfo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ElectronicStoreController {
    private final ElectronicStoreItemService electronicStoreItemService;

    @Operation(summary = "모든 아이템 조회", description = "저장된 모든 아이템의 리스트를 반환합니다.")
    @GetMapping("/items")
    public List<Item>findAllItem(){
        log.info("GET /items 요청이 들어왔습니다.");
        List<Item> items = electronicStoreItemService.findAllItem();
        log.info("GET /items 응답 : "+ items);
        return items;
    }
    @PostMapping("/items")
    public String registerItem(@RequestBody ItemBody itemBody){
        Integer itemId = electronicStoreItemService.saveItem(itemBody);
        return "ID : "+ itemId;
    }
    @GetMapping("/items/{id}")
    public Item findItemByPathId(@PathVariable String id){
        return electronicStoreItemService.findItemById(id);
    }
    @GetMapping("/items-query")
    public Item findItemByQueryId(@RequestParam("id") String id){
        return electronicStoreItemService.findItemById(id);
    }
    @GetMapping("/items-queries")
    public List<Item> findItemByQueryIds(@RequestParam("id") List<String> ids){
        log.info("/items-queries 요청 ids: "+ids);
        List<Item> items = electronicStoreItemService.findItemsByIds(ids);
        log.info("/items-queries 응답 "+items);
        return items;
    }
    @DeleteMapping("/items/{id}")
    public String deleteItemByPathId(@PathVariable String id){
        electronicStoreItemService.deleteItem(id);
        return "Object with id = "+ id +" has been deleted";
    }

    @PutMapping("/items/{id}")
    public Item updateItem(@PathVariable String id , @RequestBody ItemBody itemBody){
        return electronicStoreItemService.updateItem(id , itemBody);
    }

    @PostMapping("/items/buy")
    public String buyItem(@RequestBody BuyOrder buyOrder){
        Integer orderItemNums = electronicStoreItemService.buyItems(buyOrder);
        return "요청하신 item 중 "+orderItemNums +" 개를 구매함.";
    }

    @GetMapping("/items-types")
    public List<Item> findItemByTypes(@RequestParam("type") List<String> types){
        log.info("/items-types 요청 types: "+types);
        List<Item> items = electronicStoreItemService.findItemsByTypes(types);
        log.info("/items-types 응답 "+items);
        return items;
    }
    @GetMapping("/items-prices")
    public List<Item> findItemByPrices(@RequestParam("max") Integer maxValue){
        List<Item> items = electronicStoreItemService.findItemsOrderByPrices(maxValue);

        return items;
    }

    @GetMapping("/items-page")
    public Page<Item> findItemsPagination(Pageable pageable){
        return electronicStoreItemService.findAllWithPageable(pageable);
    }

    @GetMapping("/items-types-page")
    public Page<Item> findItemsPagination(@RequestParam("type")List<String> types, Pageable pageable){
        return electronicStoreItemService.findAllWithPageable(types,pageable);
    }

    @GetMapping("/stores")
    public List<StoreInfo> findAllStoreInfo(){
        return electronicStoreItemService.findAllStoreInfo();
    }
}
