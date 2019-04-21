package ie.gmit.sw.ai;


import java.util.List;

	public class BFSearch{
		
		// static variable single_instance of type Singleton
	    private static volatile Search single_instance = null;

		public static synchronized Search getInstance() throws Exception
		{
		
			if (single_instance == null) {
			    single_instance = new Search();
			}
			
			return single_instance; 
		} 

	    public static List<Node> findPlayer(int spiderRow, int spiderCol) throws Exception {
	    	
			GameView.getInstance();
			
			int playerRow = GameView.getCurrentRow();
			int playerCol = GameView.getCurrentCol();
			
			GameView.getMaze();
			char[][] matrix = Maze.getMaze();
			
			
			
			return null;
	      
	    }
		
	}