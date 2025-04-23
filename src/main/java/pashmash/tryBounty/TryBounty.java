package pashmash.tryBounty;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pashmash.tryBounty.commands.BountyCommand;
import pashmash.tryBounty.economy.VaultHook;
import pashmash.tryBounty.listener.PlayerDeathListener;
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

        bountyManager = new BountyManager();

        new BountyCommand(bountyManager);
        new PlayerJoinListener();
        new PlayerDeathListener();
    }

}
