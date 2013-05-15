/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hordes.mod.core.game.managers;

import hordes.mod.utils.MinecraftLocations;
import hordes.mod.core.game.HordesPlugin;
import de.ntcomputer.minecraft.controllablemobs.api.ControllableMob;
import de.ntcomputer.minecraft.controllablemobs.api.ControllableMobs;
import hordes.mod.core.game.HordesWave;
import hordes.mod.plugins.evolvedmobs.api.targets.TargetPriority;
import hordes.mod.plugins.evolvedmobs.EvolvedMobPlugin;
import hordes.mod.plugins.evolvedmobs.implementation.EvolvedZombie;
import hordes.mod.plugins.evolvedmobs.implementation.targets.EvolvedMobTargetLocation;
import hordes.mod.plugins.timers.TimerEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Level;
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
    
    public static final String TIMER_EVOLVED_MOBS_SELECT_TARGET = "timerEvolvedMobsSelectTarget";
    public static final String TIMER_EVOLVED_MOBS_UPDATE_TARGET = "timerEvolvedMobsUpdateTarget";
    
    protected HordesPlugin plugin;
    protected ArrayList<EvolvedZombie> zombies;
    
    public HordesZombiesManager(HordesPlugin plugin) {
        this.plugin = plugin;
        this.zombies = new ArrayList<EvolvedZombie>();
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onNewNight(HourChangeEvent event){
        if(this.plugin.getGame().createWave()){
            this.createZombies();
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onUpdate(SecondTickEvent event){
        
        HordesWave currentWave = this.plugin.getGame().getCurrentWave();
        
        if(currentWave != null && currentWave.getState() == HordesWave.WAVE_IN_PROGRESS){
            if(currentWave.getKilledZombies() == currentWave.getCreatedZombies()){
                currentWave.setState(HordesWave.WAVE_ENDED, HordesWave.CAUSE_ZOMBIES_KILLED);
            }
        }
        
        Location baliseLocation = this.plugin.getGame().getBalisePosition();
        
        for (Iterator<EvolvedZombie> it = this.zombies.iterator(); it.hasNext();) {
            EvolvedZombie evolvedZombie = it.next();
            //Remove it from list if killed
            if(!evolvedZombie.getEntity().isValid()){
                it.remove();
                continue;
            }
            
            //Check if the zombie reached the balise
            Location zombieLocation = evolvedZombie.getEntity().getLocation();
            if(MinecraftLocations.isReached(baliseLocation, zombieLocation, EvolvedMobPlugin.locationReached)){
                this.plugin.getLogger().log(Level.INFO, "A zombie reached the balise !");
                this.plugin.getGame().reset();
                break;
            }
            
            //Update the zombie
            evolvedZombie.update();
        }
    }
    
    @EventHandler
    public void onTimer(TimerEvent event){
        //this.plugin.getLogger().log(Level.INFO, "event : {0}", event.getTimer().getName());
        
        if(event.getTimer().getName().equals(TIMER_EVOLVED_MOBS_SELECT_TARGET)){
            for (Iterator<EvolvedZombie> it = this.zombies.iterator(); it.hasNext();) {
                EvolvedZombie evolvedZombie = it.next();
                evolvedZombie.updateCurrentTargetSelection();
            }
        }
        
        if(event.getTimer().getName().equals(TIMER_EVOLVED_MOBS_UPDATE_TARGET)){
            for (Iterator<EvolvedZombie> it = this.zombies.iterator(); it.hasNext();) {
                EvolvedZombie evolvedZombie = it.next();
                if(evolvedZombie.getCurrentTarget() != null && !evolvedZombie.getCurrentTarget().isStopped() && !evolvedZombie.getCurrentTarget().isPaused()){
                    evolvedZombie.getCurrentTarget().update();
                }
            }
        }
    }
    
    protected void createZombies(){
        int maxZombies = this.plugin.getGame().getCurrentWave().getMaxZombies();
        int numZombieCreationFailed = 0;
        
        Location target = this.plugin.getGame().getBalisePosition();
        this.plugin.getLogger().log(Level.INFO, "Add {0} zombies ...", maxZombies);
        
        //For performance security, we can't try and fail to create a zombie more than the quantity we need
        while(this.plugin.getGame().getCurrentWave().getCreatedZombies() < maxZombies && numZombieCreationFailed < maxZombies){
            
            //Create a zombie at random location arround the balise
            Location spawnZombie = MinecraftLocations.createRandomLocation(this.plugin.getGame().getWorld(), target, 40, 20);
            //this.plugin.getLogger().log(Level.INFO, "New zombie at {0} {1} {2}", new Object[]{spawnZombie.getBlockX(), spawnZombie.getBlockY(), spawnZombie.getBlockZ()});
            Zombie addedZombie = (Zombie) this.plugin.getGame().getWorld().spawnEntity(spawnZombie, EntityType.ZOMBIE);
            
            //We put it under control
            ControllableMob<Zombie> zombieControlled = null;
            try{
                zombieControlled = ControllableMobs.assign(addedZombie);
            }catch(Exception e){
                //If failed, we removed it and try again
                numZombieCreationFailed++;
                this.plugin.getLogger().log(Level.WARNING, "The zombie created is not under control, removing ...");
                this.removeZombie(addedZombie);
                continue;
            }
            
            //We pass the zombie under control to our Evolved class
            EvolvedZombie evolvedZombie = new EvolvedZombie(zombieControlled);
            //We mark it as a part of the current wave
            this.plugin.getGame().getCurrentWave().addZombie(addedZombie);
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
        Location target = this.plugin.getGame().getBalisePosition();
        evolvedZombie.addTarget(new EvolvedMobTargetLocation(evolvedZombie, target, TargetPriority.HIGH));
    }
    
    protected void removeZombie(Zombie entity){
        EntityDeathEvent event = new EntityDeathEvent((LivingEntity)entity, Collections.<ItemStack>emptyList());
        this.plugin.getServer().getPluginManager().callEvent(event);
        entity.remove();
    }
}
