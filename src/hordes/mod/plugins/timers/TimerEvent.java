/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hordes.mod.plugins.timers;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Tseho
 */
public class TimerEvent extends Event implements Cancellable {
    
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private Timer timer;
    
    public TimerEvent(Timer timer){
        this.timer = timer;
    }
    
    public Timer getTimer(){
        return this.timer;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
    
}
