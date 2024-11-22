package kindly.klan.kindlytext;

import kindly.klan.kindlytext.managers.TextDisplayManager;
import kindly.klan.kindlytext.managers.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.io.IOException;
import java.util.List;

@Mod.EventBusSubscriber
public class CountdownDisplay extends TextDisplayManager.TextDisplay {
    private final long endTime;
    private final String countdownText;

    public CountdownDisplay(int x, int y, String text, int minutes, int seconds) {
        super(x, y, text, (minutes * 60 + seconds) * 20);
        this.endTime = System.currentTimeMillis() + (minutes * 60 + seconds) * 1000;
        this.countdownText = text;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(Minecraft mc, GuiGraphics guiGraphics, Font customFont) {
        long remainingTime = (endTime - System.currentTimeMillis()) / 1000;
        long minutes = remainingTime / 60;
        long seconds = remainingTime % 60;
        String displayText = countdownText + " " + minutes + "m " + seconds + "s";
        if (remainingTime > 0) {
            this.ticksRemaining = 1; // Keep text visible while counting down
        } else {
            this.ticksRemaining = 0; // Hide text when countdown is over
            executeCommands();
        }
        super.text = displayText; // Actualiza el texto para ser visible
        super.textLength = displayText.length(); // Actualiza la longitud del texto, para el contador
        super.render(mc, guiGraphics, customFont);
    }

    private void executeCommands() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            try {
                List<String> commands = ConfigManager.getCommands();
                CommandSourceStack source = server.createCommandSourceStack().withSuppressedOutput();
                for (String command : commands) {
                    try {
                        server.getCommands().getDispatcher().execute(command, source);
                    } catch (CommandSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}