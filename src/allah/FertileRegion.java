package allah;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class FertileRegion {

	List<Material> fertilities;
	Chunk[] chunks = new Chunk[2];
	
	public FertileRegion(List<Material> fertilities, Chunk chunk1, Chunk chunk2) {
		this.fertilities = fertilities;
		this.chunks[0] = chunk1;
		this.chunks[1] = chunk2;
	}
	
	public boolean isInRegion(Chunk chunk) {
		if(chunks[0].getX() > chunks[1].getX()) {
			if(!(chunk.getX() > chunks[1].getX() && chunk.getX() < chunks[0].getX())) {
				return false;
			}
		} else { 
			if(!(chunk.getX() < chunks[1].getX() && chunk.getX() > chunks[0].getX())) {
				return false;
			}
		}
		
		if(chunks[0].getZ() > chunks[1].getZ()) {
			if(!(chunk.getZ() > chunks[1].getZ() && chunk.getZ() < chunks[0].getZ())) {
				return false;
			}
		} else { 
			if(!(chunk.getZ() < chunks[1].getX() && chunk.getX() > chunks[0].getZ())) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean isFertile(Block block) {
		if(fertilities.contains(block.getType())) {
			return true;
		}
		return false;
	}
}
