package com.aleksey.combatradar;

import com.aleksey.combatradar.config.RadarConfig;
import com.aleksey.combatradar.gui.GuiLocationAndColorScreen;
import com.aleksey.combatradar.gui.GuiMainScreen;
import com.aleksey.combatradar.gui.GuiPlayerSettingsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.util.List;

/**
 * @author Aleksey Terzi
 */
@Mod(CombatRadarForgeMod.MODID)
public class CombatRadarForgeMod {
    public static final String MODID = "combatradar";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    private RadarConfig _config;
    private Radar _radar;

    public CombatRadarForgeMod() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }
    
    @SubscribeEvent
    public void init(FMLClientSetupEvent event) {
        File configDir = new File(event.getMinecraftSupplier().get().gameDir, "/combatradar/");
        if(!configDir.isDirectory()) {
            configDir.mkdir();
        }

        File configFile = new File(configDir, "config.json");
        KeyBinding settingsKey = new KeyBinding("Combat Radar Settings", GLFW.GLFW_KEY_R, "Combat Radar");

        _config = new RadarConfig(configFile, settingsKey);

        if(!configFile.isFile()) {
            try {
                configFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            _config.save();
        } else {
            if(!_config.load())
                _config.save();
        }

        _config.setIsJourneyMapEnabled(isJourneyMapEnabled());
        _config.setIsVoxelMapEnabled(isVoxelMapEnabled());

        _radar = new Radar(_config);

        ClientRegistry.registerKeyBinding(settingsKey);

        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("mod enabled");
    }

    private static boolean isJourneyMapEnabled() {
        try {
            Class.forName("journeymap.common.Journeymap");
        } catch (ClassNotFoundException ex) {
            LOGGER.info("JourneyMap is NOT found");
            return false;
        }

        LOGGER.info("JourneyMap is found");

        return true;
    }

    private static boolean isVoxelMapEnabled() {
        try {
            Class.forName("com.mamiyaotaru.voxelmap.forgemod.ForgeModVoxelMap");
        } catch (ClassNotFoundException ex) {
            LOGGER.info("VoxelMap is NOT found");
            return false;
        }

        LOGGER.info("VoxelMap is found");

        return true;
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        //render over everything
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();

        if (!Minecraft.isGuiEnabled()
                || minecraft.currentScreen != null
                    && !(minecraft.currentScreen instanceof ChatScreen)
                    && !(minecraft.currentScreen instanceof GuiMainScreen)
                    && !(minecraft.currentScreen instanceof GuiLocationAndColorScreen)
                    && !(minecraft.currentScreen instanceof GuiPlayerSettingsScreen)
                )
        {
            return;
        }

        _radar.render(minecraft);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.world == null) {
            return;
        }

        _radar.calcSettings(minecraft);
        _radar.scanEntities(minecraft);
        _radar.playSounds(minecraft);
        _radar.sendMessages(minecraft);

        if (!Minecraft.isGuiEnabled()
                || minecraft.currentScreen != null
                && !(minecraft.currentScreen instanceof ChatScreen)
                && !(minecraft.currentScreen instanceof GuiMainScreen)
                && !(minecraft.currentScreen instanceof GuiLocationAndColorScreen)
                && !(minecraft.currentScreen instanceof GuiPlayerSettingsScreen)
        )
        {
            return;
        }

        long handle = minecraft.mainWindow.getHandle();

        if (minecraft.currentScreen == null && _config.getSettingsKey().isPressed()) {
            if(InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_CONTROL) || InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_CONTROL)) {
                if(InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_ALT) || InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_ALT)) {
                    _config.setEnabled(!_config.getEnabled());
                    _config.save();
                } else {
                    _config.revertNeutralAggressive();
                    _config.save();
                }
            } else {
                minecraft.displayGuiScreen(new GuiMainScreen(minecraft.currentScreen, _config));
            }
        }
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if(!_config.getLogPlayerStatus()) {
            return;
        }

        if(event.getMessage() != null) {
            String message = event.getMessage().getString();

            List<ITextComponent> siblings = event.getMessage().getSiblings();
            TextFormatting color = siblings != null && siblings.size() > 1 ? siblings.get(1).getStyle().getColor() : null;

            if (color == TextFormatting.YELLOW) {
                if (message.contains(" joined the game") || message.contains(" left the game")) {
                    event.setCanceled(true);
                }
            }
        }
    }
}