package com.empire.rpg.player.attacks;

import com.empire.rpg.player.animations.AnimationState;
import java.util.Map;

public class Attack {
    private String id; // ex : "ONE_SLASH1"
    private String name; // ex : "Fente tranchante"
    private String categoryKey; // ex : "ONE3"
    private Map<String, AnimationState> animationStates; // Clé : direction ("UP", "DOWN", "LEFT", "RIGHT"), Valeur : AnimationState
    private float duration; // Durée de l'attaque
    private float damage; // Dégâts infligés
    private float cooldown; // Temps de récupération

    // Constructeur
    public Attack(String id, String name, String categoryKey, Map<String, AnimationState> animationStates, float duration, float damage, float cooldown) {
        this.id = id;
        this.name = name;
        this.categoryKey = categoryKey;
        this.animationStates = animationStates;
        this.duration = duration;
        this.damage = damage;
        this.cooldown = cooldown;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategoryKey() { return categoryKey; }
    public Map<String, AnimationState> getAnimationStates() { return animationStates; }
    public float getDuration() { return duration; }
    public float getDamage() { return damage; }
    public float getCooldown() { return cooldown; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCategoryKey(String categoryKey) { this.categoryKey = categoryKey; }
    public void setAnimationStates(Map<String, AnimationState> animationStates) { this.animationStates = animationStates; }
    public void setDuration(float duration) { this.duration = duration; }
    public void setDamage(float damage) { this.damage = damage; }
    public void setCooldown(float cooldown) { this.cooldown = cooldown; }
}
