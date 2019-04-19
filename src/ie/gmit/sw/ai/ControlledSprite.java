package ie.gmit.sw.ai;

public class ControlledSprite extends Sprite{
	
	// static variable single_instance of type Singleton
    private static volatile ControlledSprite single_instance = null;
    private int health = 100;
	
	public static synchronized ControlledSprite getInstance() throws Exception
    {

		if (single_instance == null) {
		    single_instance = new ControlledSprite();
		}
        
		return single_instance;
    } 
	
	public ControlledSprite(String name, int frames, String... images) throws Exception{
		super(name, frames, images);
	}

	public ControlledSprite() {
		super();
	}

	public void setDirection(Direction d){
		switch(d.getOrientation()) {
		case 0: case 1:
			super.setImageIndex(0); //UP or DOWN
			break;
		case 2:
			super.setImageIndex(1);  //LEFT
			break;
		case 3:
			super.setImageIndex(2);  //LEFT
		default:
			break; //Ignore...
		}		
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
}