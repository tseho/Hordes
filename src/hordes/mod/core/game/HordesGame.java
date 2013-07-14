package hordes.mod.core.game;

import hordes.mod.core.game.HordesWave.WaveDifficulty;
import hordes.mod.core.game.HordesWave.WaveState;
import hordes.mod.core.game.managers.HordesMobsManager;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * Hordes game. Each game has his own world and his own settings.
 * @author Tseho
 */
public class HordesGame {
    
    public static enum GameState { FINISHED, PAUSED, RUNNING;}
    
    protected HordesPlugin plugin;
    protected HordesWave wave;
    protected Location balisePosition;
    protected World world;
    protected GameState state;
    
    protected String name;
    protected WaveDifficulty difficulty;
    protected List<String> mobsAllowed;
    protected Map<String, Object> mobsPermissions;
    
    public HordesGame(HordesPlugin plugin, String name, Map<String, Object> config){
        
        this.plugin = plugin;
        
        this.name = name;
        
        // World loaded from config
        World worldLoaded = this.plugin.getServer().getWorld(config.get("world").toString());
        if(worldLoaded != null){
            HordesPlugin.logger.log(Level.INFO, "[{0}] World \"{1}\" found and loaded.", new Object[]{this.name, config.get("world").toString()});
            this.world = worldLoaded;
        }else{
            HordesPlugin.logger.log(Level.WARNING, "[{0}] The world \"{1}\" doesn't exists", new Object[]{this.name, config.get("world").toString()});
        }
        
        // Difficulty loaded from config
        try{
            this.difficulty = WaveDifficulty.valueOf(config.get("difficulty").toString());
        }catch(EnumConstantNotPresentException e){
            HordesPlugin.logger.log(Level.WARNING, "Unknown difficulty for world \"{0}\"", this.name);
            this.difficulty = WaveDifficulty.PEACEFULL;
        }
        
        // Mobs permissions loaded from config
        this.mobsPermissions = ((ConfigurationSection) config.get("mobs_permissions")).getValues(false);
        for (Map.Entry<String, Object> entry : this.mobsPermissions.entrySet()) {
            HordesPlugin.logger.log(Level.INFO, "[{0}] Permissions for {1} : {2}", new Object[]{this.name, entry.getKey(), entry.getValue().toString()});
        }
        
        // List of mobs allowed to spawn from config
        this.mobsAllowed = (List<String>) config.get("mobs_allowed");
        HordesPlugin.logger.log(Level.INFO, "[{0}] Mobs allowed to spawn: {1}", new Object[]{this.name, this.mobsAllowed.toString()});
    
        //TODO update world and set correct balise
        this.balisePosition = new Location(this.world, -476, 64, 97);
    }
    
    /**
     * Create a new wave of zombies
     * @return true if a new wave is created, false otherwise.
     */
    public boolean createWave(){
        
        if(!this.state.equals(GameState.RUNNING)){
            return false;
        }
        
        if(this.getWorld().getPlayers().isEmpty()){
            HordesPlugin.logger.log(Level.INFO, "Nobody's here, new wave aborted.");
            return false;
        }
        
        if(this.wave != null && this.wave.getState() != WaveState.ENDED){
            HordesPlugin.logger.log(Level.INFO, "Previous wave is not finished !");
        }
        
        int waveNumber = 1;
        if(this.wave != null){
            waveNumber = this.wave.getWaveNum() + 1;
        }
        
        HordesWave waveCreated = new HordesWave(
                waveNumber,
                this.getWorld().getPlayers().size(),
                this.difficulty);
        waveCreated.launchWave();
        this.wave = waveCreated;
        
        HordesPlugin.logger.log(Level.INFO, "New wave created");
        
        return true;
    }
    
    /**
     * Gets the current wave
     * @return Current wave or null
     */
    public HordesWave getCurrentWave(){
        return this.wave;
    }
    
    /**
     * Init the game.
     * <ul>
     * <li> /!\ The world is not reseted, I hope it will be added soon</li>
     * <li>Each player is teleported at balise position</li>
     * <li>All remaining zombies are killed</li>
     * </ul>
     * 
     */
    protected void init(){
        
        this.wave = null;
        
        //TODO reset the world
        
        //Move all connected players to the balise
        List<Player> playersConnected = this.world.getPlayers();
        for (Iterator<Player> it = playersConnected.iterator(); it.hasNext();) {
            Player playerConnected = it.next();
            boolean teleported = playerConnected.teleport(this.balisePosition);
            if(!teleported){
                //HordesPlugin.logger.log(LoggerLevel.WARNING, "Player {0} can't be teleported for new game.", playerConnected.getPlayerListName());
            }
        }
        
        //Kill all mobs
        HordesMobsManager.killAll(this.world, this.plugin);
    }
    
    /**
     * Launch this game
     */
    public void start(){
        if(this.world == null){
            HordesPlugin.logger.log(Level.WARNING, "[{0}] World is not loaded, cannot be started.", new Object[]{this.name});
            return;
        }
        HordesPlugin.logger.log(Level.INFO, "[{0}] New game launched !", new Object[]{this.name});
        this.state = GameState.RUNNING;
        this.init();
    }
    
    /**
     * Reset this game
     * It's just a shorcut for finish() and start()
     */
    public void reset(){
        this.finish();
        this.start();
    }
    
    /**
     * Resume this game
     */
    public void resume(){
        if(this.world == null){
            HordesPlugin.logger.log(Level.WARNING, "[{0}] World is not loaded, cannot be resumed.", new Object[]{this.name});
            return;
        }
        HordesPlugin.logger.log(Level.INFO, "[{0}] Game resumed", new Object[]{this.name});
        this.state = GameState.RUNNING;
    }
    
    /**
     * Pause this game
     */
    public void pause(){
        HordesPlugin.logger.log(Level.INFO, "[{0}] Game paused", new Object[]{this.name});
        this.state = GameState.PAUSED;
    }
    
    /**
     * Finish this game
     */
    public void finish(){
        this.getCurrentWave().setState(WaveState.ENDED);
        HordesPlugin.logger.log(Level.INFO, "[{0}] Game ended. Wait for new game ...", new Object[]{this.name});
        this.state = GameState.FINISHED;
    }
    
    /**
     * Gets this game world
     * @return Game world
     */
    public World getWorld(){
        return this.world;
    }
    
    /**
     * Gets this game balise position
     * @return Balise position
     */
    public Location getBalisePosition(){
        return this.balisePosition;
    }
    
    /**
     * Gets the instance of HordesPlugin
     * @return HordesPlugin instance
     */
    public HordesPlugin getPlugin(){
        return this.plugin;
    }
}
