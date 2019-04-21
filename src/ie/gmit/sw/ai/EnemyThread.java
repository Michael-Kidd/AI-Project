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
	private int startX;
	private int startY;
	
	public enum Action {
		
		Chase(0), Attack(1), Hide(2), Heal(3);

		private Action(int val) {
			this.value = val;
		}

		private int value;

		public int getValue() {
			return value;
		}
	}
	
	private Action state;
	
	private int venom = 100;
	private int strength = 100;
	
	private int nextToPlayer = 0;
	private int nextToHidePosition = 0;
	
	EnemyThread(int[] p, char v){
		
		this.pos = p;
		this.val= v;
		
		this.setStartX(pos[0]);
		this.setStartY(pos[1]);
		
		exec.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {

					try {
						state = checkState();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					switch(state) {
						case Attack:
							attack();
							break;
						case Chase:
							try {
								findPlayer();
							} catch (Exception e) {
							}
							break;
						case Hide:
							try {
								hide();
							} catch (Exception e) {
							}
							break;
						case Heal:
							heal();
							break;
						default:	
					}
					
			}
			
		}, 0, 2, TimeUnit.SECONDS);
		
	}
	
	public Action checkState() throws Exception {
		
		int hasStrength = 0;
		int hasVenom = 0;
		
		if(strength > 0)
			hasStrength = 1;
		else
			hasVenom = 0;
		
		if(venom > 0)
			hasVenom = 1;
		else
			hasVenom = 0;
		
		if(new ManhattanDistance().getDistanceFromPlayer(new Node(pos[0], pos[1])) <= 1 ) {
			nextToPlayer = 1;
		}
		else {
			nextToPlayer = 0;
		}
		
		int value = EncogNN.getState(hasStrength, hasVenom, nextToPlayer, nextToHidePosition);

		return Action.values()[value];
		
	}
	
	protected void attack() {
		
		double attack = getAttackValue();
		
		double potency = getPotencyValue();
		
		int damage = getDamageValue(attack, potency);
		
		if(venom > 0)
			venom -= damage;
		else 
			venom = 0;
		
		try {
		
			ControlledSprite.getInstance().setHealth(ControlledSprite.getInstance().getHealth() - damage);
			
			JOptionPane.showMessageDialog(null, getSpidertype() +" Spider Dealt " + damage + " damage"
					+ "\nYou have " +ControlledSprite.getInstance().getHealth() +" Health left \n Spider has " +venom +" Venom\nSpider has "
							+strength +" Strength");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void heal() {
		if(strength < 100)
			strength++;
		if(venom < 100)
			venom++;
	}
	
	public double getAttackValue() {
		
		FIS file = FIS.load("resources/fuzzy/damage.fcl", true);
		
		FunctionBlock damageLogic = file.getFunctionBlock("AttackDamage");
		
		damageLogic.setVariable("attack", strength);
		
		damageLogic.evaluate();
		
		return damageLogic.getVariable("damage").getValue();
		
	}
	
	public double getPotencyValue() {
		
		FIS file = FIS.load("resources/fuzzy/damage.fcl", true);
		
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
	
	public void hide() throws Exception {
		
		
	}
        
	public void move(int row, int col, Node node, char val) throws Exception {
		
		
		if(strength > 0)
			strength --;
		
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

	public int getNextToHidePosition() {
		return nextToHidePosition;
	}

	public void setNextToHidePosition(int nextToHidePosition) {
		this.nextToHidePosition = nextToHidePosition;
	}

	public int getNextToPlayer() {
		return nextToPlayer;
	}

	public void setNextToPlayer(int nextToPlayer) {
		this.nextToPlayer = nextToPlayer;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}

	public int getStartX() {
		return startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

}