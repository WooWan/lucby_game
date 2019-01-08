package lugbygame;


public abstract class Unit {
	int x;
	int y;
	
	public Unit(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getDistance(Unit u1, Unit u2) {
		return 0;
	}
}
