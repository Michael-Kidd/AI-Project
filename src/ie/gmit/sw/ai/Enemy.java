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
	
	Enemy(int[] p, char v){
		this.pos = p;
		this.val = v;
	}
	

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
	    	
	    		int x2 = 0;
	    		int y2 = 0;
	    		
				try {
					
					x2 = GameView.getInstance().getCurrentRow();
					y2 = GameView.getInstance().getCurrentCol();
					
				} catch (Exception e) {
					return;
				}
	    		
				//Using manhattan distance to determine how far each spider if from the player
	    		int manhattan_distance =  Math.abs(pos[0] - x2) +  Math.abs(pos[1] - y2);
	    	
	    		fis.setVariable("distance", manhattan_distance);
	    		fis.evaluate();
	    		
	    		//System.out.println(fis.getVariable("accuracy").getValue());
	    		
	    		if(manhattan_distance < 5 && manhattan_distance > -5) {
	    			
		    		System.out.println(val +" Spider near you " + fis.getVariable("accuracy").getValue());
		    		
		    		
	    		}
	    		
	    		//Move the character and implement the logic
	    	
	    }
	    
	};
	
	public int[] getPos() {
		return pos;
	}
	
	public void setPos(int[] pos) {
		this.pos = pos;
	}

}