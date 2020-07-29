package com.aleksey.combatradar.gui;

import com.aleksey.combatradar.SoundHelper;
import com.aleksey.combatradar.config.PlayerType;
import com.aleksey.combatradar.config.RadarConfig;
import com.aleksey.combatradar.config.SoundInfo;
import com.google.common.math.IntMath;
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
        _titleTop = this.height / 4 - 40;

        this.buttons.clear();
        this.children.clear();

        int topY = this.height / 4 - 16;
        int leftX = this.width / 2 - 60;

        int y = topY;
        int x = leftX;

        String sound = _config.getPlayerTypeInfo(_playerType).soundEventName;

        for(int i = 0; i < SoundInfo.SOUND_LIST.length; i++) {
            SoundInfo info = SoundInfo.SOUND_LIST[i];

            int finalI = i;
            GuiCheckButton chk = new GuiCheckButton(x, y, 80, info.name, b -> chooseSound(finalI));
            chk.setChecked(sound.equalsIgnoreCase(info.value));

            addButton(chk);

            if(i == IntMath.divide(SoundInfo.SOUND_LIST.length, 2, RoundingMode.UP) - 1) {
                y = topY;
                x = leftX + 80;
            } else {
                y += 20;
            }
        }

        addButton(new Button(this.width / 2 - 100, y, 200, 20, "Done", b -> minecraft.displayGuiScreen(_parent)));
    }

    @Override
    public void onClose() {
        _config.save();
        minecraft.displayGuiScreen(_parent);
    }

    private void chooseSound(int index) {
        for(int i = 0; i < SoundInfo.SOUND_LIST.length; i++) {
            boolean isChecked = i == index;
            ((GuiCheckButton)this.buttons.get(i)).setChecked(isChecked);
        }

        SoundInfo soundInfo = SoundInfo.SOUND_LIST[index];

        _config.getPlayerTypeInfo(_playerType).soundEventName = soundInfo.value;
        _config.save();

        SoundHelper.playSound(this.minecraft, soundInfo.value, this.minecraft.player.getUniqueID());
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
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

        renderDirtBackground(0);
        drawCenteredString(this.font, title, this.width / 2, _titleTop, Color.WHITE.getRGB());
        super.render(mouseX, mouseY, partialTicks);
    }
}