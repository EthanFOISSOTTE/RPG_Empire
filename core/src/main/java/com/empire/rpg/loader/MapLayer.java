package com.empire.rpg.loader;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class MapLayer {
        @JsonProperty("name")
        private String name;

        @JsonProperty("type")
        private String type;

        @JsonProperty("properties")
        private Map<String, Object> properties;

        @JsonProperty("objects")
        private List<MapObject> objects;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public Map<String, Object> getProperties() { return properties; }
        public void setProperties(Map<String, Object> properties) { this.properties = properties; }

        public List<MapObject> getObjects() { return objects; }
        public void setObjects(List<MapObject> objects) { this.objects = objects; }
    }

