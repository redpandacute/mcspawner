package allah;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class Slave implements Listener {

	private Main plugin;

	// String spawntoolnameunused = ChatColor.GREEN + "Select spawn area",
	// spawntoolnameused = ChatColor.RED + "Select spawn area";
	// String fertilitytoolname;

	public Slave(Main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);

	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (event.getItem() != null && event.getItem().getItemMeta().getDisplayName() != null) {
			if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(plugin.spawntoolnameunused)) {
				plugin.spawn1 = p.getLocation().clone();

				p.sendMessage("Location 1. set to: " + plugin.spawn1.getBlockX() + ", " + plugin.spawn1.getBlockY()
						+ ", " + plugin.spawn1.getBlockZ());

				ItemStack item = event.getItem();
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(plugin.spawntoolnameused);
				item.setItemMeta(meta);

			} else if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(plugin.spawntoolnameused)) {
				plugin.spawn2 = p.getLocation().clone();

				p.sendMessage("Location 2. set to: " + plugin.spawn2.getBlockX() + ", " + plugin.spawn2.getBlockY()
						+ ", " + plugin.spawn2.getBlockZ());

				ItemStack item = event.getItem();
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(plugin.spawntoolnameunused);
				item.setItemMeta(meta);
			}
		}
	}
}
