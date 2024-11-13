package com.empire.rpg.player.states;

import com.empire.rpg.collisions.CollisionManager;
import com.empire.rpg.player.Player;

public abstract class State {
    protected Player player;

    public State(Player player) {
        this.player = player;
    }

    public abstract void enter();

    public abstract void update(float deltaTime, CollisionManager collisionManager);

    public abstract void exit();
}
