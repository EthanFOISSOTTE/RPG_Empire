package com.empire.rpg.Entity;

import com.empire.rpg.Component.*;

import java.util.Map;
import java.util.UUID;

public class MOB extends Entity{

    public MOB(String name, Map<Class<? extends Component>, Component> components, UUID id) {
        super(name, components, id);
    }

    @Override
    public Entity addEntity() {
        return null;
    }

    @Override
    public Entity removeEntity(String name) {
        return null;
    }

}
