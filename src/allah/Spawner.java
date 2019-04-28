package allah;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;

public abstract class Spawner {
	
	long spawnrate;
	Integer chunkentitylimit, areaentitylimit, slopelimit;  
	EntityType entity;
	List<Material> spawnMaterial;
	List<Chunk> chunk;
	
	boolean active = false;
	
	Main plugin;
	
	Integer friendlyEntities() {
		
		int count = 0;
		
		for(int i = 0; i < chunk.size(); i++) {
			Entity[] entities = chunk.get(i).getEntities();
			for(Entity entity : entities) {
				if(entity instanceof Animals) {
					count++;
				}
			}
		}
		
		return count;
	}
	
	Integer hostileEntities() {
		
		int count = 0;
		
		for(int i = 0; i < chunk.size(); i++) {
			Entity[] entities = chunk.get(i).getEntities();
			for(Entity entity : entities) {
				if(entity instanceof Monster) {
					count++;
				}
			}
		}
		
		return count;
	}
	
	/**void spawn() {
			Chunk spawnchunk = chunk[(int) Math.round(Math.random() * chunk.length)];
			if(spawnchunk.getEntities().length < chunkentitylimit && getChunkSlope(spawnchunk) < 6) {
				ArrayList<Block> spawnableground = getSpawnableGround(spawnchunk);
				if(spawnableground.size() > 5) {
					spawnableground.get((int) Math.round(Math.random() * spawnableground.size()));
					
					//TODO spawnentity
				}
			}
	}
	**/
	
	List<Chunk> getChunks(Location loc1, Location loc2) {
		List<Chunk> chunks = new ArrayList<Chunk>();
		
		Chunk chunk1 = loc1.getChunk();
		Chunk chunk2 = loc2.getChunk();
		
		World world = chunk1.getWorld();
		
		int lowX, lowZ;
		int highX, highZ;
		
		if(chunk1.getX() < chunk2.getX()) {
			lowX = chunk1.getX();
			highX = chunk2.getX();
		} else {
			lowX = chunk2.getX();
			highX = chunk1.getX();
		}
		
		if(chunk1.getZ() < chunk2.getZ()) {
			lowZ = chunk1.getZ();
			highZ = chunk2.getZ();
		} else {
			lowZ = chunk2.getZ();
			highZ = chunk1.getZ();
		}
		
		for(int x = lowX; x <= highX; x++) {
			for(int z = lowZ; z <= highZ; z++) {
				chunks.add(world.getChunkAt(x, z));
			}	
		}
		
		return chunks;
	}
	
	int getChunkSlope(Chunk chunk) {		
		int low = 128, high = 0;
		
		for (int x = 0; x < 16; x++)
	    {
	        for (int z = 0; z < 16; z++)
	        {
	        	
	        	boolean blockfound = false;
	            for (int y = 128; !blockfound && y > 0; y--) {
	            	Block block = chunk.getBlock(x, y, z);
	            	if(block.getType() != Material.AIR && !block.isLiquid() && !block.getType().isFlammable() && !block.isPassable()) {
	            		blockfound = true;
	            		if(block.getY() > high) {
	                		high = block.getY();
	                	} else if(block.getY() < low) {
	                		low = block.getY();
	                	}
	                }
	            }
	        }
	    }

		return high - low;
	}
	
	/**ArrayList<Block> getSpawnableGround(Chunk chunk) {
		
		ArrayList<Block> spawnableGround = new ArrayList();
		
		for (int x = chunk.getX(); x < chunk.getX() + 16; x++)
	    {
	        for (int z = chunk.getZ(); z < chunk.getZ() + 16; z++)
	        {
	            for (int y = 40; y < 130; y++)
	            {
	            	Block block = chunk.getBlock(x, y, z);
	                if(spawnground.contains(block.getType()) && chunk.getBlock(x, y, z+1).getType() == Material.AIR && chunk.getBlock(x, y, z+2).getType() == Material.AIR) {
	                	spawnableGround.add(block);
	                }
	            }
	        }
	    }
		
		return spawnableGround;
	}
	**/
	
	abstract void spawn();
	abstract ArrayList<Block> getSpawnableGround(Chunk chunk);
}
