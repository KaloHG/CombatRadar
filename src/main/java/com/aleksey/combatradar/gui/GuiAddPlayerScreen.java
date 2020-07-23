package com.aleksey.combatradar.gui;

import com.aleksey.combatradar.config.PlayerType;
import com.aleksey.combatradar.config.RadarConfig;
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
    public void init() {
        this.buttons.clear();
        this.children.clear();

        int y = this.height / 3;
        int x = this.width / 2 - 100;

        addButton(_playerNameTextField = new TextFieldWidget(font, x, y, 200, 20, ""));
        y += 24;
        addButton(new Button(x, y, 200, 20, "Add", b -> actionAdd()));
        y += 24;
        addButton(new Button(x, y, 200, 20, "Cancel", b -> actionCancel()));

        setFocused(_playerNameTextField);
        _playerNameTextField.setFocused2(true);
    }

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        if(p_keyPressed_1_ == GLFW.GLFW_KEY_ENTER)
            actionAdd();
        else if(p_keyPressed_1_ == GLFW.GLFW_KEY_ESCAPE)
            actionCancel();

        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    private void actionAdd() {
        String playerName = _playerNameTextField.getText().trim();

        if(playerName.length() == 0)
            return;

        _config.setPlayerType(playerName, _playerType);
        _config.save();

        minecraft.displayGuiScreen(_parent);
    }

    private void actionCancel() {
        minecraft.displayGuiScreen(_parent);
    }

    @Override
    public void onClose() {
        minecraft.displayGuiScreen(_parent);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        String playerTypeName = _playerType == PlayerType.Ally ? "Ally" : "Enemy";

        renderDirtBackground(0);
        drawCenteredString(this.font, "Add " + playerTypeName + " Player", this.width / 2, this.height / 4 - 40, Color.WHITE.getRGB());

        font.drawStringWithShadow("Player username", this.width / 2 - 100, _playerNameTextField.y - 12, Color.LIGHT_GRAY.getRGB());

        super.render(mouseX, mouseY, partialTicks);
    }
}