package ie.gmit.sw.ai;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.sourceforge.jFuzzyLogic.FIS;

public class Enemy implements Move{
	
	//keep enemy pos
	int[] pos = new int[2];
	char val;
	private FIS logic;
	
	Enemy(int[] p, char v){
		this.pos = p;
		this.val = v;
	}

	public void start(){
		
		//load fuzzy Rules
		String fileName = "resources/fuzzy/logic.fcl";
		
		logic = FIS.load(fileName, true);
		
		if( logic == null )
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
				
				//get players location
				x2 = GameView.getInstance().getCurrentRow();
				y2 = GameView.getInstance().getCurrentCol();
				
			} catch (Exception e) {
				return;
			}
    		
			//Using manhattan distance to determine how far each spider if from the player
    		int manhattan_distance =  Math.abs(pos[0] - x2) +  Math.abs(pos[1] - y2);
    	
    		//set the dstance variable in fuzzy logic to inform of manhattan distance to player
    		logic.setVariable("distance", manhattan_distance);
    		
    		logic.evaluate();
 
    		
			try {
				
				if(manhattan_distance > 5) {
					
		            boolean pathExists = new BfSearch().pathExists(GameView.getInstance().getMaze().getMaze(), pos);
		            
		            System.out.println(pathExists ? "YES" : "NO");
		    		
				}
	    		
			} catch (Exception e) {
				
				e.printStackTrace();
			
			}
	    }
	};
	
	public boolean movedown(int row, int col, char val) throws Exception {
		
		Maze maze = GameView.getInstance().getMaze();
		
		char[][] arr = maze.getMaze();
		
		if(pos[0] + 1 > 99) {
			
			return false;
		}
		
		if(arr[pos[0]+1][pos[1]] == '\u0020') {
			
			GameView.getInstance().setMaze(pos[0], pos[1], '\u0020');
			GameView.getInstance().setMaze(pos[0]+1, pos[1], val);
			
			pos[0] = pos[0] + 1;
			
			return true;
			
		}
		
		return false;
	}

	public boolean moveUp(int row, int col, char val) throws Exception {
		
		Maze maze = GameView.getInstance().getMaze();
		
		char[][] arr = maze.getMaze();
		
		if(pos[0] - 1 < 0) {
			
			return false;
		}
		
		if(arr[pos[0]-1][pos[1]] == '\u0020') {
			
			GameView.getInstance().setMaze(pos[0], pos[1], '\u0020');
			GameView.getInstance().setMaze(pos[0]-1, pos[1], val);
			
			pos[0] = pos[0] - 1;
			
			return true;
			
		}
		
		return false;
	}

	public boolean moveRight(int row, int col, char val) throws Exception {
		Maze maze = GameView.getInstance().getMaze();
		
		char[][] arr = maze.getMaze();
		
		if(pos[1] + 1 > 99) {
			
			return false;
		}
		
		if(arr[pos[0]][pos[1]+1] == '\u0020') {
			
			GameView.getInstance().setMaze(pos[0], pos[1], '\u0020');
			GameView.getInstance().setMaze(pos[0], pos[1]+1, val);
			
			pos[1] = pos[1]+1;
			
			return true;
			
		}
		
		return false;
	}


	public boolean moveLeft(int row, int col, char val) {
		
		return false;
	}
	
	public int[] getPos() {
		return pos;
	}
	
	public void setPos(int[] pos) {
		this.pos = pos;
	}

}