package com.empire.rpg.Component;

public class PositionComponent implements Component{
    private int x;
    private int y;

    public PositionComponent(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void getPosition(){
        System.out.println("X: " + x + " Y: " + y);
    }

    public void move(int x, int y){
        this.x += x;
        this.y += y;
    }


}
