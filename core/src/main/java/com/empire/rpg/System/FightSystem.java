package com.empire.rpg.System;

import com.empire.rpg.Component.DefenseComponent;
import com.empire.rpg.Component.HealthComponent;
import com.empire.rpg.Component.WeaponComponent;
import com.empire.rpg.Entity.Entity;

/**
 * Système de Combat (FightSystem)
 * Utilisé pour gérer les interactions de combat, ce système s'assure que les attaques et les réductions de points de vie
 * sont correctement appliquées. Il s'appuie sur HealthComponent, DefenseComponent, et WeaponComponent pour
 * calculer les dégâts infligés.
 */

public class FightSystem {
    private Entity entityTarget;
    private HealthComponent healthComponent;
    private DefenseComponent defenseComponent;
    private WeaponComponent weaponComponent;
    private int damage;
    private int health;

    public FightSystem(Entity entityTarget) {
        this.entityTarget = entityTarget;
        this.healthComponent = (HealthComponent) entityTarget.getComponent(HealthComponent.class);
        this.defenseComponent = (DefenseComponent) entityTarget.getComponent(DefenseComponent.class);
        this.weaponComponent = (WeaponComponent) entityTarget.getComponent(WeaponComponent.class);
        this.damage = 0;
        this.health = 0;
    }

    public Entity getEntityTarget() {
        return entityTarget;
    }

    public void setEntityTarget(Entity entityTarget) {
        this.entityTarget = entityTarget;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public HealthComponent getHealthComponent() {
        return healthComponent;
    }

    public void setHealthComponent(HealthComponent healthComponent) {
        this.healthComponent = healthComponent;
    }

    public DefenseComponent getDefenseComponent() {
        return defenseComponent;
    }

    public void setDefenseComponent(DefenseComponent defenseComponent) {
        this.defenseComponent = defenseComponent;
    }

    public WeaponComponent getWeaponComponent() {
        return weaponComponent;
    }

    public void setWeaponComponent(WeaponComponent weaponComponent) {
        this.weaponComponent = weaponComponent;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;

    }
    public void attack(Entity target, int damage) {
        // Vérification de la validité de la cible
        if (target == null) {
            System.out.println("Cible invalide");
            return;
        }

        // Vérification de la validité de l'arme
        if (weaponComponent == null) {
            System.out.println("Arme invalide");
            return;
        }

        // Calcul des dégâts

        if (target.getComponent(DefenseComponent.class) != null) {
            setDamage(weaponComponent.getDamage() - target.getComponent(DefenseComponent.class).getDamageReduction());
            healthComponent.setCurrentHealthPoints(healthComponent.getCurrentHealthPoints() - damage);
        } else {
            setDamage(weaponComponent.getDamage());
            healthComponent.setCurrentHealthPoints(healthComponent.getCurrentHealthPoints() - damage);
        }

        // Application des dégâts
        target.getComponent(HealthComponent.class).setCurrentHealthPoints(target.getComponent(HealthComponent.class).getCurrentHealthPoints() - getDamage());

        // Affichage des dégâts infligés
        System.out.println("Dégâts infligés: " + getDamage());

        // Affichage des points de vie restants
        System.out.println("Points de vie restants: " + target.getComponent(HealthComponent.class).getCurrentHealthPoints());
    }





}
