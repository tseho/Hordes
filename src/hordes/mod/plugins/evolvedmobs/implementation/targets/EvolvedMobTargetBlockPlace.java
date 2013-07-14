package hordes.mod.plugins.evolvedmobs.implementation.targets;

import hordes.mod.plugins.evolvedmobs.api.EvolvedMob;
import hordes.mod.plugins.evolvedmobs.api.targets.TargetPriority;
import org.bukkit.block.Block;

/**
 *
 * @author Tseho
 */
public class EvolvedMobTargetBlockPlace extends EvolvedMobTargetBase{
    
    protected Block blockToPick;
    protected Block targetBlock;
    protected boolean isPicked = false;
    protected boolean aborted = false;
    
    public EvolvedMobTargetBlockPlace(EvolvedMob evolvedMob, Block targetBlock) {
        super(evolvedMob);
        this.targetBlock = targetBlock;
    }
    
    public EvolvedMobTargetBlockPlace(EvolvedMob evolvedMob, Block targetBlock, TargetPriority priority) {
        super(evolvedMob);
        this.targetBlock = targetBlock;
        this.priority = priority;
    }
    
    public EvolvedMobTargetBlockPlace(EvolvedMob evolvedMob, Block blockToPick, Block targetBlock) {
        super(evolvedMob);
        this.blockToPick = blockToPick;
        this.targetBlock = targetBlock;
    }

    public EvolvedMobTargetBlockPlace(EvolvedMob evolvedMob, Block blockToPick, Block targetBlock, TargetPriority priority) {
        super(evolvedMob);
        this.blockToPick = blockToPick;
        this.targetBlock = targetBlock;
        this.priority = priority;
    }
    
    @Override
    public void onInit() {
        //Useless, we can't control how they manage pickup items
        //this.getEvolvedMob().getEntity().setCanPickupItems(true);
        
        if(this.blockToPick == null){
            this.blockToPick = this.getRandomBlockToPick();
        }else if(this.blockToPick.isEmpty()){
            this.aborted = true;
        }
    }

    @Override
    public void onUpdate() {
        
    }
    
    @Override
    public boolean isCompleted() {
        if(this.aborted){
            return true;
        }
        return false;
    }

    @Override
    public boolean isAllowed() {
        return this.evolvedMob.isAllowedTo(PLACE_BLOCK);
    }
    
    protected Block getRandomBlockToPick(){
        
        return null;
    }
}
