package me.sfclog.candlandcapturepoint;

import me.sfclog.candlandcapturepoint.command.CapturePointCommand;
import me.sfclog.candlandcapturepoint.event.PlayerEvent;
import me.sfclog.candlandcapturepoint.game.ArenaSystem;
import me.sfclog.candlandcapturepoint.region.CandyRegion;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {


    public static Plugin pl;
    public static CandyRegion region;
    public static ArenaSystem arena;

    public static World CANDYLAND;

    public static void sendlog(String up) {
        Bukkit.getConsoleSender().sendMessage(up);
    }

    @Override
    public void onEnable() {
        pl = this;
        CANDYLAND = Bukkit.getWorld("candyland");;
        region = new CandyRegion();
        arena = new ArenaSystem();
        getServer().getPluginManager().registerEvents(new PlayerEvent(),Main.pl);

        getServer().getPluginCommand("capturepoint").setExecutor(new CapturePointCommand());
        getServer().getPluginCommand("capturepoint").setTabCompleter(new CapturePointCommand());

        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.pl, new Runnable() {
         public void run() {
             arena.update();
           }
        },20,20);
    }


    public static CandyRegion getRegion() {
        return region;
    }
    public static ArenaSystem getArena() {
        return arena;
    }

    @Override
    public void onDisable() {
        if(arena != null) {
            arena.end();
        }
        pl = null;
    }
}
