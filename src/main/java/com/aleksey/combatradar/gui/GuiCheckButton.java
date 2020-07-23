package com.aleksey.combatradar.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

/**
 * @author Aleksey Terzi
 */
public class GuiCheckButton extends Button {
    private static final ResourceLocation _texture = new ResourceLocation("combatradar", "textures/gui/checkbox.png");

    public static final int BUTTON_HEIGHT = 14;

    private static final int TEXTURE_SIZE = 7;
    private static final int CHECKED_TEXTURE_X = 8;
    private static final int UNCHECKED_TEXTURE_X = 0;
    private static final int INDENT = 9;

    private boolean _checked;

    public boolean isChecked() { return _checked; }
    public void setChecked(boolean value) { _checked = value; }

    public GuiCheckButton(int x, int y, int width, String name, IPressable onPress) {
        super(x, y, width, BUTTON_HEIGHT, name, onPress);
    }

    @Override
    public void playDownSound(SoundHandler p_playDownSound_1_) {}

    @Override
    public void render(int xPos, int yPos, float p_drawButton_4_) {
        if(!this.visible) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();

        this.isHovered = xPos >= this.x && yPos >= this.y && xPos < this.x + this.width && yPos < this.y + this.height;

        int textureX = _checked ? CHECKED_TEXTURE_X : UNCHECKED_TEXTURE_X;

        minecraft.getTextureManager().bindTexture(_texture);

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        blit(this.x, this.y + (this.height - TEXTURE_SIZE) / 2, textureX, 0, TEXTURE_SIZE, TEXTURE_SIZE);

        int textColor = this.isHovered ? 16777120 : Color.LIGHT_GRAY.getRGB();

        minecraft.fontRenderer.drawString(this.getMessage(), this.x + INDENT, this.y + (this.height - 8) / 2, textColor);
    }
}
