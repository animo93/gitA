package com.example.animo.gita.model;

import java.util.List;

/**
 * Created by animo on 3/12/17.
 */

public class WebHookRegister {

    private String id;
    private String url;
    private String test_url;
    private String ping_url;
    private String name;
    private boolean active;
    private Config config;
    private List<String> events;

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTest_url() {
        return test_url;
    }

    public void setTest_url(String test_url) {
        this.test_url = test_url;
    }

    public String getPing_url() {
        return ping_url;
    }

    public void setPing_url(String ping_url) {
        this.ping_url = ping_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}
