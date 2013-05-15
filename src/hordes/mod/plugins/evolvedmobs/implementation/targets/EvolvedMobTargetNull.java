package hordes.mod.plugins.evolvedmobs.implementation.targets;

import hordes.mod.plugins.evolvedmobs.api.EvolvedMob;
import hordes.mod.plugins.evolvedmobs.api.targets.TargetPriority;

/**
 *
 * @author Tseho
 */
public class EvolvedMobTargetNull extends EvolvedMobTargetBase{
    
    public EvolvedMobTargetNull(EvolvedMob evolvedMob) {
        super(evolvedMob);
        this.priority = TargetPriority.LOWEST;
    }
    
    @Override
    public void init() {
        
    }
    
    @Override
    public void update(){
        
    }
}
