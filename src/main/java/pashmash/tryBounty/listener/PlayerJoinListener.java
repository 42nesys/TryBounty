package pashmash.tryBounty.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pashmash.tryBounty.TryBounty;

public class PlayerJoinListener implements Listener {
    public PlayerJoinListener() {
        Bukkit.getPluginManager().registerEvents(this, TryBounty.getInstance());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskAsynchronously(TryBounty.getInstance(), () -> {
            String uuid = player.getUniqueId().toString();
            try {
                String existingUUID = TryBounty.getInstance().getSqlUtil().get("Bounty", "UUID", "UUID=?", uuid);

                if (existingUUID == null || !existingUUID.equalsIgnoreCase(uuid)) {
                    TryBounty.getInstance().getSqlUtil().update("INSERT INTO Bounty (UUID, amount) VALUES (?, ?)", uuid, 0);
                }

                TryBounty.getInstance().getBountyManager().loadLocal(player.getUniqueId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}