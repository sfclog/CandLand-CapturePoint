package me.sfclog.candlandcapturepoint.utils;

import org.bukkit.entity.Player;

public class Send {

    public static void send(Player p , String msg) {
        p.sendMessage(Color.tran("&8(&dCapturePoint&8) &r" + msg));
    }
}
