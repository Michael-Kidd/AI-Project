package ie.gmit.sw.ai;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Enemy{
	
	//keep enemy pos
	int[] pos = new int[2];
	
	Enemy(int[] p){
		this.pos = p;
	}
	
	public int[] getPos() {
		return pos;
	}

	public void setPos(int[] pos) {
		this.pos = pos;
	}

	public void go(){
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(moveable, 0, 1, TimeUnit.SECONDS);
	}
	
	//Multi-thread the enemies so they can move individually
	Runnable moveable = new Runnable() {
		
	    public void run() {
	        System.out.println("move");
	    }
	    
	};

}