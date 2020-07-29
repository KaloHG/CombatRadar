package com.aleksey.combatradar.gui;

import com.aleksey.combatradar.config.RadarConfig;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;


/**
 * @author Aleksey Terzi
 */
public class GuiMainScreen extends Screen {
    private RadarConfig _config;
    private Screen _parent;
    private Button _playerStatusButton;
    private Button _enableButton;

    public GuiMainScreen(Screen parent, RadarConfig config) {
        super(new StringTextComponent("Main"));
        _parent = parent;
        _config = config;
    }

    @Override
    public void func_231160_c_() {
        this.field_230705_e_.clear();
        this.field_230710_m_.clear();

        int y = this.field_230709_l_ / 4 - 16;
        int x = this.field_230708_k_ / 2 - 100;

        func_230481_d_(new Button(x, y, 200, 20, new StringTextComponent("Location and Color"), b -> Minecraft.getInstance().displayGuiScreen(new GuiLocationAndColorScreen(this, _config))));
        y += 24;
        func_230481_d_(new Button(x, y, 200, 20, new StringTextComponent("Radar Entities"), b -> Minecraft.getInstance().displayGuiScreen(new GuiEntityScreen(this, _config))));
        y += 24;
        func_230481_d_(new Button(x, y, 200, 20, new StringTextComponent("Manage Players"), b -> Minecraft.getInstance().displayGuiScreen(new GuiManagePlayerScreen(this, _config))));
        y += 24;
        func_230481_d_(new Button(x, y, 200, 20, new StringTextComponent("Player Settings"), b -> Minecraft.getInstance().displayGuiScreen(new GuiPlayerSettingsScreen(this, _config))));
        y += 24;
        func_230481_d_(_playerStatusButton = new Button(x, y, 200, 20, new StringTextComponent("Log Players Statuses:"), b -> {
            _config.setLogPlayerStatus(!_config.getLogPlayerStatus());
            _config.save();
        }));
        y += 24;
        func_230481_d_(_enableButton = new Button(x, y, 100, 20, new StringTextComponent("Radar: "), b -> {
            _config.setEnabled(!_config.getEnabled());
            _config.save();
        }));
        func_230481_d_(new Button(x + 101, y, 100, 20, new StringTextComponent("Done"), b -> Minecraft.getInstance().displayGuiScreen(_parent)));
    }

    @Override
    public void func_231023_e_() {  //tick
        _playerStatusButton.func_238482_a_(new StringTextComponent("Log Players Statuses: " + (_config.getLogPlayerStatus() ? "On" : "Off")));
        _enableButton.func_238482_a_(new StringTextComponent("Radar: " + (_config.getEnabled() ? "On" : "Off")));
    }

    @Override
    public void func_231175_as__() {  //onClose
        Minecraft.getInstance().displayGuiScreen(_parent);
    }

    @Override
    public void func_230430_a_(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {  //render
        ITextComponent keyName = _config.getSettingsKey().func_238171_j_();

        func_230446_a_(new MatrixStack()); //renderBackground()
        func_238471_a_(new MatrixStack(), this.field_230712_o_,"Combat Radar Settings", this.field_230708_k_ / 2, this.field_230709_l_ / 4 - 40, Color.WHITE.getRGB());
        func_238471_a_(new MatrixStack(), this.field_230712_o_,"Ctrl+Alt+" + keyName.getUnformattedComponentText() + " - enable/disable radar", this.field_230708_k_ / 2, _enableButton.field_230691_m_ + 24, Color.LIGHT_GRAY.getRGB());
        func_238471_a_(new MatrixStack(), this.field_230712_o_,"Ctrl+" + keyName.getUnformattedComponentText() + " - enable/disable mobs", this.field_230708_k_ / 2, _enableButton.field_230691_m_ + 24 + 12, Color.LIGHT_GRAY.getRGB());
        super.func_230430_a_(matrix, mouseX, mouseY, partialTicks);
    }
}
