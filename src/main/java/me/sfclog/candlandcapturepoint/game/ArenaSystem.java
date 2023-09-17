package me.sfclog.candlandcapturepoint.game;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.sfclog.candlandcapturepoint.Main;
import me.sfclog.candlandcapturepoint.fakearmor.FakeArmor;
import me.sfclog.candlandcapturepoint.scoreboard.ScoreBoardManage;
import me.sfclog.candlandcapturepoint.utils.Color;
import me.sfclog.candlandcapturepoint.utils.TimeUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArenaSystem {

    public Status status;
      public static ArrayList<Player> all;
    public static ArrayList<Player> red_team;
    public static ArrayList<Player> blue_team;

    //start
    public boolean start = false;

    //time
    public int waittime = 20;

    public int arenatime = 1200;

    //mine car
    public Minecart car;
    public Hologram hologram;
    public String holouuid;

    public BukkitTask xetnt;

    public boolean car_is_run;

    public ArenaSystem() {
        status = Status.WAIT;
        red_team = new ArrayList<>();
        blue_team = new ArrayList<>();
        all = new ArrayList<>();
        Main.CANDYLAND.getPlayers().forEach(p -> {
            p.teleport(Main.CANDYLAND.getSpawnLocation());
            FakeArmor.reset(p);
        });
        Main.CANDYLAND.getEntities().stream().filter(e -> e instanceof Minecart || e instanceof Creeper).forEach(a -> a.remove());
        xetnt = null;
        holouuid = UUID.randomUUID().toString();
        hologram = null;
        car_is_run = false;

    }
    public void end() {
        if(xetnt != null) xetnt.cancel();
        if(hologram != null) DHAPI.removeHologram(holouuid);
        Main.CANDYLAND.getEntities().stream().filter(e -> e instanceof Minecart || e instanceof Creeper).forEach(a -> a.remove());

    }
    public void reset() {
        Bukkit.getScheduler().runTask(Main.pl, () -> {
        start = false;
        //stop bukkit runnable
        if(xetnt != null) xetnt.cancel();
        if(hologram != null) DHAPI.removeHologram(holouuid);
        hologram = null;
        xetnt = null;
        status = Status.WAIT;
        waittime = 20;
        arenatime = 1200;
        holouuid = UUID.randomUUID().toString();
        Main.CANDYLAND.getPlayers().forEach(p -> {
            p.teleport(Main.CANDYLAND.getSpawnLocation());
            FakeArmor.reset(p);
        });
        red_team.clear();
        blue_team.clear();
        all.clear();
        Main.CANDYLAND.getEntities().stream().filter(e -> e instanceof Minecart || e instanceof Creeper).forEach(a -> a.remove());
     });
    }

    public boolean is_start() {
        return start;
    }

    public boolean start_arena() {
        if(!is_start()) {
            start = true;
            return true;
        }
        return false;
    }
    public boolean stop_arena() {
        if(is_start()) {
            reset();
            return true;
        }
        return false;
    }

    public boolean is_inred(Player p) {
        return red_team.contains(p);
    }
    public boolean is_inblue(Player p) {
        return blue_team.contains(p);
    }

    public void balanceTeams() {
        List<Player> allPlayers = Main.CANDYLAND.getPlayers();
        List<Player> unassignedPlayers = new ArrayList<>(allPlayers);

        double totalRedMaxHealth = calculateTotalMaxHealth(red_team);
        double totalBlueMaxHealth = calculateTotalMaxHealth(blue_team);

        boolean addToRed = totalRedMaxHealth <= totalBlueMaxHealth;

        for (Player player : unassignedPlayers) {
            if (addToRed) {
                red_team.add(player);
                player.teleport(Main.getRegion().getRedSpawn());
                FakeArmor.send_fake(player, org.bukkit.Color.fromRGB(255, 0, 0));
                totalRedMaxHealth += player.getMaxHealth();
            } else {
                blue_team.add(player);
                player.teleport(Main.getRegion().getBlueSpawn());
                FakeArmor.send_fake(player, org.bukkit.Color.fromRGB(0, 0, 255));
                totalBlueMaxHealth += player.getMaxHealth();
            }

            addToRed = !addToRed;
        }
    }

    private double calculateTotalMaxHealth(List<Player> players) {
        double totalMaxHealth = 0;
        for (Player player : players) {
            totalMaxHealth += player.getMaxHealth();
        }
        return totalMaxHealth;
    }


    public void update() {
        ScoreBoardManage.update();

        if(start) {
            if (!(waittime <= 0)) {
                waittime--;
                if (status == Status.WAIT) {
                    status = Status.WAITSTART;
                    send_boastcast("&eĐấu trường chuẩn bị bắt đầu", Sound.BLOCK_NOTE_BLOCK_BIT);
                }
                if (waittime <= 5) {
                    send_boastcast("&aĐấu trường bắt đầu sau &f" + waittime, Sound.BLOCK_NOTE_BLOCK_BIT);
                }
            } else {
                if (status == Status.WAITSTART) {
                    status = Status.START;
                    Bukkit.getScheduler().runTask(Main.pl, new Runnable() {
                        @Override
                        public void run() {

                            balanceTeams();

                            car = EntityArena.summon_tnt_car(Main.getRegion().getTNT());
                            hologram = DHAPI.createHologram(holouuid, car.getLocation().clone().add(0, 3, 0));

                            xetnt = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    long range = getRange();
                                    List<Player> p = getPlayerNear();
                                    car_is_run = p.size() == 0 ? false : true;
                                    double speed = p.size() == 0 ? 0 : p.size() * 0.05;

                                    List<String> holo = new ArrayList<>();
                                    holo.add("&9&lXE &c&lTNT");
                                    holo.add(" &fQ.Đường: &b" + range + " Km");
                                    holo.add(" &fTốc Độ: &c" + speed);
                                    holo.add(" &fNgười Đẩy: &d" + p.size());
                                    DHAPI.setHologramLines(hologram,holo);
                                    DHAPI.moveHologram(hologram, car.getLocation().clone().add(0,3,0));

                                    if (!p.isEmpty()) {
                                        Vector carVelocity = car.getVelocity();

                                        if (carVelocity.lengthSquared() > 0.0) { // Check for non-zero vector
                                            Vector horizontalDirection = carVelocity.clone().setY(0).normalize();
                                            car.setVelocity(horizontalDirection.multiply(speed));
                                        } else {
                                            car.setVelocity(new Vector(0, 0, 0));
                                        }
                                    } else {
                                        car.setVelocity(new Vector(0, 0, 0));
                                    }

                                    if(range <=5) {
                                        reset();
                                        setWin_Blue();
                                        this.cancel();
                                    }
                                }
                            }.runTaskTimer(Main.pl, 1, 1);;
                        }
                    });
                }
                if(!(arenatime <= 0)) {

                    if(!car_is_run) {
                        arenatime--;
                    }

                    red_team.forEach(p ->  FakeArmor.send_fake(p, org.bukkit.Color.fromRGB(255, 0, 0)));
                    blue_team.forEach(p ->  FakeArmor.send_fake(p, org.bukkit.Color.fromRGB(0, 0, 255)));

                    Main.CANDYLAND.getPlayers().forEach(p -> {
                        String bar = Color.tran("&aVị trí &cXe TNT &acách &f" + getRange(p) + " Khối");
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(bar));
                    });

                } else {
                    setWin_Red();
                    reset();
                }
            }
        }
    }

    private void setWin_Red() {
        List<String> pl = new ArrayList<>();
        red_team.forEach(p -> pl.add(p.getName()));
        send_boastcast(" ");
        send_boastcast(" &e&lChúc Mừng! &fĐội &c&lRED &fĐã giành chiến thắng");
        send_boastcast(" ");
        send_boastcast(" &fCác thành viên tham dự::");
        send_boastcast(" ");
        send_boastcast(" &d" + pl);
        send_boastcast(" ");
    }
    private void setWin_Blue() {
        List<String> pl = new ArrayList<>();
        blue_team.forEach(p -> pl.add(p.getName()));
        send_boastcast(" ");
        send_boastcast(" &e&lChúc Mừng! &fĐội &9&lBLUE &fĐã giành chiến thắng");
        send_boastcast(" ");
        send_boastcast(" &fCác thành viên tham dự::");
        send_boastcast(" ");
        send_boastcast(" &d" + pl);
        send_boastcast(" ");
    }


    public String getTitle() {
        return Color.tran("&#fb3659&lＣ&#fb3c5e&lＡ&#fc4162&lＰ&#fc4767&lＴ&#fc4c6c&lＵ&#fd5270&lＲ&#fd5775&lＥ");
    }

    public List<Player> getPlayerNear() {
        List<Player> pl = new ArrayList<>();
        car.getNearbyEntities(8,8,8).forEach(e -> {
            if(e instanceof Player) {
                Player p = (Player) e;
                if(is_inblue(p)) {
                    pl.add(p);
                }
            }
        });
        return pl;
    }
    public void send_boastcast(String msg,Sound s) {
        Main.CANDYLAND.getPlayers().forEach(p -> {
            p.sendMessage(Color.tran("&8(&dCapturePoint&8) &r" + msg));
            p.playSound(p.getLocation(),s,50,1);
        });
    }

    public void send_boastcast(String msg) {
        Main.CANDYLAND.getPlayers().forEach(p -> {
            p.sendMessage(Color.tran(msg));
        });
    }
    public List<String> getLines(Player p) {

        if(status == Status.WAIT) {
            List<String> list = new ArrayList<>();
            list.add(" ");
            list.add("&fThời Gian:");
            list.add(" &d" + TimeUtil.gettime(waittime));
            list.add(" ");
            list.add("&fNgười Chơi:");
            list.add(" &b" + Main.CANDYLAND.getPlayers().size());
            list.add(" ");
            list.add("&fTrạng Thái:");
            list.add(" &aĐang chờ...");
            list.add(" ");
            list.add(" &aᴘʟᴀʏ.sɪᴍᴘᴍᴄ.ɴᴇᴛ");
            list.replaceAll(a -> Color.tran(a));
            return list;

        } else if(status == Status.WAITSTART) {
            List<String> list = new ArrayList<>();
            list.add(" ");
            list.add("&fThời Gian:");
            list.add(" &d" + TimeUtil.gettime(waittime));
            list.add(" ");
            list.add("&fNgười Chơi:");
            list.add(" &b" + Main.CANDYLAND.getPlayers().size());
            list.add(" ");
            list.add("&fTrạng Thái:");
            list.add(" &eChuẩn bị...");
            list.add(" ");
            list.add(" &aᴘʟᴀʏ.sɪᴍᴘᴍᴄ.ɴᴇᴛ");
            list.replaceAll(a -> Color.tran(a));
            return list;
        }else if(status == Status.START) {

            double range = Main.getRegion().tnt_car.distance(Main.getRegion().red_hole);
            double tnt = car.getLocation().distance(Main.getRegion().red_hole);

            double done = (tnt / range) * 100;



            String redcheck = is_inred(p) ? " &7(Bạn)" : "";
            String bluecheck = is_inblue(p) ? " &7(Bạn)" : "";

            List<String> list = new ArrayList<>();
            list.add(" ");
            list.add("&fThời Gian:");
            list.add(" &d" + TimeUtil.gettime(arenatime));
            list.add(" ");
            list.add(" &c&l▉ &e" + red_team.size() + redcheck);
            list.add(" &9&l▉ &e" + blue_team.size() + bluecheck);
            list.add(" ");
            list.add("&fVị Trí TNT:");
            list.add(" &e" + getRange(p) + " &7Khối");
            list.add(" ");
            list.add("&fQ.Đường TNT:");
            list.add(" &cCòn &e" + getRange() + " Km");
            list.add(" &8[" + getProgressBar(done) + "&8]");
            list.add(" ");
            list.add(" &aᴘʟᴀʏ.sɪᴍᴘᴍᴄ.ɴᴇᴛ");
            list.replaceAll(a -> Color.tran(a));
            return list;
        }
        return new ArrayList<>();
    }

    private String getProgressBar(double percent) {
        int progressBarLength = 35; // Độ dài của ProgressBar (số biểu tượng)
        int progress = (int) Math.ceil(progressBarLength * (percent / 100.0));

        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < progressBarLength; i++) {
            if (i < progress) {
                bar.append(ChatColor.RED + "|");
            } else {
                bar.append(ChatColor.BLUE + "|");
            }
        }
        return bar.toString();
    }

    public long getRange() {
        Location hole = Main.getRegion().getRedHole();
        if(car != null && car.getLocation() != null) {
            return Math.round(car.getLocation().distance(hole));
        }
        return 0;
    }

    public long getRange(Player p) {
        if(car != null && car.getLocation() != null) {
            return Math.round(car.getLocation().distance(p.getLocation()));
        }
        return 0;
    }
}
