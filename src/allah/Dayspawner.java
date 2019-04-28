package allah;

import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;

public class Dayspawner extends Spawner{

public Dayspawner(Location loc1, Location loc2, EntityType entity, long spawnrate, int areaentitylimit, int chunkentitylimit, Main plugin) {
		
		this.chunk = this.getChunks(loc1, loc2);
		this.entity = entity;
		this.spawnrate = spawnrate;
		this.areaentitylimit = areaentitylimit;
		this.chunkentitylimit = chunkentitylimit;
		
		this.slopelimit = 250;
		
		this.spawnMaterial = new ArrayList<Material>();
		
		spawnMaterial.add(Material.GRASS_BLOCK);
		spawnMaterial.add(Material.DIRT);
		spawnMaterial.add(Material.SAND);
		spawnMaterial.add(Material.SNOW);
		spawnMaterial.add(Material.SNOW_BLOCK);
		
		this.plugin = plugin;
	}
	

	public void activate() {
		
		if(!active) {
			this.plugin.getServer().getScheduler().runTaskTimer(this.plugin,
					
					new Runnable() {

						@Override
						public void run() {
							if(chunk.get(0).getWorld().getTime() < 13000 && chunk.get(0).getWorld().getTime() > 23000) {
								spawn();
							}
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
				
				if(spawnableground.size() > 5) {
					found = true;
				}
			}
			searchcount++;
		}
		
		if(found) {
			spawnableground.get((int) Math.floor(Math.random() * spawnableground.size()));
		
			Block spawnblock = spawnableground.get((int) Math.round(Math.random() * spawnableground.size()));
			spawnchunk.getWorld().spawnEntity(spawnblock.getLocation().add(0, 2, 0), entity);
			
			plugin.getServer().getConsoleSender().sendMessage("Nightspawner: Spawned entities at: X(" + spawnchunk.getX() +") Z(" + spawnchunk.getZ() + ")");
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
	                if(!block.isPassable() && spawnMaterial.contains(block) && chunk.getBlock(x, y+1, z).isPassable() && chunk.getBlock(x, y+2, z).isPassable() && block.getLightLevel() > 8 && !chunk.getBlock(x, y+1, z).isLiquid() && !chunk.getBlock(x, y+2, z).isLiquid()) {
	                	spawnableGround.add(block);
	                }
	            }
	        }
	    }
		
		return spawnableGround;
	}



}
