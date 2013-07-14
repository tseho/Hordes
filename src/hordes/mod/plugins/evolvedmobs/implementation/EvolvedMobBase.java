package hordes.mod.plugins.evolvedmobs.implementation;

import de.ntcomputer.minecraft.controllablemobs.api.ControllableMob;
import static hordes.mod.core.game.HordesPlugin.TIMER_EVOLVED_MOBS_UPDATE_TARGET_SELECTION;
import static hordes.mod.core.game.HordesPlugin.TIMER_EVOLVED_MOBS_UPDATE_TARGET;
import hordes.mod.plugins.evolvedmobs.EvolvedMobPlugin;
import hordes.mod.plugins.evolvedmobs.api.EvolvedMob;
import hordes.mod.plugins.evolvedmobs.api.targets.EvolvedMobTarget;
import hordes.mod.plugins.evolvedmobs.implementation.targets.EvolvedMobTargetNull;
import hordes.mod.plugins.timers.TimerEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;

/**
 *
 * @author Tseho
 */
public class EvolvedMobBase implements EvolvedMob{
    
    protected ControllableMob<LivingEntity> controllableMob;
    protected EvolvedMobTarget currentTarget;
    protected ArrayList<EvolvedMobTarget> targets;
    protected ArrayList<String> permissions = new ArrayList<String>();
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public EvolvedMobBase(ControllableMob controllableMob){
        this.controllableMob = controllableMob;
        this.targets = new ArrayList<EvolvedMobTarget>();
        this.currentTarget = new EvolvedMobTargetNull(this);
        
        //TODO resolve it
//        ArrayList<String> configPermissions = (ArrayList<String>) EvolvedMobPlugin.mobsPermissions.get(this.controllableMob.getEntity().getType().getName());
//        if(configPermissions != null){
//            this.permissions.addAll(configPermissions);
//        }
    }

    @Override
    public LivingEntity getEntity() {
        return this.controllableMob.getEntity();
    }
    
    @Override
    public ControllableMob<LivingEntity> getControllableMob() {
        return this.controllableMob;
    }

    @Override
    public EvolvedMobTarget getCurrentTarget() {
        return this.currentTarget;
    }
    
    @Override
    public void setCurrentTarget(EvolvedMobTarget target) {
        //Pause current target
        if(this.currentTarget != null){
            this.currentTarget.pause();
        }
        
        //If new target is null
        if(target == null){
            this.currentTarget = null;
            return;
        }
        
        //Initialize new target
        if(!target.isInitialized()){
            target.onInit();
        }
        if(!target.isLaunched()){
            target.launch();
        }
        if(target.isPaused()){
            target.resume();
        }
        
        this.currentTarget = target;
    }

    @Override
    public EvolvedMobTarget updateCurrentTargetSelection() {
        
        EvolvedMobTarget previousTarget = this.getCurrentTarget();
        EvolvedMobTarget newTarget = null;
        
        for (Iterator<EvolvedMobTarget> it = targets.iterator(); it.hasNext();) {
            EvolvedMobTarget target = it.next();
            
            //If target is stopped or completed, we remove it
            if(target.isStopped()){
                it.remove();
                continue;
            }
            
            //If target has highter priority than current, we launch it
            if(this.getCurrentTarget() == null || target.getPriority().isHighterThan(this.getCurrentTarget().getPriority())){
                //If previousTarget is paused, we force switch to another
                if(!previousTarget.isPaused() || (previousTarget.isPaused() && !previousTarget.equals(target))){
                    newTarget = target;
                }
            }
        }
        
        this.setCurrentTarget(newTarget);
        
        return this.getCurrentTarget();
    }

    @Override
    public boolean addTarget(EvolvedMobTarget target) {
        return this.targets.add(target);
    }
    
    @Override
    public boolean addTargets(Collection<EvolvedMobTarget> targets) {
        return this.targets.addAll(targets);
    }
    
    @Override
    public void clearTargets() {
        this.targets.clear();
    }
    
    @Override
    public boolean isAllowedTo(String permission){
        return this.permissions.contains(permission);
    }

    @Override
    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public void addPermission(String permission) {
        this.permissions.add(permission);
    }

    @Override
    public void removePermission(String permission) {
        this.permissions.remove(permission);
    }
    
    @Override
    public void onUpdate() {
        throw new UnsupportedOperationException("The update() method should be overrided by subclasses.");
    }
    
    @EventHandler
    public void onTimer(TimerEvent event){
        
        if(event.getTimer().getName().equals(TIMER_EVOLVED_MOBS_UPDATE_TARGET_SELECTION)){
            this.updateCurrentTargetSelection();
        }
        
        if(event.getTimer().getName().equals(TIMER_EVOLVED_MOBS_UPDATE_TARGET)){
            if(this.getCurrentTarget() != null && !this.getCurrentTarget().isStopped() && this.getCurrentTarget().isPaused()){
                //If current target is paused, we switch to another
                this.updateCurrentTargetSelection();
            }else if(this.getCurrentTarget() != null && !this.getCurrentTarget().isStopped() && !this.getCurrentTarget().isPaused()){
                //If current target is not stopped and not paused, we fire update event on it
                this.getCurrentTarget().onUpdate();
            }
        }
    }
    
}
