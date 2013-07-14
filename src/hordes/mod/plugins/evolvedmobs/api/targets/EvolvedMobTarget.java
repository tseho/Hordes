package hordes.mod.plugins.evolvedmobs.api.targets;

import hordes.mod.plugins.evolvedmobs.api.EvolvedMob;

/**
 *
 * @author Tseho
 */
public interface EvolvedMobTarget {
    
    public void launch();
    
    public void pause();
    
    public void resume();
    
    public void stop();
    
    public boolean isLaunched();
    
    public boolean isPaused();
    
    public boolean isRunning();
    
    public boolean isStopped();
    
    public TargetPriority getPriority();
    
    public EvolvedMob getEvolvedMob();
    
    public void setInitialized(Boolean initialized);
    
    public Boolean isInitialized();
    
    public TargetState getState();
    
    public void onInit();
    
    public void onUpdate();
    
    public boolean isCompleted();
    
    public boolean isAllowed();
}
