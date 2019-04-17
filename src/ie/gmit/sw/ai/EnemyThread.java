package ie.gmit.sw.ai;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.sourceforge.jFuzzyLogic.FIS;

public class EnemyThread extends Thread{

	static ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
	
	//keep enemy pos
	int[] pos = new int[2];
	char val;
	private FIS logic;
	
	EnemyThread(int[] p, char v){
		
		this.pos = p;
		this.val = v;
    	
		String fileName = "resources/fuzzy/logic.fcl";
		
		logic = FIS.load(fileName, true);
		
		if( logic == null )
		{
			System.err.println("Can't load file: '" + fileName + "'");
			return;
		}
	
		int x2 = 0;
		int y2 = 0;
		
		try {
			
			//get players location
			x2 = GameView.getInstance().getCurrentRow();
			y2 = GameView.getInstance().getCurrentCol();
			
		} catch (Exception e) {
			System.out.println("here");
			return;
		}
		
		//Using manhattan distance to determine how far each spider if from the player
		int manhattan_distance =  Math.abs(pos[0] - x2) +  Math.abs(pos[1] - y2);
	
		//set the dstance variable in fuzzy logic to inform of manhattan distance to player
		logic.setVariable("distance", manhattan_distance);
		logic.evaluate();
		
		
		try {
			
			char[][] matrix= GameView.getInstance().getMaze().getMaze();
			System.out.println("test");
			//System.out.println(new BfSearch().pathExists(matrix, pos));
		
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
        
	
	public void move(int row, int col, int newRow, int newCol, char val) throws Exception {
		System.out.println("moved");
		GameView.getInstance().setMaze(pos[0], pos[1], '\u0020');
		GameView.getInstance().setMaze(newRow, newCol, val);
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

}