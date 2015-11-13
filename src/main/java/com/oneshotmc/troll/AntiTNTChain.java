package com.oneshotmc.troll;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotId;

@SuppressWarnings("deprecation")
public class AntiTNTChain extends JavaPlugin implements Listener {

	PlotAPI api = new PlotAPI();
	FileConfiguration config;
	int tickDur;
	int maxAmount;
	public HashMap<PlotId, PlotScore> scores = new HashMap<PlotId, PlotScore>();

	public void onEnable() {
		
		this.getServer().getPluginManager().registerEvents(this, this);
		this.config = this.getConfig();
		config.options().copyDefaults(true);
		maxAmount = config.getInt("maxTNTChains");
		tickDur = config.getInt("clearDurationTicks");
		new Run(this);
		this.saveDefaultConfig();
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onExplode(EntityExplodeEvent e) {
		List<Block> blockList = e.blockList();
		Location loc = e.getLocation();
		Plot plot = api.getPlot(loc);
		if(plot == null)return;
		PlotId id = plot.getId();
		Iterator<Block> blocks = blockList.iterator();
		while (blocks.hasNext()) {
			Block block = blocks.next();
			if (block.getType() == Material.TNT) {
				PlotScore score = scores.get(id);
				if (score == null) {
					scores.put(id, new PlotScore(plot));
					score = scores.get(id);
				}
				score.addOne();
				int amount=score.getAmount();
				if (amount > maxAmount) {
					if(amount == maxAmount+1){
					ChatUtil.sendMessage(plot, "You have reached your max TNT chain amount!", ChatType.WARNING);
					}
					blocks.remove();
				}
			}
		}
	}

	public class Run extends BukkitRunnable {

		public Run(Plugin plugin) {
			this.runTaskTimer(plugin, tickDur, tickDur);
		}

		public void run() {
			for(PlotScore score : scores.values()){
				score.clear();
			}
		}

	}
}
