package vk.com.merofunk.filter;

import java.util.Map;
import java.io.IOException;
import java.io.File;
import java.util.UUID;
import org.bukkit.configuration.file.YamlConfiguration;

public class Data
{
    private static final String path = "plugins/Filter/Data/PlayerData.yml";
    
    public static void save() {
        final YamlConfiguration playerData = new YamlConfiguration();
        for (final UUID uuid : Main.ChatLock.keySet()) {
            playerData.set(uuid.toString(), (Object) Main.ChatLock.get(uuid));
        }
        try {
            playerData.save("plugins/Filter/Data/PlayerData.yml");
        }
        catch (IOException e) {
            final File file = new File("plugins/Filter/Data/PlayerData.yml");
            try {
                file.createNewFile();
                playerData.save("plugins/Filter/Data/PlayerData.yml");
            }
            catch (IOException ex) {}
        }
    }
    
    public static void load() {
        final YamlConfiguration yamlConfiguration1 = YamlConfiguration.loadConfiguration(new File("plugins/Filter/Data/PlayerData.yml"));
        final Map<String, Object> configData1 = (Map<String, Object>)yamlConfiguration1.getValues(false);
        for (final String value : configData1.keySet()) {
            Main.ChatLock.put(UUID.fromString(value), (Long) configData1.get(value));
        }
    }
}
