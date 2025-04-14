package pashmash.tryBounty.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pashmash.tryBounty.TryBounty;
import pashmash.tryBounty.hook.VaultHook;
import pashmash.tryBounty.manager.BountyManager;
import pashmash.tryBounty.util.NumberUtil;


import java.util.Objects;
import java.util.UUID;

public class BountyCommand implements CommandExecutor {

    private final BountyManager bountyManager;

    public BountyCommand(BountyManager bountyManager) {
        Objects.requireNonNull(TryBounty.getInstance().getCommand("bounty")).setExecutor(this);
        this.bountyManager = bountyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3 || !args[0].equalsIgnoreCase("add")) {
            sender.sendMessage("Usage: /bounty add <player> <amount>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        if (!target.hasPlayedBefore()) {
            sender.sendMessage("Player not found!");
            return true;
        }

        double amount;
        try {
            amount = NumberUtil.parseSuffixed(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid amount format!");
            return true;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!VaultHook.takeMoney(player, amount)) {
                sender.sendMessage("You don't have enough money!");
                return true;
            }
        }

        UUID targetUUID = target.getUniqueId();
        int currentBounty = bountyManager.get(targetUUID); // Get the current bounty
        int newBounty = currentBounty + (int) amount; // Add the new amount to the existing bounty
        bountyManager.set(targetUUID, newBounty); // Update the bounty in the database

        sender.sendMessage("Added " + amount + " to " + target.getName() + "'s bounty! New bounty: " + newBounty);
        return true;
    }
}