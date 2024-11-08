package com.empire.rpg.Entity;

import com.empire.rpg.Component.HealthComponent;
import com.empire.rpg.Component.WeaponComponent;

public class EntityTest {
    public static void main(String[] args) {
        // Création d'une entité Player et ajout de composants
        Player player = new Player("Gandalf", new HealthComponent(100, 100), new WeaponComponent("Sword", 30), 10);
        System.out.println("Player : " + player.getName() + " with health: " + player.getHealth().getCurrentHealthPoints() + " and weapon: " + player.getWeapon().getName());

        // Création d'une entité MOB et ajout de composants
        MOB mob = new MOB("Saruman", new HealthComponent(50,50), 10);
        HealthComponent healthMob = new HealthComponent(50, 50);
        mob.addComponent(healthMob);
        System.out.println("MOB : " + mob.getName() + " with health: " + mob.getComponent(HealthComponent.class).getCurrentHealthPoints() + " and attack power: " + mob.getAttackPower());

        // Le player attaque le MOB jusqu'à ce qu'il soit tué
        while (mob.getComponent(HealthComponent.class).getCurrentHealthPoints() > 0) {
            player.attack(mob);
            mob.getComponent(HealthComponent.class).takeDamage(player.getWeapon().getAttackPoints());
            System.out.println("MOB health: " + mob.getComponent(HealthComponent.class).getCurrentHealthPoints() + " after player attack");
        }

        // Vérifie si le MOB est mort
        if (mob.getComponent(HealthComponent.class).getCurrentHealthPoints() <= 0) {
            System.out.println("MOB is dead");
        }
    }
}
