package lugbygame;

//lugby player
public class Player extends Unit {
	private int id;
	private int number;
	private boolean ballOwner = false;

	// original stat
	private int maxStregth;
	private int maxPass;
	private int maxStamina;
	// current stat
	private int strength;
	private int pass;
	private int stamina;

	// MARK insert stat(max, current)
	public Player(int id, int x, int y, int maxStrength, int maxPass, int maxStamina) {
		super(x, y);
		this.id = id;
		if (id >= 0 && id < 6) {
			number = id;
		} else if (id >= 6 && id < 12) {
			number = id - 6;
		}

		this.maxStregth = strength = maxStrength;
		this.maxPass = pass = maxPass;
		this.maxStamina = stamina = maxStamina;
	}

	public void getBall() {
		ballOwner = true;
	}

	public void move(int dx, int dy) {
		if (dx != 0)
			dx /= dx;
		if (dy != 0)
			dy /= dy;
		// range check
		if (x == _Game.WIDTH - 1 && dx == 1) {
			dx = 0;
		} else if (x == 0 && dx == -1) {
			dx = 0;
		}
		if (y == _Game.HEIGHT - 1 && dy == 1) {
			dy = 0;
		} else if (y == 0 && dy == -1) {
			dy = 0;
		}
		// stamina check
		if (ballOwner && stamina < 6 || stamina < 3) {
			dx = 0;
			dy = 0;
		}

		if (dx == 0 && dy == 0) {// if don't move
			restore();
		} else {
			if (ballOwner) {
				consumeStamina(6);
			} else {
				consumeStamina(3);
			}
			x += dx;
			y += dy;
		}
	}

	public void restore() {
		stamina += 10;
		if (stamina > maxStamina) {
			stamina = maxStamina;
		}
		updateStat();
	}

	public boolean isSamePositionWith(Player p) {
		if (p.x == x && p.y == y)
			return true;
		else
			return false;
	}

	public void winContending() {
		consumeStamina(2);
	}

	public void loseContending() {
		consumeStamina(5);
		if (ballOwner)
			ballOwner = false;
	}
	// -----------------------------------------------------------------------------------------------------------------------------//

	private void consumeStamina(int n) {
		stamina -= n;
		if (stamina < 0)
			stamina = 0;
		updateStat();
	}

	private void updateStat() {
		double a = (double) stamina / maxStamina;
		strength = (int) (a * maxStregth);
		pass = (int) (a * maxPass);
	}

	// getter
	public int getId() {
		return id;
	}

	public int getNumber() {
		return number;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getStamina() {
		return stamina;
	}

	public int getStrength() {
		return strength;
	}

	public int getPass() {
		return pass;
	}

	public boolean isBallOwner() {
		return ballOwner;
	}
}
