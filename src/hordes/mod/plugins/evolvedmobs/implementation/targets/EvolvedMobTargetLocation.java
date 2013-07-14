package hordes.mod.plugins.evolvedmobs.implementation.targets;

import de.ntcomputer.minecraft.controllablemobs.api.actions.ActionState;
import de.ntcomputer.minecraft.controllablemobs.api.actions.ControllableMobAction;
import hordes.mod.core.game.HordesPlugin;
import hordes.mod.plugins.evolvedmobs.api.targets.TargetPriority;
import hordes.mod.plugins.evolvedmobs.api.targets.TargetState;
import hordes.mod.plugins.evolvedmobs.EvolvedMobPlugin;
import hordes.mod.plugins.evolvedmobs.api.EvolvedMob;
import hordes.mod.utils.MinecraftLocations;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 *
 * @author Tseho
 */
public class EvolvedMobTargetLocation extends EvolvedMobTargetBase{
    
    protected Location location;
    protected ControllableMobAction action;
    private boolean completed = false;
    
    public EvolvedMobTargetLocation(EvolvedMob evolvedMob, Location location) {
        super(evolvedMob);
        this.location = location;
    }
    
    public EvolvedMobTargetLocation(EvolvedMob evolvedMob, Location location, TargetPriority priority) {
        super(evolvedMob);
        this.location = location;
        this.priority = priority;
    }
    
    @Override
    public void onInit() {
        if(this.priority.isLowerThan(TargetPriority.HIGH)){
            //Action added to the queue
            this.action = this.getEvolvedMob().getControllableMob().getActions().moveTo(this.location, true);
        }else{
            //Action executed directly
            this.action = this.getEvolvedMob().getControllableMob().getActions().moveTo(this.location, false);
        }
    }
    
    @Override
    public void onUpdate(){
        if(this.action.getState() == ActionState.RUNNING && this.isCompleted()){
            this.stop();
            //HordesPlugin.logger.log(LoggerLevel.DEBUG, "Mob stop moving.");
        }
        
        //this.action.
        
        if(this.action.getState() == ActionState.FINISHED && !this.isCompleted()){
            this.state = TargetState.BLOCKED;
            this.pause();
            //Mob can't reach the target
            //HordesPlugin.logger.log(LoggerLevel.DEBUG, "Mob can't reach target.");
        }
        
        if(this.state == TargetState.BLOCKED){
            //If current location is lower than objective location 
            //and if he's allowed to place a block, he will try to place a block
            if(this.evolvedMob.isAllowedTo(PLACE_BLOCK) && this.evolvedMob.getEntity().getLocation().getBlockY() < this.location.getBlockY()){
                Location locationForPlacingBlock = this.getLocationForPlacingBlock();
                if(locationForPlacingBlock != null){
                    this.evolvedMob.addTarget(new EvolvedMobTargetBlockPlace(this.evolvedMob, locationForPlacingBlock.getBlock(), this.priority));
                }
            }
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
            //HordesPlugin.logger.log(LoggerLevel.DEBUG, "Target reached.");
            this.completed = true;
            return true;
        }else{
            //HordesPlugin.logger.log(LoggerLevel.DEBUG, "Target not reached.");
            return false;
        }
    }
    
    @Override
    public boolean isAllowed() {
        return this.evolvedMob.isAllowedTo(MOVE);
    }
    
    protected Location getLocationForPlacingBlock(){
        
        int attempts = 0;
        boolean locationFound = false;
        Location mobLocation = this.evolvedMob.getEntity().getLocation();
        Location targetBlockLocation = mobLocation;
        
        String direction;
        int directionFactor;
        
        if(Math.abs(mobLocation.getBlockX() - this.location.getBlockX()) < Math.abs(mobLocation.getBlockZ() - this.location.getBlockZ())){
            direction = "Z";
            if(mobLocation.getBlockZ() < this.location.getBlockZ()){
                directionFactor = -1;
            }else{
                directionFactor = 1;
            }
        }else{
            direction = "X";
            if(mobLocation.getBlockX() < this.location.getBlockX()){
                directionFactor = -1;
            }else{
                directionFactor = 1;
            }
        }
        
        while(attempts < 3 && locationFound == false){
            
            Block targetBlock = targetBlockLocation.getBlock();
            
            if(targetBlock.isEmpty() 
                    && targetBlock.getRelative(BlockFace.UP, 1).isEmpty() 
                    && targetBlock.getRelative(BlockFace.UP, 2).isEmpty()){
                
                //Target block is empty, we test previous block in 3 directions (for jumping on block placed)
                
                if("Z".equals(direction)){
                
                    if(targetBlock.getRelative(0, 0, directionFactor).isEmpty() 
                        && targetBlock.getRelative(0, 1, directionFactor).isEmpty() 
                        && targetBlock.getRelative(0, 2, directionFactor).isEmpty()){
                            
                            locationFound = true;
                            
                    }else if(targetBlock.getRelative(1, 0, directionFactor).isEmpty() 
                        && targetBlock.getRelative(1, 1, directionFactor).isEmpty() 
                        && targetBlock.getRelative(1, 2, directionFactor).isEmpty()){
                            
                            locationFound = true;
                            
                    }else if(targetBlock.getRelative(-1, 0, directionFactor).isEmpty() 
                        && targetBlock.getRelative(-1, 1, directionFactor).isEmpty() 
                        && targetBlock.getRelative(-1, 2, directionFactor).isEmpty()){
                            
                            locationFound = true;
                            
                    }
                    
                }else if("X".equals(direction)){
                    
                    if(targetBlock.getRelative(directionFactor, 0, 0).isEmpty() 
                        && targetBlock.getRelative(directionFactor, 1, 0).isEmpty() 
                        && targetBlock.getRelative(directionFactor, 2, 0).isEmpty()){
                            
                            locationFound = true;
                            
                    }else if(targetBlock.getRelative(directionFactor, 0, 1).isEmpty() 
                        && targetBlock.getRelative(directionFactor, 1, 1).isEmpty() 
                        && targetBlock.getRelative(directionFactor, 2, 1).isEmpty()){
                            
                            locationFound = true;
                            
                    }else if(targetBlock.getRelative(directionFactor, 0, -1).isEmpty() 
                        && targetBlock.getRelative(directionFactor, 1, -1).isEmpty() 
                        && targetBlock.getRelative(directionFactor, 2, -1).isEmpty()){
                            
                            locationFound = true;
                            
                    }
                }
            }
            
            //If not found, we will try on next block
            
            if(locationFound == false){
                if("Z".equals(direction)){
                    targetBlockLocation = targetBlock.getRelative(0, 0, directionFactor).getLocation();
                }else if("X".equals(direction)){
                    targetBlockLocation = targetBlock.getRelative(directionFactor, 0, 0).getLocation();
                }
            }
            
            //Attemps counter for performances security
            attempts++;
        }
        
        if(locationFound){
            return targetBlockLocation;
        }
        
        return null;
    }
}
