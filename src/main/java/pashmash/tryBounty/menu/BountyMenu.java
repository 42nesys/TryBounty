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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        BountyManager bountyManager = TryBounty.getInstance().getBountyManager();

        List<Map.Entry<UUID, @NotNull Long>> sortedBounties = Stream.concat(
                        Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId),
                        Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getUniqueId)

                ).distinct()
                .collect(Collectors.toMap(
                        uuid -> uuid,
                        bountyManager::get
                ))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .toList();

        for (Map.Entry<UUID, Long> entry : sortedBounties) {
            UUID uuid = entry.getKey();
            String formattedBounty = NumberUtil.formatSuffixed(entry.getValue());

            OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);
            ItemStack playerHead = SkullCreator.itemFromUuid(uuid);

            gui.addItem(ItemBuilder.from(playerHead)
                    .name(ColorUtil.translate(ColorUtil.BLUE + target.getName()))
                    .lore(List.of(
                            ColorUtil.translate("&7Bounty: " + ColorUtil.GREEN + formattedBounty)
                    ))
                    .asGuiItem(event -> event.setCancelled(true)));
        }
        gui.setDefaultClickAction(event -> event.setCancelled(true));
        gui.open(player);
    }
}