/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hordes.mod.core.game.managers;

import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 * @author Tseho
 */
public class HordesLocationManager {
    
    public static int calculateDistance(Location p1, Location p2){
        return Location.locToBlock(Math.sqrt(
            (p1.getX() - p2.getX()) * (p1.getX() - p2.getX()) + 
            (p1.getZ() - p2.getZ()) * (p1.getZ() - p2.getZ())
        ));
    }
    
    public static Location createRandomLocation(World world, Location center, int radiusMax){
        return createRandomLocation(world, center, radiusMax, 1);
    }
    
    public static Location createRandomLocation(World world, Location center, int radiusMax, int radiusMin){
        int x, y, z;
        double u, v, w, t;
        
        //Create a randow location around center
        do{
            u = Math.random();
            v = Math.random();

            w = radiusMax * Math.sqrt(u);
            t = 2 * Math.PI * v;

            x = (int) (w * Math.cos(t));
            z = (int) (w * Math.sin(t));
        } while((Math.abs(x) + Math.abs(z)) < radiusMin);
        
        x = x + center.getBlockX();
        z = z + center.getBlockZ();
        
        //TODO : Search the lowest place above the ground for ignoring trees, roofs, etc
        
        //Get hightest location
        y  = world.getHighestBlockYAt(x, z) + 1;
        
        return new Location(world, x, y, z);
    }
    
}
