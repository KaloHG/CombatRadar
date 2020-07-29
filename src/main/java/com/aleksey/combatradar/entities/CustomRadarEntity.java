package com.aleksey.combatradar.entities;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.ResourceHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * @author Aleksey Terzi
 */
public class CustomRadarEntity extends RadarEntity {
    private ResourceLocation _resourceLocation;

    public CustomRadarEntity(Entity entity, EntitySettings settings, String resourcePath) {
        super(entity, settings);

        _resourceLocation = new ResourceLocation("combatradar", resourcePath);
    }

    @Override
    protected void renderInternal(Minecraft minecraft, float displayX, float displayY) {
        float iconScale = getSettings().iconScale;

        minecraft.getTextureManager().bindTexture(_resourceLocation);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, getSettings().iconOpacity);
        GlStateManager.enableBlend();

        GlStateManager.pushMatrix();
        GlStateManager.translatef(displayX, displayY, 0);
        GlStateManager.rotatef(minecraft.player.rotationYaw, 0.0F, 0.0F, 1.0F);
        GlStateManager.scalef(iconScale, iconScale, iconScale);

        ResourceHelper.blit(-8, -8, 0, 0, 16, 16, 16, 16);

        GlStateManager.popMatrix();
        GlStateManager.disableBlend();
    }
}
