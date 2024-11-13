package com.empire.rpg.player.animations;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.empire.rpg.player.Player;
import com.empire.rpg.player.components.*;
import com.empire.rpg.player.utils.Constants;
import com.empire.rpg.player.animations.spritesheet.SpriteSheet;
import com.empire.rpg.player.equipment.Tool;
import com.empire.rpg.player.attacks.Attack;

import java.util.HashMap;
import java.util.Map;

public class AnimationController {
    private AnimationState currentAnimationState;
    private Body body;
    private Outfit outfit;
    private Hair hair;
    private Hat hat;
    private Tool1 tool1;
    private Tool2 tool2;
    private Player player;

    private Map<AnimationState, CustomAnimation> animations;
    private float stateTime;

    private Map<String, SpriteSheet> bodySpriteSheets;
    private Map<String, SpriteSheet> outfitSpriteSheets;
    private Map<String, SpriteSheet> hairSpriteSheets;
    private Map<String, SpriteSheet> hatSpriteSheets;

    private Map<String, Map<String, SpriteSheet>> tool1SpriteSheets;
    private Map<String, Map<String, SpriteSheet>> tool2SpriteSheets;

    public AnimationController(Player player, Body body, Outfit outfit, Hair hair, Hat hat, Tool1 tool1, Tool2 tool2) {
        this.player = player;
        this.body = body;
        this.outfit = outfit;
        this.hair = hair;
        this.hat = hat;
        this.tool1 = tool1;
        this.tool2 = tool2;
        animations = new HashMap<>();
        loadSpriteSheets();
        createAnimations();
        currentAnimationState = AnimationState.STANDING_DOWN; // Par défaut, le joueur regarde vers le bas
    }

    // Vérifier si l'animation actuelle nécessite un outil
    private boolean requiresTool(AnimationState state) {
        return Constants.STATES_REQUIRING_TOOLS.contains(state.name());
    }

    // Charger les SpriteSheets pour chaque composant
    private void loadSpriteSheets() {
        bodySpriteSheets = new HashMap<>();
        outfitSpriteSheets = new HashMap<>();
        hairSpriteSheets = new HashMap<>();
        hatSpriteSheets = new HashMap<>();
        tool1SpriteSheets = new HashMap<>();
        tool2SpriteSheets = new HashMap<>();

        for (String key : Constants.BODY_SPRITESHEET_PATHS.keySet()) {
            String path = Constants.BODY_SPRITESHEET_PATHS.get(key);
            SpriteSheet spriteSheet = new SpriteSheet(path);
            bodySpriteSheets.put(key, spriteSheet);
        }

        for (String key : Constants.OUTFIT_SPRITESHEET_PATHS.keySet()) {
            String path = Constants.OUTFIT_SPRITESHEET_PATHS.get(key);
            SpriteSheet spriteSheet = new SpriteSheet(path);
            outfitSpriteSheets.put(key, spriteSheet);
        }

        for (String key : Constants.HAIR_SPRITESHEET_PATHS.keySet()) {
            String path = Constants.HAIR_SPRITESHEET_PATHS.get(key);
            SpriteSheet spriteSheet = new SpriteSheet(path);
            hairSpriteSheets.put(key, spriteSheet);
        }

        for (String key : Constants.HAT_SPRITESHEET_PATHS.keySet()) {
            String path = Constants.HAT_SPRITESHEET_PATHS.get(key);
            SpriteSheet spriteSheet = new SpriteSheet(path);
            hatSpriteSheets.put(key, spriteSheet);
        }

        // Charger les SpriteSheets des outils (Tool1)
        for (String categoryKey : Constants.TOOL1_SPRITESHEET_PATHS.keySet()) {
            Map<String, String> tools = Constants.TOOL1_SPRITESHEET_PATHS.get(categoryKey);
            Map<String, SpriteSheet> toolMap = new HashMap<>();
            for (String toolKey : tools.keySet()) {
                String path = tools.get(toolKey);
                SpriteSheet spriteSheet = new SpriteSheet(path);
                toolMap.put(toolKey, spriteSheet);
            }
            tool1SpriteSheets.put(categoryKey, toolMap);
        }

        // Charger les SpriteSheets des outils (Tool2)
        for (String categoryKey : Constants.TOOL2_SPRITESHEET_PATHS.keySet()) {
            Map<String, String> tools = Constants.TOOL2_SPRITESHEET_PATHS.get(categoryKey);
            Map<String, SpriteSheet> toolMap = new HashMap<>();
            for (String toolKey : tools.keySet()) {
                String path = tools.get(toolKey);
                SpriteSheet spriteSheet = new SpriteSheet(path);
                toolMap.put(toolKey, spriteSheet);
            }
            tool2SpriteSheets.put(categoryKey, toolMap);
        }
    }

    // Créer les animations pour chaque état
    private void createAnimations() {
        for (AnimationState state : AnimationState.values()) {
            String stateName = state.name();
            int[][] frameIndices = Constants.FRAME_INDICES.get(stateName);
            float[] durations = Constants.FRAME_TIMINGS.get(stateName);

            if (frameIndices != null && durations != null) {
                // Déterminer le spritesheet approprié en fonction de l'état d'animation
                String spritesheetKey = getSpritesheetKeyForState(state);

                // Récupérer les SpriteSheets pour chaque composant
                SpriteSheet bodySheet = bodySpriteSheets.get(spritesheetKey);
                SpriteSheet outfitSheet = outfitSpriteSheets.get(spritesheetKey);
                SpriteSheet hairSheet = hairSpriteSheets.get(spritesheetKey);
                SpriteSheet hatSheet = hatSpriteSheets.get(spritesheetKey);

                // Gestions des outils
                TextureRegion[] tool1Frames = null;
                TextureRegion[] tool2Frames = null;

                if (requiresTool(state)) {
                    // Charger les frames de tool1 et tool2 pour les états nécessitant des outils
                    if (player.getCurrentTool1() != null) {
                        String categoryKey1 = player.getCurrentTool1().getCategoryKey();
                        String toolKey1 = player.getCurrentTool1().getSpritesheetKey();
                        SpriteSheet tool1Sheet = tool1SpriteSheets.getOrDefault(categoryKey1, new HashMap<>()).get(toolKey1);
                        tool1Frames = tool1Sheet != null ? getFrames(tool1Sheet, frameIndices) : null;
                    }

                    if (player.getCurrentTool2() != null) {
                        String categoryKey2 = player.getCurrentTool2().getCategoryKey();
                        String toolKey2 = player.getCurrentTool2().getSpritesheetKey();
                        SpriteSheet tool2Sheet = tool2SpriteSheets.getOrDefault(categoryKey2, new HashMap<>()).get(toolKey2);
                        tool2Frames = tool2Sheet != null ? getFrames(tool2Sheet, frameIndices) : null;
                    }
                }

                // Créer les frames pour chaque composant
                TextureRegion[] bodyFrames = getFrames(bodySheet, frameIndices);
                TextureRegion[] outfitFrames = getFrames(outfitSheet, frameIndices);
                TextureRegion[] hairFrames = getFrames(hairSheet, frameIndices);
                TextureRegion[] hatFrames = getFrames(hatSheet, frameIndices);

                // Créer l'animation personnalisée en incluant les outils uniquement pour les états nécessitant des outils
                CustomAnimation animation = new CustomAnimation(
                    bodyFrames, outfitFrames, hairFrames, hatFrames, tool1Frames, tool2Frames, durations, true
                );
                animations.put(state, animation);
            }
        }
    }

    // Déterminer le spritesheet à utiliser en fonction de l'état
    private String getSpritesheetKeyForState(AnimationState state) {
        String stateName = state.name();
        if (stateName.startsWith("ONE_SLASH1")) {
            return "ONE3";
        } else if (stateName.startsWith("ONE_DODGE")) {
            return "ONE1";
        } else {
            // Utiliser "P1" pour les animations normales
            return "P1";
        }
    }

    // Créer les frames pour chaque composant
    private TextureRegion[] getFrames(SpriteSheet spriteSheet, int[][] frameIndices) {
        TextureRegion[] frames = new TextureRegion[frameIndices.length];
        for (int i = 0; i < frameIndices.length; i++) {
            int row = frameIndices[i][0];
            int col = frameIndices[i][1];
            frames[i] = spriteSheet.getFrame(row, col);
        }
        return frames;
    }

    // Changer l'état de l'animation
    public void setAnimationState(AnimationState state) {
        if (currentAnimationState != state) {
            currentAnimationState = state;
            stateTime = 0f;
        }
    }

    // Mettre à jour l'animation
    public void update(float deltaTime) {
        stateTime += deltaTime;
        CustomAnimation currentAnimation = animations.get(currentAnimationState);
        if (currentAnimation != null) {
            TextureRegion currentBodyFrame = currentAnimation.getBodyKeyFrame(stateTime);
            TextureRegion currentOutfitFrame = currentAnimation.getOutfitKeyFrame(stateTime);
            TextureRegion currentHairFrame = currentAnimation.getHairKeyFrame(stateTime);
            TextureRegion currentHatFrame = currentAnimation.getHatKeyFrame(stateTime);

            // Vérifier si l'animation nécessite un outil avant de mettre à jour les composants d'outil
            if (requiresTool(currentAnimationState)) {
                TextureRegion currentTool1Frame = currentAnimation.getTool1KeyFrame(stateTime);
                TextureRegion currentTool2Frame = currentAnimation.getTool2KeyFrame(stateTime);

                // Mettre à jour les composants d'outils
                if (tool1 != null) {
                    tool1.update(currentTool1Frame);
                }
                if (tool2 != null) {
                    tool2.update(currentTool2Frame);
                }
            } else {
                // Désactiver les composants d'outils en dehors des animations d'attaque
                if (tool1 != null) {
                    tool1.update(null);
                }
                if (tool2 != null) {
                    tool2.update(null);
                }
            }

            // Mettre à jour les autres composants avec les frames correspondantes
            body.update(currentBodyFrame);
            outfit.update(currentOutfitFrame);
            hair.update(currentHairFrame);
            hat.update(currentHatFrame);
        }
    }

    // Rendre l'animation
    public void dispose() {
        for (SpriteSheet sheet : bodySpriteSheets.values()) {
            sheet.dispose();
        }
        for (SpriteSheet sheet : outfitSpriteSheets.values()) {
            sheet.dispose();
        }
        for (SpriteSheet sheet : hairSpriteSheets.values()) {
            sheet.dispose();
        }
        for (SpriteSheet sheet : hatSpriteSheets.values()) {
            sheet.dispose();
        }
    }
}
