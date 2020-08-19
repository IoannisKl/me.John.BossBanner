package me.John.BossBanner;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	
	public List<String> textLines;
	
	FileConfiguration config = getConfig();
	
	public Bar bar;
	
	public int textArraySize = this.getConfig().getStringList("texts").size();
	
	public int gettextArraySize() {
		return textArraySize;
	}

	public void settextArraySize(int textArraySize) {
		this.textArraySize = textArraySize;
	}
	
	public void setTextLines() {
		textLines = this.getConfig().getStringList("texts");
	}
	
	public List<String> getTextLines() {
		return textLines;
	}
	
	@Override
	public void onEnable(){
		config.addDefault("startTicks", 0);
		config.addDefault("repeatTicks", 20);
		config.addDefault("progressTime", 20);
		config.addDefault("text", "&a&lhello world");
		config.addDefault("barColor", BarColor.RED);
        config.options().copyDefaults(true);
        saveDefaultConfig();        
		this.getServer().getPluginManager().registerEvents(this, this);
		bar = new Bar(this);
		bar.createBar();
		
		if (Bukkit.getOnlinePlayers().size()>0) {
			for (Player on: Bukkit.getOnlinePlayers()) {
				bar.addPlayer(on);
			}
		}
	}

	@Override
	public void onDisable() {
		bar.getBar().removeAll();
		Bukkit.getScheduler().cancelTask(bar.getTaskID());
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		if(!bar.getBar().getPlayers().contains(event.getPlayer())) {
			bar.addPlayer(event.getPlayer());
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		if(!bar.getBar().getPlayers().contains(event.getPlayer())) {
			bar.RemovePlayer(event.getPlayer());
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("bossbar")) {
			if (args[0].equalsIgnoreCase("TextsReload")) {
				if (sender instanceof Player) {//player command
					Player player = (Player) sender;
					if (player.hasPermission("bossbar.admin")) {//has permission
						textLines = this.getConfig().getStringList("texts");
						player.sendMessage("config texts succesfully copied");
						return true;
					} else {// does not have permission
						player.sendMessage("You do not have appropriate permission to execute this command");
						return false;
					}
				}
			}
			else if (args[0].equalsIgnoreCase("disable")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					bar.RemovePlayer(player);
				}
			}
			else if (args[0].equalsIgnoreCase("enable")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					bar.addPlayer(player);
				}
			}
		}
		else {//console command
			return true;
		}
		return false;
	}
	
}
