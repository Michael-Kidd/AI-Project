package ie.gmit.sw.ai;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import net.sourceforge.jFuzzyLogic.FIS;

public class EnemyThread extends Thread{

	static ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
	
	//keep enemy pos
	private int[] pos = new int[2];
	private char val;
	private FIS logic;
	private String fileName = "resources/fuzzy/logic.fcl";
	
	EnemyThread(int[] p, char v){
		
		try {
			
			fuzzyLogic();
			
		} catch (Exception e1) {
			
			e1.printStackTrace();
		
		}
		
		this.pos = p;
		this.val= v;
		
		try {
			
			GameView.getInstance();
			
			GameView.getMaze();
			
			char[][] maze = deepCopy(Maze.getMaze());
		
			List<Node> test = new Search().getPath(maze, pos[0], pos[1]);
			
			if(test.size() != 0)
			 System.out.println(test.size());
		
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
			
	}
        
	
	public void move(int row, int col, int newRow, int newCol, char val) throws Exception {
		System.out.println("moved");
		GameView.getInstance();
		GameView.setMaze(pos[0], pos[1], '\u0020');
		GameView.setMaze(newRow, newCol, val);
	}
	
	public int[] getPos() {
		return pos;
	}
	
	public void setPos(int[] pos) {
		this.pos = pos;
	}
	
	public static char[][] deepCopy(char[][] original) {
		
	    if (original == null) {
	        return null;
	    }

	    final char[][] result = new char[original.length][];
	    
	    for (int i = 0; i < original.length; i++) {
	    	
	        result[i] = Arrays.copyOf(original[i], original[i].length);

	    }
	    
	    return result;
	}

	public char getVal() {
		return val;
	}


	public void setVal(char val) {
		this.val = val;
	}
	
	void fuzzyLogic() throws Exception {
		
		logic = FIS.load(fileName, true);
		
		if( logic == null )
		{
			System.err.println("Can't load file: '" + fileName + "'");
			return;
		}
		
		int manhattan_distance = getDistance();
		
		//set the distance variable in fuzzy logic to inform of manhattan distance to player
		logic.setVariable("distance", manhattan_distance);
		logic.evaluate();
		
	}
	
	int getDistance() throws Exception {	
		int x2 = 0;
		int y2 = 0;
		
		GameView.getInstance();
		
		//get players location
		x2 = GameView.getCurrentRow();
		y2 = GameView.getCurrentCol();
		
		//Using manhattan distance to determine how far each spider if from the player
		return  Math.abs(pos[0] - x2) +  Math.abs(pos[1] - y2);
			
	}

}