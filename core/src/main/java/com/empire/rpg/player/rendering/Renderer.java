package com.empire.rpg.player.rendering;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.empire.rpg.player.components.*;

public class Renderer {
    public void render(Batch batch, Body body, Outfit outfit, Hair hair, Hat hat, Tool1 tool1, Tool2 tool2, float x, float y, float scale) {
        // Rendre les calques dans l'ordre approprié
        body.render(batch, x, y, scale);
        outfit.render(batch, x, y, scale);
        hair.render(batch, x, y, scale);
        hat.render(batch, x, y, scale);

        if (tool1 != null) {
            tool1.render(batch, x, y, scale);
        }
        if (tool2 != null) {
            tool2.render(batch, x, y, scale);
        }
    }
}