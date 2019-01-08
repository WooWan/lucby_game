package lugbygame;

//lugby player
public class Player extends Unit{
	private int id;
	private boolean ballOwner = false;
	
	//MARK insert stat(max, current)
	public Player(int id, int x, int y, int maxStrength, int maxPass, int maxStamina) {
		super(x, y);
		this.id = id;
	}
	
	public boolean isBallOwner() {
		return ballOwner;
	}
	
	public void getBall() {
		
	}
	
}
