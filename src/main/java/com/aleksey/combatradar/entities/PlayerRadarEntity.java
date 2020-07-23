package com.aleksey.combatradar.entities;

import com.aleksey.combatradar.config.PlayerType;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.Map;

/**
 * @author Aleksey Terzi
 */
public class PlayerRadarEntity extends RadarEntity {
    private PlayerType _playerType;

    public PlayerRadarEntity(Entity entity, EntitySettings settings, PlayerType playerType) {
        super(entity, settings);
        _playerType = playerType;
    }

    @Override
    protected void renderInternal(Minecraft minecraft, float displayX, float displayY) {
        RemoteClientPlayerEntity player = (RemoteClientPlayerEntity)getEntity();

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, getSettings().iconOpacity);
        GlStateManager.enableBlend();

        GlStateManager.pushMatrix();
        GlStateManager.translatef(displayX, displayY, 0);
        GlStateManager.rotatef(minecraft.player.rotationYaw, 0.0F, 0.0F, 1.0F);

        GlStateManager.pushMatrix();
        GlStateManager.scalef(getSettings().iconScale, getSettings().iconScale, getSettings().iconScale);

        try {
            GameProfile gameProfile = player.getGameProfile();
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> texMap = minecraft.getSkinManager().loadSkinFromCache(gameProfile);

            if (texMap.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                MinecraftProfileTexture profileTexture = texMap.get(MinecraftProfileTexture.Type.SKIN);
                minecraft.getTextureManager().bindTexture(minecraft.getSkinManager().loadSkin(profileTexture, MinecraftProfileTexture.Type.SKIN));
                AbstractGui.blit(-8, -8, 16, 16, 8, 8, 8, 8, 64, 64);
            } else {
                minecraft.getTextureManager().bindTexture(new ResourceLocation("combatradar", "icons/player.png"));
                AbstractGui.blit(-8, -8, 16, 16, 0, 0, 8, 8, 8, 8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        if (getSettings().showPlayerNames) {
            Color color = _playerType == PlayerType.Ally ? getSettings().allyPlayerColor : (_playerType == PlayerType.Enemy ? getSettings().enemyPlayerColor : getSettings().neutralPlayerColor);

            GlStateManager.pushMatrix();
            GlStateManager.scalef(getSettings().fontScale, getSettings().fontScale, getSettings().fontScale);

            String playerName = player.getName().getString();
            if (getSettings().showExtraPlayerInfo) {
                playerName += " (" + (int)getDistanceToEntity(minecraft.player, player) + "m)(Y" + (int) player.posY + ")";
            }
            int yOffset = -4 + (int) ((getSettings().iconScale * getSettings().radarScale + 8));
            drawCenteredString(minecraft.fontRenderer, playerName, 0, yOffset, color.getRGB());

            GlStateManager.popMatrix();
        }

        GlStateManager.popMatrix();
    }

    private static void drawCenteredString(FontRenderer fontRenderer, String text, int x, int y, int color)
    {
        fontRenderer.drawStringWithShadow(text, x - fontRenderer.getStringWidth(text) / 2, y, color);
    }

    private static float getDistanceToEntity(Entity e1, Entity e2)
    {
        float f = (float)(e1.posX - e2.posX);
        float f1 = (float)(e1.posY - e2.posY);
        float f2 = (float)(e1.posZ - e2.posZ);
        return MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
    }
}
