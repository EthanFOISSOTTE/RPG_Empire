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
    private CustomAnimation currentCustomAnimation;
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
        // Initialiser les maps pour les spritesheets
        bodySpriteSheets = new HashMap<>();
        outfitSpriteSheets = new HashMap<>();
        hairSpriteSheets = new HashMap<>();
        hatSpriteSheets = new HashMap<>();
        tool1SpriteSheets = new HashMap<>();
        tool2SpriteSheets = new HashMap<>();

        // Charger les spritesheets pour le corps
        for (String key : Constants.BODY_SPRITESHEET_PATHS.keySet()) {
            String path = Constants.BODY_SPRITESHEET_PATHS.get(key);
            SpriteSheet spriteSheet = new SpriteSheet(path);
            bodySpriteSheets.put(key, spriteSheet);
        }

        // Charger les spritesheets pour la tenue
        for (String key : Constants.OUTFIT_SPRITESHEET_PATHS.keySet()) {
            String path = Constants.OUTFIT_SPRITESHEET_PATHS.get(key);
            SpriteSheet spriteSheet = new SpriteSheet(path);
            outfitSpriteSheets.put(key, spriteSheet);
        }

        // Charger les spritesheets pour les cheveux
        for (String key : Constants.HAIR_SPRITESHEET_PATHS.keySet()) {
            String path = Constants.HAIR_SPRITESHEET_PATHS.get(key);
            SpriteSheet spriteSheet = new SpriteSheet(path);
            hairSpriteSheets.put(key, spriteSheet);
        }

        // Charger les spritesheets pour le chapeau
        for (String key : Constants.HAT_SPRITESHEET_PATHS.keySet()) {
            String path = Constants.HAT_SPRITESHEET_PATHS.get(key);
            SpriteSheet spriteSheet = new SpriteSheet(path);
            hatSpriteSheets.put(key, spriteSheet);
        }

        // Charger les spritesheets pour tool1 et tool2
        for (Attack attack : Constants.ATTACKS.values()) {
            String categoryKey = attack.getCategoryKey();

            // Chargement des spritesheets pour tool1
            Map<String, String> toolsForCategory1 = Constants.TOOL1_SPRITESHEET_PATHS.get(categoryKey);
            if (toolsForCategory1 != null) {
                Map<String, SpriteSheet> toolSheets1 = new HashMap<>();
                for (String spritesheetKey : toolsForCategory1.keySet()) {
                    String path = toolsForCategory1.get(spritesheetKey);
                    SpriteSheet spriteSheet = new SpriteSheet(path);
                    toolSheets1.put(spritesheetKey, spriteSheet);
                }
                tool1SpriteSheets.put(categoryKey, toolSheets1);
            }

            // Chargement des spritesheets pour tool2
            Map<String, String> toolsForCategory2 = Constants.TOOL2_SPRITESHEET_PATHS.get(categoryKey);
            if (toolsForCategory2 != null) {
                Map<String, SpriteSheet> toolSheets2 = new HashMap<>();
                for (String spritesheetKey : toolsForCategory2.keySet()) {
                    String path = toolsForCategory2.get(spritesheetKey);
                    SpriteSheet spriteSheet = new SpriteSheet(path);
                    toolSheets2.put(spritesheetKey, spriteSheet);
                }
                tool2SpriteSheets.put(categoryKey, toolSheets2);
            }
        }
    }

    // Créer les animations pour chaque état
    private void createAnimations() {
        for (AnimationState state : AnimationState.values()) {
            String stateName = state.name();

            // Vérifier si l'état est un état d'attaque
            if (stateName.startsWith("ONE_SLASH") || stateName.startsWith("ONE_DODGE")) {
                // Ne pas créer d'animations pour les états d'attaque
                continue;
            }

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

                // Créer les frames pour chaque composant
                TextureRegion[] bodyFrames = getFrames(bodySheet, frameIndices);
                TextureRegion[] outfitFrames = getFrames(outfitSheet, frameIndices);
                TextureRegion[] hairFrames = getFrames(hairSheet, frameIndices);
                TextureRegion[] hatFrames = getFrames(hatSheet, frameIndices);

                // Créer l'animation personnalisée sans outils
                CustomAnimation animation = new CustomAnimation(
                    bodyFrames, outfitFrames, hairFrames, hatFrames, null, null, durations, true
                );
                animations.put(state, animation);
            }
        }
    }

    // Déterminer le spritesheet à utiliser en fonction de l'état
    private String getSpritesheetKeyForState(AnimationState state) {
        String stateName = state.name();
        if (stateName.startsWith("RUNNING") || stateName.startsWith("WALKING") || stateName.startsWith("STANDING")) {
            return "P1"; // Ou la clé appropriée pour les animations non-attaques
        } else {
            // Pour les autres états (potentiellement des attaques), nous ne les traitons pas ici
            return null;
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

    // Créer une animation d'attaque
    public CustomAnimation createAttackAnimation(String categoryKey, String tool1SpritesheetKey, String tool2SpritesheetKey, AnimationState state) {
        int[][] frameIndices = Constants.FRAME_INDICES.get(state.name());
        float[] durations = Constants.FRAME_TIMINGS.get(state.name());

        if (frameIndices == null || durations == null) {
            // Gérer l'erreur ou retourner null
            return null;
        }

        // Récupérer les SpriteSheets
        SpriteSheet bodySheet = bodySpriteSheets.get(categoryKey);
        SpriteSheet outfitSheet = outfitSpriteSheets.get(categoryKey);
        SpriteSheet hairSheet = hairSpriteSheets.get(categoryKey);
        SpriteSheet hatSheet = hatSpriteSheets.get(categoryKey);

        // Récupérer le SpriteSheet de tool1
        Map<String, SpriteSheet> tool1Sheets = tool1SpriteSheets.get(categoryKey);
        SpriteSheet tool1Sheet = null;
        if (tool1Sheets != null && tool1SpritesheetKey != null) {
            tool1Sheet = tool1Sheets.get(tool1SpritesheetKey);
        }

        // Récupérer le SpriteSheet de tool2
        Map<String, SpriteSheet> tool2Sheets = tool2SpriteSheets.get(categoryKey);
        SpriteSheet tool2Sheet = null;
        if (tool2Sheets != null && tool2SpritesheetKey != null) {
            tool2Sheet = tool2Sheets.get(tool2SpritesheetKey);
        }

        // Récupérer les frames
        TextureRegion[] bodyFrames = getFrames(bodySheet, frameIndices);
        TextureRegion[] outfitFrames = getFrames(outfitSheet, frameIndices);
        TextureRegion[] hairFrames = getFrames(hairSheet, frameIndices);
        TextureRegion[] hatFrames = getFrames(hatSheet, frameIndices);
        TextureRegion[] tool1Frames = getFrames(tool1Sheet, frameIndices);
        TextureRegion[] tool2Frames = getFrames(tool2Sheet, frameIndices);

        // Créer l'animation personnalisée
        return new CustomAnimation(bodyFrames, outfitFrames, hairFrames, hatFrames, tool1Frames, tool2Frames, durations, false);
    }

    public void setCustomAnimation(CustomAnimation animation) {
        this.currentCustomAnimation = animation;
        this.stateTime = 0f; // Réinitialiser le temps d'état
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

        CustomAnimation currentAnimation = (currentCustomAnimation != null) ? currentCustomAnimation : animations.get(currentAnimationState);

        if (currentAnimation != null) {
            TextureRegion currentBodyFrame = currentAnimation.getBodyKeyFrame(stateTime);
            TextureRegion currentOutfitFrame = currentAnimation.getOutfitKeyFrame(stateTime);
            TextureRegion currentHairFrame = currentAnimation.getHairKeyFrame(stateTime);
            TextureRegion currentHatFrame = currentAnimation.getHatKeyFrame(stateTime);
            TextureRegion currentTool1Frame = currentAnimation.getTool1KeyFrame(stateTime);
            TextureRegion currentTool2Frame = currentAnimation.getTool2KeyFrame(stateTime);

            // Mettre à jour les composants
            body.update(currentBodyFrame);
            outfit.update(currentOutfitFrame);
            hair.update(currentHairFrame);
            hat.update(currentHatFrame);
            tool1.update(currentTool1Frame);
            tool2.update(currentTool2Frame);
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
