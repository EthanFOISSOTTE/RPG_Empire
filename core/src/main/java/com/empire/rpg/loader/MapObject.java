package com.empire.rpg.loader;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class MapObject {
        @JsonProperty("id")
        private int id;

        @JsonProperty("type")
        private String type;

        @JsonProperty("x")
        private int x;

        @JsonProperty("y")
        private int y;

        @JsonProperty("properties")
        private Map<String, Object> properties;

        // Getters et Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public int getX() { return x; }
        public void setX(int x) { this.x = x; }

        public int getY() { return y; }
        public void setY(int y) { this.y = y; }

        public Map<String, Object> getProperties() { return properties; }
        public void setProperties(Map<String, Object> properties) { this.properties = properties; }
    }
