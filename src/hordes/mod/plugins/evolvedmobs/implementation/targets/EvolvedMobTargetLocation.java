package hordes.mod.plugins.evolvedmobs.implementation.targets;

import de.ntcomputer.minecraft.controllablemobs.api.actions.ActionState;
import de.ntcomputer.minecraft.controllablemobs.api.actions.ActionType;
import de.ntcomputer.minecraft.controllablemobs.api.actions.ControllableMobAction;
import hordes.mod.core.game.HordesPlugin;
import hordes.mod.plugins.evolvedmobs.api.targets.TargetPriority;
import hordes.mod.plugins.evolvedmobs.api.targets.TargetState;
import hordes.mod.plugins.evolvedmobs.implementation.EvolvedMobPlugin;
import hordes.mod.utils.MinecraftLocations;
import java.util.logging.Level;
import org.bukkit.Location;

/**
 *
 * @author Tseho
 */
public class EvolvedMobTargetLocation extends EvolvedMobTargetBase{
    
    protected Location location;
    protected ControllableMobAction action;
    private boolean completed = false;
    
    public EvolvedMobTargetLocation(Location location) {
        this.location = location;
    }

    public EvolvedMobTargetLocation(Location location, TargetPriority priority) {
        this.location = location;
        this.priority = priority;
    }
    
    @Override
    public void init() {
        if(this.priority.isLowerThan(TargetPriority.HIGH)){
            //Action added to the queue
            this.action = this.getEvolvedMob().getControllableMob().getActions().moveTo(this.location, true);
        }else{
            //Action executed directly
            this.action = this.getEvolvedMob().getControllableMob().getActions().moveTo(this.location, false);
        }
    }
    
    @Override
    public void update(){
        if(this.action.getState() == ActionState.RUNNING && this.isCompleted()){
            this.stop();
            HordesPlugin.logger.log(Level.INFO, "Mob stop moving.");
        }
        
        if(this.action.getState() == ActionState.FINISHED && !this.isCompleted()){
            this.state = TargetState.BLOCKED;
            this.pause();
            HordesPlugin.logger.log(Level.INFO, "Mob can't reach target.");
        }
        /*
        if(this.getEvolvedMob().getControllableMob().getActions().isActionRunning(ActionType.MOVE)){
            HordesPlugin.logger.log(Level.INFO, "Mob is moving to target.");
        }else{
            HordesPlugin.logger.log(Level.INFO, "Mob stop moving.");
        }
        HordesPlugin.logger.log(Level.INFO, "Action state : {0}", this.action.getState().toString());
        */
    }
    
    @Override
    public boolean isCompleted(){
        if(this.completed){
            return true;
        }
        if(MinecraftLocations.isReached(this.location, this.getEvolvedMob().getEntity().getLocation(), EvolvedMobPlugin.locationReached)){
            HordesPlugin.logger.log(Level.INFO, "Target reached.");
            this.completed = true;
            return true;
        }else{
            HordesPlugin.logger.log(Level.INFO, "Target not reached.");
            return false;
        }
    }
}
