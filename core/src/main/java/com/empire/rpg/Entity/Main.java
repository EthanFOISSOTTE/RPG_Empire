package com.empire.rpg.Entity;

import com.empire.rpg.Component.*;
import java.util.Map;
import java.util.UUID;
import com.empire.rpg.System.*;

public class Main {
    public static void main(String[] args) {
        // Création d'une entité Player
        EntityManager entityManager2 = new EntityManager("Gandalf") {
            @Override
            public void createEntity(UUID id) {
                System.out.println("Creating entity with id: " + id);
            }

            @Override
            public Entity addEntity() {
                Map<Class<? extends Component>, Component> components = Map.of(
                    HealthComponent.class, new HealthComponent(70, 100),
                    PositionComponent.class, new PositionComponent(0, 0),
                    WeaponComponent.class, new WeaponComponent("Epee", 15)
                );
                UUID id = UUID.randomUUID();
                return new Player("Gandalf", components, id);
            }
        };

        System.out.println("-----------------------------");
        System.out.println("LANCEMENT DU JEU");
        System.out.println("-----------------------------");

        Entity player = entityManager2.addEntity();
        entityManager2.createEntity(player.getId());
        displayEntityDetails(player, entityManager2);

        // Création d'une entité MOB
        EntityManager entityManager = new EntityManager("Balrog") {
            @Override
            public void createEntity(UUID id) {
                System.out.println("Creating entity with id: " + id);
            }

            @Override
            public Entity addEntity() {
                Map<Class<? extends Component>, Component> components = Map.of(
                    HealthComponent.class, new HealthComponent(50, 50),
                    PositionComponent.class, new PositionComponent(0, 0),
                    WeaponComponent.class, new WeaponComponent("Boules de feu", 10),
                    MovementComponent.class, new MovementComponent(1.5f, "Nord"),
                    CollisionComponent.class, new CollisionComponent(true)
                );
                UUID id = UUID.randomUUID();
                return new MOB("Balrog", components, id);
            }
        };

        Entity mob = entityManager.addEntity();
        entityManager.createEntity(mob.getId());
        displayEntityDetails(mob, entityManager);

        // Création d'une entité HealthPotion
        HealthPotion healthPotion = new HealthPotion(5, 5, 20);
        healthPotion.addEntity();
        System.out.println("Le joueur trouve une potion de vie pour se soigner: " + ((HealingComponent) healthPotion.getComponent(HealingComponent.class)).getHealingPoints() + " HP");

        // Création d'une entité Sign
        Sign sign = new Sign("Pancarte", Map.of(
            SignComponent.class, new SignComponent("You shall not pass! ")
        ), UUID.randomUUID());

        // Interagir avec la pancarte
        System.out.println("Le joueur interagit avec la pancarte...");
        sign.interact(); // Affiche le message

        System.out.println("Le joueur interagit de nouveau avec la pancarte...");
        sign.interact(); // Ferme la pancarte

        // Création des systèmes

        // Création du système d'inventaire
        InventorySystem inventorySystem = new InventorySystem();

        // Création du système de combat
        FightSystem fightSystem = new FightSystem(new MOB("Balrog", Map.of(
            HealthComponent.class, new HealthComponent(50, 50),
            PositionComponent.class, new PositionComponent(0, 0),
            WeaponComponent.class, new WeaponComponent("Boules de feu", 10),
            MovementComponent.class, new MovementComponent(1.5f, "Forward"),
            CollisionComponent.class, new CollisionComponent(true)
        ), UUID.randomUUID()));


        // Ajout d'items à l'inventaire du joueur
        Item healthPotion20 = new Item("Potion de vie", 20, "Healing");
        Item goldCoin5 = new Item("Pièce d'or", 5, "Money");
        inventorySystem.addItem(healthPotion20, 2);
        inventorySystem.addItem(goldCoin5, 10);

        // Création d'un ennemi
        UUID mobId = UUID.randomUUID();
        MOB enemy = new MOB("Orc", Map.of(
            HealthComponent.class, new HealthComponent(80, 80),
            PositionComponent.class, new PositionComponent(7, 7),
            WeaponComponent.class, new WeaponComponent("Club", 15),
            DefenseComponent.class, new DefenseComponent(3, 3)
        ), mobId);

        // Affichage initial de l'inventaire et des positions
        System.out.println("=== État Initial du Joueur ===");
        inventorySystem.displayInventory();
        System.out.println("Position du joueur : " + ((PositionComponent) player.getComponent(PositionComponent.class)).getX() + ", " + ((PositionComponent) player.getComponent(PositionComponent.class)).getY());

        // Simulation de combat entre le joueur et l'ennemi
        System.out.println("\n=== Combat Simulation ===");
        fightSystem.attack(enemy, 40);  // Player attaque l'Orc
        fightSystem.attack(player, 10);  // Orc contre-attaque

        // Affichage des états après combat
        System.out.println("\n=== État Après Combat ===");
        System.out.println("Vie du joueur : " + (player.getComponent(HealthComponent.class)).getCurrentHealthPoints() + " / " + ((HealthComponent) player.getComponent(HealthComponent.class)).getMaxHealthPoints());
        System.out.println("Vie de l'ennemi : " + (enemy.getComponent(HealthComponent.class)).getCurrentHealthPoints() + " / " + ((HealthComponent) enemy.getComponent(HealthComponent.class)).getMaxHealthPoints());

        // Test de l'inventaire : utilisation de la potion de vie
        System.out.println("\n=== Utilisation d'une Potion de Vie ===");
        if (inventorySystem.hasItem(healthPotion20)) {
            HealthComponent health = (HealthComponent) player.getComponent(HealthComponent.class);

            // Soigne le joueur de 30 points de vie
            new HealingComponent(30);
            health.setCurrentHealthPoints(health.getCurrentHealthPoints() + 30);
            inventorySystem.removeItem(healthPotion20, 1);  // Retire une potion de l'inventaire
        }

        // Affichage de l'inventaire et de la santé après l'utilisation de la potion
        System.out.println("\n=== État Final ===");
        inventorySystem.displayInventory();
        System.out.println("Vie du joueur : " + ((HealthComponent) player.getComponent(HealthComponent.class)).getCurrentHealthPoints() + " / " + ((HealthComponent) player.getComponent(HealthComponent.class)).getMaxHealthPoints());
    }

    // Affiche les détails de l'entité
    private static void displayEntityDetails(Entity entity, EntityManager entityManager) {
        System.out.println("Creating " + entity.getClass().getSimpleName() + " entity with caracteristics:");
        System.out.println(entityManager.getName() + " apparait et il a les caracteristiques suivantes : ");
        HealthComponent health = (HealthComponent) entity.getComponent(HealthComponent.class);
        PositionComponent position = (PositionComponent) entity.getComponent(PositionComponent.class);
        WeaponComponent weapon = (WeaponComponent) entity.getComponent(WeaponComponent.class);
        MovementComponent movement = (MovementComponent) entity.getComponent(MovementComponent.class);
        CollisionComponent collision = (CollisionComponent) entity.getComponent(CollisionComponent.class);

        if (health != null) {
            System.out.println("Vie: " + health.getCurrentHealthPoints() + " / " + health.getMaxHealthPoints());
        }
        if (position != null) {
            System.out.println("Position: " + position.getX() + ", " + position.getY());
        }
        if (weapon != null) {
            System.out.println("Arme: " + weapon.getName() + " (Puissance: " + weapon.getDamage() + ")");
        }
        if (movement != null) {
            System.out.println("Mouvement: Vitesse " + movement.getSpeed() + ", Direction " + movement.getDirection());
        }
        if (collision != null) {
            System.out.println("Collision activee: " + collision.isCollidable());
        }
        System.out.println();
    }
}

