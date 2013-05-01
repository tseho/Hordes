package hordes.mod.core.game;

import hordes.mod.core.game.managers.HordesMobsManager;
import hordes.mod.core.game.managers.HordesZombiesManager;
import hordes.mod.plugins.evolvedmobs.implementation.EvolvedMobPlugin;
import hordes.mod.plugins.timers.Timer;
import hordes.mod.plugins.timers.TimersListener;
import hordes.mod.utils.MinecraftArea;
import java.util.logging.Logger;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Tseho
 */
public final class HordesPlugin extends JavaPlugin {
    
    protected HordesGame hordesGame;
    public static Logger logger;
    
    @Override
    public void onEnable(){
        
        //Debug
    	HordesPlugin.logger = this.getLogger();
        
        //Configuration
        this.saveDefaultConfig();
        
        this.hordesGame = new HordesGame(this);
        this.hordesGame.start();
        
        //EvolvedMobPlugin configuration        
        EvolvedMobPlugin.locationReached = new MinecraftArea(this.getConfig().getConfigurationSection("location_reached").getValues(false));
        
        
        // Register our events
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new HordesZombiesManager(this), this);
        pm.registerEvents(new HordesMobsManager(this), this);
        
        //Add timers
        TimersListener timersListener = new TimersListener(pm);
        timersListener.addTimer(new Timer(HordesZombiesManager.TIMER_EVOLVED_MOBS_SELECT_TARGET, 3, this.getConfig().getString("world_name")));
        timersListener.addTimer(new Timer(HordesZombiesManager.TIMER_EVOLVED_MOBS_UPDATE_TARGET, 1, this.getConfig().getString("world_name")));
        
        pm.registerEvents(timersListener, this);
    }
 
    @Override
    public void onDisable() {
        // TODO Insert logic to be performed when the plugin is disabled
    }
    
    public HordesGame getGame(){
        return this.hordesGame;
    }
}