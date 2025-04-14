package pashmash.tryBounty.menu;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import pashmash.tryBounty.util.ColorUtil;

public class BountyMenu {
    public void openBountyMenu (Player player) {
        PaginatedGui gui = Gui.paginated()
                .title(ColorUtil.translate(ColorUtil.PREFIX + "Bounty Menu" ))
                .rows(5)
                .create();

        gui.setItem(5, 1 , ItemBuilder.from(Material.ARROW).name(ColorUtil.translate(ColorUtil.BLUE + "Back")).asGuiItem());
        gui.setItem(5, 9 , ItemBuilder.from(Material.ARROW).name(ColorUtil.translate(ColorUtil.BLUE + "Next")).asGuiItem());










        for (int slot = 2; slot <= 8; slot++) {
            gui.setItem(5, slot, ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE)
                    .name(ColorUtil.translate(ColorUtil.RED + " ")).asGuiItem());
        }







        gui.setDefaultClickAction(event -> event.setCancelled(true));

    }
}
