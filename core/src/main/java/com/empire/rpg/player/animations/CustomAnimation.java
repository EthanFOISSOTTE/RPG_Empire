package com.empire.rpg.player.animations;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CustomAnimation {
    private TextureRegion[] bodyFrames;
    private TextureRegion[] outfitFrames;
    private TextureRegion[] hairFrames;
    private TextureRegion[] hatFrames;
    private TextureRegion[] tool1Frames;
    private TextureRegion[] tool2Frames;
    private float[] frameDurations;
    private boolean looping;
    private float totalDuration;

    public CustomAnimation(TextureRegion[] bodyFrames, TextureRegion[] outfitFrames, TextureRegion[] hairFrames,
                           TextureRegion[] hatFrames, TextureRegion[] tool1Frames, TextureRegion[] tool2Frames,
                           float[] frameDurations, boolean looping) {
        this.bodyFrames = bodyFrames;
        this.outfitFrames = outfitFrames;
        this.hairFrames = hairFrames;
        this.hatFrames = hatFrames;
        this.tool1Frames = tool1Frames;
        this.tool2Frames = tool2Frames;
        this.frameDurations = frameDurations;
        this.looping = looping;
        for (float duration : frameDurations) {
            totalDuration += duration;
        }
    }

    // Méthodes pour obtenir les frames de chaque composant

    public TextureRegion getBodyKeyFrame(float stateTime) {
        int frameIndex = getFrameIndex(stateTime);
        return bodyFrames[frameIndex];
    }

    public TextureRegion getOutfitKeyFrame(float stateTime) {
        int frameIndex = getFrameIndex(stateTime);
        return outfitFrames[frameIndex];
    }

    public TextureRegion getHairKeyFrame(float stateTime) {
        int frameIndex = getFrameIndex(stateTime);
        return hairFrames[frameIndex];
    }

    public TextureRegion getHatKeyFrame(float stateTime) {
        int frameIndex = getFrameIndex(stateTime);
        return hatFrames[frameIndex];
    }

    public TextureRegion getTool1KeyFrame(float stateTime) {
        if (tool1Frames == null) return null;
        int frameIndex = getFrameIndex(stateTime);
        return tool1Frames[frameIndex];
    }

    public TextureRegion getTool2KeyFrame(float stateTime) {
        if (tool2Frames == null) return null;
        int frameIndex = getFrameIndex(stateTime);
        return tool2Frames[frameIndex];
    }

    private int getFrameIndex(float stateTime) {
        if (looping) {
            stateTime = stateTime % totalDuration;
        } else if (stateTime >= totalDuration) {
            return bodyFrames.length - 1;
        }

        float time = 0f;
        for (int i = 0; i < frameDurations.length; i++) {
            time += frameDurations[i];
            if (stateTime < time) {
                return i;
            }
        }
        return bodyFrames.length - 1; // Dernière frame par défaut
    }
}
