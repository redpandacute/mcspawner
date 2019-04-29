package allah;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Main extends JavaPlugin implements Listener {

	Slave slave;

	String spawntoolnameunused = ChatColor.GREEN + "Select spawn area",
			spawntoolnameused = ChatColor.RED + "Select spawn area";
	String fertilitytoolname;

	Location spawn1, spawn2;

	List<Spawner> spawnerList;

	int dayarealimit, nightlimit;

	@Override
	public void onEnable() {

		slave = new Slave(this);

		spawn1 = null;
		spawn2 = null;

		saveDefaultConfig();
		saveConfig();
		loadConfig();
	}

	@Override
	public void onDisable() {
		// TODO Insert logic to be performed when the plugin is disabled
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("al")) {
			if (args[0].equalsIgnoreCase("spawntool") && args.length == 1) {
				if (sender instanceof Player) {
					Player player = (Player) sender;

					ItemStack stool = new ItemStack(Material.WOODEN_SWORD);
					ItemMeta meta = stool.getItemMeta();

					meta.setDisplayName(spawntoolnameunused);
					meta.addEnchant(Enchantment.WATER_WORKER, 1, true);

					stool.setItemMeta(meta);

					player.getInventory().addItem(stool);
					return true;
				}
			}

			if (args[0].equalsIgnoreCase("addherdspawner") && args.length >= 4 && args.length < 6) {
				if (sender instanceof Player && spawn1 != null && spawn2 != null) {
					try {
						EntityType entity = EntityType.valueOf(args[1].toUpperCase());

						String spawnerid;

						if (args.length == 5) {
							spawnerid = args[4];
						} else {
							spawnerid = UUID.randomUUID().toString();
						}

						this.getConfig().set("spawner.herdspawner." + spawnerid + ".entity", entity.toString());
						this.getConfig().set("spawner.herdspawner." + spawnerid + ".spawnrate",
								Long.parseLong(args[2]));
						this.getConfig().set("spawner.herdspawner." + spawnerid + ".world",
								spawn1.getWorld().getName());
						this.getConfig().set("spawner.herdspawner." + spawnerid + ".location1", spawn1.toVector());
						this.getConfig().set("spawner.herdspawner." + spawnerid + ".location2", spawn2.toVector());
						this.getConfig().set("spawner.herdspawner." + spawnerid + ".limit", Integer.parseInt(args[3]));
						// TODO save into config

						getServer().getConsoleSender().sendMessage("Allah: Herdspawner set.");
						sender.sendMessage(ChatColor.RED + "Herdspawner set.");
						saveConfig();
						loadConfig();
						return true;

					} catch (Exception e) {
						e.printStackTrace();
						getServer().getConsoleSender().sendMessage("Allah: Invalid Input!");
					}
				}
			}

			if (args[0].equalsIgnoreCase("addnightspawner") && args.length >= 5 && args.length < 7) {
				if (sender instanceof Player && spawn1 != null && spawn2 != null) {
					try {
						EntityType entity = EntityType.valueOf(args[1].toUpperCase());

						String spawnerid;

						if (args.length == 6) {
							spawnerid = args[5];
						} else {
							spawnerid = UUID.randomUUID().toString();
						}

						this.getConfig().set("spawner.nightspawner." + spawnerid + ".entity", entity.toString());
						this.getConfig().set("spawner.nightspawner." + spawnerid + ".spawnrate",
								Long.parseLong(args[2]));
						this.getConfig().set("spawner.nightspawner." + spawnerid + ".world",
								spawn1.getWorld().getName());
						this.getConfig().set("spawner.nightspawner." + spawnerid + ".location1", spawn1.toVector());
						this.getConfig().set("spawner.nightspawner." + spawnerid + ".location2", spawn2.toVector());
						this.getConfig().set("spawner.nightspawner." + spawnerid + ".limit", Integer.parseInt(args[3]));
						this.getConfig().set("spawner.nightspawner." + spawnerid + ".killratio", Double.parseDouble(args[4]));

						// TODO save into config

						getServer().getConsoleSender().sendMessage("Allah: Nightspawner set.");
						sender.sendMessage(ChatColor.RED + "Nightspawner set.");
						saveConfig();
					} catch (Exception e) {
						e.printStackTrace();
						getServer().getConsoleSender().sendMessage("Allah: Invalid Input!");
					}
					
						loadConfig();
						return true;

				}
			}

			if (args[0].equalsIgnoreCase("adddayspawner") && args.length >= 4 && args.length < 6) {
				if (sender instanceof Player && spawn1 != null && spawn2 != null) {
					try {
						EntityType entity = EntityType.valueOf(args[1].toUpperCase());

						String spawnerid;

						if (args.length == 5) {
							spawnerid = args[4];
						} else {
							spawnerid = UUID.randomUUID().toString();
						}

						this.getConfig().set("spawner.dayspawner." + spawnerid + ".entity", entity.toString());
						this.getConfig().set("spawner.dayspawner." + spawnerid + ".spawnrate", Long.parseLong(args[2]));
						this.getConfig().set("spawner.dayspawner." + spawnerid + ".world", spawn1.getWorld().getName());
						this.getConfig().set("spawner.dayspawner." + spawnerid + ".location1", spawn1.toVector());
						this.getConfig().set("spawner.dayspawner." + spawnerid + ".location2", spawn2.toVector());
						this.getConfig().set("spawner.dayspawner." + spawnerid + ".limit", Integer.parseInt(args[3]));
						// TODO save into config

						getServer().getConsoleSender().sendMessage("Allah: Dayspawner set.");
						sender.sendMessage(ChatColor.RED + "Dayspawner set.");					
						saveConfig();
						
					} catch (Exception e) {
							e.printStackTrace();
							getServer().getConsoleSender().sendMessage("Allah: Invalid Input!");
					}
						
						loadConfig();
						return true;


				}
			}

			if (args[0].equalsIgnoreCase("reloadallah") && args.length == 1) {

				this.getServer().getScheduler().cancelTasks(this);
				this.reloadConfig();
				this.loadConfig();

				return true;
			}

			if (args[0].equalsIgnoreCase("alterherdspawner") && args.length == 4) {
				if(args[2].equalsIgnoreCase("entity")) {
					this.getConfig().set("spawner.herdspawner." + args[1] + "." + args[2], args[3].toUpperCase());
				} else {
					this.getConfig().set("spawner.herdspawner." + args[1] + "." + args[2], args[3]);
				}
				this.saveConfig();
				this.reloadConfig();
				this.loadConfig();
			}
			
			if (args[0].equalsIgnoreCase("alternightspawner") && args.length == 4) {
				
				if(args[2].equalsIgnoreCase("entity")) {
					this.getConfig().set("spawner.nightspawner." + args[1] + "." + args[2], args[3].toUpperCase());
				} else {
					this.getConfig().set("spawner.nightspawner." + args[1] + "." + args[2], args[3]);
				}
				this.saveConfig();
				this.reloadConfig();
				this.loadConfig();
			}
			
			if (args[0].equalsIgnoreCase("alterdayspawner") && args.length == 4) {
				
				if(args[2].equalsIgnoreCase("entity")) {
					this.getConfig().set("spawner.dayspawner." + args[1] + "." + args[2], args[3].toUpperCase());
				} else {
					this.getConfig().set("spawner.dayspawner." + args[1] + "." + args[2], args[3]);
				}
				this.saveConfig();
				this.reloadConfig();
				this.loadConfig();
			}
		}
		return false;
	}

	public void loadConfig() {
		this.getServer().getScheduler().cancelTasks(this);

		spawnerList = null;
		spawnerList = new ArrayList<Spawner>();

		if (this.getConfig().isSet("spawner.herdspawner")) {

			ConfigurationSection herdspawnerSection = this.getConfig().getConfigurationSection("spawner.herdspawner");

			for (String key : herdspawnerSection.getKeys(false)) {

				EntityType entity = EntityType.valueOf(herdspawnerSection.getString(key + ".entity"));
				long spawnrate = herdspawnerSection.getLong(key + ".spawnrate");
				String world = herdspawnerSection.getString(key + ".world");
				Vector v1 = herdspawnerSection.getVector(key + ".location1");
				Vector v2 = herdspawnerSection.getVector(key + ".location2");
				Location loc1 = v1.toLocation(this.getServer().getWorld(world));
				Location loc2 = v2.toLocation(this.getServer().getWorld(world));
				int limit = herdspawnerSection.getInt(key + ".limit");

				Herdspawner spawner = new Herdspawner(loc1, loc2, entity, spawnrate, limit,
						getConfig().getInt("limits.chunklimit"), getConfig().getInt("limits.slopelimit"), this);
				spawner.activate();
				spawnerList.add(spawner);
				// spawnerList.add(new Herdspawner(loc1, loc2, entity, spawnrate, 100, 10,
				// this));
			}
		}

		if (this.getConfig().isSet("spawner.nightspawner")) {
			
			ConfigurationSection herdspawnerSection = this.getConfig().getConfigurationSection("spawner.nightspawner");

			for (String key : herdspawnerSection.getKeys(false)) {

				EntityType entity = EntityType.valueOf(herdspawnerSection.getString(key + ".entity"));
				long spawnrate = herdspawnerSection.getLong(key + ".spawnrate");
				String world = herdspawnerSection.getString(key + ".world");
				Vector v1 = herdspawnerSection.getVector(key + ".location1");
				Vector v2 = herdspawnerSection.getVector(key + ".location2");
				Location loc1 = v1.toLocation(this.getServer().getWorld(world));
				Location loc2 = v2.toLocation(this.getServer().getWorld(world));
				int limit = herdspawnerSection.getInt(key + ".limit");
				double ratio = herdspawnerSection.getDouble(key + ".killratio");

				Nightspawner spawner = new Nightspawner(loc1, loc2, entity, spawnrate, limit,
						getConfig().getInt("limits.chunklimit"), ratio, this);
				spawner.activate();
				spawnerList.add(spawner);
				// spawnerList.add(new Herdspawner(loc1, loc2, entity, spawnrate, 100, 10,
				// this));
			}
		}
		if (this.getConfig().isSet("spawner.dayspawner")) {

			ConfigurationSection herdspawnerSection = this.getConfig().getConfigurationSection("spawner.dayspawner");

			for (String key : herdspawnerSection.getKeys(false)) {

				EntityType entity = EntityType.valueOf(herdspawnerSection.getString(key + ".entity"));
				long spawnrate = herdspawnerSection.getLong(key + ".spawnrate");
				String world = herdspawnerSection.getString(key + ".world");
				Vector v1 = herdspawnerSection.getVector(key + ".location1");
				Vector v2 = herdspawnerSection.getVector(key + ".location2");
				Location loc1 = v1.toLocation(this.getServer().getWorld(world));
				Location loc2 = v2.toLocation(this.getServer().getWorld(world));
				int limit = herdspawnerSection.getInt(key + ".limit");

				Dayspawner spawner = new Dayspawner(loc1, loc2, entity, spawnrate, limit,
						getConfig().getInt("limits.chunklimit"), this);
				spawner.activate();
				spawnerList.add(spawner);
				// spawnerList.add(new Herdspawner(loc1, loc2, entity, spawnrate, 100, 10,
				// this));
			}
		}
	}
}
