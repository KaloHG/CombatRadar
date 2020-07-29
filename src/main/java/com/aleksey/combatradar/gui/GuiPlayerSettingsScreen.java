package com.aleksey.combatradar.gui;

import com.aleksey.combatradar.config.PlayerType;
import com.aleksey.combatradar.config.PlayerTypeInfo;
import com.aleksey.combatradar.config.RadarConfig;
import com.aleksey.combatradar.config.SoundInfo;
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
        this.buttons.clear();
        this.children.clear();

        int y = this.height / 4 - 16 + 12;
        int x = this.width / 2 - 100;

        PlayerTypeInfo neutralInfo = _config.getPlayerTypeInfo(PlayerType.Neutral);
        PlayerTypeInfo allyInfo = _config.getPlayerTypeInfo(PlayerType.Ally);
        PlayerTypeInfo enemyInfo = _config.getPlayerTypeInfo(PlayerType.Enemy);

        addButton(_neutralRedSlider = new GuiSlider(x, y, 66, 1, 0, "Red", neutralInfo.color.getRed() / 255f, false));
        addButton(_neutralGreenSlider = new GuiSlider(x + 66 + 1, y, 66, 1, 0, "Green", neutralInfo.color.getGreen() / 255f, false));
        addButton(_neutralBlueSlider = new GuiSlider(x + 66 + 1 + 66 + 1, y, 66, 1, 0, "Blue", neutralInfo.color.getBlue() / 255f, false));
        y += 24 + 12;
        addButton(_allyRedSlider = new GuiSlider(x, y, 66, 1, 0, "Red", allyInfo.color.getRed() / 255f, false));
        addButton(_allyGreenSlider = new GuiSlider(x + 66 + 1, y, 66, 1, 0, "Green", allyInfo.color.getGreen() / 255f, false));
        addButton(_allyBlueSlider = new GuiSlider(x + 66 + 1 + 66 + 1, y, 66, 1, 0, "Blue", allyInfo.color.getBlue() / 255f, false));
        y += 24 + 12;
        addButton(_enemyRedSlider = new GuiSlider(x, y, 66, 1, 0, "Red", enemyInfo.color.getRed() / 255f, false));
        addButton(_enemyGreenSlider = new GuiSlider(x + 66 + 1, y, 66, 1, 0, "Green", enemyInfo.color.getGreen() / 255f, false));
        addButton(_enemyBlueSlider = new GuiSlider(x + 66 + 1 + 66 + 1, y, 66, 1, 0, "Blue", enemyInfo.color.getBlue() / 255f, false));
        y += 24;
        addButton(_neutralPingButton = new Button(x, y, 133, 20, "Neutral Player Ping", b -> {
            PlayerTypeInfo playerTypeInfo = _config.getPlayerTypeInfo(PlayerType.Neutral);
            playerTypeInfo.ping = !playerTypeInfo.ping;
            _config.save();
        }));
        addButton(_neutralSoundButton = new Button(x + 133 + 1, y, 66, 20, "Sound", b -> minecraft.displayGuiScreen(new GuiChooseSoundScreen(this, _config, PlayerType.Neutral))));
        y += 24;
        addButton(_allyPingButton = new Button(x, y, 133, 20, "Ally Player Ping", b -> {
            PlayerTypeInfo playerTypeInfo = _config.getPlayerTypeInfo(PlayerType.Ally);
            playerTypeInfo.ping = !playerTypeInfo.ping;
            _config.save();
        }));
        addButton(_allySoundButton = new Button(x + 133 + 1, y, 66, 20, "Sound", b -> minecraft.displayGuiScreen(new GuiChooseSoundScreen(this, _config, PlayerType.Ally))));
        y += 24;
        addButton(_enemyPingButton = new Button(x, y, 133, 20, "Enemy Player Ping", b -> {
            PlayerTypeInfo playerTypeInfo = _config.getPlayerTypeInfo(PlayerType.Enemy);
            playerTypeInfo.ping = !playerTypeInfo.ping;
            _config.save();
        }));
        addButton(_enemySoundButton = new Button(x + 133 + 1, y, 66, 20, "Sound", b -> minecraft.displayGuiScreen(new GuiChooseSoundScreen(this, _config, PlayerType.Enemy))));
        y += 24;
        addButton(new Button(x, y, 200, 20, "Done", b -> minecraft.displayGuiScreen(_parent)));
    }

    @Override
    public void tick() {
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

        _neutralPingButton.setMessage("Neutral Player Ping: " + (neutralPlayer.ping ? "On" : "Off"));
        _neutralSoundButton.setMessage(SoundInfo.getByValue(neutralPlayer.soundEventName).name);
        _allyPingButton.setMessage("Ally Player Ping: " + (allyPlayer.ping ? "On" : "Off"));
        _allySoundButton.setMessage(SoundInfo.getByValue(allyPlayer.soundEventName).name);
        _enemyPingButton.setMessage("Enemy Player Ping: " + (enemyPlayer.ping ? "On" : "Off"));
        _enemySoundButton.setMessage(SoundInfo.getByValue(enemyPlayer.soundEventName).name);
    }

    private boolean changeColor(PlayerType playerType, Color color) {
        PlayerTypeInfo info = _config.getPlayerTypeInfo(playerType);

        if(info.color == color)
            return false;

        info.color = color;

        return true;
    }

    @Override
    public void onClose() {
        minecraft.displayGuiScreen(_parent);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        drawCenteredString(this.font, "Player Settings", this.width / 2, this.height / 4 - 40, Color.WHITE.getRGB());
        drawCenteredString(this.font, "Neutral", this.width / 2, _neutralRedSlider.y - 12, _config.getPlayerTypeInfo(PlayerType.Neutral).color.getRGB());
        drawCenteredString(this.font, "Ally", this.width / 2, _allyRedSlider.y - 12, _config.getPlayerTypeInfo(PlayerType.Ally).color.getRGB());
        drawCenteredString(this.font, "Enemy", this.width / 2, _enemyRedSlider.y - 12, _config.getPlayerTypeInfo(PlayerType.Enemy).color.getRGB());
        super.render(mouseX, mouseY, partialTicks);
    }
}
