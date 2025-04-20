package pashmash.tryBounty;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pashmash.tryBounty.commands.BountyCommand;
import pashmash.tryBounty.economy.VaultHook;
import pashmash.tryBounty.listener.PlayerJoinListener;
import pashmash.tryBounty.manager.BountyManager;
import pashmash.tryBounty.util.SqlUtil;


@Getter
public final class TryBounty extends JavaPlugin {

    @Getter
    public static TryBounty instance;
    private SqlUtil sqlUtil;
    private BountyManager bountyManager;
    private VaultHook vaultHook;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;

        // Initialize SqlUtil
        sqlUtil = new SqlUtil(
                getConfig().getString("Sql.Host"),
                getConfig().getString("Sql.Port"),
                getConfig().getString("Sql.Database"),
                getConfig().getString("Sql.Username"),
                getConfig().getString("Sql.Password")
        );
        sqlUtil.update("CREATE TABLE IF NOT EXISTS Bounty (" +
                "UUID VARCHAR(36) PRIMARY KEY," +
                "amount INT" +
                ");");

        vaultHook = new VaultHook(instance);
        if (!vaultHook.setupEconomy()) {
            Bukkit.getPluginManager().disablePlugin(this);
            getLogger().severe("Vault is not available! Disabling plugin.");
            return;
        }

        // Initialize BountyManager
        bountyManager = new BountyManager();

        // Register commands and listeners
        new BountyCommand(bountyManager);
        new PlayerJoinListener();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
