package com.oneshotmc.troll;

import com.intellectualcrafters.plot.object.Plot;

public class PlotScore {
	int amount=0;
	Plot plot;
	public PlotScore(Plot plot){
		this.plot=plot;
	}
	
	public int addOne(){
		return amount++;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Plot getPlot() {
		return plot;
	}

	public void setPlot(Plot plot) {
		this.plot = plot;
	}
	
	public void clear(){
		amount=0;
	}
	
	
}
