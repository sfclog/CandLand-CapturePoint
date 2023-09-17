package me.sfclog.candlandcapturepoint.fakearmor;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

public class FakeArmor {

    public static void reset(Player p) {
        PacketContainer packetOthers = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
        packetOthers.getIntegers().write(0, p.getEntityId());

        List<Pair<EnumWrappers.ItemSlot, ItemStack>> pairList = new ArrayList<>();
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, p.getInventory().getHelmet()));
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.CHEST,p.getInventory().getChestplate()));
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.LEGS, p.getInventory().getLeggings()));
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.FEET, p.getInventory().getBoots()));
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.MAINHAND, p.getInventory().getItemInMainHand().clone()));
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.OFFHAND, p.getInventory().getItemInOffHand().clone()));

        packetOthers.getSlotStackPairLists().write(0, pairList);

        Bukkit.getOnlinePlayers().stream().filter(a -> a != p).forEach(b -> {
            ProtocolLibrary.getProtocolManager().sendServerPacket(b, packetOthers);
        });
    }




    public static void send_fake(Player p,Color color) {
        PacketContainer packetOthers = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
        packetOthers.getIntegers().write(0, p.getEntityId());

        List<Pair<EnumWrappers.ItemSlot, ItemStack>> pairList = new ArrayList<>();
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, color.equals(org.bukkit.Color.fromRGB(255, 0, 0)) ? new ItemStack(Material.RED_BANNER) :  new ItemStack(Material.BLUE_BANNER)));
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.CHEST, getItem(Material.LEATHER_CHESTPLATE, color)));
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.LEGS, getItem(Material.LEATHER_LEGGINGS, color)));
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.FEET, getItem(Material.LEATHER_BOOTS, color)));
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.MAINHAND, p.getInventory().getItemInMainHand().clone()));
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.OFFHAND, p.getInventory().getItemInMainHand().clone()));

        packetOthers.getSlotStackPairLists().write(0, pairList);

        Bukkit.getOnlinePlayers().stream().filter(a -> a != p).forEach(b -> {
            ProtocolLibrary.getProtocolManager().sendServerPacket(b, packetOthers);
        });
    }


    public static ItemStack getItem(Material m, Color color) {
        ItemStack item =  new ItemStack(m);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(color);
        item.setItemMeta(meta);
        return item;
    }
}
