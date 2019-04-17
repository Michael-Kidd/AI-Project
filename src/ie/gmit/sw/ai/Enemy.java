package ie.gmit.sw.ai;

import java.util.Arrays;

import net.sourceforge.jFuzzyLogic.FIS;

public class Enemy extends Thread{
	
	
	
	
	//keep enemy pos
	int[] pos = new int[2];
	char val;
	private FIS logic;
	
	Enemy(int[] p, char v){
		this.pos = p;
		this.val = v;
	}
	
	Enemy() {
		System.out.println("start");
		//load fuzzy Rules
		String fileName = "resources/fuzzy/logic.fcl";
		
		logic = FIS.load(fileName, true);
		
		if( logic == null )
		{
			System.err.println("Can't load file: '" + fileName + "'");
			return;
		}
		
		while(true) {
			/*
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
			
			logic.evaluate();*/
			
			char[][] matrix;
			
			try {
				
				move(pos[0], pos[1], pos[0]-1, pos[1], val);
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
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