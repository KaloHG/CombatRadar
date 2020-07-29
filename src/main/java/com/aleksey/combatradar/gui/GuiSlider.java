package com.aleksey.combatradar.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

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
        super(x, y, width, 20, new StringTextComponent(name + ": " + _decimalFormat.format(value)), b -> {});
        _maxValue = maxValue;
        _minValue = minValue;
        _value = value;
        _name = name;
        _integer = integer;
    }

    @Override
    protected void func_230983_a_(double p_onDrag_1_, double p_onDrag_3_, double p_onDrag_5_, double p_onDrag_7_) { //onDrag
        if (!this.field_230694_p_) { //isVisible check
            return;
        }

        if (_dragging) {
            calculateValue((int) p_onDrag_1_);
        }
        update();
    }

    @Override
    public void func_230982_a_(double mouseX, double mouseY) {  //onClick
        calculateValue((int) mouseX);

        _dragging = true;
    }
    
    @Override
    public void func_231000_a__(double mouseX, double mouseY) { //onRelease
        _dragging = false;
    }

    private void update() {
    	func_238482_a_(new StringTextComponent(_name + ": " + _decimalFormat.format(_value)));
    }

    private void calculateValue(int mouseX) {
        _value = ((float) (mouseX - 4 - this.field_230690_l_)) * (_maxValue - _minValue) / (this.field_230690_l_ - 8) + _minValue;

        if(_integer)
            _value = Math.round(_value);

        if (_value < _minValue)
            _value = _minValue;
        else if (_value > _maxValue)
            _value = _maxValue;
    }

    @Override
    protected int func_230989_a_(boolean p_getYImage_1_) { //getYImage
        return 0;
    }

    @Override
    protected void func_230441_a_(MatrixStack matrix, Minecraft p_renderBg_1_, int p_renderBg_2_, int p_renderBg_3_) { //renderBg
        p_renderBg_1_.getTextureManager().bindTexture(field_230687_i_);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.field_230692_n_ ? 2 : 1) * 20; //if isHovered
        //blit
        func_238474_b_(new MatrixStack(),this.field_230690_l_ + (int)(normalizedValue() * (double)(this.field_230690_l_ - 8)), this.field_230691_m_, 0, 46 + i, 4, 20);
      //blit
        func_238474_b_(new MatrixStack(),this.field_230690_l_ + (int)(normalizedValue() * (double)(this.field_230690_l_ - 8)) + 4, this.field_230691_m_, 196, 46 + i, 4, 20);
    }

    private double normalizedValue() {
        return (_value - _minValue) / (_maxValue - _minValue);
    }
}