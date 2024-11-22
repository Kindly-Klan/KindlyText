// src/main/java/me/javivi/kindlytext/Kindlytext.java
package kindly.klan.kindlytext;

import kindly.klan.kindlytext.managers.ConfigManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

@Mod(Kindlytext.MODID)
public class Kindlytext {
    public static final String MODID = "kindlytext";
    private static final Logger LOGGER = Logger.getLogger(Kindlytext.class.getName());

    public Kindlytext() {
        setupLogger();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientOnly::registerClientEvents);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setupLogger() {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        LOGGER.addHandler(handler);
        LOGGER.setLevel(Level.ALL);
        LOGGER.setUseParentHandlers(false);
    }

    private void setupConfig() {
        try {
            List<String> commands = ConfigManager.getCommands();
            ConfigManager.executeCommands(commands);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading config file", e);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        setupConfig(); // Crea la config
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        KindlytextsCommand.register(event.getDispatcher());
    }

    private static class ClientOnly {
        public static void registerClientEvents() {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientOnly::clientSetup);
        }

        public static void clientSetup(final FMLClientSetupEvent event) {
            // Client setup code
        }
    }
}