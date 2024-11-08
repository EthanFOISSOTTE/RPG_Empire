package com.empire.rpg.Entity;
import com.empire.rpg.Component.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Entity implements Component {
    private final UUID id;  // Identifiant unique de l'entité
    private final Map<Class<? extends Component>, Component> components; // Map des composants


    public Entity() {
        this.id = UUID.randomUUID();
        this.components = new HashMap<>();
    }

    public UUID getId() {
        return id;
    }

    // Ajouter un composant à l'entité
    public <T extends Component> void addComponent(T component) {
        components.put(component.getClass(), component);
    }

    // Récupérer un composant par son type
    public <T extends Component> T getComponent(Class<T> componentType) {
        return componentType.cast(components.get(componentType));
    }

    // Vérifie si l'entité possède un composant de ce type
    public <T extends Component> boolean hasComponent(Class<T> componentType) {
        return components.containsKey(componentType);
    }

    // Supprime un composant de l'entité
    public <T extends Component> void removeComponent(Class<T> componentType) {
        components.remove(componentType);
    }

}
