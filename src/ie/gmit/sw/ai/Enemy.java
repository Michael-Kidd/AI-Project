package ie.gmit.sw.ai;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.sourceforge.jFuzzyLogic.FIS;

public class Enemy{
	
	//keep enemy pos
	int[] pos = new int[2];
	char val;
	private FIS fis;

	public void go(){
		
		String fileName = "resources/fuzzy/logic.fcl";
		fis = FIS.load(fileName, true);
		
		if( fis == null )
		{
			System.err.println("Can't load file: '" + fileName + "'");
			return;
		}
		
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(moveable, 0, 1, TimeUnit.SECONDS);
	}
	
	//Multi-thread the enemies so they can move individually
	Runnable moveable = new Runnable() {
		
	    public void run() {
	    	
	    		fis.setVariable("distance", 10);
	    		fis.evaluate();
	    		
	    		System.out.println(fis.getVariable("accuracy").getValue());
	    		
	    		//Move the character and implement the logic
	    	
	    }
	    
	};

	Enemy(int[] p, char val){
		this.pos = p;
	}
	
	public int[] getPos() {
		return pos;
	}
	
	public void setPos(int[] pos) {
		this.pos = pos;
	}

}