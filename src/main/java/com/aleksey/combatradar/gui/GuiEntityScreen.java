package com.aleksey.combatradar.gui;

import com.aleksey.combatradar.config.GroupType;
import com.aleksey.combatradar.config.RadarConfig;
import com.aleksey.combatradar.config.RadarEntityInfo;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Aleksey Terzi
 */
public class GuiEntityScreen extends Screen {
    private static class EntityGroup {
        public GroupType groupType;
        public List<RadarEntityInfo> entities;
        public List<Integer> listColTextWidth;

        public int getColWidth(int colIndex) {
            return ICON_WIDTH + listColTextWidth.get(colIndex) + BUTTON_WIDTH + 25;
        }

        public int getIconAndTextWidth(int colIndex) {
            return ICON_WIDTH + listColTextWidth.get(colIndex) + 1;
        }

        public int getTotalWidth() {
            int totalWidth = 0;

            for(int i = 0; i < listColTextWidth.size(); i++)
                totalWidth += getColWidth(i);

            return totalWidth;
        }

        public EntityGroup(GroupType groupType) {
            this.groupType = groupType;
            this.entities = new ArrayList<RadarEntityInfo>();
            this.listColTextWidth = new ArrayList<Integer>();
        }
    }

    private static final int MAX_ENTITIES_PER_COL = 8;
    private static final int ICON_WIDTH = 12;
    private static final int LINE_HEIGHT = 16;
    private static final int BUTTON_WIDTH = 24;

    private static GroupType _activeGroupType = GroupType.Neutral;

    private RadarConfig _config;
    private Screen _parent;
    private Button _enableButton;

    private Map<GroupType, EntityGroup> _groups;
    private int _titleTop;
    private int _buttonTop;
    private int _iconTop, _iconLeft;
    private EntityGroup _activeGroup;
    private String _groupName;

    public GuiEntityScreen(Screen parent, RadarConfig config) {
        super(new StringTextComponent("Entity"));
        _parent = parent;
        _config = config;
    }

    @Override
    public void func_231160_c_() {
        _titleTop = this.height / 4 - 40;
        _buttonTop = this.height - this.height / 4 - 10;
        _iconTop = _titleTop + 16 + (this.height - (this.height - _buttonTop) - _titleTop - 16 - MAX_ENTITIES_PER_COL * LINE_HEIGHT) / 2;

        createEntityGroups();
        showGroup(_activeGroupType);
    }

    private void createEntityGroups() {
        _groups = new HashMap<GroupType, EntityGroup>();

        for(RadarEntityInfo info : _config.getEntities()) {
            EntityGroup group = _groups.get(info.getGroupType());

            if(group == null)
                _groups.put(info.getGroupType(), group = new EntityGroup(info.getGroupType()));

            int colIndex = group.entities.size() / MAX_ENTITIES_PER_COL;
            int textWidth = this.font.getStringWidth(info.getName());

            if(group.listColTextWidth.size() <= colIndex)
                group.listColTextWidth.add(textWidth);
            else if(group.listColTextWidth.get(colIndex) < textWidth)
                group.listColTextWidth.set(colIndex, textWidth);

            group.entities.add(info);
        }
    }

    private void showGroup(GroupType groupType) {
        _activeGroupType = groupType;
        _activeGroup = _groups.get(groupType);
        _iconLeft = (this.width - _activeGroup.getTotalWidth() + 25) / 2;

        this.buttons.clear();
        this.children.clear();

        int y = _buttonTop;
        int x = this.width / 2 - 100;

        Button neutralButton, aggressiveButton, otherButton;

        addButton(neutralButton = new Button(x, y, 66, 20, "Neutral", b -> showGroup(GroupType.Neutral)));
        addButton(aggressiveButton = new Button(x + 66 + 1, y, 66, 20, "Agressive", b -> showGroup(GroupType.Aggressive)));
        addButton(otherButton = new Button(x + 66 + 1 + 66 + 1, y, 66, 20, "Other", b -> showGroup(GroupType.Other)));

        switch(groupType) {
            case Neutral:
                neutralButton.active = false;
                _groupName = "Neutral";
                break;
            case Aggressive:
                aggressiveButton.active = false;
                _groupName = "Aggressive";
                break;
            case Other:
                otherButton.active = false;
                _groupName = "Other";
                break;
        }

        y += 24;
        addButton(_enableButton = new Button(x, y, 200, 20, getEnableButtonText(), b -> {
            _config.setGroupEnabled(_activeGroupType, !_config.isGroupEnabled(_activeGroupType));
            _config.save();
            _enableButton.setMessage(getEnableButtonText());
        }));
        y += 24;
        addButton(new Button(x, y, 200, 20, "Done", b -> minecraft.displayGuiScreen(_parent)));

        addIconButtons();
    }

    private String getEnableButtonText() {
        return _groupName + " Entities: " + (_config.isGroupEnabled(_activeGroupType) ? "On" : "Off");
    }

    private void addIconButtons() {
        int colIndex = 0;
        int rowIndex = 0;
        int colX = _iconLeft;
        int buttonX = colX + _activeGroup.getIconAndTextWidth(colIndex);
        int buttonY = _iconTop - 4;
        int buttonHeight = LINE_HEIGHT - 1;

        for(RadarEntityInfo info : _activeGroup.entities) {
            if(rowIndex == MAX_ENTITIES_PER_COL) {
                colX += _activeGroup.getColWidth(colIndex);

                colIndex++;
                rowIndex = 0;

                buttonX = colX + _activeGroup.getIconAndTextWidth(colIndex);
                buttonY = _iconTop - 4;
            }

            String buttonText = info.getEnabled() ? "on": "off";
            final RadarEntityInfo infoFinal = info;

            addButton(new Button(buttonX, buttonY, BUTTON_WIDTH, buttonHeight, buttonText, b -> {
                infoFinal.setEnabled(!infoFinal.getEnabled());
                _config.save();
                b.setMessage(info.getEnabled() ? "on": "off");
            }));

            buttonY += LINE_HEIGHT;

            rowIndex++;
        }
    }

    @Override
    public void onClose() {
        _config.save();
        minecraft.displayGuiScreen(_parent);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderDirtBackground(0);
        drawCenteredString(this.font, "Radar Entities", this.width / 2, _titleTop, Color.WHITE.getRGB());
        renderIcons();
        super.render(mouseX, mouseY, partialTicks);
    }

    private void renderIcons() {
        int colIndex = 0;
        int rowIndex = 0;
        int x = _iconLeft;
        int y = _iconTop;

        for(RadarEntityInfo info : _activeGroup.entities) {
            if(rowIndex == MAX_ENTITIES_PER_COL) {
                x += _activeGroup.getColWidth(colIndex);
                y = _iconTop;

                colIndex++;
                rowIndex = 0;
            }

            renderIcon(x, y + 4, info);

            Color color = info.getEnabled() ? Color.WHITE : Color.DARK_GRAY;
            this.font.drawStringWithShadow(info.getName(), x + ICON_WIDTH, y, color.getRGB());

            y += LINE_HEIGHT;

            rowIndex++;
        }
    }

    private void renderIcon(float x, float y, RadarEntityInfo info) {
        GlStateManager.pushMatrix();
        GlStateManager.translatef(x, y, 0);
        GlStateManager.scalef(0.6f, 0.6f, 0.6f);
        GlStateManager.color4f(1f, 1f, 1f, 1f);

        minecraft.getTextureManager().bindTexture(info.getIcon());

        AbstractGui.blit(-8, -8, 0, 0, 16, 16, 16, 16);

        GlStateManager.popMatrix();
    }
}