package com.empire.rpg.Entity;


import com.empire.rpg.Component.*;
import java.util.Map;
import java.util.UUID;

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
                return new MOB("Orc", components, id);
            }
        };

        Entity mob = entityManager.addEntity();
        entityManager.createEntity(mob.getId());
        displayEntityDetails(mob, entityManager);

        // Création d'une entité HealthPotion
        HealthPotion healthPotion = new HealthPotion(5, 5, 20);
        healthPotion.addEntity();
        System.out.println("Le joueur trouve une potion de vie pour se soigner: " + ((HealingComponent) healthPotion.getComponent(HealingComponent.class)).getHealingPoints() + " HP");
        // Création d'une entité Coin
        Coin coin = new Coin(10, 10, 5);
        coin.addEntity();
        System.out.println("Gandalf a trouve " + ((ValueComponent) coin.getComponent(ValueComponent.class)).getValue() + " euros par terre!");

        // Création d'une entité Sign
        Sign sign = new Sign("Pancarte", Map.of(
            SignComponent.class, new SignComponent("You shall not pass! ")
        ), UUID.randomUUID());

        // Interagir avec la pancarte
        System.out.println("Le joueur interagit avec la pancarte...");
        sign.interact(); // Affiche le message

        System.out.println("Le joueur interagit de nouveau avec la pancarte...");
        sign.interact(); // Ferme la pancarte
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
            System.out.println("Arme: " + weapon.getName() + " (Puissance: " + weapon.getAttackPoints() + ")");
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


