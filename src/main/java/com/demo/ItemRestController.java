package com.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("items")
public class ItemRestController {

    ItemService itemService;

    @Autowired
    ItemRestController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<Item> create(@RequestBody(required=false) Item item) {
        return new ResponseEntity<>(itemService.create(item), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Collection<Item>> getItems() {
        return new ResponseEntity<>(itemService.getItems(), HttpStatus.OK);
    }

}
