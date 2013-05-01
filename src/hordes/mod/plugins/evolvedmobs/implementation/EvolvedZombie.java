package hordes.mod.plugins.evolvedmobs.implementation;

import de.ntcomputer.minecraft.controllablemobs.api.ControllableMob;
import hordes.mod.core.game.HordesPlugin;
import java.util.logging.Level;

import org.bukkit.entity.Zombie;

/**
 *
 * @author Tseho
 */
public class EvolvedZombie extends EvolvedMobBase{
    
    public EvolvedZombie(ControllableMob<Zombie> controllableMob){
        super(controllableMob);
    }
    
    @Override
    public void update() {
        
    }
    
}
