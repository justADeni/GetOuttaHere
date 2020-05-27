package me.prostedeni.goodcraft.getouttahere;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public final class GetOuttaHere extends JavaPlugin implements Listener {

    public ArrayList<String> plist = new ArrayList<>();

    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent e){
        // Checking if the reason we are being kicked is a full server
        if (e.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            // If the condition above is true, we execute the following code, that is allow the player on the server
            if (e.getPlayer().hasPermission("getouttahere.stay")) {
                if (plist.size() > 0) {
                            int randomNumber = ThreadLocalRandom.current().nextInt(0, plist.size());
                            String theChosenOne = plist.get(randomNumber);
                            Player p = Bukkit.getServer().getPlayer(theChosenOne);
                            String mess1 = getConfig().getString("KickMessage");
                            String msgcolor1 = (ChatColor.translateAlternateColorCodes('&',mess1));
                            p.kickPlayer(msgcolor1);
                            e.allow();
            } else if (plist.size() == 0){
                            String mess2 = getConfig().getString("NoKickMessage");
                            String msgcolor2 = (ChatColor.translateAlternateColorCodes('&',mess2));
                            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, msgcolor2);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        if (!(e.getPlayer().hasPermission("getouttahere.stay"))){
            if (!(plist.contains(e.getPlayer().getName()))) {
                plist.add(e.getPlayer().getName());
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        if (plist.contains(e.getPlayer().getName())){
            plist.remove(e.getPlayer().getName());
        }
    }

    @EventHandler
    public void onPlayerKicked(PlayerKickEvent e){
        if (plist.contains(e.getPlayer().getName())){
            plist.remove(e.getPlayer().getName());
        }
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);

        saveDefaultConfig();

        for(Player p : Bukkit.getOnlinePlayers()){
            if (!(p.hasPermission("getouttahere.stay"))){
                plist.add(p.getName());
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();
    }
}
