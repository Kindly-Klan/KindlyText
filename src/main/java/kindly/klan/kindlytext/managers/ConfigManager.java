package kindly.klan.kindlytext.managers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ConfigManager {
    private static final Path CONFIG_PATH = Paths.get("config/kindlytext/commands.txt");
    private static final Path EXECUTED_COMMANDS_PATH = Paths.get("config/kindlytext/executed_commands.txt");
    private static final Logger LOGGER = Logger.getLogger(ConfigManager.class.getName());
    private static final Set<String> executedCommands = new HashSet<>();

    static {
        try {
            if (Files.exists(EXECUTED_COMMANDS_PATH)) {
                executedCommands.addAll(Files.readAllLines(EXECUTED_COMMANDS_PATH));
            }
        } catch (IOException e) {
            LOGGER.severe("Error cargando los comandos ejecutados: " + e.getMessage());
        }
    }

    public static List<String> getCommands() throws IOException {
        if (!Files.exists(CONFIG_PATH)) {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.createFile(CONFIG_PATH);
        }
        return Files.readAllLines(CONFIG_PATH);
    }

    public static void executeCommands(List<String> commands) {
        List<String> newExecutedCommands = commands.stream()
                .filter(command -> !executedCommands.contains(command))
                .collect(Collectors.toList());

        for (String command : newExecutedCommands) {
            // Faltaria agregar la lógica
            executedCommands.add(command);
        }

        try {
            Files.write(EXECUTED_COMMANDS_PATH, executedCommands);
        } catch (IOException e) {
            LOGGER.severe("Error saving executed commands: " + e.getMessage());
        }
    }
}