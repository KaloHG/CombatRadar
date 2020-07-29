package com.aleksey.combatradar.gui;

import com.aleksey.combatradar.config.RadarConfig;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

/**
 * @author Aleksey Terzi
 */
public class GuiLocationAndColorScreen extends Screen {
    private RadarConfig _config;
    private Screen _parent;
    private GuiSlider _redSlider;
    private GuiSlider _greenSlider;
    private GuiSlider _blueSlider;
    private GuiSlider _opacitySlider;
    private GuiSlider _sizeSlider;
    private GuiSlider _rangeSlider;
    private GuiSlider _iconScaleSlider;
    private GuiSlider _fontScaleSlider;

    public GuiLocationAndColorScreen(Screen parent, RadarConfig config) {
        super(new StringTextComponent("Location and Color"));
        _parent = parent;
        _config = config;
    }

    @Override
    public void func_231160_c_() { //init
        this.field_230705_e_.clear();
        this.field_230710_m_.clear();

        int y = this.field_230709_l_ / 4 - 16;
        int x = this.field_230708_k_ / 2 - 100;

        func_230481_d_(_redSlider = new GuiSlider(x, y, 66, 1, 0, "Red", _config.getRadarColor().getRed() / 255f, false));
        func_230481_d_(_greenSlider = new GuiSlider(x + 66 + 1, y, 66, 1, 0, "Green", _config.getRadarColor().getGreen() / 255f, false));
        func_230481_d_(_blueSlider = new GuiSlider(x + 66 + 1 + 66 + 1, y, 66, 1, 0, "Blue", _config.getRadarColor().getBlue() / 255f, false));
        y += 24;
        func_230481_d_(_opacitySlider = new GuiSlider(x, y, 200, 1, 0, "Radar Opacity", _config.getRadarOpacity(), false));
        y += 24;
        func_230481_d_(_sizeSlider = new GuiSlider(x, y, 100, 1, 0.1f, "Radar Size", _config.getRadarSize(), false));
        func_230481_d_(_rangeSlider = new GuiSlider(x + 101, y, 100, 8, 3, "Radar Range", _config.getRadarDistance() / 16f, true));
        y += 24;
        func_230481_d_(_iconScaleSlider = new GuiSlider(x, y, 100, 1f, 0.1f, "Icon Size", _config.getIconScale() / 3f, false));
        func_230481_d_(_fontScaleSlider = new GuiSlider(x + 101, y, 100, 1f, 0.2f, "Font Size", _config.getFontScale() / 3f, false));
        y += 24 + 24;
        func_230481_d_(new Button(x, y, 100, 20, new StringTextComponent("Snap top left"), b -> {
            _config.setRadarX(0);
            _config.setRadarY(0);
            _config.save();
        }));
        func_230481_d_(new Button(x + 101, y, 100, 20, new StringTextComponent("Snap top right"), b -> {
            _config.setRadarX(1);
            _config.setRadarY(0);
            _config.save();
        }));
        y += 24;
        func_230481_d_(new Button(x, y, 100, 20, new StringTextComponent("Snap bottom left"), b -> {
            _config.setRadarX(0);
            _config.setRadarY(1);
            _config.save();
        }));
        func_230481_d_(new Button(x + 101, y, 100, 20, new StringTextComponent("Snap bottom right"), b -> {
            _config.setRadarX(1);
            _config.setRadarY(1);
            _config.save();
        }));
        y += 24;
        func_230481_d_(new Button(x, y, 200, 20, new StringTextComponent("Done"), b -> Minecraft.getInstance().displayGuiScreen(_parent)));
    }

    @Override
    public void func_231023_e_() { //tick
        boolean isChanged = false;

        MainWindow res = Minecraft.getInstance().getMainWindow();
        float xSpeed = 1.f / res.getScaledWidth();
        float ySpeed = 1.f / res.getScaledHeight();
        long handle = Minecraft.getInstance().getMainWindow().getHandle();

        if(InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_LEFT)) {
            _config.setRadarX(_config.getRadarX() - xSpeed);
            isChanged = true;
        }
        if(InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT)) {
            _config.setRadarX(_config.getRadarX() + xSpeed);
            isChanged = true;
        }
        if(InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_UP)) {
            _config.setRadarY(_config.getRadarY() - ySpeed);
            isChanged = true;
        }
        if(InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_DOWN)) {
            _config.setRadarY(_config.getRadarY() + ySpeed);
            isChanged = true;
        }

        Color radarColor = new Color(_redSlider.getValue(), _greenSlider.getValue(), _blueSlider.getValue());

        isChanged = _config.setRadarColor(radarColor) || isChanged;
        isChanged = _config.setRadarOpacity(_opacitySlider.getValue()) || isChanged;
        isChanged = _config.setRadarSize(_sizeSlider.getValue()) || isChanged;
        isChanged = _config.setRadarDistance((int)(_rangeSlider.getValue() * 16)) || isChanged;
        isChanged = _config.setIconScale(_iconScaleSlider.getValue() * 3f) || isChanged;
        isChanged = _config.setFontScale(_fontScaleSlider.getValue() * 3f) || isChanged;

        if(isChanged)
            _config.save();
    }

    @Override
    public void func_231175_as__() {  //onClose
        Minecraft.getInstance().displayGuiScreen(_parent);
    }

    @Override
    public void func_230430_a_(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {  //render
    	func_231165_f_(0); //draw dirt background
        func_238471_a_(new MatrixStack(), this.field_230712_o_, "Location and Color", this.field_230708_k_ / 2, this.field_230709_l_ / 4 - 40, Color.WHITE.getRGB());
        func_238471_a_(new MatrixStack(), this.field_230712_o_, "Use arrow keys to reposition radar", this.field_230708_k_ / 2, _iconScaleSlider.field_230691_m_ + 24 + 12, Color.WHITE.getRGB());
        super.func_230430_a_(matrix, mouseX, mouseY, partialTicks);
    }
}