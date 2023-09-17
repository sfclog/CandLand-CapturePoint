package me.sfclog.candlandcapturepoint.region;

import me.sfclog.candlandcapturepoint.Main;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class CandyRegion {


    public Cuboid3D2 cubo;

    public Location red_team;
    public Location blue_team;
    public Location tnt_car;

    public Location red_hole;

    public List<Location> capture;

    public CandyRegion() {
        capture = new ArrayList<>();
        Location pos1 = new Location(Main.CANDYLAND,411 ,15 ,-883);
        Location pos2 = new Location(Main.CANDYLAND,-622 ,300 ,146);
        cubo = new Cuboid3D2(pos1,pos2);

        blue_team = new Location(Main.CANDYLAND,-38.499 ,78 ,-764.291,0.3f,2.5f);
        tnt_car = new Location(Main.CANDYLAND,-38.498 ,79 ,-758.594);
        red_team = new Location(Main.CANDYLAND,-3.564 ,79 ,-31.696,-179.6f,-1.0f);

        red_hole = new Location(Main.CANDYLAND,-3 ,77 ,-39);


        //capture spawn blue
        capture.add(new Location(Main.CANDYLAND,-300.573993, 82.000000, -507.786142));
        capture.add(new Location(Main.CANDYLAND,98.447672, 82.000000, -623.292866));
        capture.add(new Location(Main.CANDYLAND,-79.490120, 82.000000, -514.509877));
        capture.add(new Location(Main.CANDYLAND, -219.681501, 79.000000, -430.538655));
        capture.add(new Location(Main.CANDYLAND,179.505257, 81.000000, -456.585392));
        capture.add(new Location(Main.CANDYLAND,18.370376, 79.000000, -219.382188));
        capture.add(blue_team);


    }

    public Location findNearestLocation(Location car) {
        double minDistance = Double.MAX_VALUE;
        Location nearestLocation = null;
        for (Location location : capture) {
            double distance = location.distance(car);
            if (distance < minDistance) {
                minDistance = distance;
                nearestLocation = location;
            }
        }

        return nearestLocation;
    }

    public Location getRedSpawn() {
        return red_team;
    }

    public Location getBlueSpawn() {
        return blue_team;
    }

    public Location getRedHole() {
        return red_hole;
    }

    public boolean isIn(Player p) {
        return cubo.isIn(p);
    }


    public boolean inWorld(World world) {
        return Main.CANDYLAND == world;
    }

    public Location getSpawn() {
        return Main.CANDYLAND.getSpawnLocation();
    }

    public Location getTNT() {
        return tnt_car;
    }
}
