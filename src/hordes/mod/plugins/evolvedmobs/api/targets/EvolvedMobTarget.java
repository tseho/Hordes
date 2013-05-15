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
    
    public void init();
    
    public void update();
    
    public boolean isCompleted();
    
    public boolean isAllowed();
    
    
    /*
    protected ZombieTargetType type;
    protected Metadatable target;
    protected ZombieTargetPriority priority;

    public ZombieTarget(Metadatable target) {
        this.target = target;
    }

    public ZombieTarget(Metadatable target, ZombieTargetPriority priority) {
        this.target = target;
        this.priority = priority;
    }
    
    public void launchAction(){
        if(this.type == null && this.analyzeTargetType()){
            switch(this.type){
                case LOCATION:
                    break;
                case BLOCK:
                    
                case ENTITY:
            }
        }
        
    }
    
    private boolean analyzeTargetType(){
        if(this.target instanceof Location){
            this.type = ZombieTargetType.LOCATION;
        }
        if(this.target instanceof Entity){
            this.type = ZombieTargetType.ENTITY;
        }
        if(this.target instanceof Block){
            this.type = ZombieTargetType.BLOCK;
        }
        
        if(this.target != null){
            return true;
        }else{
            return false;
        }
    }
    */
    
}
