package hordes.mod.core.game;

import hordes.mod.core.game.managers.HordesMobsManager;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author Tseho
 */
public class HordesGame {
    public static final int GAME_FINISHED = 0;
    public static final int GAME_PAUSED = 1;
    public static final int GAME_RUNNING = 2;
    
    protected HordesPlugin plugin;
    protected HordesWave wave;
    protected Location balisePosition;
    protected World world;
    protected int state = 0;
    
    public HordesGame(HordesPlugin plugin){
        this.plugin = plugin;
        this.world = this.plugin.getServer().getWorld(this.plugin.getConfig().getString("world_name"));
        
        //TODO
        this.balisePosition = new Location(world, -476, 64, 97);
    }
    
    public boolean createWave(){
        
        if(this.state != GAME_RUNNING){
            return false;
        }
        
        if(this.plugin.getGame().getWorld().getPlayers().isEmpty()){
            this.plugin.getLogger().log(Level.INFO, "Nobody's here, new wave aborted.");
            return false;
        }
        
        this.plugin.getLogger().log(Level.INFO, "New wave created");
        if(this.wave != null && this.wave.getState() != HordesWave.WAVE_ENDED){
            this.plugin.getLogger().log(Level.WARNING, "Previous wave is not finished !");
        }
        
        int waveNumber = 1;
        if(this.wave != null){
            waveNumber = this.wave.getNumber() + 1;
        }
        
        HordesWave waveCreated = new HordesWave(
                waveNumber,
                this.plugin.getServer().getWorld(this.plugin.getConfig().getString("world_name")).getPlayers().size(),
                this.plugin.getConfig().getInt("difficulty"),
                this.plugin.getLogger());
        waveCreated.launchWave();
        this.wave = waveCreated;
        
        return true;
    }
    
    public HordesWave getCurrentWave(){
        return this.wave;
    }
    
    protected void init(){
        
        this.wave = null;
        
        //this.getWorld().setTime(18000);
        
        //Move all connected players to the balise
        List<Player> playersConnected = this.world.getPlayers();
        for (Iterator<Player> it = playersConnected.iterator(); it.hasNext();) {
            Player playerConnected = it.next();
            boolean teleported = playerConnected.teleport(this.balisePosition);
            if(!teleported){
                this.plugin.getLogger().log(Level.INFO, "Player {0} can't be teleported for new game.", playerConnected.getPlayerListName());
            }
        }
        
        //Kill all mobs
        HordesMobsManager.killAll(this.world, this.plugin);
    }
    
    public void start(){
        this.plugin.getLogger().log(Level.INFO, "New game launched !");
        this.state = HordesGame.GAME_RUNNING;
        this.init();
    }
    
    public void reset(){
        this.finish();
        this.start();
    }
    
    public void resume(){
        this.plugin.getLogger().log(Level.INFO, "Game resumed.");
        this.state = HordesGame.GAME_RUNNING;
    }
    
    public void pause(){
        this.plugin.getLogger().log(Level.INFO, "Game paused.");
        this.state = HordesGame.GAME_PAUSED;
    }
    
    public void finish(){
        this.getCurrentWave().setState(HordesWave.WAVE_ENDED);
        this.plugin.getLogger().log(Level.INFO, "Game ended. Wait for new game ...");
        this.state = HordesGame.GAME_FINISHED;
    }
    
    public World getWorld(){
        return this.world;
    }
    
    public Location getBalisePosition(){
        return this.balisePosition;
    }
}
