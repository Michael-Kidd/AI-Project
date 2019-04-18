package ie.gmit.sw.ai;

import java.util.*;

class Node {
    int x;
    int y;
    
    Node(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class FindPlayer
{  
	
	private char[][] visited = new char[100][100];
	
    public List<Node> find(char[][] matrix, int row, int col) {
        
        List<Node> queue = new ArrayList<Node>();
        
        Node currentNode = new Node(row, col);
        
        queue.add(currentNode);
        
        queue.addAll(getNeighbors(matrix, currentNode));

        return queue;

    }
    
    public List<Node> getNeighbors(char[][] matrix, Node node) {
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
    
    public boolean isValidPoint(char[][] matrix, int x, int y) {
        return !(x < 0 || x >= matrix.length || y < 0 || y >= matrix.length) && (matrix[x][y] != '0');
    }

	public char[][] getVisited() {
		return visited;
	}

	public void setVisited(char[][] visited) {
		this.visited = visited;
	}
}