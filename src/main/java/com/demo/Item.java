package com.demo;

import java.time.Instant;

/**
 * A simple object (model) class
 *
 * @author VuKD
 */
public class Item {

    private long id;
    private Instant timestamp;

    public Item(long id) {
        this.id = id;
        this.timestamp = Instant.now();
    }

    public long getId() {
        return id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Id: ").append(this.id).append(", timestamp: ").append(this.timestamp);
        return sb.toString();
    }
}
