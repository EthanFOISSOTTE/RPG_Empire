package com.empire.rpg.entity.player.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

import com.empire.rpg.entity.player.equipment.Tool;
import com.empire.rpg.entity.player.attacks.Attack;
import com.empire.rpg.entity.player.animations.AnimationState;

public class Constants {
    // Variables pour le joueur
    public static final float PLAYER_WALKING_SPEED = 150f; // Vitesse de marche du joueur
    public static final float PLAYER_RUNNING_SPEED = 300f; // Vitesse de course du joueur
    public static final float PLAYER_SCALE = 2.0f; // Facteur d'échelle par défaut du joueur

    // Variables pour la collision du joueur
    public static final float PLAYER_COLLISION_WIDTH = 15f; // Largeur de la boîte de collision
    public static final float PLAYER_COLLISION_HEIGHT = 10f; // Hauteur de la boîte de collision
    public static final float PLAYER_COLLISION_OFFSET_X = -(PLAYER_COLLISION_WIDTH / 2); // Décalage en X de la boîte de collision
    public static final float PLAYER_COLLISION_OFFSET_Y = -12f; // Décalage en Y de la boîte de collision

    // Variables pour les sprites du joueur
    public static final int SPRITE_WIDTH = 64;
    public static final int SPRITE_HEIGHT = 64;

    // Types et versions des sprites du joueur
    public static final String BodyType = "humn", BodyVersion = "v00";
    public static final String OutfitType = "pfpn", OutfitVersion = "v05";
    public static final String HairType = "bob1", HairVersion = "v11";
    public static final String HatType = "empty", HatVersion = "v00";

    // Version des sprites des armes
    public static final String bo01Version = "v01", bo02Version = "v01", bo03Version = "v01";
    public static final String ax01Version = "v01", mc01Version = "v01", sw01Version = "v01", sw02Version = "v01";
    public static final String hb01Version = "v01", sp01Version = "v01", sp02Version = "v01";
    public static final String qv01Version = "v01";
    public static final String sh01Version = "v01", sh02Version = "v01", sh03Version = "v01";

    // Chemins des sprites du joueur
    public static final Map<String, String> BODY_SPRITESHEET_PATHS = new HashMap<>();
    public static final Map<String, String> OUTFIT_SPRITESHEET_PATHS = new HashMap<>();
    public static final Map<String, String> HAIR_SPRITESHEET_PATHS = new HashMap<>();
    public static final Map<String, String> HAT_SPRITESHEET_PATHS = new HashMap<>();
    public static final Map<String, Map<String, String>> TOOL1_SPRITESHEET_PATHS = new HashMap<>();
    public static final Map<String, Map<String, String>> TOOL2_SPRITESHEET_PATHS = new HashMap<>();

    // Indices des frames et des timings pour les animations du joueur
    public static final Map<String, int[][]> FRAME_INDICES = new HashMap<>();
    public static final Map<String, float[]> FRAME_TIMINGS = new HashMap<>();

    // Initialisation les outils et les attaques
    public static final Map<String, Attack> ATTACKS = new HashMap<>();
    public static final Map<String, Tool> TOOLS = new HashMap<>();
    // Ensemble des états nécessitant des outils
    public static final Set<String> STATES_REQUIRING_TOOLS = new HashSet<>();

    // Initialisation des chemins des sprites du joueur, des outils et des animations
    static {
        // Lecture du fichier JSON du joueur
        FileHandle file = Gdx.files.internal("Player/Player.json");
        JsonReader jsonReader = new JsonReader();
        JsonValue base = jsonReader.parse(file);

        // Chemins des sprites du joueur
        BODY_SPRITESHEET_PATHS.put("P1", base.get("body").get(BodyType).get(BodyVersion).getString("char_a_p1"));
        BODY_SPRITESHEET_PATHS.put("P2", base.get("body").get(BodyType).get(BodyVersion).getString("char_a_p2"));
        BODY_SPRITESHEET_PATHS.put("P3", base.get("body").get(BodyType).get(BodyVersion).getString("char_a_p3"));
        BODY_SPRITESHEET_PATHS.put("P4", base.get("body").get(BodyType).get(BodyVersion).getString("char_a_p4"));
        BODY_SPRITESHEET_PATHS.put("BOW1", base.get("body").get(BodyType).get(BodyVersion).getString("char_a_pBOW1"));
        BODY_SPRITESHEET_PATHS.put("BOW2", base.get("body").get(BodyType).get(BodyVersion).getString("char_a_pBOW2"));
        BODY_SPRITESHEET_PATHS.put("BOW3", base.get("body").get(BodyType).get(BodyVersion).getString("char_a_pBOW3"));
        BODY_SPRITESHEET_PATHS.put("ONE1", base.get("body").get(BodyType).get(BodyVersion).getString("char_a_pONE1"));
        BODY_SPRITESHEET_PATHS.put("ONE2", base.get("body").get(BodyType).get(BodyVersion).getString("char_a_pONE2"));
        BODY_SPRITESHEET_PATHS.put("ONE3", base.get("body").get(BodyType).get(BodyVersion).getString("char_a_pONE3"));
        BODY_SPRITESHEET_PATHS.put("POL1", base.get("body").get(BodyType).get(BodyVersion).getString("char_a_pPOL1"));
        BODY_SPRITESHEET_PATHS.put("POL2", base.get("body").get(BodyType).get(BodyVersion).getString("char_a_pPOL2"));
        BODY_SPRITESHEET_PATHS.put("POL3", base.get("body").get(BodyType).get(BodyVersion).getString("char_a_pPOL3"));

        OUTFIT_SPRITESHEET_PATHS.put("P1", base.get("1out").get(OutfitType).get(OutfitVersion).getString("char_a_p1"));
        OUTFIT_SPRITESHEET_PATHS.put("P2", base.get("1out").get(OutfitType).get(OutfitVersion).getString("char_a_p2"));
        OUTFIT_SPRITESHEET_PATHS.put("P3", base.get("1out").get(OutfitType).get(OutfitVersion).getString("char_a_p3"));
        OUTFIT_SPRITESHEET_PATHS.put("P4", base.get("1out").get(OutfitType).get(OutfitVersion).getString("char_a_p4"));
        OUTFIT_SPRITESHEET_PATHS.put("BOW1", base.get("1out").get(OutfitType).get(OutfitVersion).getString("char_a_pBOW1"));
        OUTFIT_SPRITESHEET_PATHS.put("BOW2", base.get("1out").get(OutfitType).get(OutfitVersion).getString("char_a_pBOW2"));
        OUTFIT_SPRITESHEET_PATHS.put("BOW3", base.get("1out").get(OutfitType).get(OutfitVersion).getString("char_a_pBOW3"));
        OUTFIT_SPRITESHEET_PATHS.put("ONE1", base.get("1out").get(OutfitType).get(OutfitVersion).getString("char_a_pONE1"));
        OUTFIT_SPRITESHEET_PATHS.put("ONE2", base.get("1out").get(OutfitType).get(OutfitVersion).getString("char_a_pONE2"));
        OUTFIT_SPRITESHEET_PATHS.put("ONE3", base.get("1out").get(OutfitType).get(OutfitVersion).getString("char_a_pONE3"));
        OUTFIT_SPRITESHEET_PATHS.put("POL1", base.get("1out").get(OutfitType).get(OutfitVersion).getString("char_a_pPOL1"));
        OUTFIT_SPRITESHEET_PATHS.put("POL2", base.get("1out").get(OutfitType).get(OutfitVersion).getString("char_a_pPOL2"));
        OUTFIT_SPRITESHEET_PATHS.put("POL3", base.get("1out").get(OutfitType).get(OutfitVersion).getString("char_a_pPOL3"));

        HAIR_SPRITESHEET_PATHS.put("P1", base.get("4har").get(HairType).get(HairVersion).getString("char_a_p1"));
        HAIR_SPRITESHEET_PATHS.put("P2", base.get("4har").get(HairType).get(HairVersion).getString("char_a_p2"));
        HAIR_SPRITESHEET_PATHS.put("P3", base.get("4har").get(HairType).get(HairVersion).getString("char_a_p3"));
        HAIR_SPRITESHEET_PATHS.put("P4", base.get("4har").get(HairType).get(HairVersion).getString("char_a_p4"));
        HAIR_SPRITESHEET_PATHS.put("BOW1", base.get("4har").get(HairType).get(HairVersion).getString("char_a_pBOW1"));
        HAIR_SPRITESHEET_PATHS.put("BOW2", base.get("4har").get(HairType).get(HairVersion).getString("char_a_pBOW2"));
        HAIR_SPRITESHEET_PATHS.put("BOW3", base.get("4har").get(HairType).get(HairVersion).getString("char_a_pBOW3"));
        HAIR_SPRITESHEET_PATHS.put("ONE1", base.get("4har").get(HairType).get(HairVersion).getString("char_a_pONE1"));
        HAIR_SPRITESHEET_PATHS.put("ONE2", base.get("4har").get(HairType).get(HairVersion).getString("char_a_pONE2"));
        HAIR_SPRITESHEET_PATHS.put("ONE3", base.get("4har").get(HairType).get(HairVersion).getString("char_a_pONE3"));
        HAIR_SPRITESHEET_PATHS.put("POL1", base.get("4har").get(HairType).get(HairVersion).getString("char_a_pPOL1"));
        HAIR_SPRITESHEET_PATHS.put("POL2", base.get("4har").get(HairType).get(HairVersion).getString("char_a_pPOL2"));
        HAIR_SPRITESHEET_PATHS.put("POL3", base.get("4har").get(HairType).get(HairVersion).getString("char_a_pPOL3"));

        HAT_SPRITESHEET_PATHS.put("P1", base.get("5hat").get(HatType).get(HatVersion).getString("char_a_p1"));
        HAT_SPRITESHEET_PATHS.put("P2", base.get("5hat").get(HatType).get(HatVersion).getString("char_a_p2"));
        HAT_SPRITESHEET_PATHS.put("P3", base.get("5hat").get(HatType).get(HatVersion).getString("char_a_p3"));
        HAT_SPRITESHEET_PATHS.put("P4", base.get("5hat").get(HatType).get(HatVersion).getString("char_a_p4"));
        HAT_SPRITESHEET_PATHS.put("BOW1", base.get("5hat").get(HatType).get(HatVersion).getString("char_a_pBOW1"));
        HAT_SPRITESHEET_PATHS.put("BOW2", base.get("5hat").get(HatType).get(HatVersion).getString("char_a_pBOW2"));
        HAT_SPRITESHEET_PATHS.put("BOW3", base.get("5hat").get(HatType).get(HatVersion).getString("char_a_pBOW3"));
        HAT_SPRITESHEET_PATHS.put("ONE1", base.get("5hat").get(HatType).get(HatVersion).getString("char_a_pONE1"));
        HAT_SPRITESHEET_PATHS.put("ONE2", base.get("5hat").get(HatType).get(HatVersion).getString("char_a_pONE2"));
        HAT_SPRITESHEET_PATHS.put("ONE3", base.get("5hat").get(HatType).get(HatVersion).getString("char_a_pONE3"));
        HAT_SPRITESHEET_PATHS.put("POL1", base.get("5hat").get(HatType).get(HatVersion).getString("char_a_pPOL1"));
        HAT_SPRITESHEET_PATHS.put("POL2", base.get("5hat").get(HatType).get(HatVersion).getString("char_a_pPOL2"));
        HAT_SPRITESHEET_PATHS.put("POL3", base.get("5hat").get(HatType).get(HatVersion).getString("char_a_pPOL3"));

        // Chemins des sprites des outils (TOOL1)
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("P2", k -> new HashMap<>()).put("BNET", base.get("6tla").get("char_a_p2").get("bnet").getString("v01"));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("P2", k -> new HashMap<>()).put("FARM", base.get("6tla").get("char_a_p2").get("farm").getString("v01"));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("P2", k -> new HashMap<>()).put("MINE", base.get("6tla").get("char_a_p2").get("mine").getString("v01"));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("P2", k -> new HashMap<>()).put("WOOD", base.get("6tla").get("char_a_p2").get("wood").getString("v01"));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("P3", k -> new HashMap<>()).put("RODA", base.get("6tla").get("char_a_p3").get("roda").getString("v01"));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("P4", k -> new HashMap<>()).put("FARM", base.get("6tla").get("char_a_p4").get("farm").getString("v01"));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("P4", k -> new HashMap<>()).put("SMTH", base.get("6tla").get("char_a_p4").get("smth").getString("v01"));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("P4", k -> new HashMap<>()).put("WOOD", base.get("6tla").get("char_a_p4").get("wood").getString("v01"));

        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("BOW1", k -> new HashMap<>()).put("BO01", base.get("6tla").get("char_a_pBOW1").get("bo01").getString(bo01Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("BOW1", k -> new HashMap<>()).put("BO02", base.get("6tla").get("char_a_pBOW1").get("bo02").getString(bo02Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("BOW1", k -> new HashMap<>()).put("BO03", base.get("6tla").get("char_a_pBOW1").get("bo03").getString(bo03Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("BOW2", k -> new HashMap<>()).put("BO01", base.get("6tla").get("char_a_pBOW2").get("bo01").getString(bo01Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("BOW2", k -> new HashMap<>()).put("BO02", base.get("6tla").get("char_a_pBOW2").get("bo02").getString(bo02Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("BOW2", k -> new HashMap<>()).put("BO03", base.get("6tla").get("char_a_pBOW2").get("bo03").getString(bo03Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("BOW3", k -> new HashMap<>()).put("BO01", base.get("6tla").get("char_a_pBOW3").get("bo01").getString(bo01Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("BOW3", k -> new HashMap<>()).put("BO02", base.get("6tla").get("char_a_pBOW3").get("bo02").getString(bo02Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("BOW3", k -> new HashMap<>()).put("BO03", base.get("6tla").get("char_a_pBOW3").get("bo03").getString(bo03Version));

        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("ONE1", k -> new HashMap<>()).put("AX01", base.get("6tla").get("char_a_pONE1").get("ax01").getString(ax01Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("ONE1", k -> new HashMap<>()).put("MC01", base.get("6tla").get("char_a_pONE1").get("mc01").getString(mc01Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("ONE1", k -> new HashMap<>()).put("SW01", base.get("6tla").get("char_a_pONE1").get("sw01").getString(sw01Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("ONE1", k -> new HashMap<>()).put("SW02", base.get("6tla").get("char_a_pONE1").get("sw02").getString(sw02Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("ONE2", k -> new HashMap<>()).put("AX01", base.get("6tla").get("char_a_pONE2").get("ax01").getString(ax01Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("ONE2", k -> new HashMap<>()).put("MC01", base.get("6tla").get("char_a_pONE2").get("mc01").getString(mc01Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("ONE2", k -> new HashMap<>()).put("SW01", base.get("6tla").get("char_a_pONE2").get("sw01").getString(sw01Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("ONE2", k -> new HashMap<>()).put("SW02", base.get("6tla").get("char_a_pONE2").get("sw02").getString(sw02Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("ONE3", k -> new HashMap<>()).put("AX01", base.get("6tla").get("char_a_pONE3").get("ax01").getString(ax01Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("ONE3", k -> new HashMap<>()).put("MC01", base.get("6tla").get("char_a_pONE3").get("mc01").getString(mc01Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("ONE3", k -> new HashMap<>()).put("SW01", base.get("6tla").get("char_a_pONE3").get("sw01").getString(sw01Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("ONE3", k -> new HashMap<>()).put("SW02", base.get("6tla").get("char_a_pONE3").get("sw02").getString(sw02Version));

        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("POL1", k -> new HashMap<>()).put("HB01", base.get("6tla").get("char_a_pPOL1").get("hb01").getString(hb01Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("POL1", k -> new HashMap<>()).put("SP01", base.get("6tla").get("char_a_pPOL1").get("sp01").getString(sp01Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("POL1", k -> new HashMap<>()).put("SP02", base.get("6tla").get("char_a_pPOL1").get("sp02").getString(sp02Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("POL2", k -> new HashMap<>()).put("HB01", base.get("6tla").get("char_a_pPOL2").get("hb01").getString(hb01Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("POL2", k -> new HashMap<>()).put("SP01", base.get("6tla").get("char_a_pPOL2").get("sp01").getString(sp01Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("POL2", k -> new HashMap<>()).put("SP02", base.get("6tla").get("char_a_pPOL2").get("sp02").getString(sp02Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("POL3", k -> new HashMap<>()).put("HB01", base.get("6tla").get("char_a_pPOL3").get("hb01").getString(hb01Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("POL3", k -> new HashMap<>()).put("SP01", base.get("6tla").get("char_a_pPOL3").get("sp01").getString(sp01Version));
        TOOL1_SPRITESHEET_PATHS.computeIfAbsent("POL3", k -> new HashMap<>()).put("SP02", base.get("6tla").get("char_a_pPOL3").get("sp02").getString(sp02Version));

        // Chemins des sprites des outils (TOOL2)
        TOOL2_SPRITESHEET_PATHS.computeIfAbsent("BOW1", k -> new HashMap<>()).put("QV01", base.get("7tlb").get("char_a_pBOW1").get("qv01").getString(qv01Version));
        TOOL2_SPRITESHEET_PATHS.computeIfAbsent("BOW2", k -> new HashMap<>()).put("QV01", base.get("7tlb").get("char_a_pBOW2").get("qv01").getString(qv01Version));
        TOOL2_SPRITESHEET_PATHS.computeIfAbsent("BOW3", k -> new HashMap<>()).put("QV01", base.get("7tlb").get("char_a_pBOW3").get("qv01").getString(qv01Version));

        TOOL2_SPRITESHEET_PATHS.computeIfAbsent("ONE1", k -> new HashMap<>()).put("SH01", base.get("7tlb").get("char_a_pONE1").get("sh01").getString(sh01Version));
        TOOL2_SPRITESHEET_PATHS.computeIfAbsent("ONE1", k -> new HashMap<>()).put("SH02", base.get("7tlb").get("char_a_pONE1").get("sh02").getString(sh02Version));
        TOOL2_SPRITESHEET_PATHS.computeIfAbsent("ONE1", k -> new HashMap<>()).put("SH03", base.get("7tlb").get("char_a_pONE1").get("sh03").getString(sh03Version));
        TOOL2_SPRITESHEET_PATHS.computeIfAbsent("ONE2", k -> new HashMap<>()).put("SH01", base.get("7tlb").get("char_a_pONE2").get("sh01").getString(sh01Version));
        TOOL2_SPRITESHEET_PATHS.computeIfAbsent("ONE2", k -> new HashMap<>()).put("SH02", base.get("7tlb").get("char_a_pONE2").get("sh02").getString(sh02Version));
        TOOL2_SPRITESHEET_PATHS.computeIfAbsent("ONE2", k -> new HashMap<>()).put("SH03", base.get("7tlb").get("char_a_pONE2").get("sh03").getString(sh03Version));
        TOOL2_SPRITESHEET_PATHS.computeIfAbsent("ONE3", k -> new HashMap<>()).put("SH01", base.get("7tlb").get("char_a_pONE3").get("sh01").getString(sh01Version));
        TOOL2_SPRITESHEET_PATHS.computeIfAbsent("ONE3", k -> new HashMap<>()).put("SH02", base.get("7tlb").get("char_a_pONE3").get("sh02").getString(sh02Version));
        TOOL2_SPRITESHEET_PATHS.computeIfAbsent("ONE3", k -> new HashMap<>()).put("SH03", base.get("7tlb").get("char_a_pONE3").get("sh03").getString(sh03Version));

        // Indices des frames et des timings pour les animations debout dans les 4 directions
        FRAME_INDICES.put("STANDING_UP", new int[][] {{1, 0}});
        FRAME_TIMINGS.put("STANDING_UP", new float[] {1f});
        FRAME_INDICES.put("STANDING_DOWN", new int[][] {{0, 0}});
        FRAME_TIMINGS.put("STANDING_DOWN", new float[] {1f});
        FRAME_INDICES.put("STANDING_LEFT", new int[][] {{3, 0}});
        FRAME_TIMINGS.put("STANDING_LEFT", new float[] {1f});
        FRAME_INDICES.put("STANDING_RIGHT", new int[][] {{2, 0}});
        FRAME_TIMINGS.put("STANDING_RIGHT", new float[] {1f});

        // Indices des frames et des timings pour les animations de marche dans les 4 directions
        FRAME_INDICES.put("WALKING_UP", new int[][] {{5, 0}, {5, 1}, {5, 2}, {5, 3}, {5, 4}, {5, 5}});
        FRAME_TIMINGS.put("WALKING_UP", new float[] {0.135f, 0.135f, 0.135f, 0.135f, 0.135f, 0.135f});
        FRAME_INDICES.put("WALKING_DOWN", new int[][] {{4, 0}, {4, 1}, {4, 2}, {4, 3}, {4, 4}, {4, 5}});
        FRAME_TIMINGS.put("WALKING_DOWN", new float[] {0.135f, 0.135f, 0.135f, 0.135f, 0.135f, 0.135f});
        FRAME_INDICES.put("WALKING_LEFT", new int[][] {{7, 0}, {7, 1}, {7, 2}, {7, 3}, {7, 4}, {7, 5}});
        FRAME_TIMINGS.put("WALKING_LEFT", new float[] {0.135f, 0.135f, 0.135f, 0.135f, 0.135f, 0.135f});
        FRAME_INDICES.put("WALKING_RIGHT", new int[][] {{6, 0}, {6, 1}, {6, 2}, {6, 3}, {6, 4}, {6, 5}});
        FRAME_TIMINGS.put("WALKING_RIGHT", new float[] {0.135f, 0.135f, 0.135f, 0.135f, 0.135f, 0.135f});

        // Indices des frames et des timings pour les animations de course dans les 4 directions
        FRAME_INDICES.put("RUNNING_UP", new int[][] {{5, 0}, {5, 1}, {5, 6}, {5, 3}, {5, 4}, {5, 7}});
        FRAME_TIMINGS.put("RUNNING_UP", new float[] {0.080f, 0.055f, 0.115f, 0.080f, 0.055f, 0.115f});
        FRAME_INDICES.put("RUNNING_DOWN", new int[][] {{4, 0}, {4, 1}, {4, 6}, {4, 3}, {4, 4}, {4, 7}});
        FRAME_TIMINGS.put("RUNNING_DOWN", new float[] {0.080f, 0.055f, 0.115f, 0.080f, 0.055f, 0.115f});
        FRAME_INDICES.put("RUNNING_LEFT", new int[][] {{7, 0}, {7, 1}, {7, 6}, {7, 3}, {7, 4}, {7, 7}});
        FRAME_TIMINGS.put("RUNNING_LEFT", new float[] {0.080f, 0.055f, 0.115f, 0.080f, 0.055f, 0.115f});
        FRAME_INDICES.put("RUNNING_RIGHT", new int[][] {{6, 0}, {6, 1}, {6, 6}, {6, 3}, {6, 4}, {6, 7}});
        FRAME_TIMINGS.put("RUNNING_RIGHT", new float[] {0.080f, 0.055f, 0.115f, 0.080f, 0.055f, 0.115f});

        // Indices des frames et des timings pour les animations de l'attaque (ONE_SLASH1) dans les 4 directions
        FRAME_INDICES.put("ONE_SLASH1_UP", new int[][] {{1, 0}, {1, 1}, {1, 2}, {1, 3}});
        FRAME_TIMINGS.put("ONE_SLASH1_UP", new float[] {0.075f, 0.075f, 0.075f, 0.075f});
        FRAME_INDICES.put("ONE_SLASH1_DOWN", new int[][] {{0, 0}, {0, 1}, {0, 2}, {0, 3}});
        FRAME_TIMINGS.put("ONE_SLASH1_DOWN", new float[] {0.075f, 0.075f, 0.075f, 0.075f});
        FRAME_INDICES.put("ONE_SLASH1_LEFT", new int[][] {{3, 0}, {3, 1}, {3, 2}, {3, 3}});
        FRAME_TIMINGS.put("ONE_SLASH1_LEFT", new float[] {0.075f, 0.075f, 0.075f, 0.075f});
        FRAME_INDICES.put("ONE_SLASH1_RIGHT", new int[][] {{2, 0}, {2, 1}, {2, 2}, {2, 3}});
        FRAME_TIMINGS.put("ONE_SLASH1_RIGHT", new float[] {0.075f, 0.075f, 0.075f, 0.075f});
        STATES_REQUIRING_TOOLS.add("ONE_SLASH1_UP");
        STATES_REQUIRING_TOOLS.add("ONE_SLASH1_DOWN");
        STATES_REQUIRING_TOOLS.add("ONE_SLASH1_LEFT");
        STATES_REQUIRING_TOOLS.add("ONE_SLASH1_RIGHT");

        // Indices des frames et des timings pour les animations de l'attaque (ONE_SLASH2) dans les 4 directions
        FRAME_INDICES.put("ONE_SLASH2_UP", new int[][] {{1, 4}, {1, 5}, {1, 6}, {1, 7}});
        FRAME_TIMINGS.put("ONE_SLASH2_UP", new float[] {0.075f, 0.075f, 0.075f, 0.075f});
        FRAME_INDICES.put("ONE_SLASH2_DOWN", new int[][] {{0, 4}, {0, 5}, {0, 6}, {0, 7}});
        FRAME_TIMINGS.put("ONE_SLASH2_DOWN", new float[] {0.075f, 0.075f, 0.075f, 0.075f});
        FRAME_INDICES.put("ONE_SLASH2_LEFT", new int[][] {{3, 4}, {3, 5}, {3, 6}, {3, 7}});
        FRAME_TIMINGS.put("ONE_SLASH2_LEFT", new float[] {0.075f, 0.075f, 0.075f, 0.075f});
        FRAME_INDICES.put("ONE_SLASH2_RIGHT", new int[][] {{2, 4}, {2, 5}, {2, 6}, {2, 7}});
        FRAME_TIMINGS.put("ONE_SLASH2_RIGHT", new float[] {0.075f, 0.075f, 0.075f, 0.075f});
        STATES_REQUIRING_TOOLS.add("ONE_SLASH2_UP");
        STATES_REQUIRING_TOOLS.add("ONE_SLASH2_DOWN");
        STATES_REQUIRING_TOOLS.add("ONE_SLASH2_LEFT");
        STATES_REQUIRING_TOOLS.add("ONE_SLASH2_RIGHT");

        // Indices des frames et des timings pour les animations de blocage (ONE_DODGE) dans les 4 directions
        FRAME_INDICES.put("ONE_DODGE_UP", new int[][] {{1, 4}});
        FRAME_TIMINGS.put("ONE_DODGE_UP", new float[] {1f});
        FRAME_INDICES.put("ONE_DODGE_DOWN", new int[][] {{0, 4}});
        FRAME_TIMINGS.put("ONE_DODGE_DOWN", new float[] {1f});
        FRAME_INDICES.put("ONE_DODGE_LEFT", new int[][] {{3, 4}});
        FRAME_TIMINGS.put("ONE_DODGE_LEFT", new float[] {1f});
        FRAME_INDICES.put("ONE_DODGE_RIGHT", new int[][] {{2, 4}});
        FRAME_TIMINGS.put("ONE_DODGE_RIGHT", new float[] {1f});
        STATES_REQUIRING_TOOLS.add("ONE_DODGE_UP");
        STATES_REQUIRING_TOOLS.add("ONE_DODGE_DOWN");
        STATES_REQUIRING_TOOLS.add("ONE_DODGE_LEFT");
        STATES_REQUIRING_TOOLS.add("ONE_DODGE_RIGHT");

        // Indices des frames et des timings pour les animations de l'attaque (POL_SLASH1) dans les 4 directions
        FRAME_INDICES.put("POL_SLASH1_UP", new int[][] {{1, 0}, {1, 1}, {1, 2}, {1, 3}});
        FRAME_TIMINGS.put("POL_SLASH1_UP", new float[] {0.075f, 0.075f, 0.075f, 0.075f});
        FRAME_INDICES.put("POL_SLASH1_DOWN", new int[][] {{0, 0}, {0, 1}, {0, 2}, {0, 3}});
        FRAME_TIMINGS.put("POL_SLASH1_DOWN", new float[] {0.075f, 0.075f, 0.075f, 0.075f});
        FRAME_INDICES.put("POL_SLASH1_LEFT", new int[][] {{3, 0}, {3, 1}, {3, 2}, {3, 3}});
        FRAME_TIMINGS.put("POL_SLASH1_LEFT", new float[] {0.075f, 0.075f, 0.075f, 0.075f});
        FRAME_INDICES.put("POL_SLASH1_RIGHT", new int[][] {{2, 0}, {2, 1}, {2, 2}, {2, 3}});
        FRAME_TIMINGS.put("POL_SLASH1_RIGHT", new float[] {0.075f, 0.075f, 0.075f, 0.075f});
        STATES_REQUIRING_TOOLS.add("POL_SLASH1_UP");
        STATES_REQUIRING_TOOLS.add("POL_SLASH1_DOWN");
        STATES_REQUIRING_TOOLS.add("POL_SLASH1_LEFT");
        STATES_REQUIRING_TOOLS.add("POL_SLASH1_RIGHT");

        // Indices des frames et des timings pour les animations de l'attaque ("BOW_SHOOT1") dans les 4 directions
        FRAME_INDICES.put("BOW_SHOOT1_UP", new int[][] {{5, 0}, {5, 1}, {5, 2}, {5, 3}, {5, 4}, {5, 5}, {5, 6}, {5, 7}});
        FRAME_TIMINGS.put("BOW_SHOOT1_UP", new float[] {0.180f, 0.100f, 0.100f, 0.100f, 0.400f, 0.050f, 0.050f, 0.100f});
        FRAME_INDICES.put("BOW_SHOOT1_DOWN", new int[][] {{4, 0}, {4, 1}, {4, 2}, {4, 3}, {4, 4}, {4, 5}, {4, 6}, {4, 7}});
        FRAME_TIMINGS.put("BOW_SHOOT1_DOWN", new float[] {0.180f, 0.100f, 0.100f, 0.100f, 0.400f, 0.050f, 0.050f, 0.100f});
        FRAME_INDICES.put("BOW_SHOOT1_LEFT", new int[][] {{7, 0}, {7, 1}, {7, 2}, {7, 3}, {7, 4}, {7, 5}, {7, 6}, {7, 7}});
        FRAME_TIMINGS.put("BOW_SHOOT1_LEFT", new float[] {0.180f, 0.100f, 0.100f, 0.100f, 0.400f, 0.050f, 0.050f, 0.100f});
        FRAME_INDICES.put("BOW_SHOOT1_RIGHT", new int[][] {{6, 0}, {6, 1}, {6, 2}, {6, 3}, {6, 4}, {6, 5}, {6, 6}, {6, 7}});
        FRAME_TIMINGS.put("BOW_SHOOT1_RIGHT", new float[] {0.180f, 0.100f, 0.100f, 0.100f, 0.400f, 0.050f, 0.050f, 0.100f});
        STATES_REQUIRING_TOOLS.add("BOW_SHOOT1_UP");
        STATES_REQUIRING_TOOLS.add("BOW_SHOOT1_DOWN");
        STATES_REQUIRING_TOOLS.add("BOW_SHOOT1_LEFT");
        STATES_REQUIRING_TOOLS.add("BOW_SHOOT1_RIGHT");

            // Définition de l'attaque "ONE_SLASH1"
        Map<String, AnimationState> oneSlash1States = new HashMap<>();
        oneSlash1States.put("UP", AnimationState.ONE_SLASH1_UP);
        oneSlash1States.put("DOWN", AnimationState.ONE_SLASH1_DOWN);
        oneSlash1States.put("LEFT", AnimationState.ONE_SLASH1_LEFT);
        oneSlash1States.put("RIGHT", AnimationState.ONE_SLASH1_RIGHT);
        ATTACKS.put("ONE_SLASH1", new Attack(
            "ONE_SLASH1", "Fente tranchante", "ONE3", oneSlash1States,
            0.3f, 10f, 1.0f, 80f, 40f
        ));

        // Définition de l'attaque "ONE_SLASH2"
        Map<String, AnimationState> oneSlash2States = new HashMap<>();
        oneSlash2States.put("UP", AnimationState.ONE_SLASH2_UP);
        oneSlash2States.put("DOWN", AnimationState.ONE_SLASH2_DOWN);
        oneSlash2States.put("LEFT", AnimationState.ONE_SLASH2_LEFT);
        oneSlash2States.put("RIGHT", AnimationState.ONE_SLASH2_RIGHT);
        ATTACKS.put("ONE_SLASH2", new Attack(
            "ONE_SLASH2", "Fente tranchante", "ONE3", oneSlash2States,
            0.3f, 10f, 1.0f, 80f, 40f
        ));

        // Définition de l'attaque "ONE_DODGE"
        Map<String, AnimationState> oneDodgeStates = new HashMap<>();
        oneDodgeStates.put("UP", AnimationState.ONE_DODGE_UP);
        oneDodgeStates.put("DOWN", AnimationState.ONE_DODGE_DOWN);
        oneDodgeStates.put("LEFT", AnimationState.ONE_DODGE_LEFT);
        oneDodgeStates.put("RIGHT", AnimationState.ONE_DODGE_RIGHT);
        ATTACKS.put("ONE_DODGE", new Attack(
            "ONE_DODGE", "Blocage", "ONE1", oneDodgeStates,
            0.3f, 0f, 0.5f, 50f, 25f
        ));

        // Définition de l'attaque "POL_SLASH1"
        Map<String, AnimationState> polSlash1States = new HashMap<>();
        polSlash1States.put("UP", AnimationState.POL_SLASH1_UP);
        polSlash1States.put("DOWN", AnimationState.POL_SLASH1_DOWN);
        polSlash1States.put("LEFT", AnimationState.POL_SLASH1_LEFT);
        polSlash1States.put("RIGHT", AnimationState.POL_SLASH1_RIGHT);
        ATTACKS.put("POL_SLASH1", new Attack(
            "POL_SLASH1", "Coup tranchante", "POL3", polSlash1States,
            0.3f, 15f, 1.0f, 150f, 50f
        ));

        // Définition de l'attaque "BOW_SHOOT1"
        Map<String, AnimationState> bowShoot1States = new HashMap<>();
        bowShoot1States.put("UP", AnimationState.BOW_SHOOT1_UP);
        bowShoot1States.put("DOWN", AnimationState.BOW_SHOOT1_DOWN);
        bowShoot1States.put("LEFT", AnimationState.BOW_SHOOT1_LEFT);
        bowShoot1States.put("RIGHT", AnimationState.BOW_SHOOT1_RIGHT);
        ATTACKS.put("BOW_SHOOT1", new Attack(
            "BOW_SHOOT1", "Tir à l'arc", "BOW3", bowShoot1States,
            1.08f, 0f, 2.2f, 0f, 0f
        ));

        // Définition de l'outil
        TOOLS.put("AX01", new Tool("AX01", "Hache", "AX01", Arrays.asList("ONE_SLASH1", "ONE_SLASH2")));
        TOOLS.put("MC01", new Tool("MC01", "Marteau de guerre", "MC01", Arrays.asList("ONE_SLASH1", "ONE_SLASH2")));
        TOOLS.put("SW01", new Tool("SW01", "Petite épée", "SW01", Arrays.asList("ONE_SLASH1", "ONE_SLASH2")));
        TOOLS.put("SW02", new Tool("SW02", "Grosse épée", "SW02", Arrays.asList("ONE_SLASH1", "ONE_SLASH2")));
        TOOLS.put("SH01", new Tool("SH01", "Bouclier de base", "SH01", Arrays.asList("ONE_DODGE")));
        TOOLS.put("SH02", new Tool("SH02", "Bouclier moyen", "SH02", Arrays.asList("ONE_DODGE")));
        TOOLS.put("SH03", new Tool("SH03", "Bouclier lourd", "SH03", Arrays.asList("ONE_DODGE")));
        TOOLS.put("HB01", new Tool("HB01", "Lance", "HB01", Arrays.asList("POL_SLASH1")));
        TOOLS.put("BO02", new Tool("BO02", "Arc de base", "BO02", Arrays.asList("BOW_SHOOT1")));
        TOOLS.put("QV01", new Tool("QV01", "Carquois", "QV01", Arrays.asList()));
    }
}
