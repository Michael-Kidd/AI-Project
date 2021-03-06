package ie.gmit.sw.ai;


public class Maze {
	
	private static char[][] maze; //An array does not lend itself to the type of mazge generation alogs we use in the labs. There are no "walls" to carve...
	
	public Maze(int dimension){
		
		maze = new char[dimension][dimension];
		init();
		buildMaze();
		
		int featureNumber = (int)((dimension * dimension) * 0.0001); //Change this value to control the number of objects
		
		//Removed the objects as I don't intend to use them
		//addFeature('\u0031', '0', featureNumber); //1 is a sword, 0 is a hedge
		/*addFeature('\u0032', '0', featureNumber); //2 is help, 0 is a hedge
		addFeature('\u0033', '0', featureNumber); //3 is a bomb, 0 is a hedge
		addFeature('\u0034', '0', featureNumber); //4 is a hydrogen bomb, 0 is a hedge*/
		
		//Creates the enemy spiders
		featureNumber = (int)(15); //Change this value to control the number of spiders
		addFeature('\u0036', '0', featureNumber); //6 is a Black Spider, 0 is a hedge
		addFeature('\u0037', '0', featureNumber); //7 is a Blue Spider, 0 is a hedge
		addFeature('\u0038', '0', featureNumber); //8 is a Brown Spider, 0 is a hedge
		addFeature('\u0039', '0', featureNumber); //9 is a Green Spider, 0 is a hedge
		addFeature('\u003A', '0', featureNumber); //: is a Grey Spider, 0 is a hedge
		addFeature('\u003B', '0', featureNumber); //; is a Orange Spider, 0 is a hedge
		addFeature('\u003C', '0', featureNumber); //< is a Red Spider, 0 is a hedge
		addFeature('\u003D', '0', featureNumber); //= is a Yellow Spider, 0 is a hedge
		
	}
	
	private void init(){
		for (int row = 0; row < maze.length; row++){
			for (int col = 0; col < maze[row].length; col++){
				maze[row][col] = '0'; //Index 0 is a hedge...
			}
		}
	}
	
	private void addFeature(char val, char replace, int number){
		
		int counter = 0;
		
		while (counter < number){ //Keep looping until feature number of items have been added
			int row = (int) (maze.length * Math.random());
			int col = (int) (maze[0].length * Math.random());
			
			if (maze[row][col] == replace){
				
				maze[row][col] = val;
				
				int[] pos = {row, col};
				
				EnemyThread et = new EnemyThread(pos, val);
				et.start();
				
				counter++;
			}
		}
	}
	
	//Creates the random Maze
	//Here I could make the Randomised Exits
	private void buildMaze(){
		for (int row = 1; row < maze.length - 1; row++){
			for (int col = 1; col < maze[row].length - 1; col++){
				int num = (int) (Math.random() * 10);
				if (isRoom(row, col)) continue;
				if (num > 5 && col + 1 < maze[row].length - 1){
					maze[row][col + 1] = '\u0020'; //\u0020 = 0x20 = 32 (base 10) = SPACE
				}else {
					if (row + 1 < maze.length - 1) maze[row + 1][col] = '\u0020';
				}
			}
		}	
	}
	
	private boolean isRoom(int row, int col){ //Flaky and only works half the time, but reduces the number of rooms
		return row > 1 && maze[row - 1][col] == '\u0020' && maze[row - 1][col + 1] == '\u0020';
	}
	
	public static char[][] getMaze(){
		return maze;
	}
	
	public char get(int row, int col){
		return Maze.maze[row][col];
	}
	
	public void set(int row, int col, char c){
		Maze.maze[row][col] = c;
	}
	
	public int size(){
		return Maze.maze.length;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for (int row = 0; row < maze.length; row++){
			for (int col = 0; col < maze[row].length; col++){
				sb.append(maze[row][col]);
				if (col < maze[row].length - 1) sb.append(",");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}