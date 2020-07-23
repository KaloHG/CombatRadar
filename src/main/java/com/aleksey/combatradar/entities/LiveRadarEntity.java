package com.aleksey.combatradar.entities;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.util.ResourceLocation;

/**
 * @author Aleksey Terzi
 */
public class LiveRadarEntity extends RadarEntity {
    private static final String[] HORSE_VARIANTS = {"white", "creamy", "chestnut", "brown", "black", "gray", "darkbrown"};

    private ResourceLocation _resourceLocation;

    public LiveRadarEntity(Entity entity, EntitySettings settings) {
        super(entity, settings);
    }

    @Override
    protected void renderInternal(Minecraft minecraft, float displayX, float displayY) {
        ResourceLocation resourceLocation = getResourceLocation(minecraft);

        if (resourceLocation == null)
            return;

        float iconScale = getSettings().iconScale;

        minecraft.getTextureManager().bindTexture(resourceLocation);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, getSettings().iconOpacity);
        GlStateManager.enableBlend();

        GlStateManager.pushMatrix();
        GlStateManager.translatef(displayX, displayY, 0);
        GlStateManager.rotatef(minecraft.player.rotationYaw, 0.0F, 0.0F, 1.0F);
        GlStateManager.scalef(iconScale, iconScale, iconScale);

        AbstractGui.blit(-8, -8, 0, 0, 16, 16, 16, 16);

        GlStateManager.popMatrix();
        GlStateManager.disableBlend();
    }

    private ResourceLocation getResourceLocation(Minecraft minecraft) {
        if(_resourceLocation == null) {
            try {
                EntityRendererManager renderManager = minecraft.getRenderManager();
                EntityRenderer render = renderManager.getRenderer(getEntity());

                if (render instanceof HorseRenderer) {
                    HorseEntity horseEntity = (HorseEntity) getEntity();
                    int horseVariant = (0xff & horseEntity.getHorseVariant()) % 7;

                    _resourceLocation = new ResourceLocation("combatradar", "icons/horse/horse_" + HORSE_VARIANTS[horseVariant] + ".png");
                } else if (render instanceof LlamaRenderer) {
                    _resourceLocation = new ResourceLocation("combatradar", "icons/llama/llama.png");
                } else if (render instanceof ParrotRenderer) {
                    _resourceLocation = new ResourceLocation("combatradar", "icons/parrot/parrot.png");
                } else if (render instanceof ShulkerRenderer) {
                    _resourceLocation = new ResourceLocation("combatradar", "icons/shulker/shulker.png");
                } else if (render instanceof GhastRenderer) {
                    _resourceLocation = new ResourceLocation("combatradar", "icons/ghast/ghast.png");
                } else {
                    ResourceLocation original = ResourceHelper.getEntityTexture(render, getEntity());
                    _resourceLocation = new ResourceLocation("combatradar", original.getPath().replace("textures/entity/", "icons/"));
                }
            } catch (Throwable e) {
                System.out.println("Can't get entityTexture for " + getEntity().getName());
            }
        }

        return _resourceLocation;
    }
}
