package hordes.mod.plugins.evolvedmobs.implementation.targets;

import hordes.mod.plugins.evolvedmobs.api.EvolvedMob;
import hordes.mod.plugins.evolvedmobs.api.targets.TargetPriority;
import org.bukkit.entity.Entity;

/**
 *
 * @author Tseho
 */
public class EvolvedMobTargetEntityKill extends EvolvedMobTargetBase{
    
    protected Entity entity;
    
    public EvolvedMobTargetEntityKill(EvolvedMob evolvedMob, Entity entity) {
        super(evolvedMob);
        this.entity = entity;
    }

    public EvolvedMobTargetEntityKill(EvolvedMob evolvedMob, Entity entity, TargetPriority priority) {
        super(evolvedMob);
        this.entity = entity;
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
