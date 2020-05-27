package me.prostedeni.goodcraft.getouttahere;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Random;

public final class GetOuttaHere extends JavaPlugin implements Listener {

    public ArrayList<String> plist = new ArrayList<>();

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent e){
        // Checking if the reason we are being kicked is a full server
        if (e.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            // If the condition above is true, we execute the following code, that is allow the player on the server
            if (e.getPlayer().hasPermission("getouttahere.stay")) {
                for(Player p : Bukkit.getOnlinePlayers()){
                    if (!(p.hasPermission("getouttahere.stay"))){
                        plist.add(p.getName());
                    }
                }
                int randomNumber = getRandomNumberInRange(1,plist.size());
                String theChosenOne = plist.get(randomNumber);
                Player p = Bukkit.getServer().getPlayer(theChosenOne);
                p.kickPlayer(getConfig().getString("Message:"));
                e.allow();
            }
        }
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);

        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();
    }
}
