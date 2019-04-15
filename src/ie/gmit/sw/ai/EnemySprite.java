package ie.gmit.sw.ai;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EnemySprite extends Sprite{
	public EnemySprite(String name, int frames, String... images) throws Exception{
		super(name, frames, images);
		move();
	}
	
	public void move(){
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