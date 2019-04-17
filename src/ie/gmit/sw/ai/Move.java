package ie.gmit.sw.ai;

public interface Move {

	public boolean movedown(int row, int col, char val) throws Exception;
	public boolean moveUp(int row, int col, char val) throws Exception;
	public boolean moveRight(int row, int col, char val) throws Exception;
	public boolean moveLeft(int row, int col, char val);
	
}
