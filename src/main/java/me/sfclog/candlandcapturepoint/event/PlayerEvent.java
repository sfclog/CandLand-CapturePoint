package me.sfclog.candlandcapturepoint.event;

import me.sfclog.candlandcapturepoint.Main;
import me.sfclog.candlandcapturepoint.damagemap.DamageMap;
import me.sfclog.candlandcapturepoint.region.CandyRegion;
import me.sfclog.candlandcapturepoint.utils.Send;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class PlayerEvent implements Listener {


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        if(p.getGameMode() != GameMode.SURVIVAL) {
            return;
        }
        CandyRegion reg = Main.getRegion();
        if (reg != null) {
            if (!reg.inWorld(p.getWorld())) {
                return;
            }
            if (!reg.isIn(p)) {
                p.teleport(reg.getSpawn());
            }
        }
    }
    @EventHandler
    public void onEntityCombust(EntityCombustByEntityEvent event) {
        if (event.getEntityType() == EntityType.MINECART_TNT) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onEntityInteract(EntityInteractEvent event) {
        if (event.getEntity().getType() == EntityType.MINECART_TNT) {
            event.setCancelled(true); // Hủy sự kiện để ngăn chặn tương tác
        }
    }

    @EventHandler
    public void onRespawn(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if(p.getGameMode() != GameMode.SURVIVAL) {
            return;
        }
        CandyRegion reg = Main.getRegion();
        if (reg != null) {
            if (reg.inWorld(p.getWorld())) {
                String cmd = e.getMessage();
                if(!cmd.contains("spawn")
                        && !cmd.contains("shop")
                        && !cmd.contains("sui")
                        && !cmd.contains("pv")
                        && !cmd.contains("ec")) {
                    Send.send(p,"&aDùng lệnh &f/spawn &fđẻ thoát khỏi đấu trường");
                    e.setCancelled(true);
                }
            }
        }
    }
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.MINECART) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Creeper || event.getEntity() instanceof Minecart) {
            event.setCancelled(true);
        }
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damagedPlayer = (Player) event.getEntity();
            Player damagerPlayer = (Player) event.getDamager();
            CandyRegion reg = Main.getRegion();
            if (reg != null) {
                if(damagedPlayer.getWorld().equals(Main.CANDYLAND) && damagerPlayer.getWorld().equals(Main.CANDYLAND)) {
                   if(Main.arena.is_inred(damagerPlayer) && Main.arena.is_inred(damagedPlayer)) {
                       event.setCancelled(true);
                   } else if(Main.arena.is_inblue(damagerPlayer) && Main.arena.is_inblue(damagedPlayer)) {
                       event.setCancelled(true);
                   } else {
                       if(!DamageMap.damage(damagedPlayer)) {
                           event.setDamage(1);
                       }
                   }
                }
            }
        } else  if (event.getEntity() instanceof Player && event.getDamager() instanceof Arrow) {
            Player damagedPlayer = (Player) event.getEntity();
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player) {
                Player damagerPlayer = (Player) arrow.getShooter();
                if(damagedPlayer.getWorld().equals(Main.CANDYLAND) && damagerPlayer.getWorld().equals(Main.CANDYLAND)) {
                    if(Main.arena.is_inred(damagerPlayer) && Main.arena.is_inred(damagedPlayer)) {
                        event.setCancelled(true);
                    } else if(Main.arena.is_inblue(damagerPlayer) && Main.arena.is_inblue(damagedPlayer)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }


     @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        CandyRegion reg = Main.getRegion();

        if (reg != null) {
            if (!reg.inWorld(p.getWorld())) {
                return;
            }
            if (reg.isIn(p)) {

               if(Main.arena.is_inred(p)) {

                   Bukkit.getScheduler().runTaskLater(Main.pl, new Runnable() {
                       @Override
                       public void run() {
                           p.teleport(reg.getRedSpawn());
                       }
                   }, 30L);

               } else if(Main.arena.is_inblue(p)) {

                   Bukkit.getScheduler().runTaskLater(Main.pl, new Runnable() {
                       @Override
                       public void run() {
                           PotionEffect eff = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,50,50);
                           eff.apply(p);
                           p.teleport(reg.findNearestLocation(Main.arena.car.getLocation()));
                       }
                   }, 30L);
               }
            }
        }
    }
}
