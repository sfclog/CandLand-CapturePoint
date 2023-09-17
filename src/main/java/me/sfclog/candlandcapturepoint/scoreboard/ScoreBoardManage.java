package me.sfclog.candlandcapturepoint.scoreboard;


import fr.mrmicky.fastboard.FastBoard;
import me.sfclog.candlandcapturepoint.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.HashMap;


public class ScoreBoardManage {

    public static HashMap<Player, FastBoard> board = new HashMap<Player, FastBoard>();



    public static void update() {

        Bukkit.getOnlinePlayers().stream().filter(p -> p.getWorld().equals(Main.CANDYLAND)).forEach(p -> add(p));
        for (Player player : board.keySet()) {
            FastBoard b = board.get(player);
            if(player == null || player.getWorld() != Main.CANDYLAND) {
                b.delete();
                board.remove(player);
            }
        }

        board.values().forEach(p -> {
             p.updateTitle(Main.getArena().getTitle());
             p.updateLines(Main.getArena().getLines(p.getPlayer()));
        });
    }

    public static void add(Player p) {
        if(!board.containsKey(p)) {
            board.put(p,new FastBoard(p));
        }
    }
    public static void remove(Player p) {
        if(board.containsKey(p)) {
            board.remove(p);
        }
    }
}
