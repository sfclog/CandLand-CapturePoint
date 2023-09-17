package me.sfclog.candlandcapturepoint.region;



import org.bukkit.Location;
import org.bukkit.entity.Player;


public class Cuboid3D2 {

    private final int xMin;
    private final int xMax;
    private final int yMin;
    private final int yMax;
    private final int zMin;
    private final int zMax;
    private Location basicloc;

    public Cuboid3D2(final Location point1, final Location point2) {
        this.xMin = Math.min(point1.getBlockX(), point2.getBlockX());
        this.xMax = Math.max(point1.getBlockX(), point2.getBlockX());
        this.yMin = Math.min(point1.getBlockY(), point2.getBlockY());
        this.yMax = Math.max(point1.getBlockY(), point2.getBlockY());
        this.zMin = Math.min(point1.getBlockZ(), point2.getBlockZ());
        this.zMax = Math.max(point1.getBlockZ(), point2.getBlockZ());
    }

    public void setBasicLoc(Location loc) {
        this.basicloc = loc;
    }

    public Location getBasicLoc() {
        return basicloc.clone().add(0.5, 0, 0.5);
    }

    public boolean isIn(final Location loc) {
      return loc.getBlockX() >= this.xMin && loc.getBlockX() <= this.xMax &&
              loc.getBlockY() >= this.yMin && loc.getBlockY() <= this.yMax &&
              loc.getBlockZ() >= this.zMin && loc.getBlockZ() <= this.zMax;
    }


    public boolean isIn(Player pp) {
       return isIn(pp.getLocation());
    }
}
