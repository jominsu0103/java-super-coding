package com.github.supercoding.web.controller;

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
    @GetMapping("/items")
    public List<Item>findAllItem(){
     return items;
    }
    @PostMapping("/items")
    public String registerItem(@RequestBody ItemBody itemBody){
        Item newItem = new Item(serialItemId++, itemBody);
        items.add(newItem);
        return "ID : "+newItem.getId();
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
        Item itemFounded = items.stream()
                .filter((item -> item.getId().equals(id)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException());
        items.remove(itemFounded);

        Item itemUpdated = new Item(Integer.valueOf(id), itemBody);
        items.add(itemUpdated);

        return itemUpdated;
    }
}
