package hordes.mod.plugins.evolvedmobs.implementation.targets;

import hordes.mod.plugins.evolvedmobs.api.targets.TargetPriority;
import org.bukkit.block.Block;

/**
 *
 * @author Tseho
 */
public class EvolvedMobTargetBlockDestroy extends EvolvedMobTargetBase{
    
    protected Block block;
    
    public EvolvedMobTargetBlockDestroy(Block block) {
        this.block = block;
    }

    public EvolvedMobTargetBlockDestroy(Block block, TargetPriority priority) {
        this.block = block;
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
