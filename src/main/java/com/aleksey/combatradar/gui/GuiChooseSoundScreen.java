package com.aleksey.combatradar.gui;

import com.aleksey.combatradar.SoundHelper;
import com.aleksey.combatradar.config.PlayerType;
import com.aleksey.combatradar.config.RadarConfig;
import com.aleksey.combatradar.config.SoundInfo;
import com.google.common.math.IntMath;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;
import java.math.RoundingMode;

/**
 * @author Aleksey Terzi
 */
public class GuiChooseSoundScreen extends Screen {
    private RadarConfig _config;
    private Screen _parent;
    private PlayerType _playerType;
    private int _titleTop;

    public GuiChooseSoundScreen(Screen parent, RadarConfig config, PlayerType playerType) {
        super(new StringTextComponent("Choose Sound"));
        _parent = parent;
        _config = config;
        _playerType = playerType;
    }

    @Override
    public void func_231160_c_() {
        _titleTop = this.field_230709_l_ / 4 - 40;

        this.field_230710_m_.clear();
        this.field_230705_e_.clear();

        int topY = this.field_230709_l_ / 4 - 16;
        int leftX = this.field_230708_k_ / 2 - 60;

        int y = topY;
        int x = leftX;

        String sound = _config.getPlayerTypeInfo(_playerType).soundEventName;

        for(int i = 0; i < SoundInfo.SOUND_LIST.length; i++) {
            SoundInfo info = SoundInfo.SOUND_LIST[i];

            int finalI = i;
            GuiCheckButton chk = new GuiCheckButton(x, y, 80, info.name, b -> chooseSound(finalI));
            chk.setChecked(sound.equalsIgnoreCase(info.value));

            func_230481_d_(chk);

            if(i == IntMath.divide(SoundInfo.SOUND_LIST.length, 2, RoundingMode.UP) - 1) {
                y = topY;
                x = leftX + 80;
            } else {
                y += 20;
            }
        }

        func_230481_d_(new Button(this.field_230708_k_ / 2 - 100, y, 200, 20, new StringTextComponent("Done"), b -> Minecraft.getInstance().displayGuiScreen(_parent)));
    }

    @Override
    public void func_231175_as__() {  //onClose
        _config.save();
        Minecraft.getInstance().displayGuiScreen(_parent);
    }

    private void chooseSound(int index) {
        for(int i = 0; i < SoundInfo.SOUND_LIST.length; i++) {
            boolean isChecked = i == index;
            ((GuiCheckButton)this.field_230705_e_.get(i)).setChecked(isChecked);
        }

        SoundInfo soundInfo = SoundInfo.SOUND_LIST[index];

        _config.getPlayerTypeInfo(_playerType).soundEventName = soundInfo.value;
        _config.save();

        SoundHelper.playSound(Minecraft.getInstance(), soundInfo.value, Minecraft.getInstance().player.getUniqueID());
    }

    @Override
    public void func_230430_a_(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {  //render
        String playerTypeName;

        switch(_playerType) {
            case Ally:
                playerTypeName = "Ally";
                break;
            case Enemy:
                playerTypeName = "Enemy";
                break;
            default:
                playerTypeName = "Neutral";
                break;
        }

        String title = "Ping Sound for " + playerTypeName + " Players";

        func_231165_f_(0); //draw dirt background
        func_238471_a_(new MatrixStack(), this.field_230712_o_,title, this.field_230708_k_ / 2, _titleTop, Color.WHITE.getRGB());
        super.func_230430_a_(matrix, mouseX, mouseY, partialTicks);
    }
}