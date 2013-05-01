package hordes.mod.plugins.evolvedmobs.implementation.targets;

import hordes.mod.plugins.evolvedmobs.api.targets.TargetPriority;

/**
 *
 * @author Tseho
 */
public class EvolvedMobTargetNull extends EvolvedMobTargetBase{
    
    public EvolvedMobTargetNull() {
        this.priority = TargetPriority.LOWEST;
    }
    
    @Override
    public void init() {
        
    }
    
    @Override
    public void update(){
        
    }
}
