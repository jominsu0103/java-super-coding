package com.github.supercoding.web.controller;

import com.github.supercoding.service.ElectronicStoreItemService;
import com.github.supercoding.web.dto.BuyOrder;
import com.github.supercoding.web.dto.Item;
import com.github.supercoding.web.dto.ItemBody;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ElectronicStoreController {
    private ElectronicStoreItemService electronicStoreItemService;

    public ElectronicStoreController(ElectronicStoreItemService electronicStoreItemService) {
        this.electronicStoreItemService = electronicStoreItemService;
    }

    @GetMapping("/items")
    public List<Item>findAllItem(){
     return electronicStoreItemService.findAllItem();
    }
    @PostMapping("/items")
    public String registerItem(@RequestBody ItemBody itemBody){
//        Item newItem = new Item(serialItemId++, itemBody);
//        items.add(newItem);
//        return "ID : "+newItem.getId();
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
        return electronicStoreItemService.findItemsByIds(ids);
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
}
