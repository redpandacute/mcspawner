package allah;

import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;

public class Dayspawner extends Spawner {

	public Dayspawner(Location loc1, Location loc2, EntityType entity, long spawnrate, int areaentitylimit,
			int chunkentitylimit, Main plugin) {

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

		this.slopelimit = slopelimit;

		this.plugin = plugin;
	}

	public void activate() {

		if (!active) {
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

		while (!found && searchcount < 5 && countEntity() < areaentitylimit) {
			spawnchunk = chunk.get((int) Math.round(Math.random() * (chunk.size() - 1)));
			if (spawnchunk.getEntities().length < chunkentitylimit
					&& spawnchunk.isLoaded()) {
				
				spawnableground = getSpawnableGround(spawnchunk);

				if (spawnableground == null) {
					searchcount--;
				} else if (spawnableground.size() > 10) {
					found = true;
				}
			}
			searchcount++;
		}

		if (found) {
			spawnableground.get((int) Math.floor(Math.random() * spawnableground.size()));

			Block spawnblock = spawnableground.get((int) Math.round(Math.random() * (spawnableground.size() - 1)));
			spawnchunk.getWorld().spawnEntity(spawnblock.getLocation().add(0, 2, 0), entity);
		}
	}

	@Override
	ArrayList<Block> getSpawnableGround(Chunk chunk) {

		ArrayList<Block> spawnableGround = new ArrayList<Block>();
		int liqcount = 0;

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 50; y < 128; y++) {
					Block block = chunk.getBlock(x, y, z);

					if (spawnMaterial.contains(block.getType()) && chunk.getBlock(x, y + 1, z).isPassable()
							&& chunk.getBlock(x, y + 2, z).isPassable() && !chunk.getBlock(x, y + 1, z).isLiquid()
							&& !chunk.getBlock(x, y + 2, z).isLiquid()
							&& chunk.getBlock(x, y + 1, z).getLightLevel() > 7) {
						// if(spawnMaterial.contains(block.getType()) && chunk.getBlock(x, y+1,
						// z).getType().equals(Material.AIR) && chunk.getBlock(x, y+2,
						// z).getType().equals(Material.AIR) && !chunk.getBlock(x, y+1, z).isLiquid() &&
						// !chunk.getBlock(x, y+2, z).isLiquid()) {
						
						spawnableGround.add(block);
					} else if (!block.isPassable() && chunk.getBlock(x, y + 1, z).isLiquid()) {
						liqcount++;
					}
				}
			}
		}

		if (liqcount > 100) {
			return null;
		} else {
			return spawnableGround;
		}

	}
}
