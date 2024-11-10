package com.empire.rpg.Entity;
import com.empire.rpg.Component.Component;
import com.empire.rpg.Entity.EntityManager;
import java.util.Map;
import java.util.UUID;

public abstract class Entity extends EntityManager {
    private final UUID id;  // Identifiant unique de l'entité
    private final Map<Class<? extends Component>, Component> components; // Map des composants;;

    public Entity(String name, Map<Class<? extends Component>, Component> components, UUID id) {
        super(name);
        this.components = components;
        this.id = id;
    }

    public abstract Entity addEntity();
    public abstract Entity removeEntity(String name);

    @Override
    public void createEntity(UUID id) {
        System.out.println("Creating entity with id: " + id);
    }

    public UUID getId() {
        return id;
    }

    public Map<Class<? extends Component>, Component> getComponents() {
        return components;
    }


    public void addComponent(Class<? extends Component> component, Component value) {
        components.put(component, value );
    }


    public void removeComponent(Class<? extends Component> component) {
        components.remove(component);
    }

    public Component getComponent(Class<? extends Component> component) {
        return components.get(component);
    }

    public void updateComponent(Class<? extends Component> component, Component value) {
        components.replace(component, value);
    }

    public void updateComponents(Map<Class<? extends Component>, Component> components) {
        this.components.putAll(components);
    }

    public void removeComponents() {
        components.clear();
    }

    public void updateEntity(Entity entity) {
        this.components.putAll(entity.getComponents());
    }

    public void removeEntity() {
        components.clear();
    }


}
