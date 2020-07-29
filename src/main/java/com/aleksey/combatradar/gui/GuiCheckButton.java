package com.aleksey.combatradar.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

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
        super(x, y, width, BUTTON_HEIGHT, new StringTextComponent(name), onPress);
    }

    @Override
    public void func_230988_a_(SoundHandler p_playDownSound_1_) {//playDownSound	
    }

    @Override
    public void func_230430_a_(MatrixStack matrix, int xPos, int yPos, float partialTicks) {  //render
        if(!this.field_230694_p_) { //visible check
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();

        //setting isHovered
        this.field_230692_n_ = xPos >= this.field_230690_l_ && yPos >= this.field_230691_m_ && xPos < this.field_230690_l_ + this.field_230688_j_ && yPos < this.field_230691_m_ + this.field_230689_k_;

        int textureX = _checked ? CHECKED_TEXTURE_X : UNCHECKED_TEXTURE_X;

        minecraft.getTextureManager().bindTexture(_texture);

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param);
        //blit
        func_238474_b_(new MatrixStack(), this.field_230690_l_, this.field_230691_m_ + (this.field_230689_k_ - TEXTURE_SIZE) / 2, textureX, 0, TEXTURE_SIZE, TEXTURE_SIZE);

        int textColor = this.field_230692_n_ ? 16777120 : Color.LIGHT_GRAY.getRGB();

        Minecraft.getInstance().fontRenderer.func_238405_a_(new MatrixStack(), this.func_230458_i_().getUnformattedComponentText(), this.field_230690_l_ + INDENT, this.field_230691_m_ + (this.field_230689_k_ - 8) / 2, textColor);
    }
}
