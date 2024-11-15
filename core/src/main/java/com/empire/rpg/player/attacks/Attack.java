package com.empire.rpg.player.attacks;

import com.empire.rpg.player.animations.AnimationState;
import com.empire.rpg.player.audio.SoundManager;

import java.util.Map;

public class Attack {
    private String id;
    private String name;
    private String categoryKey;
    private Map<String, AnimationState> animationStates;
    private float duration;
    private float damage;
    private float cooldown;
    private float hitboxWidth;
    private float hitboxHeight;

    public Attack(String id, String name, String categoryKey, Map<String, AnimationState> animationStates, float duration, float damage, float cooldown, float hitboxWidth, float hitboxHeight) {
        this.id = id;
        this.name = name;
        this.categoryKey = categoryKey;
        this.animationStates = animationStates;
        this.duration = duration;
        this.damage = damage;
        this.cooldown = cooldown;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategoryKey() {
        return categoryKey;
    }

    public Map<String, AnimationState> getAnimationStates() {
        return animationStates;
    }

    public float getDuration() {
        return duration;
    }

    public float getDamage() {
        return damage;
    }

    public float getCooldown() {
        return cooldown;
    }

    public float getHitboxWidth() {
        return hitboxWidth;
    }

    public float getHitboxHeight() {
        return hitboxHeight;
    }

    // Méthode pour jouer le son associé à l'attaque
    public void playSound() {
        SoundManager.playSound(this.id);
    }
}
