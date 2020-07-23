package com.aleksey.combatradar.entities;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;

/**
 * @author Aleksey Terzi
 */
public class ItemRadarEntity extends RadarEntity {
    private ItemStack _item;

    public ItemRadarEntity(Entity entity, EntitySettings settings) {
        super(entity, settings);

        _item = ((ItemEntity)getEntity()).getItem();
    }

    public ItemRadarEntity(Entity entity, EntitySettings settings, ItemStack item) {
        super(entity, settings);

        _item = item;
    }

    @Override
    protected void renderInternal(Minecraft minecraft, float displayX, float displayY) {
        float iconScale = getSettings().iconScale;

        GlStateManager.pushMatrix();
        GlStateManager.translatef(displayX, displayY, 0);
        GlStateManager.rotatef(minecraft.player.rotationYaw, 0.0F, 0.0F, 1.0F);
        GlStateManager.scalef(iconScale, iconScale, iconScale);

        minecraft.getItemRenderer().renderItemIntoGUI(_item, -8, -8);
        GlStateManager.disableLighting();

        GlStateManager.popMatrix();
    }
}
