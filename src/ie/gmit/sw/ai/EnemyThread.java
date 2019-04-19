package ie.gmit.sw.ai;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import net.sourceforge.jFuzzyLogic.FIS;

public class EnemyThread extends Thread{

	ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
	
	//keep enemy pos
	private int[] pos = new int[2];
	private char val;
	
	private int venom = 10;
	private int strength = 0;
	
	EnemyThread(int[] p, char v, int s){
		
		this.pos = p;
		this.val= v;
		this.setStrength(s);
		
		exec.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				
				try {
					
					if( new ManhattanDistance().getDistance(new Node(pos[0], pos[1])) <= 1 ){
						attack();
					}
					else 
					{
						findPlayer();
					}
					
				}
				catch (Exception e) {
					
				}
				
			}
			
		}, 0, 2, TimeUnit.SECONDS);
		
	}
	
	protected void attack() {
		
		FIS damageLogic = FIS.load("resources/fuzzy/damageLogic.fcl", true);
		
		damageLogic.setVariable("attack", strength);
		
		if(venom < 0)
			damageLogic.setVariable("venom", venom);
		else
			damageLogic.setVariable("venom", 0);
		
		damageLogic.evaluate();
		
		int damage = (int)damageLogic.getVariable("damage").getValue();
		
		try {
		
			ControlledSprite.getInstance().setHealth(ControlledSprite.getInstance().getHealth() - damage);
			
			if(venom > 0)
				venom -= damage;
			
			JOptionPane.showMessageDialog(null, getSpidertype() +" Spider Dealt " + damage + " damage"
					+ "\nYou have " +ControlledSprite.getInstance().getHealth() +" Health left");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

	}
	
	public String getSpidertype() {
		
		switch(val) {
		case '6':
			return "Black";
		case '7':
			return "Blue";
		case '8':
			return "Brown";
		case '9':
			return "Green";
		case ':':
			return "Grey";
		case ';':
			return "Orange";
		case '<':
			return "Red";
		case '=':
			return "Yellow";
		default:
			return "";
		}
	}

	public void findPlayer() throws Exception {
		
		GameView.getInstance();
		GameView.getMaze();
		
		char[][] matrix = Maze.getMaze();
		
		Node node = null;

		node = new ManhattanDistance().find(matrix, pos[0], pos[1]);

		move(pos[0], pos[1], node, val);
		
	}
        
	public void move(int row, int col, Node node, char val) throws Exception {
		
		GameView.getInstance();
		
		int newRow = node.x;
		int newCol = node.y;
		
		GameView.setMaze(pos[0], pos[1], '\u0020');
		GameView.setMaze(newRow, newCol, val);

		pos[0] = newRow;
		pos[1]= newCol;
		
	}
	
	public int[] getPos() {
		return pos;
	}
	
	public void setPos(int[] pos) {
		this.pos = pos;
	}

	public char getVal() {
		return val;
	}


	public void setVal(char val) {
		this.val = val;
	}

	public int getVenom() {
		return venom;
	}

	public void setVenom(int venom) {
		this.venom = venom;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

}