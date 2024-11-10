package com.empire.rpg.Entity;


import com.empire.rpg.Component.Component;
import java.util.Map;
import java.util.UUID;

public class Player extends Entity  {

    @Override
    public Entity addEntity() {
        return null;
    }

    @Override
    public Entity removeEntity(String name) {
        return null;
    }

    public Player(String name, Map<Class<? extends Component>, Component> components, UUID id) {
        super(name, components, id);
    }

}
