package hordes.mod.core.game.managers;

import hordes.mod.utils.MinecraftLocations;
import hordes.mod.core.game.HordesPlugin;
import de.ntcomputer.minecraft.controllablemobs.api.ControllableMob;
import de.ntcomputer.minecraft.controllablemobs.api.ControllableMobs;
import hordes.mod.core.game.HordesGame;
import hordes.mod.core.game.HordesWave;
import hordes.mod.plugins.evolvedmobs.api.targets.TargetPriority;
import hordes.mod.plugins.evolvedmobs.EvolvedMobPlugin;
import hordes.mod.plugins.evolvedmobs.implementation.EvolvedZombie;
import hordes.mod.plugins.evolvedmobs.implementation.targets.EvolvedMobTargetLocation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import me.coldandtired.extraevents.HourChangeEvent;
import me.coldandtired.extraevents.SecondTickEvent;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;


/**
 *
 * @author Tseho
 */
public class HordesZombiesManager implements Listener {
    
    protected HordesGame game;
    protected ArrayList<EvolvedZombie> zombies;
    
    public HordesZombiesManager(HordesGame game) {
        this.game = game;
        this.zombies = new ArrayList<EvolvedZombie>();
    }
    
    /*
     * TODO : Change HourChangeEvent by Midnight event
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onNewNight(HourChangeEvent event){
        if(this.game.createWave()){
            this.createZombies();
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onUpdate(SecondTickEvent event){
        
        HordesWave currentWave = this.game.getCurrentWave();
        
        if(currentWave != null && currentWave.getState().equals(HordesWave.WaveState.IN_PROGRESS)){
            if(currentWave.getKilledZombies() == currentWave.getCreatedZombies()){
                currentWave.end(HordesWave.WaveEndReason.ALL_ZOMBIES_KILLED);
            }
        }
        
        Location beaconLocation = this.game.getBeaconPosition();
        
        for (Iterator<EvolvedZombie> it = this.zombies.iterator(); it.hasNext();) {
            EvolvedZombie evolvedZombie = it.next();
            //Remove it from list if killed
            if(!evolvedZombie.getEntity().isValid()){
                it.remove();
                continue;
            }
            
            //Check if the zombie reached the beacon
            Location zombieLocation = evolvedZombie.getEntity().getLocation();
            if(MinecraftLocations.isReached(beaconLocation, zombieLocation, EvolvedMobPlugin.locationReached)){
                //HordesPlugin.logger.log(LoggerLevel.INFO, "A zombie reached the beacon !");
                this.game.reset();
                break;
            }
            
            //Update the zombie
            evolvedZombie.onUpdate();
        }
    }
    
    protected void createZombies(){
        int maxZombies = this.game.getCurrentWave().getMaxZombies();
        int numZombieCreationFailed = 0;
        
        Location target = this.game.getBeaconPosition();
        //HordesPlugin.logger.log(LoggerLevel.DEBUG, "Add {0} zombies ...", maxZombies);
        
        //For performance security, we can't try and fail to create a zombie more than the quantity we need
        while(this.game.getCurrentWave().getCreatedZombies() < maxZombies && numZombieCreationFailed < maxZombies){
            
            //Create a zombie at random location arround the beacon
            Location spawnZombie = MinecraftLocations.createRandomLocation(this.game.getWorld(), target, 40, 20);
            //HordesPlugin.logger.log(LoggerLevel.DEBUG, "New zombie at {0} {1} {2}", new Object[]{spawnZombie.getBlockX(), spawnZombie.getBlockY(), spawnZombie.getBlockZ()});
            Zombie addedZombie = (Zombie) this.game.getWorld().spawnEntity(spawnZombie, EntityType.ZOMBIE);
            
            //We put it under control
            ControllableMob<Zombie> zombieControlled = null;
            try{
                zombieControlled = ControllableMobs.assign(addedZombie);
            }catch(Exception e){
                //If failed, we removed it and try again
                numZombieCreationFailed++;
                //HordesPlugin.logger.log(LoggerLevel.WARNING, "The zombie created is not under control, removing ...");
                this.removeZombie(addedZombie);
                continue;
            }
            
            //We pass the zombie under control to our Evolved class
            EvolvedZombie evolvedZombie = new EvolvedZombie(zombieControlled);
            //We mark it as a part of the current wave
            this.game.getCurrentWave().addZombie(addedZombie);
            //We add it to this manager
            this.zombies.add(evolvedZombie);
            //We initialize this zombie
            this.initializeZombie(evolvedZombie);
        }
    }
    
    protected void initializeZombie(EvolvedZombie evolvedZombie){
        //Adjust his health
        evolvedZombie.getEntity().setMaxHealth(3);
        evolvedZombie.getEntity().setHealth(3);
        //Add targets
        Location target = this.game.getBeaconPosition();
        evolvedZombie.addTarget(new EvolvedMobTargetLocation(evolvedZombie, target, TargetPriority.HIGH));
    }
    
    protected void removeZombie(Zombie entity){
        EntityDeathEvent event = new EntityDeathEvent((LivingEntity)entity, Collections.<ItemStack>emptyList());
        this.game.getPlugin().getServer().getPluginManager().callEvent(event);
        entity.remove();
    }
}
