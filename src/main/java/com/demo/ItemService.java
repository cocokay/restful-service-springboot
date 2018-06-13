package com.demo;

import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.concurrent.TimeUnit.SECONDS;

@Repository
public class ItemService {

    private static AtomicLong counter = new AtomicLong();
    private final List<Item> items = Collections.synchronizedList(new ArrayList<>());
    private static final int MAX_DURATION = 2;  //within 2 the last seconds
    private static final int MAX_LAST_POSTED = 100; //the last 100 POSTed items

    /**
     * Create a new item object
     * @param element
     * @return an item object
     */
    public Item create(Item element) {
        long id = counter.incrementAndGet();
        if (element==null){
            element = new Item(id);
        }else {
            element.setId(id);
        }
        element.setTimestamp(Instant.now());
        items.add(element);
        return element;
    }

    /**
     * 
     * @return the list of items POSTed in the last 2 seconds or the list of last 100 POSTed items, whichever greater
     * todo
     */
    public List<Item> getItems() {
        if (items.isEmpty()) return items;
        List<Item> collect = new ArrayList<>();
        items.forEach((item) -> {
            long diffAsSeconds = ChronoUnit.SECONDS.between(item.getTimestamp(), Instant.now());
            System.out.println(item.getId() + " timestamp: "+ item.getTimestamp() + " elapsed: -->"
                    + Duration.between(item.getTimestamp(), Instant.now()).getSeconds());
            if (diffAsSeconds<=MAX_DURATION) {
                collect.add(item);
            }
        });

        if (collect.size()>items.size()){
            return collect;
        }else{
            if (items.size()>MAX_LAST_POSTED) {
                return items.subList(0, MAX_LAST_POSTED);
            }
            return items;
        }
    }
}
