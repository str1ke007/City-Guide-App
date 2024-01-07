package com.jkh.cityguideapp.Models;

import java.util.List;
import java.util.UUID;

public class Location {
    String id;
    int views;

    long createdAt;

    boolean featured;

    String thumbnail;

    String name;

    String description;

    String category;
    List<String> highlights;
    String location;

    public Location() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = System.currentTimeMillis();
        this.views = 0;
        this.featured = false;
        this.thumbnail = "";
        this.description = "";
        this.name = "";
        this.location = "";
    }

    // Update constructor to include the location field
    public Location(String id, int views, boolean featured, String category, String thumbnail,
                    String name, String description, List<String> highlights, Long createdAt,
                    String location) {
        this.id = id;
        this.views = views;
        this.featured = featured;
        this.thumbnail = thumbnail;
        this.name = name;
        this.description = description;
        this.highlights = highlights;
        this.category = category;
        this.createdAt = createdAt;
        this.location = location;
    }

    // Add a getter and setter for the location field
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getViews() {
        return views;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getHighlights() {
        return highlights;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public void setHighlights(List<String> highlights) {
        this.highlights = highlights;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
