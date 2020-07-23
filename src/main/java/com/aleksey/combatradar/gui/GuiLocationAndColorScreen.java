package com.aleksey.combatradar.gui;

import com.aleksey.combatradar.config.RadarConfig;
import net.minecraft.client.MainWindow;
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
    public void init() {
        this.buttons.clear();
        this.children.clear();

        int y = this.height / 4 - 16;
        int x = this.width / 2 - 100;

        addButton(_redSlider = new GuiSlider(x, y, 66, 1, 0, "Red", _config.getRadarColor().getRed() / 255f, false));
        addButton(_greenSlider = new GuiSlider(x + 66 + 1, y, 66, 1, 0, "Green", _config.getRadarColor().getGreen() / 255f, false));
        addButton(_blueSlider = new GuiSlider(x + 66 + 1 + 66 + 1, y, 66, 1, 0, "Blue", _config.getRadarColor().getBlue() / 255f, false));
        y += 24;
        addButton(_opacitySlider = new GuiSlider(x, y, 200, 1, 0, "Radar Opacity", _config.getRadarOpacity(), false));
        y += 24;
        addButton(_sizeSlider = new GuiSlider(x, y, 100, 1, 0.1f, "Radar Size", _config.getRadarSize(), false));
        addButton(_rangeSlider = new GuiSlider(x + 101, y, 100, 8, 3, "Radar Range", _config.getRadarDistance() / 16f, true));
        y += 24;
        addButton(_iconScaleSlider = new GuiSlider(x, y, 100, 1f, 0.1f, "Icon Size", _config.getIconScale() / 3f, false));
        addButton(_fontScaleSlider = new GuiSlider(x + 101, y, 100, 1f, 0.2f, "Font Size", _config.getFontScale() / 3f, false));
        y += 24 + 24;
        addButton(new Button(x, y, 100, 20, "Snap top left", b -> {
            _config.setRadarX(0);
            _config.setRadarY(0);
            _config.save();
        }));
        addButton(new Button(x + 101, y, 100, 20, "Snap top right", b -> {
            _config.setRadarX(1);
            _config.setRadarY(0);
            _config.save();
        }));
        y += 24;
        addButton(new Button(x, y, 100, 20, "Snap bottom left", b -> {
            _config.setRadarX(0);
            _config.setRadarY(1);
            _config.save();
        }));
        addButton(new Button(x + 101, y, 100, 20, "Snap bottom right", b -> {
            _config.setRadarX(1);
            _config.setRadarY(1);
            _config.save();
        }));
        y += 24;
        addButton(new Button(x, y, 200, 20, "Done", b -> minecraft.displayGuiScreen(_parent)));
    }

    @Override
    public void tick() {
        boolean isChanged = false;

        MainWindow res = minecraft.mainWindow;
        float xSpeed = 1.f / res.getScaledWidth();
        float ySpeed = 1.f / res.getScaledHeight();
        long handle = minecraft.mainWindow.getHandle();

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
    public void onClose() {
        minecraft.displayGuiScreen(_parent);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        drawCenteredString(this.font, "Location and Color", this.width / 2, this.height / 4 - 40, Color.WHITE.getRGB());
        drawCenteredString(this.font, "Use arrow keys to reposition radar", this.width / 2, _iconScaleSlider.y + 24 + 12, Color.WHITE.getRGB());
        super.render(mouseX, mouseY, partialTicks);
    }
}