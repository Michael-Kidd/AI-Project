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

public class BfSearch{
	
	char[][] visited = new char[100][100];
    
    public List<Node> pathExists(char[][] matrix, int[] pos) {
        
        List<Node> queue = new ArrayList<Node>();
        
        queue.add(new Node(pos[0], pos[1]));
        
        while(!queue.isEmpty()) {
            Node current = queue.remove(0);
            if(matrix[current.x][current.y] == '5') {
                
            	return queue;
            }
            
            visited[current.x][current.y] = '0'; // mark as visited
            
            List<Node> neighbors = getNeighbors(matrix, current);
            queue.addAll(neighbors);
        }
        
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
        return !(x < 0 || x >= matrix.length || y < 0 || y >= matrix.length) && (visited[x][y] != '0');
    }
	
}
