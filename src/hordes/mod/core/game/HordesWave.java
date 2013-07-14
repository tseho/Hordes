package hordes.mod.core.game;

import java.util.ArrayList;
import org.bukkit.entity.Entity;

/**
 * Hordes wave. Each wave has his own zombies.
 * @author Tseho
 */
public class HordesWave {
    
    public static enum WaveState { CREATED, IN_PROGRESS, ENDED;}
    public static enum WaveEndReason { ALL_ZOMBIES_KILLED, WAVE_ABORTED;}
    public static enum WaveDifficulty{ PEACEFULL,EASY,HARD,EXTREME;}
    
    protected int waveNum = 0;
    protected int playersOnWaveBegin = 0;
    protected int zombiesCreated = 0;
    protected int zombiesKilled = 0;
    protected WaveState state;
    protected WaveDifficulty difficulty;
    protected ArrayList<Entity> zombies;
    
    public HordesWave(int waveNum, int playersOnWaveBegin, WaveDifficulty difficulty){
        this.waveNum = waveNum;
        this.playersOnWaveBegin = playersOnWaveBegin;
        this.difficulty = difficulty;
        this.state = WaveState.CREATED;
        this.zombies = new ArrayList<Entity>();
    }
    
    /**
     * Launch the wave
     */
    public void launchWave(){
        this.state = WaveState.IN_PROGRESS;
    }
    
    /**
     * Gets the number of zombies alives from this wave
     * @return Zombies alives quantity
     */
    public int getZombiesAlives() {
        this.updateZombiesKilledCounter();
        return this.zombiesCreated - this.zombiesKilled;
    }
    
    /**
     * Gets the number of zombies created by this wave
     * @return Zombies created quantity
     */
    public int getCreatedZombies(){
        return this.zombiesCreated;
    }
    
    /**
     * Gets the number of zombies killed during this wave
     * @return Zombies killed quantity
     */
    public int getKilledZombies(){
        this.updateZombiesKilledCounter();
        return this.zombiesKilled;
    }
    
    /**
     * Gets all zombies from this wave
     * @return All Zombies
     */
    public ArrayList<Entity> getZombies(){
        return this.zombies;
    }
    
    /**
     * Add a zombie to this wave
     * @param zombie 
     */
    public void addZombie(Entity zombie){
        if(this.zombiesCreated < this.getMaxZombies()){
            this.zombies.add(zombie);
            this.zombiesCreated++;
        }
    }
    
    /**
     * Update the counter of zombies killed
     */
    protected void updateZombiesKilledCounter(){
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

    /**
     * Gets this wave number [1..n]
     * @return Wave number
     */
    public int getWaveNum() {
        return this.waveNum;
    }

    /**
     * Gets the number of players connected when the wave was launched
     * @return 
     */
    public int getPlayersOnWaveBegin() {
        return this.playersOnWaveBegin;
    }

    /**
     * Gets the wave state
     * @return state
     */
    public WaveState getState() {
        return this.state;
    }

    /**
     * Set the wave state
     * @param state 
     */
    public void setState(WaveState state) {
        this.state = state;
    }
    
    /**
     * End this wave and indicate origin.
     * @param cause 
     */
    public void end(WaveEndReason cause){
        this.setState(WaveState.ENDED);
        
        if(cause.equals(WaveEndReason.WAVE_ABORTED)){
            //HordesPlugin.logger.log(LoggerLevel.INFO, "Wave aborted.");
        }else if(cause.equals(WaveEndReason.ALL_ZOMBIES_KILLED)){
            //HordesPlugin.logger.log(LoggerLevel.INFO, "Wave success : All zombies were killed.");
        }
    }
    
    /**
     * Gets the max number of zombies allowed for this wave.
     * @return 
     */
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
