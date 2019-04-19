package ie.gmit.sw.ai;

import java.util.ArrayList;
import java.util.List;

public class ByDistance {
	
    public Node find(char[][] matrix, int row, int col) {

    	
    	
        return new Node(row, col);
    }
	
	int getDistance(int[] pos) throws Exception {
		
		int x2 = 0;
		int y2 = 0;
		
		GameView.getInstance();
		
		//get players location
		x2 = GameView.getCurrentRow();
		y2 = GameView.getCurrentCol();
		
		//Using Manhattan distance to determine how far each spider if from the player
		return  Math.abs(pos[0] - x2) +  Math.abs(pos[1] - y2);
			
	}
	
    public List<Node> getNeighbors(char[][] matrix, Node node) {
    	
        List<Node> neighbors = new ArrayList<Node>();
        
        neighbors.add(new Node(node.x - 1, node.y));

        neighbors.add(new Node(node.x + 1, node.y));

        neighbors.add(new Node(node.x, node.y - 1));

        neighbors.add(new Node(node.x, node.y + 1));
        
        return neighbors;
        
    }
}
