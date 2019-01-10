package lugbygame;

//주문
public class Order {
	// dx,dy of 각 player(0-5)
	// passto
	// initialize
	int[] dx = new int[6];
	int[] dy = new int[6];
	int passto = -1;

	public void clear() {
		dx = new int[6];
		dy = new int[6];
		int passto = -1;
	}

	public Order() {
	}

	public Order(Order o, boolean mirror) {
		dx = o.dx;
		dy = o.dy;
		passto = o.passto;

		for (int i = 0; i < dx.length; i++) {
			dx[i] *= -1;
		}
	}
}
