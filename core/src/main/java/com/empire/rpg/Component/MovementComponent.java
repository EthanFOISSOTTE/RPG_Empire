package com.empire.rpg.Component;

public class MovementComponent implements Component {
    private float speed;
    private String direction;

    public MovementComponent(float speed, String direction) {
        this.speed = speed;
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void move(String direction){
        this.direction = direction;
    }

    public void stop(){
        this.direction = "stop";
    }

}
