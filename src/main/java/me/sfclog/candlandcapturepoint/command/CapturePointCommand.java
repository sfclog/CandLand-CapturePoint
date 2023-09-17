package me.sfclog.candlandcapturepoint.command;

import me.sfclog.candlandcapturepoint.Main;
import me.sfclog.candlandcapturepoint.fakearmor.FakeArmor;
import me.sfclog.candlandcapturepoint.utils.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CapturePointCommand implements CommandExecutor , TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if(p.isOp() || p.hasPermission("*")) {
                if (strings.length < 1) {
                    send(commandSender, " ");
                    send(commandSender, "&#fb3659&lＣ&#fb3c5e&lＡ&#fc4162&lＰ&#fc4767&lＴ&#fc4c6c&lＵ&#fd5270&lＲ&#fd5775&lＥ &f| &eMinigame by SkyCandy");
                    send(commandSender, " ");
                    send(commandSender, " &f/capturepoint start &aĐể bắt đầu đấu trường");
                    send(commandSender, " &f/capturepoint stop &aĐể dừng đấu trường");
                    send(commandSender, " ");
                } else if(strings[0].equalsIgnoreCase("start")) {
                    if(Main.arena.start_arena()) {
                        send(commandSender,"&aBắt đầu đấu trường");
                    } else {
                        send(commandSender,"&cĐấu trường đã bắt đầu");
                    }
                } else if(strings[0].equalsIgnoreCase("stop")) {
                    if(Main.arena.stop_arena()) {
                        send(commandSender,"&aĐã dừng đấu trường");
                    } else {
                        send(commandSender,"&cĐấu trường chưa bắt đầu");
                    }
                }

            } else {
                send(commandSender, "&cLỗi &fBạn không có quyền lầm điều này");
            }

        }
        return false;
    }

    public static void send(CommandSender send,String msg) {
        send.sendMessage(Color.tran(msg));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> cmd = new ArrayList<>();
        cmd.add("start");
        cmd.add("stop");
        return cmd;
    }
}
