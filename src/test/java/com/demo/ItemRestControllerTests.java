package com.demo;

import net.minidev.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemRestControllerTests {

    private final AtomicLong counter = new AtomicLong();
    private final String URL = "/items/";
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;
    //@MockBean
    @Autowired
    private ItemService itemService;

    @Before
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }


    // ================== Initial ==================
    @Test
    public void testGetInitial() throws Exception {
        itemService.clear();
        this.mvc.perform(get(URL)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0))).andReturn();
    }


    // ================== Create Item (s) ==================

    @Test
    public void testCreateItem() throws Exception {
        itemService.clear();
        assertEquals(0, itemService.getItems().size());
        itemService.add(new Item(counter.incrementAndGet()));
        assertEquals(1, itemService.getItems().size());

        this.mvc.perform(get(URL)).andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(JSONArray.class)))
                //.andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].timestamp", isA(String.class)))
                .andExpect(jsonPath("$", hasSize(1))).andReturn();

    }

    @Test
    public void testPost() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect((status().isCreated()));
    }


    // ================== Get the items POSTed in the last 2 seconds ==================
    @Test
    public void testGetLastPOSTedByTime() {

        List<Item> listOfAllItemsDisplay = new ArrayList<>();

        int createdSize = ItemService.RECENT_POSTED_SIZE + 10;//test with a random number

        ItemService ir = new ItemService();
        for (int i = 0; i < createdSize; i++) {
                ir.add(new Item(counter.incrementAndGet()));
        }

        listOfAllItemsDisplay.addAll(ir.getItems());

        assertEquals(createdSize, listOfAllItemsDisplay.size());

        listOfAllItemsDisplay.forEach((item) -> {
            assert (Instant.now().getEpochSecond() - item.getTimestamp().getEpochSecond() <= ItemService.ELAPSED_TIME);
        });
    }


    // ================== Get the last 100 POSTed Items ==================
    @Test
    public void testGetLastPOSTedBySize() throws Exception {

        List<Item> listOfAllItemsCreated = new ArrayList<>();
        List<Item> listOfAllItemsDisplay = new ArrayList<>();

        int createdSize = ItemService.RECENT_POSTED_SIZE + 10;//test with a random number > 100

        ItemService ir = new ItemService();
        for (int i = 0; i < createdSize; i++) {
            listOfAllItemsCreated.add(ir.add(new Item(counter.incrementAndGet())));
        }

        Thread.sleep(3000); // 2secs passed

        assertEquals(createdSize, listOfAllItemsCreated.size());  //assert the number of items added
        listOfAllItemsCreated.forEach((item) -> {   //assert the timestamp older than 2secs
            assert (item.getTimestamp().getEpochSecond() < Instant.now().minusSeconds(ItemService.ELAPSED_TIME).getEpochSecond());
        });

        listOfAllItemsDisplay.addAll(ir.getItems());

        //expect getting the list of last 100 POSTed items
        //assert the displayed items <=100
        assertEquals(ItemService.RECENT_POSTED_SIZE, listOfAllItemsDisplay.size());
    }

}
