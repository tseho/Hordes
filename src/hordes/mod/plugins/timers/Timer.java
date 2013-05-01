/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hordes.mod.plugins.timers;

/**
 *
 * @author Tseho
 */
public class Timer {
    
    private String name;
    private String worldName;
    private int interval;
    private int ticks = 0;
    private boolean enabled = true;
    
    public Timer(String name, int interval) {
        this.name = name;
        this.interval = interval;
    }
    
    public Timer(String name, int interval, String worldName){
        this.name = name;
        this.interval = interval;
        this.worldName = worldName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.ticks = 0;
        this.enabled = enabled;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }
    
    public void tick(){
        this.ticks++;
    }
    
    public int getTicks(){
        return this.ticks;
    }
    
    public void resetTicks(){
        this.ticks = 0;
    }
    
}
