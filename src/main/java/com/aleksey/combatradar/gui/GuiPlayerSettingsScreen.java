package com.aleksey.combatradar.gui;

import com.aleksey.combatradar.config.PlayerType;
import com.aleksey.combatradar.config.PlayerTypeInfo;
import com.aleksey.combatradar.config.RadarConfig;
import com.aleksey.combatradar.config.SoundInfo;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;

/**
 * @author Aleksey Terzi
 */
public class GuiPlayerSettingsScreen extends Screen {
    private RadarConfig _config;
    private Screen _parent;
    private GuiSlider _neutralRedSlider;
    private GuiSlider _neutralGreenSlider;
    private GuiSlider _neutralBlueSlider;
    private GuiSlider _allyRedSlider;
    private GuiSlider _allyGreenSlider;
    private GuiSlider _allyBlueSlider;
    private GuiSlider _enemyRedSlider;
    private GuiSlider _enemyGreenSlider;
    private GuiSlider _enemyBlueSlider;
    private Button _neutralPingButton;
    private Button _neutralSoundButton;
    private Button _allyPingButton;
    private Button _allySoundButton;
    private Button _enemyPingButton;
    private Button _enemySoundButton;

    public GuiPlayerSettingsScreen(Screen parent, RadarConfig config) {
        super(new StringTextComponent("Player Settings"));
        _parent = parent;
        _config = config;
    }

    @Override
    public void func_231160_c_() {
        this.field_230705_e_.clear();
        this.field_230710_m_.clear();

        int y = this.field_230709_l_ / 4 - 16 + 12;
        int x = this.field_230708_k_ / 2 - 100;

        PlayerTypeInfo neutralInfo = _config.getPlayerTypeInfo(PlayerType.Neutral);
        PlayerTypeInfo allyInfo = _config.getPlayerTypeInfo(PlayerType.Ally);
        PlayerTypeInfo enemyInfo = _config.getPlayerTypeInfo(PlayerType.Enemy);

        func_230481_d_(_neutralRedSlider = new GuiSlider(x, y, 66, 1, 0, "Red", neutralInfo.color.getRed() / 255f, false));
        func_230481_d_(_neutralGreenSlider = new GuiSlider(x + 66 + 1, y, 66, 1, 0, "Green", neutralInfo.color.getGreen() / 255f, false));
        func_230481_d_(_neutralBlueSlider = new GuiSlider(x + 66 + 1 + 66 + 1, y, 66, 1, 0, "Blue", neutralInfo.color.getBlue() / 255f, false));
        y += 24 + 12;
        func_230481_d_(_allyRedSlider = new GuiSlider(x, y, 66, 1, 0, "Red", allyInfo.color.getRed() / 255f, false));
        func_230481_d_(_allyGreenSlider = new GuiSlider(x + 66 + 1, y, 66, 1, 0, "Green", allyInfo.color.getGreen() / 255f, false));
        func_230481_d_(_allyBlueSlider = new GuiSlider(x + 66 + 1 + 66 + 1, y, 66, 1, 0, "Blue", allyInfo.color.getBlue() / 255f, false));
        y += 24 + 12;
        func_230481_d_(_enemyRedSlider = new GuiSlider(x, y, 66, 1, 0, "Red", enemyInfo.color.getRed() / 255f, false));
        func_230481_d_(_enemyGreenSlider = new GuiSlider(x + 66 + 1, y, 66, 1, 0, "Green", enemyInfo.color.getGreen() / 255f, false));
        func_230481_d_(_enemyBlueSlider = new GuiSlider(x + 66 + 1 + 66 + 1, y, 66, 1, 0, "Blue", enemyInfo.color.getBlue() / 255f, false));
        y += 24;
        func_230481_d_(_neutralPingButton = new Button(x, y, 133, 20, new StringTextComponent("Neutral Player Ping"), b -> {
            PlayerTypeInfo playerTypeInfo = _config.getPlayerTypeInfo(PlayerType.Neutral);
            playerTypeInfo.ping = !playerTypeInfo.ping;
            _config.save();
        }));
        func_230481_d_(_neutralSoundButton = new Button(x + 133 + 1, y, 66, 20, new StringTextComponent("Sound"), b -> Minecraft.getInstance().displayGuiScreen(new GuiChooseSoundScreen(this, _config, PlayerType.Neutral))));
        y += 24;
        func_230481_d_(_allyPingButton = new Button(x, y, 133, 20, new StringTextComponent("Ally Player Ping"), b -> {
            PlayerTypeInfo playerTypeInfo = _config.getPlayerTypeInfo(PlayerType.Ally);
            playerTypeInfo.ping = !playerTypeInfo.ping;
            _config.save();
        }));
        func_230481_d_(_allySoundButton = new Button(x + 133 + 1, y, 66, 20, new StringTextComponent("Sound"), b -> Minecraft.getInstance().displayGuiScreen(new GuiChooseSoundScreen(this, _config, PlayerType.Ally))));
        y += 24;
        func_230481_d_(_enemyPingButton = new Button(x, y, 133, 20, new StringTextComponent("Enemy Player Ping"), b -> {
            PlayerTypeInfo playerTypeInfo = _config.getPlayerTypeInfo(PlayerType.Enemy);
            playerTypeInfo.ping = !playerTypeInfo.ping;
            _config.save();
        }));
        func_230481_d_(_enemySoundButton = new Button(x + 133 + 1, y, 66, 20, new StringTextComponent("Sound"), b -> Minecraft.getInstance().displayGuiScreen(new GuiChooseSoundScreen(this, _config, PlayerType.Enemy))));
        y += 24;
        func_230481_d_(new Button(x, y, 200, 20, new StringTextComponent("Done"), b -> Minecraft.getInstance().displayGuiScreen(_parent)));
    }

    @Override
    public void func_231023_e_() {  //tick
        boolean isChanged = false;

        Color neutralColor = new Color(_neutralRedSlider.getValue(), _neutralGreenSlider.getValue(), _neutralBlueSlider.getValue());
        Color allyColor = new Color(_allyRedSlider.getValue(), _allyGreenSlider.getValue(), _allyBlueSlider.getValue());
        Color enemyColor = new Color(_enemyRedSlider.getValue(), _enemyGreenSlider.getValue(), _enemyBlueSlider.getValue());

        isChanged = changeColor(PlayerType.Neutral, neutralColor) || isChanged;
        isChanged = changeColor(PlayerType.Ally, allyColor) || isChanged;
        isChanged = changeColor(PlayerType.Enemy, enemyColor) || isChanged;

        if(isChanged)
            _config.save();

        PlayerTypeInfo neutralPlayer = _config.getPlayerTypeInfo(PlayerType.Neutral);
        PlayerTypeInfo allyPlayer = _config.getPlayerTypeInfo(PlayerType.Ally);
        PlayerTypeInfo enemyPlayer = _config.getPlayerTypeInfo(PlayerType.Enemy);

        _neutralPingButton.func_238482_a_(new StringTextComponent("Neutral Player Ping: " + (neutralPlayer.ping ? "On" : "Off")));
        _neutralSoundButton.func_238482_a_(new StringTextComponent(SoundInfo.getByValue(neutralPlayer.soundEventName).name));
        _allyPingButton.func_238482_a_(new StringTextComponent("Ally Player Ping: " + (allyPlayer.ping ? "On" : "Off")));
        _allySoundButton.func_238482_a_(new StringTextComponent(SoundInfo.getByValue(allyPlayer.soundEventName).name));
        _enemyPingButton.func_238482_a_(new StringTextComponent("Enemy Player Ping: " + (enemyPlayer.ping ? "On" : "Off")));
        _enemySoundButton.func_238482_a_(new StringTextComponent(SoundInfo.getByValue(enemyPlayer.soundEventName).name));
    }

    private boolean changeColor(PlayerType playerType, Color color) {
        PlayerTypeInfo info = _config.getPlayerTypeInfo(playerType);

        if(info.color == color)
            return false;

        info.color = color;

        return true;
    }

    @Override
    public void func_231175_as__() { //onClose
        Minecraft.getInstance().displayGuiScreen(_parent);
    }

    @Override
    public void func_230430_a_(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {  //render
    	func_230446_a_(new MatrixStack()); //renderBackground()
        func_238471_a_(new MatrixStack(), this.field_230712_o_,"Player Settings", this.field_230708_k_ / 2, this.field_230709_l_ / 4 - 40, Color.WHITE.getRGB());
        func_238471_a_(new MatrixStack(),this.field_230712_o_, "Neutral", this.field_230708_k_ / 2, _neutralRedSlider.field_230691_m_ - 12, _config.getPlayerTypeInfo(PlayerType.Neutral).color.getRGB());
        func_238471_a_(new MatrixStack(),this.field_230712_o_, "Ally", this.field_230708_k_ / 2, _allyRedSlider.field_230691_m_ - 12, _config.getPlayerTypeInfo(PlayerType.Ally).color.getRGB());
        func_238471_a_(new MatrixStack(),this.field_230712_o_, "Enemy", this.field_230708_k_ / 2, _enemyRedSlider.field_230691_m_ - 12, _config.getPlayerTypeInfo(PlayerType.Enemy).color.getRGB());
        super.func_230430_a_(matrix, mouseX, mouseY, partialTicks);
    }
}
