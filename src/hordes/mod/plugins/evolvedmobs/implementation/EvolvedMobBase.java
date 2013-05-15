/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hordes.mod.plugins.evolvedmobs.implementation;

import de.ntcomputer.minecraft.controllablemobs.api.ControllableMob;
import hordes.mod.plugins.evolvedmobs.EvolvedMobPlugin;
import hordes.mod.plugins.evolvedmobs.api.EvolvedMob;
import hordes.mod.plugins.evolvedmobs.api.targets.EvolvedMobTarget;
import hordes.mod.plugins.evolvedmobs.implementation.targets.EvolvedMobTargetNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.bukkit.entity.LivingEntity;

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
        
        ArrayList<String> configPermissions = (ArrayList<String>) EvolvedMobPlugin.mobsPermissions.get(this.controllableMob.getEntity().getType().getName());
        if(configPermissions != null){
            this.permissions.addAll(configPermissions);
        }
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
        
        //Initialize new target
        if(!target.isInitialized()){
            target.init();
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
        for (Iterator<EvolvedMobTarget> it = targets.iterator(); it.hasNext();) {
            EvolvedMobTarget target = it.next();
            
            if(target.isStopped()){
                it.remove();
                continue;
            }
            
            if(this.getCurrentTarget() == null || target.getPriority().isHighterThan(this.getCurrentTarget().getPriority())){
                this.setCurrentTarget(target);
            }
        }
        return this.getCurrentTarget();
    }

    @Override
    public boolean addTarget(EvolvedMobTarget target) {
        //target.setEvolvedMob(this);
        return this.targets.add(target);
    }
    
    @Override
    public boolean addTargets(Collection<EvolvedMobTarget> targets) {
        //for (Iterator<EvolvedMobTarget> it = targets.iterator(); it.hasNext();) {
        //    EvolvedMobTarget target = it.next();
        //    target.setEvolvedMob(this);
        //}
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
    public void update() {
        throw new UnsupportedOperationException("The update() method should be overrided by subclasses.");
    }
    
}
