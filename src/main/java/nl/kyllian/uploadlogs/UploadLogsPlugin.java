package nl.kyllian.uploadlogs;

import org.bstats.bukkit.Metrics;
import nl.kyllian.uploadlogs.commands.UploadLogsExecutor;
import org.bstats.charts.SingleLineChart;
import org.bukkit.plugin.java.JavaPlugin;

public class UploadLogsPlugin extends JavaPlugin {

    private int logsUploaded = 0;

    @Override
    public void onEnable() {
        // Enable some awesome metrics
        Metrics metric = new Metrics(this, 17908);

        metric.addCustomChart(new SingleLineChart("logs_uploaded", () -> logsUploaded));

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        initCommands();
    }

    private void initCommands() {
        new UploadLogsExecutor(this);
    }

    public void notifyUploaded() {
        logsUploaded++;
    }
}
