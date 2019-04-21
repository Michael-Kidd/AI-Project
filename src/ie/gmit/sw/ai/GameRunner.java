package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class GameRunner extends Thread implements KeyListener{
	private static final int MAZE_DIMENSION = 100;
	private static final int IMAGE_COUNT = 14;
	private ControlledSprite player;
	private GameView view;
	private Maze model;
	private int currentRow;
	private int currentCol;
	
	public GameRunner() throws Exception{
		
		model = new Maze(MAZE_DIMENSION);
    	view = GameView.getInstance(model);
    	
    	Sprite[] sprites = getSprites();
    	view.setSprites(sprites);
    	
    	placePlayer();
    	
    	Dimension d = new Dimension(GameView.DEFAULT_VIEW_SIZE, GameView.DEFAULT_VIEW_SIZE);
    	view.setPreferredSize(d);
    	view.setMinimumSize(d);
    	view.setMaximumSize(d);
    	
    	JFrame f = new JFrame("GMIT - B.Sc. in Computing (Software Development)");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.addKeyListener(this);
        f.getContentPane().setLayout(new FlowLayout());
        f.add(view);
        f.setSize(1000,1000);
        f.setLocation(100,100);
        f.pack();
        f.setVisible(true);
	}
	
	private void placePlayer(){   	
    	currentRow = (int) (MAZE_DIMENSION * Math.random());
    	currentCol = (int) (MAZE_DIMENSION * Math.random());
    	model.set(currentRow, currentCol, '5'); //Player is at index 5
    	updateView(); 		
	}
	
	private void updateView(){
		view.setCurrentRow(currentRow);
		view.setCurrentCol(currentCol);
	}

    public void keyPressed(KeyEvent e) {
		
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && currentCol < MAZE_DIMENSION - 1) {
        	if (isValidMove(currentRow, currentCol + 1)){
				player.setDirection(Direction.RIGHT);
				currentCol++; 
        	}   		
        }else if (e.getKeyCode() == KeyEvent.VK_LEFT && currentCol > 0) {
        	if (isValidMove(currentRow, currentCol - 1)) {
				player.setDirection(Direction.LEFT);
				currentCol--;	
			}
        }else if (e.getKeyCode() == KeyEvent.VK_UP && currentRow > 0) {
        	if (isValidMove(currentRow - 1, currentCol)) {
				player.setDirection(Direction.UP);
				currentRow--;
			}
        }else if (e.getKeyCode() == KeyEvent.VK_DOWN && currentRow < MAZE_DIMENSION - 1) {
        	if (isValidMove(currentRow + 1, currentCol)){
        		player.setDirection(Direction.DOWN);
				currentRow++;
        	}         	  	
        }else if (e.getKeyCode() == KeyEvent.VK_Z){
        	view.toggleZoom();
        }else{
        	return;
        }
        
        updateView();       
    }
    
    public void keyReleased(KeyEvent e) {} //Ignore
	public void keyTyped(KeyEvent e) {} //Ignore
   
	private boolean isValidMove(int row, int col){
		
		if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == ' '){
			model.set(currentRow, currentCol, '\u0020');
			model.set(row, col, '5');
			return true;
		}else{
			return false; //Can't move
		}
	}
	
	//generates a set of sprites, and their different frames
	private Sprite[] getSprites() throws Exception{
		
		//Read in the images from the resources directory as sprites. Note that each
		//sprite will be referenced by its index in the array, e.g. a 3 implies a Bomb...
		//Ideally, the array should dynamically created from the images... 
		
		player = new ControlledSprite("Main Player", 3, "resources/images/player/d1.png", "resources/images/player/d2.png", "resources/images/player/d3.png", "resources/images/player/l1.png", "resources/images/player/l2.png", "resources/images/player/l3.png", "resources/images/player/r1.png", "resources/images/player/r2.png", "resources/images/player/r3.png");
		
		Sprite[] sprites = new Sprite[IMAGE_COUNT];

		sprites[0] = new Sprite("Hedge", 1, "resources/images/objects/hedge.png");
		sprites[1] = new Sprite("Sword", 1, "resources/images/objects/sword.png");
		sprites[2] = new Sprite("Help", 1, "resources/images/objects/help.png");
		sprites[3] = new Sprite("Bomb", 1, "resources/images/objects/bomb.png");
		sprites[4] = new Sprite("Hydrogen Bomb", 1, "resources/images/objects/h_bomb.png");
		
		sprites[5] = player;
		
		sprites[6] = new Sprite("Black Spider", 2, "resources/images/spiders/black_spider_1.png", "resources/images/spiders/black_spider_2.png");
		sprites[7] = new Sprite("Blue Spider", 2, "resources/images/spiders/blue_spider_1.png", "resources/images/spiders/blue_spider_2.png");
		sprites[8] = new Sprite("Brown Spider", 2, "resources/images/spiders/brown_spider_1.png", "resources/images/spiders/brown_spider_2.png");
		sprites[9] = new Sprite("Green Spider", 2, "resources/images/spiders/green_spider_1.png", "resources/images/spiders/green_spider_2.png");
		sprites[10] = new Sprite("Grey Spider", 2, "resources/images/spiders/grey_spider_1.png", "resources/images/spiders/grey_spider_2.png");
		sprites[11] = new Sprite("Orange Spider", 2, "resources/images/spiders/orange_spider_1.png", "resources/images/spiders/orange_spider_2.png");
		sprites[12] = new Sprite("Red Spider", 2, "resources/images/spiders/red_spider_1.png", "resources/images/spiders/red_spider_2.png");
		sprites[13] = new Sprite("Yellow Spider", 2, "resources/images/spiders/yellow_spider_1.png", "resources/images/spiders/yellow_spider_2.png");
		
		return sprites;
	}
	
	public static void main(String[] args) throws Exception{
		
		//get the Encog NN started
		new EncogNN().go();
		
		//These values are sometimes incorrect and it causes issues in the game
		//it may not have been a good idea for me to use encog for the state transistion or 
		//I need to learn how to use it more effectively.
		//This is alot less of an issue after making the training data static and removing the singleton of the encogg class
		//however sometime might still be attacked by a spider that isnt there, as encog has predicted a wrong value
		
		/*int val = 0; 
		val = EncogNN.getState(0, 0, 0, 0); // for testing // Should return a 2 - HIDE
		System.out.println(val);
		val = EncogNN.getState(1, 1, 0, 0); // for testing // should return a 0 - CHASE
		System.out.println(val);
		val = EncogNN.getState(0, 0, 0, 0); // for testing // should return a 2 - HIDE
		System.out.println(val);
		val = EncogNN.getState(0, 0, 0, 0); // for testing // Should return a 2 - HIDE
		System.out.println(val);
		val = EncogNN.getState(1, 1, 1, 0); // for testing // should return a 1 - ATTACK //THIS Will have a threading issue
		System.out.println(val);
		val = EncogNN.getState(0, 0, 0, 1); // for testing //should return a 3 - HEAL
		System.out.println(val);*/
		
		//Run the view in another thread from the start
		new Thread(new GameRunner());
	
	}
	
}