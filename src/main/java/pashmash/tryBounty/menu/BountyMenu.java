package pashmash.tryBounty.menu;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pashmash.tryBounty.TryBounty;
import pashmash.tryBounty.manager.BountyManager;
import pashmash.tryBounty.util.ColorUtil;
import pashmash.tryBounty.util.NumberUtil;
import pashmash.tryBounty.util.SkullCreator;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class BountyMenu {

    public void openBountyMenu(Player player) {
        PaginatedGui gui = Gui.paginated()
                .title(ColorUtil.translate(ColorUtil.PREFIX + "Bounty Menu"))
                .rows(5)
                .create();

        gui.setItem(5, 1, ItemBuilder.from(Material.ARROW)
                .name(ColorUtil.translate(ColorUtil.BLUE + "Back"))
                .asGuiItem(event -> gui.previous()));

        gui.setItem(5, 9, ItemBuilder.from(Material.ARROW)
                .name(ColorUtil.translate(ColorUtil.BLUE + "Next"))
                .asGuiItem(event -> gui.next()));

        for (int slot = 2; slot <= 8; slot++) {
            gui.setItem(5, slot, ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE)
                    .name(ColorUtil.translate(ColorUtil.RED + " "))
                    .asGuiItem());
        }

        // Get the instance of BountyManager
        BountyManager bountyManager = TryBounty.getInstance().getBountyManager();

        // Get bounties for all online players and sort by bounty amount
        List<Map.Entry<UUID, @NotNull Long>> sortedBounties = Bukkit.getOnlinePlayers().stream()
                .map(Player::getUniqueId)
                .collect(Collectors.toMap(uuid -> uuid, bountyManager::get))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .toList();

        // Add player heads to the GUI
        for (Map.Entry<UUID, Long> entry : sortedBounties) {
            UUID uuid = entry.getKey();
            String formattedBounty = NumberUtil.formatSuffixed(entry.getValue());

            OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);
            ItemStack playerHead = SkullCreator.itemFromUuid(uuid);

            gui.addItem(ItemBuilder.from(playerHead)
                    .name(ColorUtil.translate(ColorUtil.BLUE + target.getName()))
                    .lore(List.of(
                            ColorUtil.translate("&7Bounty: " + ColorUtil.GREEN + formattedBounty) // Use formatted value
                    ))
                    .asGuiItem(event -> {
                        event.setCancelled(true);
                    }));
        }
        gui.setDefaultClickAction(event -> event.setCancelled(true));
        gui.open(player);
    }
}