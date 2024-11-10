package com.empire.rpg.Component;

public class PositionComponent implements Component{
    private float x;
    private float y;

    public PositionComponent(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void getPosition(){
        System.out.println("X: " + x + " Y: " + y);
    }

    public void move(float x, float y){
        this.x += x;
        this.y += y;
    }


    @Override
    public int getDamageReduction() {
        return 0;
    }

    @Override
    public void setCurrentHealthPoints(int i) {

    }

    @Override
    public int getCurrentHealthPoints() {
        return 0;
    }
}
