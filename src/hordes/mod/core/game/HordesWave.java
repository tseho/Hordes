package hordes.mod.core.game;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.entity.Entity;

/**
 *
 * @author Tseho
 */
public class HordesWave {
    public static final int WAVE_CREATED = 0;
    public static final int WAVE_IN_PROGRESS = 1;
    public static final int WAVE_ENDED=  2;
    
    public static final int CAUSE_ZOMBIES_KILLED = 0;
    public static final int CAUSE_ABORTED = 1;
    
    protected int number = 0;
    protected int playersOnWaveBegin = 0;
    protected int zombiesCreated = 0;
    protected int zombiesKilled = 0;
    protected int state = 0;
    protected int difficulty = 0;
    protected Logger logger;
    protected ArrayList<Entity> zombies;
    
    public HordesWave(int number, int playersOnWaveBegin, int difficulty, Logger logger){
        this.number = number;
        this.playersOnWaveBegin = playersOnWaveBegin;
        this.difficulty = difficulty;
        this.state = HordesWave.WAVE_CREATED;
        this.logger = logger;
        this.zombies = new ArrayList<Entity>();
    }
    
    public void launchWave(){
        this.state = HordesWave.WAVE_IN_PROGRESS;
    }
    
    public int getZombiesAlives() {
        this.updateZombies();
        return this.zombiesCreated - this.zombiesKilled;
    }
    
    public int getCreatedZombies(){
        return this.zombiesCreated;
    }
    
    public int getKilledZombies(){
        this.updateZombies();
        return this.zombiesKilled;
    }
    
    public ArrayList<Entity> getZombies(){
        return this.zombies;
    }
    
    public void addZombie(Entity zombie){
        if(this.zombiesCreated < this.getMaxZombies()){
            this.zombies.add(zombie);
            this.zombiesCreated++;
        }
    }
    
    protected void updateZombies(){
        int tZombiesKilled = 0;
        int tZombiesAlives = 0;
        for (Entity zombie : zombies) {
            if(!zombie.isValid()){
                tZombiesKilled++;
            }else{
                tZombiesAlives++;
            }
        }
        
        if(tZombiesAlives == 0){
            this.zombiesKilled = this.zombiesCreated;
        }else{
            this.zombiesKilled = tZombiesKilled;
        }
    }

    public int getNumber() {
        return number;
    }

    public int getPlayersOnWaveBegin() {
        return playersOnWaveBegin;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
    
    public void setState(int state, int cause){
        this.setState(state);
        
        switch(cause){
            case CAUSE_ZOMBIES_KILLED:
                this.logger.log(Level.INFO, "Wave success : All zombies were killed.");
                break;
            case CAUSE_ABORTED:
                this.logger.log(Level.INFO, "Wave aborted.");
                break;
        }
    }
    
    public int getMaxZombies(){
        return 1;
        /*
        switch(this.difficulty){
            case 0:
                return 0;
            case 1:
                return this.number * 2 * this.playersOnWaveBegin;
            case 2:
                return this.number * 4 * this.playersOnWaveBegin;
            case 3:
                return this.number * 6 * this.playersOnWaveBegin;
            default:
                this.logger.log(Level.WARNING, "Difficulty level was not recognized");
                return 0;
        }
        */
    }
}
