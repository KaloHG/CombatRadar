package com.aleksey.combatradar.gui;

import com.aleksey.combatradar.config.PlayerType;
import com.aleksey.combatradar.config.RadarConfig;
import net.minecraft.client.gui.SlotGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;
import java.util.List;

/**
 * @author Aleksey Terzi
 */
public class GuiManagePlayerScreen extends Screen {
    private static final int SLOT_HEIGHT = 16;

    private class PlayerListContainer extends SlotGui {
        public PlayerListContainer() {
            super(GuiManagePlayerScreen.this.minecraft, GuiManagePlayerScreen.this.width, GuiManagePlayerScreen.this.height, 32, GuiManagePlayerScreen.this.height - 73, SLOT_HEIGHT);
        }

        @Override
        protected int getItemCount() {
            return GuiManagePlayerScreen.this._players.size();
        }

        @Override
        protected boolean selectItem(int slotIndex, int type, double mouseX, double mouseY) {
            GuiManagePlayerScreen.this._playerIndex = slotIndex;

            boolean isValidSlot = slotIndex >= 0 && slotIndex < getItemCount();

            GuiManagePlayerScreen.this._deleteButton.active = isValidSlot;

            return super.selectItem(slotIndex, type, mouseX, mouseY);
        }

        @Override
        protected boolean isSelectedItem(int slotIndex) {
            return slotIndex == GuiManagePlayerScreen.this._playerIndex;
        }

        @Override
        protected int getMaxPosition() {
            return getItemCount() * SLOT_HEIGHT;
        }

        @Override
        protected void renderBackground() {
            GuiManagePlayerScreen.this.renderDirtBackground(0);
        }

        @Override
        protected void renderItem(int entryId, int par2, int par3, int par4, int par5, int par6, float par7) {
            String playerName = GuiManagePlayerScreen.this._players.get(entryId);
            GuiManagePlayerScreen.this.drawString(minecraft.fontRenderer, playerName, par2 + 1, par3 + 1, Color.WHITE.getRGB());
        }
    }

    private static PlayerType _activePlayerType = PlayerType.Ally;

    private RadarConfig _config;
    private Screen _parent;
    private Button _allyButton;
    private Button _enemyButton;
    private Button _deleteButton;
    private PlayerListContainer _playerListContainer;

    private List<String> _players;
    private int _playerIndex = -1;

    public GuiManagePlayerScreen(Screen parent, RadarConfig config) {
        super(new StringTextComponent("Manage Player"));
        _parent = parent;
        _config = config;
    }

    @Override
    public void func_231160_c_() {
        int x = this.width / 2 - 100;
        int y = this.height - 72;

        this.buttons.clear();
        this.children.clear();
        addButton(_allyButton = new Button(x, y, 100, 20, "Allies", b -> showPlayers(PlayerType.Ally)));
        addButton(_enemyButton = new Button(x + 101, y, 100, 20, "Enemies", b -> showPlayers(PlayerType.Enemy)));
        y += 24;
        addButton(new Button(x, y, 100, 20, "Add Player", b -> minecraft.displayGuiScreen(new GuiAddPlayerScreen(this, _config, _activePlayerType))));
        addButton(_deleteButton = new Button(x + 101, y, 100, 20, "Delete Player", b -> {
            _config.setPlayerType(_players.get(_playerIndex), PlayerType.Neutral);
            _config.save();
            showPlayers(_activePlayerType);
        }));
        y += 24;
        addButton(new Button(x, y, 200, 20, "Done", b -> minecraft.displayGuiScreen(_parent)));

        _playerListContainer = new PlayerListContainer();
        children.add(_playerListContainer);

        showPlayers(_activePlayerType);
    }

    private void showPlayers(PlayerType playerType) {
        _activePlayerType = playerType;

        _players = _config.getPlayers(playerType);
        _playerIndex = _players.size() > 0 ? 0 : -1;

        _allyButton.active = playerType != PlayerType.Ally;
        _enemyButton.active = playerType != PlayerType.Enemy;
        _deleteButton.active = _playerIndex >= 0;
    }

    @Override
    public void onClose() {
        minecraft.displayGuiScreen(_parent);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        renderDirtBackground(0);
        _playerListContainer.render(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.font, "Manage Players", this.width / 2, 20, Color.WHITE.getRGB());
        super.render(mouseX, mouseY, partialTicks);
    }
}