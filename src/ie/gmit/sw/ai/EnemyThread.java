package ie.gmit.sw.ai;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;

public class EnemyThread extends Thread{

	//scheduler used for repeating the treads
	ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
	
	//keep enemy pos
	private int[] pos = new int[2];
	//type of spider
	private char val;
	//starting positions
	private int startX;
	private int startY;
	
	public enum Action {
		
		//enums used to define the spiders state
		Chase(0), Attack(1), Hide(2), Heal(3);

		private Action(int val) {
			this.value = val;
		}

		//
		private int value;

		public int getValue() {
			return value;
		}
	}
	
	//current state
	private Action state;
	
	//venom and strength
	//strength will decrease when spider moves
	//venom decreases when spider attacks player
	private int venom = 100;
	private int strength = 100;
	
	//If the spider is next to the player or near its hide position
	private int nextToPlayer = 0;
	private int nextToHidePosition = 0;
	
	//object that gets called as a thread when spiders are generated
	EnemyThread(int[] p, char v){
		
		this.pos = p;
		this.val= v;
		
		this.setStartX(pos[0]);
		this.setStartY(pos[1]);
		
		//start the scheduler, will keep the thread alive
		exec.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				
					try {
						//check if the spider should be changing state
						state = checkState();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					//switch statement to call methods depending on state
					switch(state) {
						case Attack:
							//attack the player
							attack();
							break;
						case Chase:
							try {
								//find the player
								findPlayer();
							} catch (Exception e) {
							}
							break;
						case Hide:
							try {
								//go to the hide position
								hide();
							} catch (Exception e) {
							}
							break;
						case Heal:
							//increase venom and strength back to 100
							heal();
							break;
						default:	
					}
					
			}
			//repeat every 2 seconds, will allow the first to complete
		}, 0, 2, TimeUnit.SECONDS);
		
	}
	
	//check the state of the spider
	public Action checkState() throws Exception {
		
		//if the spider has lost all health or venom, which would send them back to the hide spot
		int hasStrength = 0;
		int hasVenom = 0;
		
		//if the spider has more than no strength
		if(strength > 0)
			hasStrength = 1;
		else
			hasVenom = 0;
		
		//if the spider has more than no venom
		if(venom > 0)
			hasVenom = 1;
		else
			hasVenom = 0;
		
		//check how far the spider is from the player
		if(new ManhattanDistance().getDistanceFromPlayer(new Node(pos[0], pos[1])) <= 1 ) {
			nextToPlayer = 1;
		}
		else {
			nextToPlayer = 0;
		}
		
		//Use Encog to determine the spider state, based on these variables
		int value = EncogNN.getState(hasStrength, hasVenom, nextToPlayer, nextToHidePosition);

		//return the enum action
		return Action.values()[value];
		
	}
	
	protected void attack() {
		
		//get the attack value from fuzzy logic
		double attack = getAttackValue();
		
		//get the potency of the venom from fuzzy logic
		double potency = getPotencyValue();
		
		//get th overall damage from fuzzy logic
		int damage = getDamageValue(attack, potency);
		
		//if the player has no venom
		if(venom > 0)
			venom -= damage;
		else 
			venom = 0;
		
		try {
		
			//Decrease the players health
			ControlledSprite.getInstance().setHealth(ControlledSprite.getInstance().getHealth() - damage);
			
			//show a message informing the player of the attack info
			JOptionPane.showMessageDialog(null, getSpidertype() +" Spider Dealt " + damage + " damage"
					+ "\nYou have " +ControlledSprite.getInstance().getHealth() +" Health left \n Spider has " +venom +" Venom\nSpider has "
							+strength +" Strength");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	//heal the spider
	private void heal() {
		if(strength < 100)
			strength++;
		if(venom < 100)
			venom++;
	}
	
	//get attack value
	public double getAttackValue() {
		
		//load the fuzzy logic file
		FIS file = FIS.load("resources/fuzzy/damage.fcl", true);
		
		//get the damage logic
		FunctionBlock damageLogic = file.getFunctionBlock("AttackDamage");
		
		//set the strength
		damageLogic.setVariable("attack", strength);
		
		//evaluate the logic
		damageLogic.evaluate();
		
		//return the damage
		return damageLogic.getVariable("damage").getValue();
		
	}
	
	public double getPotencyValue() {
		
		//load the fuzzy logic file
		FIS file = FIS.load("resources/fuzzy/damage.fcl", true);
		
		//get the potency of the venom - function block
		FunctionBlock damageLogic = file.getFunctionBlock("VenomPotency");
		
		//set the venom variable
		damageLogic.setVariable("venom", venom);
		
		//evaluate
		damageLogic.evaluate();
		
		//get the potency
		return damageLogic.getVariable("potency").getValue();
		
	}
	
	public int getDamageValue(double attack, double potency) {
		
		//load the fuzzy logic file
		FIS file = FIS.load("resources/fuzzy/damage.fcl", true);
		
		//get the damage logic - function block
		FunctionBlock damageLogic = file.getFunctionBlock("damageoutput");
		
		//set the attack variable
		damageLogic.setVariable("output", attack);
		
		//set the potency of the venom
		damageLogic.setVariable("potencyofvenom", potency);
		
		//evaluate
		damageLogic.evaluate();
		
		//get the overall damage dealt
		return (int)damageLogic.getVariable("damagedealt").getValue();
		
	}

	public void findPlayer() throws Exception {
		
		//get the game view singleton - synchronised
		GameView.getInstance();
		//get the maze
		GameView.getMaze();
		
		//get the maze array
		char[][] matrix = Maze.getMaze();
		
		
		Node node = null;
		
		//get the path to the player
		node = new ManhattanDistance().find(matrix, pos[0], pos[1]);

		//move the next step
		move(pos[0], pos[1], node, val);
		
	}
	
	public void hide() throws Exception {
		
		
	}
        
	public void move(int row, int col, Node node, char val) throws Exception {
		
		//if the strength is great than nothing
		if(strength > 0)
			strength --;
		
		//get the game view instance
		GameView.getInstance();
		
		//
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
	
	//get the type of spider for JOPtionpane
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