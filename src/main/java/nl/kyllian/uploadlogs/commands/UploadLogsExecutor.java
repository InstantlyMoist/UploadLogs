package nl.kyllian.uploadlogs.commands;

import nl.kyllian.models.Paste;
import nl.kyllian.uploadlogs.UploadLogsPlugin;
import nl.kyllian.uploadlogs.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.nio.file.Paths;

public class UploadLogsExecutor implements CommandExecutor {

    private final UploadLogsPlugin plugin;

    public UploadLogsExecutor(UploadLogsPlugin plugin) {
        this.plugin = plugin;

        plugin.getCommand("uploadlogs").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!sender.hasPermission("uploadlogs.upload")) {
            sender.sendMessage(StringUtils.colorTranslate(plugin.getConfig().getString("messages.no_permission")));
            return true;
        }

        plugin.notifyUploaded();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String url = plugin.getConfig().getString("url");
            String path = plugin.getServer().getWorldContainer().getAbsolutePath() + plugin.getConfig().getString("path");

            // Check if path exists
            if (!Paths.get(path).toFile().exists()) {
                sender.sendMessage(StringUtils.colorTranslate(plugin.getConfig().getString("messages.file_not_found")));
                return;
            }

            Paste paste = new Paste(url)
                    .setMessageFromFile(path)
                    .removeIps()
                    .encrypt();

            sender.sendMessage(StringUtils.colorTranslate(plugin.getConfig().getString("messages.wait")));

            try {
                String finalUrl = paste.send();
                sender.sendMessage(StringUtils.colorTranslate(plugin.getConfig().getString("messages.success")
                        .replace("{url}", finalUrl)));
            } catch (Exception e) {
                sender.sendMessage(StringUtils.colorTranslate(plugin.getConfig().getString("messages.error")));

            }
        });
        return true;
    }
}
