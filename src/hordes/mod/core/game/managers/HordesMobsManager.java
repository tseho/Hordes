/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hordes.mod.core.game.managers;

import hordes.mod.core.game.HordesPlugin;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;


/**
 *
 * @author Tseho
 */
public class HordesMobsManager implements Listener {
    
    protected HordesPlugin plugin;
    protected List<String> allowedMobs = null;
    
    public HordesMobsManager(HordesPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void onMobSpawn(CreatureSpawnEvent event) {
        if(!this.getListAllowedMobs().contains(event.getEntityType().getName())){
            event.setCancelled(true);
        }
        //this.plugin.getLogger().log(Level.INFO, "Spawn : {0}", new Object[]{event.getEntityType().getName()});
    }
    
    protected List<String> getListAllowedMobs(){
        if(this.allowedMobs != null){
            return this.allowedMobs;
        }
        
        this.allowedMobs = new ArrayList<String>();
        
        List<String> allowedMobsLoaded = this.plugin.getConfig().getStringList("mobs_allowed");
        for (int i = 0; i < allowedMobsLoaded.size(); i++) {
            String nameCleaned = allowedMobsLoaded.get(i).toLowerCase();
            nameCleaned = Character.toUpperCase(nameCleaned.charAt(0)) + nameCleaned.substring(1);
            this.allowedMobs.add(nameCleaned);
        }
        
        this.plugin.getLogger().log(Level.INFO, "Allowed mobs : {0}", Arrays.toString(this.getListAllowedMobs().toArray()));
        
        //Add zombie if not exists
        if(!this.allowedMobs.contains("Zombie")){
            this.allowedMobs.add("Zombie");
        }
        
        return this.allowedMobs;
    }
    
    /**
    * Inspired by Essentials
    * https://github.com/essentials/Essentials/blob/master/Essentials/src/net/ess3/commands/Commandkillall.java
    */
    public static void killAll(World world, JavaPlugin plugin){
        List<Entity> entities = world.getEntities();
        int numKills = 0;
        for (Iterator<Entity> it = entities.iterator(); it.hasNext();) {
            Entity entity = it.next();
            
            if (!(entity instanceof LivingEntity) || entity instanceof HumanEntity){
                continue;
            }
            if (entity instanceof Wolf && ((Wolf)entity).isTamed()){
                continue;
            }
            if (entity instanceof Ocelot && ((Ocelot)entity).isTamed()){
                continue;
            }
            
            EntityDeathEvent event = new EntityDeathEvent((LivingEntity)entity, Collections.<ItemStack>emptyList());
            plugin.getServer().getPluginManager().callEvent(event);
            entity.remove();
            numKills++;
        }
        plugin.getLogger().log(Level.INFO, "{0} mobs killed", numKills);
    }
}
