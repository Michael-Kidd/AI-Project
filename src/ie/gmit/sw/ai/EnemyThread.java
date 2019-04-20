package ie.gmit.sw.ai;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;

public class EnemyThread extends Thread{

	ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
	
	//keep enemy pos
	private int[] pos = new int[2];
	private char val;
	
	private int venom = 100;
	private int strength = 0;
	
	EnemyThread(int[] p, char v, int s){
		
		this.pos = p;
		this.val= v;
		this.strength = s;
		
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
		
		double attack = getAttackValue();
		
		double potency = getPotencyValue();
		
		int damage = getDamageValue(attack, potency);
		
		if(venom > 0) {
			venom -= damage;
			strength -= (attack/2);
		}
		else 
			venom = 0;
		
		try {
		
			ControlledSprite.getInstance().setHealth(ControlledSprite.getInstance().getHealth() - damage);
			
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
	
	public double getAttackValue() {
		
		FIS file = FIS.load("resources/fuzzy/damageLogic.fcl", true);
		
		FunctionBlock damageLogic = file.getFunctionBlock("AttackDamage");
		
		damageLogic.setVariable("attack", strength);
		
		damageLogic.evaluate();
		
		return damageLogic.getVariable("damage").getValue();
		
	}
	
	public double getPotencyValue() {
		
		FIS file = FIS.load("resources/fuzzy/venom.fcl", true);
		
		FunctionBlock damageLogic = file.getFunctionBlock("VenomPotency");
		
		damageLogic.setVariable("venom", venom);
		
		damageLogic.evaluate();
		
		return damageLogic.getVariable("potency").getValue();
		
	}
	
	public int getDamageValue(double attack, double potency) {
		
		FIS file = FIS.load("resources/fuzzy/damage.fcl", true);
		
		FunctionBlock damageLogic = file.getFunctionBlock("damageoutput");
		
		damageLogic.setVariable("output", attack);
		
		damageLogic.setVariable("potencyofvenom", potency);
		
		damageLogic.evaluate();
		
		return (int)damageLogic.getVariable("damagedealt").getValue();
		
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