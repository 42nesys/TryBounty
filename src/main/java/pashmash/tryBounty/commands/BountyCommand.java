package pashmash.tryBounty.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pashmash.tryBounty.TryBounty;
import pashmash.tryBounty.economy.VaultHook;
import pashmash.tryBounty.manager.BountyManager;
import pashmash.tryBounty.menu.BountyMenu;
import pashmash.tryBounty.util.ColorUtil;
import pashmash.tryBounty.util.NumberUtil;

import java.util.Objects;
import java.util.UUID;

public class BountyCommand implements CommandExecutor {

    private final BountyManager bountyManager;
    private final BountyMenu bountyMenu = new BountyMenu();

    public BountyCommand(BountyManager bountyManager) {
        Objects.requireNonNull(TryBounty.getInstance().getCommand("bounty")).setExecutor(this);
        this.bountyManager = bountyManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        if (args.length < 3 || !args[0].equalsIgnoreCase("add")) {
            bountyMenu.openBountyMenu(player);
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        if (!target.hasPlayedBefore()) {
            player.sendMessage(ColorUtil.translate(ColorUtil.PREFIX + ColorUtil.RED + "Player not found!"));
            return true;
        }

        double amount;
        try {
            amount = NumberUtil.parseSuffixed(args[2]);
        } catch (NumberFormatException e) {
            player.sendMessage(ColorUtil.translate(ColorUtil.PREFIX + ColorUtil.RED + "Invalid amount format!"));
            return true;
        }

        if (!VaultHook.takeMoney(player, amount)) {
            sender.sendMessage(ColorUtil.translate(ColorUtil.PREFIX + TryBounty.getInstance().getConfig().getString("Messages.NotEnoughMoney")));
            return true;
        }

        UUID targetUUID = target.getUniqueId();
        long currentBounty = bountyManager.get(targetUUID);
        long newBounty = currentBounty + (long) amount;
        bountyManager.set(targetUUID, newBounty);

        sender.sendMessage(ColorUtil.translate(ColorUtil.PREFIX
                + Objects.requireNonNull(TryBounty.getInstance().getConfig().getString("Messages.BountyAdded"))
                .replace("%amount%", String.valueOf(amount)).replace("%player%", Objects.requireNonNull(target.getName()))));
        return true;
    }
}