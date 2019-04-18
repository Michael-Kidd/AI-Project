package ie.gmit.sw.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Node {
    int x;
    int y;
    
    Node(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class Search{
	
	static boolean[][] visited = new boolean[100][100];
	
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
    	
    	visited = new boolean[100][100];
    	
		GameView.getInstance();
		
		int playerRow = GameView.getCurrentRow();
		int playerCol = GameView.getCurrentCol();
		
		GameView.getMaze();
		char[][] matrix = Maze.getMaze().clone();
    	
        List<Node> queue = new ArrayList<Node>();
        
        queue.add(new Node(spiderRow, spiderCol));
        
        while(!queue.isEmpty()) {
            
        	//pop a node off the queue
        	Node current = queue.remove(0);
            
            if(current.x == playerRow && current.y == playerCol) {
            	return queue;
            }
            
            matrix[current.x][current.y] = '0'; // mark as visited
            
            List<Node> neighbors = getNeighbors(matrix, current);
            queue.addAll(neighbors);
        }
        
        return queue;
    }
    
    public static List<Node> getNeighbors(char[][] matrix, Node node) {
        List<Node> neighbors = new ArrayList<Node>();
        
        if(isValidPoint(matrix, node.x - 1, node.y)) {
            neighbors.add(new Node(node.x - 1, node.y));
        }
        
        if(isValidPoint(matrix, node.x + 1, node.y)) {
            neighbors.add(new Node(node.x + 1, node.y));
        }
        
        if(isValidPoint(matrix, node.x, node.y - 1)) {
            neighbors.add(new Node(node.x, node.y - 1));
        }
        
        if(isValidPoint(matrix, node.x, node.y + 1)) {
            neighbors.add(new Node(node.x, node.y + 1));
        }
        
        return neighbors;
    }
    
    public static boolean isValidPoint(char[][] matrix, int x, int y) {
        return !(x < 0 || x >= matrix.length || y < 0 || y >= matrix.length) && (matrix[x][y] != '0');
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
