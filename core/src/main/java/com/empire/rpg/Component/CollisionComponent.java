package com.empire.rpg.Component;

public class CollisionComponent implements Component {
    private boolean isCollidable;

    public CollisionComponent(boolean isCollidable) {
        this.isCollidable = isCollidable;
    }

    public boolean isCollidable() {
        return isCollidable;
    }

    public void setCollidable(boolean collidable) {
        isCollidable = collidable;
    }

    public boolean detectCollision(){
        System.out.println("Collision detected");
        return true;
    }

    public boolean toggleCollision(){
        isCollidable = !isCollidable;
        return isCollidable;
    }

}
