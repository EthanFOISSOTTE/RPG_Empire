package com.empire.rpg.player.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static Map<String, Sound> soundCache = new HashMap<>();

    public static void playSound(String attackId) {
        Sound sound = soundCache.get(attackId);
        if (sound == null) {
            String filePath = "Audio/SFX/Weapon/" + attackId + ".mp3";
            sound = Gdx.audio.newSound(Gdx.files.internal(filePath));
            soundCache.put(attackId, sound);
        }
        sound.play();
    }

    public static void dispose() {
        for (Sound sound : soundCache.values()) {
            sound.dispose();
        }
        soundCache.clear();
    }
}
