package hordes.mod.plugins.timers;

import java.util.HashMap;
import java.util.Map;
import me.coldandtired.extraevents.SecondTickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

/**
 *
 * @author Tseho
 */
public class TimersListener implements Listener {
    
    private Map<String, Timer> timers = new HashMap<String, Timer>();
    private PluginManager pluginManager;
    
    public TimersListener(PluginManager pluginManager){
        this.pluginManager = pluginManager;
    }
    
    public void addTimer(Timer timer){
        this.timers.put(timer.getName(), timer);
    }
    
    public Timer getTimer(String name){
        return this.timers.get(name);
    }
    
    public void resetTimers(){
        for (Map.Entry<String, Timer> entry : timers.entrySet()) {
            entry.getValue().resetTicks();
        }
    }
    
    public void removeTimers(){
        this.timers.clear();
    }
    
    @EventHandler
    public void onEachSecond(SecondTickEvent event){
        for (Map.Entry<String, Timer> entry : timers.entrySet()) {
            Timer timer = entry.getValue();
            if(timer.isEnabled()){
                timer.tick();
                if(timer.getTicks() == timer.getInterval()){
                    timer.resetTicks();
                    this.pluginManager.callEvent(new TimerEvent(timer));
                }
            }
        }
    }
    
}
