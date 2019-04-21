package ie.gmit.sw.ai;

import java.util.ArrayList;
import java.util.List;


//using the manhattan Distance Heuristic, checked online
//Its stated as an admissable Hueristic
public class ManhattanDistance {

	//pass the array and the position of the spider
	public Node find(char[][] matrix, int row, int col) throws Exception {

		//queue the different possible tiles
		List<Node> queue = new ArrayList<Node>();

		//current tile is the one the spider is standing on
		Node currentNode = new Node(row, col);

		//get all the neighbour tiles
		queue.addAll(getNeighbors(matrix, currentNode));

		//for every node in the queue
		for (Node n : queue) {

			//if the node new node is closer to the player than the currentnode
			if (getDistanceFromPlayer(n) < getDistanceFromPlayer(currentNode)) {
				//then make that the currennt node
				currentNode = n;
			}

		}

		//return what ever node is now the current node
		return currentNode;
	}

	int getDistanceFromPlayer(Node currentNode) throws Exception {

		int x2 = 0;
		int y2 = 0;

		GameView.getInstance();

		// get players location
		x2 = GameView.getCurrentRow();
		y2 = GameView.getCurrentCol();

		// Using Manhattan distance to determine how far each spider if from the player
		return Math.abs(currentNode.x - x2) + Math.abs(currentNode.y - y2);

	}
	
	int getDistanceFromHidePosition(Node currentNode) throws Exception {

		
		int x2 = 0;
		int y2 = 0;

		// get players location
		y2 = 10;
		x2 = 10;

		// Using Manhattan distance to determine how far each spider if from the player
		return Math.abs(currentNode.x - x2) + Math.abs(currentNode.y - y2);

	}

	public List<Node> getNeighbors(char[][] matrix, Node node) {

		List<Node> neighbors = new ArrayList<Node>();
		
		//get all valid neighbour nodes
		if (matrix[node.x - 1][node.y] == '\u0020')
			neighbors.add(new Node(node.x - 1, node.y));

		if (matrix[node.x + 1][node.y] == '\u0020')
			neighbors.add(new Node(node.x + 1, node.y));

		if (matrix[node.x][node.y - 1] == '\u0020')
			neighbors.add(new Node(node.x, node.y - 1));

		if (matrix[node.x - 1][node.y + 1] == '\u0020')
			neighbors.add(new Node(node.x, node.y + 1));

		return neighbors;

	}
}
