package com.empire.rpg.Entity;

import com.empire.rpg.Component.CollisionComponent;
import com.empire.rpg.Component.Component;
import com.empire.rpg.Component.MovementComponent;
import com.empire.rpg.Component.PositionComponent;

import java.util.Map;
import java.util.UUID;

public class PNJ extends Entity{
    private String dialog;
    private int progress;

    @Override
    public PNJ addEntity() {
        return new PNJ(name, Map.of(
            PositionComponent.class, new PositionComponent(0, 0),
            MovementComponent.class, new MovementComponent(1.5f, "north"),
            CollisionComponent.class, new CollisionComponent(true)
        ), UUID.randomUUID(), "Hello", 0);
    }

    @Override
    public Entity removeEntity(String name) {
        return null;
    }

    public PNJ(String name, Map<Class<? extends Component>, Component> components, UUID id, String dialog, int progress) {
        super(name, components, id);
        this.dialog = dialog;
        this.progress = progress;
    }

    public String getDialog() {
        return dialog;
    }

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }

    public void interact(Player player){
        System.out.println(this.dialog);
    }

    public void updateProgress(){
        this.progress++;
    }

    @Override
    public void createEntity(UUID id) {

    }
}
