package ie.gmit.sw.ai;

import java.util.ArrayList;
import java.util.List;

public class ManhattanDistance {

	public Node find(char[][] matrix, int row, int col) throws Exception {

		List<Node> queue = new ArrayList<Node>();

		Node currentNode = new Node(row, col);

		queue.addAll(getNeighbors(matrix, currentNode));

		for (Node n : queue) {

			if (getDistanceFromPlayer(n) < getDistanceFromPlayer(currentNode)) {
				currentNode = n;
			}

		}

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
