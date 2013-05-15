package hordes.mod.plugins.evolvedmobs.api;

import de.ntcomputer.minecraft.controllablemobs.api.ControllableMob;
import hordes.mod.plugins.evolvedmobs.api.targets.EvolvedMobTarget;
import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.entity.LivingEntity;

/**
 *
 * @author Tseho
 */
public interface EvolvedMob {
    
    public LivingEntity getEntity();
    
    public ControllableMob<LivingEntity> getControllableMob();
    
    public EvolvedMobTarget getCurrentTarget();
    
    public void setCurrentTarget(EvolvedMobTarget currentTarget);
    
    public EvolvedMobTarget updateCurrentTargetSelection();
    
    public boolean addTarget(EvolvedMobTarget target);
    
    public boolean addTargets(Collection<EvolvedMobTarget> targets);
    
    public void clearTargets();
    
    public boolean isAllowedTo(String autorisationName);
    
    public void setPermissions(ArrayList<String> permissions);
    
    public void addPermission(String permission);
    
    public void removePermission(String permission);
    
    public void update();
}
