package com.aleksey.combatradar.gui;

import com.aleksey.combatradar.config.RadarConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
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
    public void tick() {
        _playerStatusButton.setMessage("Log Players Statuses: " + (_config.getLogPlayerStatus() ? "On" : "Off"));
        _enableButton.setMessage("Radar: " + (_config.getEnabled() ? "On" : "Off"));
    }

    @Override
    public void onClose() {
        minecraft.displayGuiScreen(_parent);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        String keyName = _config.getSettingsKey().getLocalizedName();

        renderBackground();
        drawCenteredString(this.font, "Combat Radar Settings", this.width / 2, this.height / 4 - 40, Color.WHITE.getRGB());
        drawCenteredString(font, "Ctrl+Alt+" + keyName + " - enable/disable radar", this.width / 2, _enableButton.y + 24, Color.LIGHT_GRAY.getRGB());
        drawCenteredString(font, "Ctrl+" + keyName + " - enable/disable mobs", this.width / 2, _enableButton.y + 24 + 12, Color.LIGHT_GRAY.getRGB());
        super.render(mouseX, mouseY, partialTicks);
    }
}
