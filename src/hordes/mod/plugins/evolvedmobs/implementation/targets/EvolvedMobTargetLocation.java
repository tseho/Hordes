package hordes.mod.plugins.evolvedmobs.implementation.targets;

import de.ntcomputer.minecraft.controllablemobs.api.actions.ControllableMobAction;
import hordes.mod.plugins.evolvedmobs.api.targets.TargetPriority;
import org.bukkit.Location;

/**
 *
 * @author Tseho
 */
public class EvolvedMobTargetLocation extends EvolvedMobTargetBase{
    
    protected Location location;
    protected ControllableMobAction action;
    
    public EvolvedMobTargetLocation(Location location) {
        this.location = location;
    }

    public EvolvedMobTargetLocation(Location location, TargetPriority priority) {
        this.location = location;
        this.priority = priority;
    }
    
    @Override
    public void init() {
        if(this.priority.isLowerThan(TargetPriority.MEDIUM)){
            //Action added to the queue
            this.action = this.getEvolvedMob().getControllableMob().getActions().moveTo(this.location, true);
        }else{
            //Action executed directly
            this.action = this.getEvolvedMob().getControllableMob().getActions().moveTo(this.location, false);
        }
    }
    
    @Override
    public void update(){
        
    }
}
