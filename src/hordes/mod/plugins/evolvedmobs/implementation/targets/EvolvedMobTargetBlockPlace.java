package hordes.mod.plugins.evolvedmobs.implementation.targets;

import hordes.mod.plugins.evolvedmobs.api.targets.TargetPriority;
import org.bukkit.block.Block;

/**
 *
 * @author Tseho
 */
public class EvolvedMobTargetBlockPlace extends EvolvedMobTargetBase{
    
    protected Block block;
    protected Block position;
    
    public EvolvedMobTargetBlockPlace(Block block, Block position) {
        this.block = block;
        this.position = position;
    }

    public EvolvedMobTargetBlockPlace(Block block, Block position, TargetPriority priority) {
        this.block = block;
        this.position = position;
        this.priority = priority;
    }
    
    /*
    @Override
    public void init() {
        
    }
    
    @Override
    public void update(){
        
    }
    */
}
