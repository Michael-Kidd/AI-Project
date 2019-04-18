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
	
	protected boolean[][] visited = new boolean[100][100];
	protected List<Node> queue = new ArrayList<Node>();
	
    public List<Node> find(char[][] matrix, int row, int col) {
        
        Node currentNode = new Node(row, col);
        
        queue.add(currentNode);
        
        while(!queue.isEmpty()) {
        	
        	currentNode = queue.remove(0);
        	
        	System.out.println(queue.size());
        	
        	visited[currentNode.x][currentNode.y] = true;
        	
        	if(matrix[currentNode.x][currentNode.y] == '5') {
        		return this.queue;
        	}
        	
        	queue.addAll(getNeighbors(matrix, currentNode));
        	
        }

        return this.queue;

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
        return !(x < 0 || x >= matrix.length || y < 0 || y >= matrix.length) && (matrix[x][y] != '0') && (!visited[x][y]);
    }

}