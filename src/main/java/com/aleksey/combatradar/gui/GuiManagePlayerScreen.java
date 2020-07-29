package com.aleksey.combatradar.gui;

import com.aleksey.combatradar.config.PlayerType;
import com.aleksey.combatradar.config.RadarConfig;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.gui.widget.list.ExtendedList.AbstractListEntry;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;
import java.util.List;

/**
 * @author Aleksey Terzi
 */
public class GuiManagePlayerScreen extends Screen {
    private static final int SLOT_HEIGHT = 16;

    private class PlayerListContainerEntry extends AbstractListEntry<PlayerListContainerEntry>{
    	
    	private String playerName;
    	
    	public PlayerListContainerEntry(String playerName) {
			this.playerName = playerName;
		}

		@Override
		public void func_230432_a_(MatrixStack p_230432_1_, int entryId, int par2, int par3,
				int p_230432_5_, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_,
				float p_230432_10_) {
			String playerName = GuiManagePlayerScreen.this._players.get(entryId);
            GuiManagePlayerScreen.this.drawString(Minecraft.getInstance().fontRenderer, playerName, par2 + 1, par3 + 1, Color.WHITE.getRGB());
			
		}
    	
    }
    
    private class PlayerListContainer extends ExtendedList<PlayerListContainerEntry> {
        public PlayerListContainer() {
            super(Minecraft.getInstance(), GuiManagePlayerScreen.this.width, GuiManagePlayerScreen.this.height, 32, GuiManagePlayerScreen.this.height - 73, SLOT_HEIGHT);
        }

        @Override
        protected int func_230965_k_() { //getItemCount
            return GuiManagePlayerScreen.this._players.size();
        }

        @Override
        protected boolean func_241215_a_(PlayerListContainerEntry entry) { //setSelected
            GuiManagePlayerScreen.this._playerIndex = entry.;

            boolean isValidSlot = slotIndex >= 0 && slotIndex < getItemCount();

            GuiManagePlayerScreen.this._deleteButton.active = isValidSlot;

            return super.selectItem(slotIndex, type, mouseX, mouseY);
        }

        @Override
        protected boolean func_230957_f_(int slotIndex) { //isSelectedItem
            return slotIndex == GuiManagePlayerScreen.this._playerIndex;
        }

        @Override
        protected void func_230433_a_(MatrixStack matrix) {
            GuiManagePlayerScreen.this.func_231165_f_(0); //draw dirt background
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
    public void func_231160_c_() { //init
        int x = this.width / 2 - 100;
        int y = this.height - 72;

        this.field_230705_e_.clear();
        this.field_230710_m_.clear();
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
    public void func_231175_as__() { //onClose
        Minecraft.getInstance().displayGuiScreen(_parent);
    }

    @Override
    public void func_230430_a_(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {  //render
    	func_230446_a_(new MatrixStack()); //renderBackground()
        _playerListContainer.func_230430_a_(matrix, mouseX, mouseY, partialTicks);
        func_238471_a_(new MatrixStack(), this.field_230712_o_,"Manage Players", this.width / 2, 20, Color.WHITE.getRGB());
        super.func_230430_a_(matrix, mouseX, mouseY, partialTicks);
    }
}