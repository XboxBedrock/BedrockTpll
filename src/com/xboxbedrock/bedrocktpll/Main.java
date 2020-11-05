package com.xboxbedrock.bedrocktpll;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import me.elgamer.vanillatpll.projections.ModifiedAirocean;

public class Main extends PluginBase{
    @Override
    public void onLoad() {
        this.getLogger().info(TextFormat.WHITE + "I've been loaded!");
    }
    @Override
    public void onEnable() {
        this.getLogger().info(TextFormat.DARK_GREEN + "I've been enabled!");
    }
    @Override
    public void onDisable() {
        this.getLogger().info(TextFormat.DARK_RED + "I've been disabled!");
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        //Check is command sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage("&cYou cannot add a player to a region!");
            return true;
        }

        //Convert sender to player
        Player p = (Player) sender;

        if (!(p.hasPermission("bedrocktpll.command.tpll"))) {
            p.sendMessage(TextFormat.RED + "You do not have permission for this command!");
            return true;
        }

        if(args.length==0) {
            return false;
        }

        String[] splitCoords = args[0].split(",");

        if(splitCoords.length==2&&args.length<3) { // lat and long in single arg
            args = splitCoords;
        }

        if(args[0].endsWith(",")) {
            args[0] = args[0].substring(0, args[0].length() - 1);
        }

        if(args.length>1&&args[1].endsWith(",")) {
            args[1] = args[1].substring(0, args[1].length() - 1);
        }

        if(args.length!=2&&args.length!=3) {
            return false;
        }

        double lon, lat;

        try {
            lat = Double.parseDouble(args[0]);
            lon = Double.parseDouble(args[1]);
        } catch(Exception e) {
            return false;
        }

        if (lat>90 || lat<-90) {
            p.sendMessage(TextFormat.RED + "Latitude is out of bounds, keep it between -90 and 90");
            return true;
        }

        if (lon>180 || lon<-180) {
            p.sendMessage(TextFormat.RED + "Longitude is out of bounds, keep it between -180 and 180");
            return true;
        }

        ModifiedAirocean projection = new ModifiedAirocean();

        double proj[] = projection.fromGeo(lon, lat);
        int longint=(int) Math.round(proj[1]);
        int latint=(int) Math.round(proj[0]);







        if (p.level.getHighestBlockAt(latint, longint) == 0) {
            p.sendMessage(TextFormat.RED + "This location is above the void, you may not teleport here!");
            return true;
        }

        p.teleport(p.level.getBlock(latint, p.level.getHighestBlockAt(latint, longint), longint));
        p.sendMessage("Teleported " + p.getName() + " to " + latint + ", " + p.level.getHighestBlockAt(latint, longint) + ", " + longint);

        return true;
    }
}
