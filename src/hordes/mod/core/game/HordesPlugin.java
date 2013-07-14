package hordes.mod.core.game;

import hordes.mod.core.game.managers.HordesMobsManager;
import hordes.mod.core.game.managers.HordesZombiesManager;
import hordes.mod.plugins.evolvedmobs.EvolvedMobPlugin;
import hordes.mod.plugins.timers.Timer;
import hordes.mod.plugins.timers.TimersListener;
import hordes.mod.utils.MinecraftArea;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Tseho
 */
public final class HordesPlugin extends JavaPlugin {
    
    protected ArrayList<HordesGame> hordesGames = new ArrayList<HordesGame>();
    
    public static Logger logger;
    public static String logLevel;
    
    public static final String TIMER_EVOLVED_MOBS_UPDATE_TARGET_SELECTION = "timerEvolvedMobsUpdateTargetSelection";
    public static final String TIMER_EVOLVED_MOBS_UPDATE_TARGET = "timerEvolvedMobsUpdateTarget";
    
    @Override
    public void onEnable(){
        
        //Configuration
        this.saveDefaultConfig();
        
        //Logger
        logger = this.getLogger();
        logLevel = this.getConfig().getString("logs_level");
        logger.setFilter(this.getCustomLoggerFilter());
        
        
        logger.log(Level.FINE, "HordesPlugin enabled");
        
        Map<String, Object> games = this.getConfig().getConfigurationSection("games").getValues(false);
        
        for (Map.Entry<String, Object> en : games.entrySet()) {
            String gameName = en.getKey();
            logger.log(Level.FINE, "World config name : {0}", gameName);
            
            Map<String, Object> gameSettings = (Map<String, Object>) ((ConfigurationSection) en.getValue()).getValues(false);
            logger.log(Level.FINE, "World config : {0} settings loaded", gameSettings.size());
            
            HordesGame game = new HordesGame(this, gameName, gameSettings);
            //game.start();
            this.hordesGames.add(game);
            
        }
        
        
        /*
        
        //Load worlds from config
        List<String> worlds = this.getConfig().getStringList("worlds");
        for (int i = 0; i < worlds.size(); i++) {
            World world = this.getServer().getWorld(worlds.get(i));
            if(world != null){
                logger.log(LoggerLevel.INFO, "The world {0} is added to Hordes", new Object[]{worlds.get(i)});
                HordesGame game = new HordesGame(this, world);
                game.start();
                this.hordesGames.add(game);
            }else{
                logger.log(LoggerLevel.WARNING, "The world {0} doesn't exists", new Object[]{worlds.get(i)});
            }
        }
        
        //EvolvedMobPlugin configuration        
        EvolvedMobPlugin.locationReached = new MinecraftArea(this.getConfig().getConfigurationSection("location_reached").getValues(false));
        
        logger.log(LoggerLevel.DEBUG, "Config loaction_reached loaded : {0}", EvolvedMobPlugin.locationReached.toString());
        
        EvolvedMobPlugin.mobsPermissions = this.getConfig().getConfigurationSection("mobs_permissions").getValues(false);
                
        logger.log(LoggerLevel.DEBUG, "Config mobs_permissions loaded : {0}", EvolvedMobPlugin.mobsPermissions.toString());
        
        //Debug mobs permissions
        for (Map.Entry<String, Object> en : EvolvedMobPlugin.mobsPermissions.entrySet()) {
            String mobName = en.getKey();
            ArrayList<String> mobPermissions = (ArrayList<String>) en.getValue();
            logger.log(LoggerLevel.INFO, "Permissions for {0} : {1}", new Object[]{mobName, mobPermissions.toString()});
        }
        
        
        // Register our events
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new HordesZombiesManager(this), this);
        pm.registerEvents(new HordesMobsManager(this), this);
        
        //Add timers
        TimersListener timersListener = new TimersListener(pm);
        timersListener.addTimer(new Timer(TIMER_EVOLVED_MOBS_UPDATE_TARGET_SELECTION, 3, this.getConfig().getString("world_name")));
        timersListener.addTimer(new Timer(TIMER_EVOLVED_MOBS_UPDATE_TARGET, 1, this.getConfig().getString("world_name")));
        
        pm.registerEvents(timersListener, this);
        */
    }
 
    @Override
    public void onDisable() {
        
    }
    
    /**
     * Gets the list of hordes games created by this plugin.
     * @return HordesGame list
     */
    public ArrayList<HordesGame> getGames(){
        return this.hordesGames;
    }
    
    private Filter getCustomLoggerFilter(){
        return new Filter() {
            @Override
            public boolean isLoggable(LogRecord record) {
                if(logLevel.equals("DEBUG")){
                    if(record.getLevel().intValue() < Level.INFO.intValue()){
                        record.setLevel(Level.INFO);
                    }
                    return true;
                }
                if(logLevel.equals("INFO") && record.getLevel().intValue() >= Level.INFO.intValue()){
                    return true;
                }
                if(logLevel.equals("WARNING") && record.getLevel().intValue() >= Level.WARNING.intValue()){
                    return true;
                }
                if(logLevel.equals("ERROR") && record.getLevel().intValue() >= Level.SEVERE.intValue()){
                    return true;
                }
                
                return false;
            }
        };
    }
}