package me.prostedeni.goodcraft.getouttahere;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public final class GetOuttaHere extends JavaPlugin implements Listener {

    public static boolean nullconfig;

    public ArrayList<String> plist = new ArrayList<>();

    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent e){
        // Checking if the reason we are being kicked is a full server
        if (e.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            // If the condition above is true, we execute the following code, that is allow the player on the server
            if (e.getPlayer().hasPermission("getouttahere.stay")) {
                if (nullconfig) {
                    System.out.println("Something went wrong while retrieving config, disabling the plugin");
                    getServer().getPluginManager().disablePlugin(this);
                } else {
                    if (plist.size() > 0) {
                        int randomNumber = ThreadLocalRandom.current().nextInt(0, plist.size());
                        String theChosenOne = plist.get(randomNumber);
                        Player p = Bukkit.getServer().getPlayer(theChosenOne);
                        if (!(getConfig().getBoolean("IgnoreFull"))) {
                            p.kickPlayer(ChatColor.translateAlternateColorCodes('&', getConfig().getString("KickMessage")));
                        }
                        e.allow();
                    } else if (plist.size() == 0) {
                        if (!(getConfig().getBoolean("IgnoreFull"))) {
                            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&', getConfig().getString("NoKickMessage")));
                        } else if (getConfig().getBoolean("IgnoreFull")){
                            e.allow();
                        }
                    }
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

        this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {
                if ((getConfig().getString("KickMessage") == null) || (getConfig().getString("NoKickMessage") == null)){
                    nullconfig = true;
                } else {
                    nullconfig = false;
                }
            }
        }, 15L);
        //waiting 15 ticks before starting to operate with config to make sure
        //it won't disable itself out of lag or not yet generated config

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        reloadConfig();
        getConfig();
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            if (sender.hasPermission("getouttahere.admin")){
                if (command.getName().equals("getouttahere")){
                    if (args.length == 0){
                        sender.sendMessage(ChatColor.DARK_RED + "No arguments detected");
                    } else if (args.length == 1){
                        if (args[0].equalsIgnoreCase("reload")) {
                            reloadConfig();
                            getConfig();
                            saveConfig();
                            sender.sendMessage(ChatColor.DARK_GREEN + "Config reloaded");
                        } else if (!(args[0].equalsIgnoreCase("reload"))){
                            sender.sendMessage(ChatColor.DARK_RED + "Invalid arguments");
                        }
                    } else if (args.length > 1){
                        sender.sendMessage(ChatColor.DARK_RED + "Invalid arguments");
                    }
                }
            }
        } else {
            if (command.getName().equals("getouttahere")){
                if (args.length == 0){
                    sender.sendMessage(ChatColor.DARK_RED + "No arguments detected");
                } else if (args.length == 1){
                    if (args[0].equalsIgnoreCase("reload")) {
                        reloadConfig();
                        getConfig();
                        saveConfig();
                        sender.sendMessage(ChatColor.DARK_GREEN + "Config reloaded");
                    } else if (!(args[0].equalsIgnoreCase("reload"))){
                        sender.sendMessage(ChatColor.DARK_RED + "Invalid arguments");
                    }
                } else if (args.length > 1){
                    sender.sendMessage(ChatColor.DARK_RED + "Invalid arguments");
                }
            }
        }
        return false;
    }
}
