package lugbygame;


public class Ball extends Unit{
	int x = 10;
	private Ball(int x,int y) {
		super(x, y);
	}
	public Ball getInstance() {
		return this;
	}
	
}
