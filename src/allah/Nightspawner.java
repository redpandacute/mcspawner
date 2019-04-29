package allah;

import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;

public class Nightspawner extends Spawner {
	
	boolean killed = false;
	double killratio;

	public Nightspawner(Location loc1, Location loc2, EntityType entity, long spawnrate, int areaentitylimit,
			int chunkentitylimit, double killratio, Main plugin) {

		this.chunk = this.getChunks(loc1, loc2);
		this.entity = entity;
		this.spawnrate = spawnrate;
		this.areaentitylimit = areaentitylimit;
		this.chunkentitylimit = chunkentitylimit;
		this.killratio = killratio;

		this.plugin = plugin;
	}

	public void activate() {

		if (!active) {
			this.plugin.getServer().getScheduler().runTaskTimer(this.plugin,

					new Runnable() {
				

						@Override
						public void run() {
							if (chunk.get(0).getWorld().getTime() > 13000
									&& chunk.get(0).getWorld().getTime() < 23000) {
								spawn();
								killed = false;
							} else if(!killed) {
								new Killer().killHostiles(chunk, killratio);
								killed = true;
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

		while (!found && searchcount < 5 && countEntity() < areaentitylimit) {
			spawnchunk = chunk.get((int) Math.round(Math.random() * (chunk.size() - 1)));
			if (spawnchunk.getEntities().length < chunkentitylimit && spawnchunk.isLoaded()) {

				spawnableground = getSpawnableGround(spawnchunk);

				if (spawnableground == null) {
					searchcount--;
				} else if (spawnableground.size() > 5) {
					found = true;
				}
			}
			searchcount++;
		}

		if (found) {
			spawnableground.get((int) Math.round(Math.random() * (spawnableground.size() - 1)));

			Block spawnblock = spawnableground.get((int) Math.round(Math.random() * spawnableground.size()));
			spawnchunk.getWorld().spawnEntity(spawnblock.getLocation().add(0, 2, 0), entity);
		}
	}

	@Override
	ArrayList<Block> getSpawnableGround(Chunk chunk) {

		ArrayList<Block> spawnableGround = new ArrayList<Block>();
		int liqcount = 0;

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 50; y < 90; y++) {
					Block block = chunk.getBlock(x, y, z);
					if (!block.isPassable() && chunk.getBlock(x, y + 1, z).isPassable()
							&& chunk.getBlock(x, y + 2, z).isPassable()
							&& chunk.getBlock(x, y + 1, z).getLightLevel() < 8
							&& !chunk.getBlock(x, y + 1, z).isLiquid() && !chunk.getBlock(x, y + 2, z).isLiquid()) {
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
