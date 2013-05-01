/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hordes.mod.plugins.evolvedmobs.implementation.targets;

import hordes.mod.plugins.evolvedmobs.api.EvolvedMob;
import hordes.mod.plugins.evolvedmobs.api.targets.EvolvedMobTarget;
import hordes.mod.plugins.evolvedmobs.api.targets.TargetPriority;

/**
 *
 * @author Tseho
 */
public class EvolvedMobTargetBase implements EvolvedMobTarget {
    
    protected boolean launched = false;
    protected boolean paused = false;
    protected boolean stopped = false;
    protected boolean initialized = false;
    
    protected TargetPriority priority = TargetPriority.LOW;
    protected EvolvedMob evolvedMob;

    @Override
    public void launch() {
        if(this.paused == false && this.stopped == false){
            this.launched = true;
        }
    }

    @Override
    public void pause() {
        if(this.launched == true && this.stopped == false){
            this.paused = true;
        }
    }

    @Override
    public void resume() {
        if(this.paused == true && this.stopped == false){
            this.paused = false;
        }
    }

    @Override
    public void stop() {
        this.stopped = true;
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
    public void setEvolvedMob(EvolvedMob evolvedMob) {
        this.evolvedMob = evolvedMob;
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
    public void init() {
        throw new UnsupportedOperationException("The init() method should be overrided by subclasses.");
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException("The update() method should be overrided by subclasses.");
    }
    
}
