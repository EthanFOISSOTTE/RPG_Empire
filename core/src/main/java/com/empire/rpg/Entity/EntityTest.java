package com.empire.rpg.Entity;

import com.empire.rpg.Component.*;
import java.util.Map;
import java.util.UUID;

public class EntityTest {
    public static void main(String[] args) {

        EntityManager entityManager = new EntityManager("Balrog") {
            @Override
            public Entity addEntity() {
                Map<Class<? extends Component>, Component> components = Map.of(
                        HealthComponent.class, new HealthComponent(50, 50),
                        PositionComponent.class, new PositionComponent(0, 0),
                        WeaponComponent.class, new WeaponComponent("Sword", 10),
                        MovementComponent.class, new MovementComponent(1.5f, "north"),
                        CollisionComponent.class, new CollisionComponent(true)
                );
                UUID id = UUID.randomUUID();
                return new MOB("Orc", components, id);
            }
            @Override
            public void createEntity(UUID id) {
                System.out.println("Creating entity with id: " + id);
            }

        };
        entityManager.createEntity(UUID.randomUUID());
        System.out.println("Creating MOB entity with caracteristics : Name : " + entityManager.getName() + " Health : " + ((HealthComponent) entityManager.addEntity().getComponent(HealthComponent.class)).getCurrentHealthPoints() + " / " + (entityManager.addEntity().getComponent(HealthComponent.class) + " Position : " + ((PositionComponent) entityManager.addEntity().getComponent(PositionComponent.class)).getX() + " " + ((PositionComponent) entityManager.addEntity().getComponent(PositionComponent.class)).getY() + " Weapon : " + ((WeaponComponent) entityManager.addEntity().getComponent(WeaponComponent.class)).getName() + " " + ((WeaponComponent) entityManager.addEntity().getComponent(WeaponComponent.class)).getAttackPoints() + " Movement : " + ((MovementComponent) entityManager.addEntity().getComponent(MovementComponent.class)).getSpeed() + " " + ((MovementComponent) entityManager.addEntity().getComponent(MovementComponent.class)).getDirection() + " Collision : " + ((CollisionComponent) entityManager.addEntity().getComponent(CollisionComponent.class)).isCollidable()));





        EntityManager entityManager2 = new EntityManager("Gandalf") {
            @Override
            public void createEntity(UUID id) {
                System.out.println("Creating entity with id: " + id);
            }

            @Override
            public Entity addEntity() {
                Map<Class<? extends Component>, Component> components = Map.of(
                    HealthComponent.class, new HealthComponent(100, 100),
                    PositionComponent.class, new PositionComponent(0, 0),
                    WeaponComponent.class, new WeaponComponent("Sword", 10)
                );
                UUID id = UUID.randomUUID();
                return new Player("Gandalf", components, id);
            }

        } ;
        entityManager2.createEntity(UUID.randomUUID());

        System.out.println("Creating Player entity with caracteristics : Name : " + entityManager2.getName() + " Health : " + ((HealthComponent) entityManager2.addEntity().getComponent(HealthComponent.class)).getCurrentHealthPoints() + " / " + (entityManager2.addEntity().getComponent(HealthComponent.class) + " Position : " + ((PositionComponent) entityManager2.addEntity().getComponent(PositionComponent.class)).getX() + " " + ((PositionComponent) entityManager2.addEntity().getComponent(PositionComponent.class)).getY() + " Weapon : " + ((WeaponComponent) entityManager2.addEntity().getComponent(WeaponComponent.class)).getName() + " " + ((WeaponComponent) entityManager2.addEntity().getComponent(WeaponComponent.class)).getAttackPoints()));


    }
}
