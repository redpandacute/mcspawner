package allah;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

public class Killer {

	public void killHostiles(List<Chunk> chunk,double ratio) {
		for (int l = 0; l < chunk.size(); l++) {
			Entity[] entities = chunk.get(l).getEntities();
			for (Entity entity : entities) {
				if (entity instanceof Monster) {
					boolean nearPlayer = false;
					for(Entity nearby : entity.getNearbyEntities(20, 20, 20)) {
						if(nearby instanceof Player) {
							nearPlayer = true;
						}
					}
						
					if(!nearPlayer && Math.random() < ratio) {
							entity.remove();
					}
				}
			}
		}
	}
}
