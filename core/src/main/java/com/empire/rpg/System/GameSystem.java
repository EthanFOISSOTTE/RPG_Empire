package com.empire.rpg.System;

import com.empire.rpg.Component.Component;

public interface GameSystem<T extends Component> {
    void update(T component);
}

