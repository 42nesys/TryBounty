package pashmash.tryBounty.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import pashmash.tryBounty.TryBounty;
import pashmash.tryBounty.manager.BountyManager;
import pashmash.tryBounty.economy.VaultHook;
import pashmash.tryBounty.util.ColorUtil;

import java.util.UUID;

public class PlayerDeathListener implements Listener {

    private final BountyManager bountyManager;

    public PlayerDeathListener() {
        this.bountyManager = TryBounty.getInstance().getBountyManager();
        Bukkit.getPluginManager().registerEvents(this, TryBounty.getInstance());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID playeruuid = player.getUniqueId();

        long bountyAmount = bountyManager.get(playeruuid);
        if (bountyAmount <= 0) {
            return;
        }

        Player killer = player.getKiller();
        if (killer != null) {
            if (VaultHook.addMoney(killer, bountyAmount)) {
                killer.sendMessage(ColorUtil.translate(ColorUtil.PREFIX + ColorUtil.GREEN + " You have claimed a bounty of " +
                        ColorUtil.BLUE + bountyAmount + ColorUtil.GREEN + " by killing " +
                        ColorUtil.RED + player.getName() + "!"));
            } else {
                killer.sendMessage(ColorUtil.translate(ColorUtil.PREFIX + ColorUtil.RED + " There was an issue rewarding you with the bounty."));
            }

            Bukkit.broadcast(ColorUtil.translate(ColorUtil.PREFIX + ColorUtil.RED + player.getName() +
                    ColorUtil.GREEN + " had a bounty of " +
                    ColorUtil.BLUE + bountyAmount +
                    ColorUtil.GREEN + " claimed!"));

            bountyManager.delete(playeruuid);
        }
    }
}