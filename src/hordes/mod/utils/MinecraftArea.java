/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hordes.mod.utils;

import java.util.Map;

/**
 *
 * @author Tseho
 */
public class MinecraftArea {
    
    private int x;
    private int y;
    private int z;
    
    public MinecraftArea(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public MinecraftArea(Map<String, Object> points){
        for (Map.Entry<String, Object> entry : points.entrySet()) {
            String name = entry.getKey();
            Integer value = (Integer) entry.getValue();
            if(name.equals("x")){
                this.x = value;
            }
            if(name.equals("y")){
                this.y = value;
            }
            if(name.equals("z")){
                this.z = value;
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
    
}
