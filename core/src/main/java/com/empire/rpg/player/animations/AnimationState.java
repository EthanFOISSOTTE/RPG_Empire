package com.empire.rpg.player.animations;

public enum AnimationState {
    // États de repos
    STANDING_UP,
    STANDING_DOWN,
    STANDING_LEFT,
    STANDING_RIGHT,
    // États de marche
    WALKING_UP,
    WALKING_DOWN,
    WALKING_LEFT,
    WALKING_RIGHT,
    // États de course
    RUNNING_UP,
    RUNNING_DOWN,
    RUNNING_LEFT,
    RUNNING_RIGHT,

    // États d'attaque - ONE_SLASH1
    ONE_SLASH1_UP,
    ONE_SLASH1_DOWN,
    ONE_SLASH1_LEFT,
    ONE_SLASH1_RIGHT,
    // États d'attaque - ONE_SLASH2
    ONE_SLASH2_UP,
    ONE_SLASH2_DOWN,
    ONE_SLASH2_LEFT,
    ONE_SLASH2_RIGHT,

    // États de défense - ONE_DODGE
    ONE_DODGE_UP,
    ONE_DODGE_DOWN,
    ONE_DODGE_LEFT,
    ONE_DODGE_RIGHT,
}
