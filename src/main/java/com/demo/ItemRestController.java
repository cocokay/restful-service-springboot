package com.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;


/**
 * A simple controller class.
 * It returns a result in JSON format
 *
 * @author VuKD
 */

@RestController
@RequestMapping("items")
public class ItemRestController {
    private final AtomicLong counter = new AtomicLong();
    private final ItemService itemService;

    @Autowired
    ItemRestController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<Item> create() {
        return new ResponseEntity<>(itemService.add(new Item(counter.incrementAndGet())), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Collection<Item>> getItems() {
        return new ResponseEntity<>(itemService.getItems(), HttpStatus.OK);
    }

}
