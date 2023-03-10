package nl.kyllian.uploadlogs.utils;

import org.bukkit.ChatColor;

public class StringUtils {

    public static String colorTranslate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
