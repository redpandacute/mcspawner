package allah;

import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;

public class Herdspawner extends Spawner {
	
	public static final int MAX_HERDSIZE = 5;
	
	public Herdspawner(Location loc1, Location loc2, EntityType entity, long spawnrate, int areaentitylimit, int chunkentitylimit, Main plugin) {
		
		this.chunk = this.getChunks(loc1, loc2);
		this.entity = entity;
		this.spawnrate = spawnrate;
		this.areaentitylimit = areaentitylimit;
		this.chunkentitylimit = chunkentitylimit;
	
		this.spawnMaterial = new ArrayList<Material>();
		
		spawnMaterial.add(Material.GRASS_BLOCK);
		spawnMaterial.add(Material.DIRT);
		spawnMaterial.add(Material.SAND);
		spawnMaterial.add(Material.SNOW);
		spawnMaterial.add(Material.SNOW_BLOCK);
	
		this.slopelimit = 3;
		
		this.plugin = plugin;
	}
	

	public void activate() {
		
		if(!active) {
			this.plugin.getServer().getScheduler().runTaskTimer(this.plugin,
					
					new Runnable() {

						@Override
						public void run() {
							spawn();
						}
				
			}, 20L, spawnrate * 20L);
		}
	}
	
	@Override
	void spawn() {
		
		boolean found = false;
		ArrayList<Block> spawnableground = null;
		Chunk spawnchunk = null;
		int searchcount = 0;
		
		while(!found && searchcount < 5) {
			plugin.getServer().getConsoleSender().sendMessage("Allah: CHUNKSIZE: " + chunk.size() + " " + searchcount);
			spawnchunk = chunk.get((int) Math.floor(Math.random() * chunk.size()));
			
			plugin.getServer().getConsoleSender().sendMessage("Slope: " + getChunkSlope(spawnchunk));
			
			if(spawnchunk.getEntities().length < chunkentitylimit && getChunkSlope(spawnchunk) <= slopelimit && spawnchunk.isLoaded()) {
				
				spawnableground = getSpawnableGround(spawnchunk);
				plugin.getServer().getConsoleSender().sendMessage("Sground: " + spawnableground.size());
				
				if(spawnableground.size() > 10) {
					found = true;
				}
			}
			searchcount++;
		}
		
		if(found) {
			spawnableground.get((int) Math.floor(Math.random() * spawnableground.size()));
		
			int herdsize = (int) Math.round(Math.random() * MAX_HERDSIZE);
		
			for(int i = 0; i <= herdsize; i++) {
				Block spawnblock = spawnableground.get((int) Math.round(Math.random() * spawnableground.size()));
				spawnchunk.getWorld().spawnEntity(spawnblock.getLocation().add(0, 2, 0), entity);
			}
			
			plugin.getServer().getConsoleSender().sendMessage("Herdspawner: Spawned entities at: X(" + spawnchunk.getX() +") Z(" + spawnchunk.getZ() + ")");
		}
	}

	@Override
	ArrayList<Block> getSpawnableGround(Chunk chunk) {

		ArrayList<Block> spawnableGround = new ArrayList<Block>();
		
		for (int x = 0; x < 16; x++)
	    {
	        for (int z = 0; z < 16; z++)
	        {
	            for (int y = 50; y < 90; y++)
	            {
	            	Block block = chunk.getBlock(x, y, z);
	                if(spawnMaterial.contains(block.getType()) && chunk.getBlock(x, y+1, z).isPassable() && chunk.getBlock(x, y+2, z).isPassable() && !chunk.getBlock(x, y+1, z).isLiquid() && !chunk.getBlock(x, y+2, z).isLiquid()) {
	                	spawnableGround.add(block);
	                }
	            }
	        }
	    }
		
		return spawnableGround;
	}

}
