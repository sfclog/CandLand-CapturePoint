package me.sfclog.candlandcapturepoint.damagemap;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class DamageMap {

    public static HashMap<Player,Integer> dame = new HashMap<Player, Integer>();


    public static boolean damage(Player p) {
        if (!dame.containsKey(p)) {
            dame.put(p,1);
        } else {
            int dam = dame.get(p) + 1;
            if(dam > 4) {
                dame.replace(p,1);
                return true;
            } else {
                dame.replace(p,dam);
            }
        }
        return false;
    }
}
