package hordes.mod.plugins.evolvedmobs.implementation.targets;

import hordes.mod.plugins.evolvedmobs.api.targets.TargetPriority;
import org.bukkit.entity.Entity;

/**
 *
 * @author Tseho
 */
public class EvolvedMobTargetEntityKill extends EvolvedMobTargetBase{
    
    protected Entity entity;
    
    public EvolvedMobTargetEntityKill(Entity entity) {
        this.entity = entity;
    }

    public EvolvedMobTargetEntityKill(Entity entity, TargetPriority priority) {
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
