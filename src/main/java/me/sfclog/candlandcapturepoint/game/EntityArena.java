package me.sfclog.candlandcapturepoint.game;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;

public class EntityArena {


    public static Minecart summon_tnt_car(Location loc) {
        Minecart car = (Minecart) loc.getWorld().spawnEntity(loc, EntityType.MINECART);
        Creeper creeper = (Creeper) loc.getWorld().spawnEntity(loc, EntityType.CREEPER);
        creeper.setMaxFuseTicks(Integer.MAX_VALUE);
        creeper.setExplosionRadius(0);
        creeper.setPowered(false);
        car.setPassenger(creeper);
        return car;

    }

}
