package me.John.BossBanner;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class Bar{
		
	private int taskID = -1;
	private final Main plugin;
	private BossBar bar;
   
	public Bar(Main plugin) {
		this.plugin=plugin;
	}
		
	public void addPlayer(Player player) {
		bar.addPlayer(player);
	}
	
	public void RemovePlayer(Player player) {
		bar.removePlayer(player);
	}
	
	public 	BossBar getBar() {
		return bar;
	}
	
	public void createBar(){
		bar=Bukkit.createBossBar(format(plugin.getConfig().getString("text")), (BarColor) plugin.getConfig().get("barColor") ,BarStyle.SOLID);
		bar.setVisible(true);
		cast();
	}
		
			
	public void cast() {
        Runnable runnable = new Runnable() {
        	        	
        	int textsSize = plugin.getConfig().getStringList("texts").size();
        	int indexNum = 0;
			double progress = 1.0;//how full the bar is
			double time = 1.0/(plugin.getConfig().getInt("progressTime"));//amount of time it takes to change the progress on the bar 
			public List<String> textLines = plugin.getConfig().getStringList("texts");
			boolean pluginNull= plugin==null;

			
			@Override
			public void run() {
				
				bar.setProgress(progress);
				bar.setColor((BarColor) plugin.getConfig().get("barColor"));
				Logger.getLogger("logger").log(Level.INFO, "message length is " + textLines.size());
				Logger.getLogger("logger").log(Level.INFO, "plugin is null " + pluginNull);
				
				/*switch(indexNum) { //in these cases we add the different bars and texts. Change the texts here if you like.
				case 0 :
					bar.setColor((BarColor) plugin.getConfig().get("barColor"));
					bar.setTitle(format(plugin.getConfig().getStringList("texts").get(textsIndex)));
					textsIndex++;
					break;
				case 1 :
					bar.setColor((BarColor) plugin.getConfig().get("barColor"));
					bar.setTitle(format(plugin.getConfig().getStringList("texts").get(textsIndex)));
					textsIndex++;
					break;
				case 2 : 
					default:
					bar.setColor((BarColor) plugin.getConfig().get("barColor"));
					bar.setTitle(format(plugin.getConfig().getStringList("texts").get(textsIndex)));
					textsIndex=0;
					indexNum = -1;
					break;
				}*///taken out. this was the original logic, but the integration of a config file made this obsolete.
				
				progress = progress - time;//how fast the bar empties. depends on the value of time. After it empties we go over to the next one
				if (progress <=0) {
					if(indexNum<textsSize) {
						indexNum++;
					}
					else { 
					indexNum=0;
					progress = 1.0;
					}
				}//if bar empties
			  }
			
		};
		setTaskID((Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, runnable, plugin.getConfig().getLong("startTicks"), plugin.getConfig().getLong("repeatTicks"))));
		
	} 

	private String format(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}


}
