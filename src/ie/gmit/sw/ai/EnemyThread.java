package ie.gmit.sw.ai;


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
		
		this.pos = p;
		this.val= v;
		
			exec.scheduleWithFixedDelay(new Runnable() {
				@Override
				public void run() {
					
					try {
						findPlayer();
					} 
					catch (Exception e) {
						
					}
					
				}
				
			}, 0, 2, TimeUnit.SECONDS);
			
	}
	
	public void findPlayer() throws Exception {
		
		GameView.getInstance();
		GameView.getMaze();
		
		char[][] matrix = Maze.getMaze();
		
		Node node = null;
		
		switch(val) {
			case '7':
				
		       node = new Search().findPath(matrix, pos[0], pos[1]);
		        
	        break;
			case '6':
				//Black Spider test their neighbour blocks to see which one is closer to the player 
				//move accordingly
				node = new ByDistance().find(matrix, pos[0], pos[1]);
				break;
			default:
				System.out.println("test");
		}

		move(pos[0], pos[1], node, val);
		
	}
        
	public void move(int row, int col, Node node, char val) throws Exception {
		
		GameView.getInstance();
		
		int newRow = node.x;
		int newCol = node.y;
		
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