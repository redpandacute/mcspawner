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
	
	String spawntoolnameunused = ChatColor.GREEN + "Select spawn area", spawntoolnameused = ChatColor.RED + "Select spawn area";
	String fertilitytoolname;
	
	Location spawn1, spawn2;
	
	List<Spawner> spawnerList;
	
	@Override
    public void onEnable() {
        
		slave = new Slave(this);
		
		spawn1 = null;
		spawn2 = null;
		
		
		saveConfig();
		loadConfig();
	}
    
    @Override
    public void onDisable() {
        // TODO Insert logic to be performed when the plugin is disabled
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    	
    	if(cmd.getName().equalsIgnoreCase("spawntool") && args.length == 0) {
    		if(sender instanceof Player) {
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
    	
    	if(cmd.getName().equalsIgnoreCase("addherdspawner") && args.length == 2) {
    		if(sender instanceof Player && spawn1 != null && spawn2 != null) {
    			try {
    			EntityType entity = EntityType.valueOf(args[0]);
    			
    			String spawnerid = UUID.randomUUID().toString();
    			
    			this.getConfig().set("spawner.herdspawner." + spawnerid + ".entity", entity.toString());
    			this.getConfig().set("spawner.herdspawner." + spawnerid + ".spawnrate", Long.parseLong(args[1]));
    			this.getConfig().set("spawner.herdspawner." + spawnerid + ".world", spawn1.getWorld().getName());
    			this.getConfig().set("spawner.herdspawner." + spawnerid + ".location1", spawn1.toVector());
    			this.getConfig().set("spawner.herdspawner." + spawnerid + ".location2", spawn2.toVector());
    			//TODO save into config
    			
    			getServer().getConsoleSender().sendMessage("Allah: Herdspawner set.");
    			saveConfig();
    			loadConfig();
    			return true;
    			
    			} catch (Exception e) {
    				e.printStackTrace();
    				getServer().getConsoleSender().sendMessage("Allah: Invalid Input!");
    			}
    		}
    	}
    	
    	if(cmd.getName().equalsIgnoreCase("activatespawns") && args.length == 0) {
    		
    		loadConfig();
    		
    		return true;
    	}

    	if(cmd.getName().equalsIgnoreCase("deactivatespawns") && args.length == 0) {
	
    		this.getServer().getScheduler().cancelTasks(this);
	
    		return true;
    	}
    		
    	return false;
    }
    
    public void loadConfig() {    	
    	this.getServer().getScheduler().cancelTasks(this);
    	
    	spawnerList = null;
    	spawnerList = new ArrayList<Spawner>();
    	
    	if(this.getConfig().isSet("spawner.herdspawner")) {
    		
    		ConfigurationSection herdspawnerSection = this.getConfig().getConfigurationSection("spawner.herdspawner");
    	
    		for(String key : herdspawnerSection.getKeys(false)) {
    		
    			EntityType entity = EntityType.valueOf(herdspawnerSection.getString(key + ".entity"));
    			long spawnrate = herdspawnerSection.getLong(key + ".spawnrate");
    			String world = herdspawnerSection.getString(key + ".world");
    			Vector v1 = herdspawnerSection.getVector(key + ".location1");
    			Vector v2 = herdspawnerSection.getVector(key + ".location2");
    			Location loc1 = v1.toLocation(this.getServer().getWorld(world));
    			Location loc2 = v2.toLocation(this.getServer().getWorld(world));
    		
    			Herdspawner spawner = new Herdspawner(loc1, loc2, entity, spawnrate, 100, 10, this);
    			spawner.activate();
    			spawnerList.add(spawner);
    			//spawnerList.add(new Herdspawner(loc1, loc2, entity, spawnrate, 100, 10, this));
    		}
    	}
    }
}

