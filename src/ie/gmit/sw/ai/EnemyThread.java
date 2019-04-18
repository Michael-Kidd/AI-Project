package ie.gmit.sw.ai;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.sourceforge.jFuzzyLogic.FIS;

public class EnemyThread extends Thread{

	ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
	
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
		
			exec.scheduleWithFixedDelay(new Runnable() {
				@Override
				public void run() {
					
					try {
						
						findPath();
					
					} 
					catch (Exception e) {
						
					}
					
				}
				
			}, 1, 2, TimeUnit.SECONDS);
			
	}
	
	public void findPath() throws Exception {
		
		GameView.getInstance();
		GameView.getMaze();
		
		char[][] matrix = Maze.getMaze();
		
		List<Node> nodes = new FindPlayer().find(matrix, pos[0], pos[1]);

		move(pos[0], pos[1], nodes, val);

	}
        
	public void move(int row, int col, List<Node> nodes, char val) throws Exception {
		
		GameView.getInstance();
		
		int newRow = nodes.get(nodes.size()-1).x;
		int newCol = nodes.get(nodes.size()-1).y;
		
		GameView.setMaze(pos[0], pos[1], '\u0020');
		GameView.setMaze(newRow, newCol, val);
		
		pos[0] = newRow;
		pos[1]= newCol;
		
	}
	
	public int[] getPos() {
		return pos;
	}
	
	public void setPos(int[] pos) {
		this.pos = pos;
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