package com.empire.rpg;

import com.empire.rpg.component.*;
import com.empire.rpg.entity.*;
import com.empire.rpg.system.FightSystem;
import com.empire.rpg.system.InventorySystem;

import java.util.Map;
import java.util.UUID;

public class GameEngine {
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
                    PositionComponent.class, new PositionComponent(100 , 200),
                    WeaponComponent.class, new WeaponComponent("Epee magique", 15)
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
        EntityManager entityManager = new EntityManager("MOB") {
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
                return new MOB("MOB", components, id);
            }
        };

        MOB mobBalrog = (MOB) entityManager.addEntity();
        entityManager.createEntity(mobBalrog.getId());
        displayEntityDetails(mobBalrog, entityManager);

        // Création d'une entité HealthPotion
        HealthPotion healthPotion = new HealthPotion(5, 5, 20);
        healthPotion.addEntity();
        System.out.println("Gandalf a trouve une potion de vie pour se soigner: " + ((HealingComponent) healthPotion.getComponent(HealingComponent.class)).getHealingPoints() + " HP");

        // Création d'une entité Sign
        Sign sign = new Sign("Pancarte", Map.of(
            SignComponent.class, new SignComponent("You shall not pass! ")
        ), UUID.randomUUID());

        // Interagir avec la pancarte
        System.out.println("Le joueur interagit avec la pancarte...");
        sign.interact(); // Affiche le message

        System.out.println("Le joueur interagit de nouveau avec la pancarte...");
        sign.interact(); // Ferme la pancarte

        // Création du système d'inventaire
        InventorySystem inventorySystem = new InventorySystem();

        // Création du système de combat
        FightSystem fightSystem = new FightSystem(mobBalrog);


        // Ajout d'items à l'inventaire du joueur
        Item healthPotion20 = new Item("Potion de vie", 20, "Sante");
        Item goldCoin5 = new Item("Piece d'or", 5, "Monnaie");
        inventorySystem.addItem(healthPotion20, 2);
        inventorySystem.addItem(goldCoin5, 10);

        // Affichage initial de l'inventaire et des positions
        System.out.println("=== Etat Initial du Joueur ===");
        inventorySystem.displayInventory();
        System.out.println("Position du joueur : " + ((PositionComponent) player.getComponent(PositionComponent.class)).getX() + ", " + ((PositionComponent) player.getComponent(PositionComponent.class)).getY());

        // Apparition d'un MOB ennemi
        Entity enemy = entityManager.addEntity();
        entityManager.createEntity(enemy.getId());
        displayEntityDetails(enemy, entityManager);


        // Simulation de combat entre le joueur et l'ennemi
        System.out.println("\n=== Combat Simulation ===");
        fightSystem.attack(enemy, 50);  // Player attaque l'Orc
        System.out.println("Gandalf attaque l'Orc avec son epee magique et lui inflige " + fightSystem.getDamage() + " points de degats");
        fightSystem.attack(player, 10);  // Orc contre-attaque
        System.out.println("L'Orc contre-attaque et inflige " + fightSystem.getDamage() + " points de degats a Gandalf");
        fightSystem.attack(enemy, 30);  // Player attaque l'Orc et le tue
        System.out.println("Gandalf attaque l'Orc avec son epee magique et lui inflige " + fightSystem.getDamage() + " points de degats de plus");


        // Affichage des états après combat
        System.out.println("\n=== Etat Apres Combat ===");
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
        System.out.println("\n=== Etat Final ===");
        inventorySystem.displayInventory();
        System.out.println("Vie du joueur : " + ((HealthComponent) player.getComponent(HealthComponent.class)).getCurrentHealthPoints() + " / " + ((HealthComponent) player.getComponent(HealthComponent.class)).getMaxHealthPoints());
    }

    // Affiche les détails de l'entité
    private static void displayEntityDetails(Entity entity, EntityManager entityManager) {
        System.out.println("Creating " + entity.getClass().getSimpleName() + " entity with caracteristics:");
        System.out.println(entityManager.getName() + " apparait et a les caracteristiques suivantes : ");
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

