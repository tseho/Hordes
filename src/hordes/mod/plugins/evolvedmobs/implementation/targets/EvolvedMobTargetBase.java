package hordes.mod.plugins.evolvedmobs.implementation.targets;

import hordes.mod.plugins.evolvedmobs.api.EvolvedMob;
import hordes.mod.plugins.evolvedmobs.api.targets.EvolvedMobTarget;
import hordes.mod.plugins.evolvedmobs.api.targets.TargetPriority;
import hordes.mod.plugins.evolvedmobs.api.targets.TargetState;

/**
 *
 * @author Tseho
 */
public class EvolvedMobTargetBase implements EvolvedMobTarget {
    
    protected boolean launched = false;
    protected boolean paused = false;
    protected boolean stopped = false;
    protected boolean initialized = false;
    protected TargetState state = TargetState.IN_QUEUE;
    
    protected TargetPriority priority = TargetPriority.LOW;
    protected EvolvedMob evolvedMob;
    
    public static final String MOVE = "move";
    public static final String ATTACK_PLAYER = "attack_player";
    public static final String ATTACK_MOB = "attack_mob";
    public static final String BREAK_BLOCK = "break_block";
    public static final String PLACE_BLOCK = "place_block";
    public static final String USE_OBJECT = "use_object";
    
    public EvolvedMobTargetBase(EvolvedMob evolvedMob){
        this.evolvedMob = evolvedMob;
    }

    @Override
    public void launch() {
        if(this.paused == false && this.stopped == false){
            this.launched = true;
            this.state = TargetState.RUNNING;
        }
    }

    @Override
    public void pause() {
        if(this.launched == true && this.stopped == false){
            this.paused = true;
            this.state = TargetState.PAUSED;
        }
    }

    @Override
    public void resume() {
        if(this.paused == true && this.stopped == false){
            this.paused = false;
            this.state = TargetState.RUNNING;
        }
    }

    @Override
    public void stop() {
        this.stopped = true;
        if(this.isCompleted()){
            this.state = TargetState.COMPLETED;
        }else{
            this.state = TargetState.CANCELLED;
        }
    }

    @Override
    public boolean isLaunched() {
        return this.launched;
    }

    @Override
    public boolean isPaused() {
        return this.paused;
    }

    @Override
    public boolean isRunning() {
        return (this.launched == true && this.paused == false && this.stopped == false);
    }

    @Override
    public boolean isStopped() {
        return this.stopped;
    }

    @Override
    public TargetPriority getPriority() {
        return this.priority;
    }

    @Override
    public EvolvedMob getEvolvedMob() {
        return this.evolvedMob;
    }
    
    @Override
    public void setInitialized(Boolean initialized) {
        this.initialized = initialized;
    }

    @Override
    public Boolean isInitialized() {
        return this.initialized;
    }
    
    @Override
    public TargetState getState() {
        return this.state;
    }
    
    @Override
    public void onInit() {
        throw new UnsupportedOperationException("The init() method should be overrided by subclasses.");
    }

    @Override
    public void onUpdate() {
        throw new UnsupportedOperationException("The update() method should be overrided by subclasses.");
    }
    
    @Override
    public boolean isCompleted() {
        throw new UnsupportedOperationException("The isFinished() method should be overrided by subclasses.");
    }

    @Override
    public boolean isAllowed() {
        throw new UnsupportedOperationException("The isAllowed() method should be overrided by subclasses.");
    }
    
}
