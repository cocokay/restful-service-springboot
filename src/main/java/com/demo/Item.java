package com.demo;

import java.time.Instant;

public class Item {
    private long id;
    private Instant timestamp;

    public Item(long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp){
        this.timestamp = timestamp;
    }
}
