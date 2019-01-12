package lugbygame;

public class Unit {
	protected int x;
	protected int y;

	public Unit(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Unit(Unit u) {
		this.x = u.x;
		this.y = u.y;
	}

	public Unit(Unit u, boolean mirror) {
		this.x = _Game.WIDTH - u.x - 1;
		this.y = u.y;
	}

	public int getDistance(Unit u) {
		return (int) Math.sqrt(Math.pow((x - u.x), 2) + Math.pow(y - u.y, 2));
	}
}
