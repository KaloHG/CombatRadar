package com.aleksey.combatradar.gui;

import com.aleksey.combatradar.config.PlayerType;
import com.aleksey.combatradar.config.RadarConfig;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

/**
 * @author Aleksey Terzi
 */
public class GuiAddPlayerScreen extends Screen {
    private RadarConfig _config;
    private Screen _parent;
    private PlayerType _playerType;
    private TextFieldWidget _playerNameTextField;

    public GuiAddPlayerScreen(Screen parent, RadarConfig config, PlayerType playerType) {
        super(new StringTextComponent("Add Player"));
        _parent = parent;
        _config = config;
        _playerType = playerType;
    }

    @Override
    public void func_231160_c_() { //init
        this.field_230705_e_.clear();
        this.field_230710_m_.clear();

        int y = this.field_230709_l_ / 3;
        int x = this.field_230708_k_ / 2 - 100;

        func_230480_a_(_playerNameTextField = new TextFieldWidget(this.field_230712_o_, x, y, 200, 20, new StringTextComponent("")));
        y += 24;
        func_230481_d_(new Button(x, y, 200, 20, new StringTextComponent("Add"), b -> actionAdd()));
        y += 24;
        func_230481_d_(new Button(x, y, 200, 20, new StringTextComponent("Cancel"), b -> actionCancel()));

        func_231035_a_(_playerNameTextField); //setFocused
        _playerNameTextField.setFocused2(true);
    }

    @Override
    public boolean func_231046_a_(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        if(p_keyPressed_1_ == GLFW.GLFW_KEY_ENTER)
            actionAdd();
        else if(p_keyPressed_1_ == GLFW.GLFW_KEY_ESCAPE)
            actionCancel();

        return super.func_231046_a_(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    private void actionAdd() {
        String playerName = _playerNameTextField.getText().trim();

        if(playerName.length() == 0) {
            return;
        }

        _config.setPlayerType(playerName, _playerType);
        _config.save();

        Minecraft.getInstance().displayGuiScreen(_parent);
    }

    private void actionCancel() {
    	Minecraft.getInstance().displayGuiScreen(_parent);
    }

    @Override
    public void func_231175_as__() { //onClose
        Minecraft.getInstance().displayGuiScreen(_parent);
    }

    @Override
    public void func_230430_a_(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) { //render
        String playerTypeName = _playerType == PlayerType.Ally ? "Ally" : "Enemy";

        func_231165_f_(0); //draw dirt background
        func_238471_a_(new MatrixStack(), this.field_230712_o_, "Add " + playerTypeName + " Player", this.field_230708_k_ / 2, this.field_230709_l_ / 4 - 40, Color.WHITE.getRGB());

        field_230712_o_.drawStringWithShadow("Player username", this.field_230708_k_ / 2 - 100, _playerNameTextField.field_230691_m_ - 12, Color.LIGHT_GRAY.getRGB());

        super.func_230430_a_(matrix, mouseX, mouseY, partialTicks);
    }
}