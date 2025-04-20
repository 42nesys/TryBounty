package pashmash.tryBounty.economy;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class VaultHook {

    private static Economy economy;
    private final JavaPlugin plugin;

    public VaultHook(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        economy = rsp.getProvider();
        return true;
    }

    public static boolean addMoney(OfflinePlayer player, double amount) {
        return economy != null && economy.depositPlayer(player, amount).transactionSuccess();
    }

    public static boolean takeMoney(OfflinePlayer player, double amount) {
        return economy != null && economy.withdrawPlayer(player, amount).transactionSuccess();
    }

    public static double getBalance(OfflinePlayer player) {
        return economy == null ? 0 : economy.getBalance(player);
    }

    public static boolean setMoney(OfflinePlayer player, double amount) {
        double currentBalance = economy.getBalance(player);
        return amount > currentBalance ? addMoney(player, amount - currentBalance) : takeMoney(player, currentBalance - amount);
    }

    public static boolean has(@NotNull Player player, double amount) {
        return economy.has((OfflinePlayer) player, amount);
    }


    public static void clearMoney(Player player) {
        setMoney(player, 0);
    }
}
