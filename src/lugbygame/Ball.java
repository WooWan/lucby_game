package lugbygame;

public class Ball extends Unit {

	public Ball(int x, int y) {
		super(x, y);
	}

	public void fly2(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void dribbled(Player p) {
		this.x = p.x;
		this.y = p.y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public String toString() {
		return "$";
	}
}
