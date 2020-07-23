package com.aleksey.combatradar.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;

import java.text.DecimalFormat;

/**
 * @author Aleksey Terzi
 */
public class GuiSlider extends Button {
    private static DecimalFormat _decimalFormat = new DecimalFormat("#.##");

    private float _value;
    private boolean _dragging;
    private float _minValue;
    private float _maxValue;
    private String _name;
    private boolean _integer;

    public float getValue() { return _value; }

    public GuiSlider(int x, int y, int width, float maxValue, float minValue, String name, float value, boolean integer) {
        super(x, y, width, 20, name + ": " + _decimalFormat.format(value), b -> {});
        _maxValue = maxValue;
        _minValue = minValue;
        _value = value;
        _name = name;
        _integer = integer;
    }

    @Override
    protected void onDrag(double p_onDrag_1_, double p_onDrag_3_, double p_onDrag_5_, double p_onDrag_7_) {
        if (!this.visible)
            return;

        if (_dragging)
            calculateValue((int) p_onDrag_1_);

        update();
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        calculateValue((int) mouseX);

        _dragging = true;
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        _dragging = false;
    }

    private void update() {
        setMessage(_name + ": " + _decimalFormat.format(_value));
    }

    private void calculateValue(int mouseX) {
        _value = ((float) (mouseX - 4 - this.x)) * (_maxValue - _minValue) / (width - 8) + _minValue;

        if(_integer)
            _value = Math.round(_value);

        if (_value < _minValue)
            _value = _minValue;
        else if (_value > _maxValue)
            _value = _maxValue;
    }

    @Override
    protected int getYImage(boolean p_getYImage_1_) {
        return 0;
    }

    @Override
    protected void renderBg(Minecraft p_renderBg_1_, int p_renderBg_2_, int p_renderBg_3_) {
        p_renderBg_1_.getTextureManager().bindTexture(WIDGETS_LOCATION);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.isHovered() ? 2 : 1) * 20;
        this.blit(this.x + (int)(normalizedValue() * (double)(this.width - 8)), this.y, 0, 46 + i, 4, 20);
        this.blit(this.x + (int)(normalizedValue() * (double)(this.width - 8)) + 4, this.y, 196, 46 + i, 4, 20);
    }

    private double normalizedValue() {
        return (_value - _minValue) / (_maxValue - _minValue);
    }
}