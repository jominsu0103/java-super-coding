package com.github.supercoding.web.controller;

import com.github.supercoding.repository.ElectronalStoreItemRepository;
import com.github.supercoding.repository.ItemEntity;
import com.github.supercoding.web.dto.Item;
import com.github.supercoding.web.dto.ItemBody;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ElectronicStoreController {
    private static int serialItemId = 1;
    private List<Item> items = new ArrayList<>();

    private ElectronalStoreItemRepository electronalStoreItemRepository;


    public ElectronicStoreController(ElectronalStoreItemRepository electronalStoreItemRepository) {
        this.electronalStoreItemRepository = electronalStoreItemRepository;
    }

    @GetMapping("/items")
    public List<Item>findAllItem(){
     List<ItemEntity> itemEntities = electronalStoreItemRepository.findAllItems();
     return itemEntities.stream().map(Item::new).collect(Collectors.toList());
    }
    @PostMapping("/items")
    public String registerItem(@RequestBody ItemBody itemBody){
//        Item newItem = new Item(serialItemId++, itemBody);
//        items.add(newItem);
//        return "ID : "+newItem.getId();
        ItemEntity itemEntity = new ItemEntity(null, itemBody.getName(), itemBody.getType(),
                itemBody.getPrice(), itemBody.getSpec().getCpu(), itemBody.getSpec().getCapacity()
        );
        Integer itemId = electronalStoreItemRepository.saveItem(itemEntity);
        return "ID : "+ itemId;
    }
    @GetMapping("/items/{id}")
    public Item findItemByPathId(@PathVariable String id){
        Item itemFounded = items.stream()
                                .filter((item -> item.getId().equals(id)))
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException());
        return itemFounded;
    }
    @GetMapping("/items-query")
    public Item findItemByQueryId(@RequestParam("id") String id){
        Item itemFounded = items.stream()
                .filter((item -> item.getId().equals(id)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException());
        return itemFounded;
    }
    @GetMapping("/items-queries")
    public List<Item> findItemByQueryIds(@RequestParam("id") List<String> ids){
        Set<String> IdSet = ids.stream().collect(Collectors.toSet());
        List<Item> itemsFounded = items.stream()
                                        .filter((item -> IdSet.contains(item.getId())))
                                        .collect(Collectors.toList());
        return itemsFounded;
    }
    @DeleteMapping("/items/{id}")
    public String deleteItemByPathId(@PathVariable String id){
        Item itemFounded = items.stream()
                .filter((item -> item.getId().equals(id)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException());
        items.remove(itemFounded);
        return "Object with id = "+itemFounded.getId()+" has been deleted";
    }

    @PutMapping("/items/{id}")
    public Item updateItem(@PathVariable String id , @RequestBody ItemBody itemBody){
        Integer idInt = Integer.valueOf(id);
        ItemEntity itemEntity = new ItemEntity(idInt, itemBody.getName(), itemBody.getType(), itemBody.getPrice(), itemBody.getSpec().getCpu(), itemBody.getSpec().getCapacity());
        ItemEntity itemEntityUpdated = electronalStoreItemRepository.updateItemEntity(idInt, itemEntity);
        Item itemUpdated = new Item(itemEntityUpdated);
        return itemUpdated;
    }
}
