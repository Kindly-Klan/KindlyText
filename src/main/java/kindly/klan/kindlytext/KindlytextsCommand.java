// src/main/java/me/javivi/kindlytext/KindlytextsCommand.java
package kindly.klan.kindlytext;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import kindly.klan.kindlytext.managers.ConfigManager;
import kindly.klan.kindlytext.managers.TextDisplayManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KindlytextsCommand {
    private static final Logger LOGGER = Logger.getLogger(KindlytextsCommand.class.getName());

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        // Comando para TextDisplay
        var textDisplayCommand = Commands.literal("TextDisplay")
                .then(Commands.argument("x", IntegerArgumentType.integer())
                        .then(Commands.argument("y", IntegerArgumentType.integer())
                                .then(Commands.argument("text", StringArgumentType.string())
                                        .then(Commands.argument("ticks", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                    int x = IntegerArgumentType.getInteger(context, "x");
                                                    int y = IntegerArgumentType.getInteger(context, "y");
                                                    String text = StringArgumentType.getString(context, "text");
                                                    int ticks = IntegerArgumentType.getInteger(context, "ticks");
                                                    TextDisplayManager.addTextDisplay(x, y, text, ticks);
                                                    return 1;
                                                })))));

        // Comando para CountdownDisplay
        var countdownDisplayCommand = Commands.literal("CountdownDisplay")
                .then(Commands.argument("x", IntegerArgumentType.integer())
                        .then(Commands.argument("y", IntegerArgumentType.integer())
                                .then(Commands.argument("text", StringArgumentType.string())
                                        .then(Commands.argument("minutes", IntegerArgumentType.integer())
                                                .then(Commands.argument("seconds", IntegerArgumentType.integer())
                                                        .executes(context -> {
                                                            int x = IntegerArgumentType.getInteger(context, "x");
                                                            int y = IntegerArgumentType.getInteger(context, "y");
                                                            String text = StringArgumentType.getString(context, "text");
                                                            int minutes = IntegerArgumentType.getInteger(context, "minutes");
                                                            int seconds = IntegerArgumentType.getInteger(context, "seconds");
                                                            TextDisplayManager.addCountdownDisplay(x, y, text, minutes, seconds);
                                                            return 1;
                                                        }))))));

        // Comando para recargar config
        var reloadCommand = Commands.literal("reload")
                .executes(context -> {
                    try {
                        ConfigManager.getCommands();
                        context.getSource().sendSuccess((Supplier<Component>) Component.literal("Comandos recargados."), true);
                    } catch (IOException e) {
                        LOGGER.log(Level.SEVERE, "Error reloading commands", e);
                        context.getSource().sendFailure(Component.literal("Error al cargar los comandos."));
                    }
                    return 1;
                });

        // Registrar todos los comandos bajo kindlytexts
        dispatcher.register(Commands.literal("kindlytexts")
                .then(textDisplayCommand)
                .then(countdownDisplayCommand)
                .then(reloadCommand));
    }
}