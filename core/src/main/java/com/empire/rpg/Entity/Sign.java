package com.empire.rpg.Entity;

import com.empire.rpg.Component.Component;
import com.empire.rpg.Component.SignComponent;

import java.util.Map;
import java.util.UUID;

public class Sign extends Entity {

    public Sign(String name, Map<Class<? extends Component>, Component> components, UUID id) {
        super(name, components, id);
    }

    public void interact() {
        SignComponent signComponent = (SignComponent) getComponent(SignComponent.class);
        if (signComponent != null) {
            // Ouvre ou ferme la pancarte
            signComponent.toggle();
            if (signComponent.isOpen()) {
                System.out.println("La pancarte affiche : " + signComponent.getMessage());
            } else {
                System.out.println("La pancarte est maintenant fermee.");
            }
        }
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

