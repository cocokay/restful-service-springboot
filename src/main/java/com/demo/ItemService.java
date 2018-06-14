package com.demo;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author VuKD
 */


@Service
public class ItemService {


    public static final int ELAPSED_TIME = 2;  //within 2 the last seconds
    public static final int RECENT_POSTED_SIZE = 100; //the last 100 POSTed items
    private final List<Item> items = Collections.synchronizedList(new ArrayList<>());

    /**
     * Create a new item object
     *
     * @param element
     * @return an item object
     */
    public Item add(Item element) {
        items.add(element);
        return element;
    }

    /**
     * Return a list of itms
     *
     * @return the list of items POSTed in the last 2 seconds or the list of last 100 POSTed items, whichever greater
     */
    public List<Item> getItems() {
        if (items.isEmpty()) return items;
        Duration twoSeconds = Duration.of(1, ChronoUnit.SECONDS);
        List<Item> listRecent2secs = new ArrayList<>();

        items.forEach((item) -> {
            long diffAsSeconds = ChronoUnit.SECONDS.between(item.getTimestamp(), Instant.now());
            if (diffAsSeconds <= ELAPSED_TIME) {
                listRecent2secs.add(item);
            }
        });

        System.out.println("Size: listRecent2secs " + listRecent2secs.size() + " >< total: " + items.size());
        List<Item> listLast100 = new ArrayList<>(items);
        if (items.size() >= RECENT_POSTED_SIZE) {
            listLast100 = items.subList(items.size() - RECENT_POSTED_SIZE, items.size());
            assert (listLast100.size() == 100);
            System.out.println("Size: listRecent2secs " + listRecent2secs.size() + " >< total: " + items.size()
                    + " >< list100: " + listLast100.size());
        }

        if (listRecent2secs.size() > listLast100.size()) {
            System.out.println("The items POSTed in the last 2 seconds: " + listLast100.size());
            return listRecent2secs;
        } else {
            System.out.println("The last POSTed 100 items: " + listLast100.size());
            return listLast100;
        }
    }

    public void clear() {
        items.clear();
    }


}
