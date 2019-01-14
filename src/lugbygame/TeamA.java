package lugbygame;

public class TeamA implements Strategy {

	@Override
	public void execute(Status currentStatus, Order resultOrder) {
		resultOrder.clear();

		if (currentStatus.ballOwner == -1) {// 프리볼
			for (int i = 0; i < 6; i++) {
				move2(i, currentStatus.mine[i], currentStatus.ball, resultOrder);
			}
		} else if (currentStatus.ballOwner < 6) {// 공격
			resultOrder.passto = 5;
			for (int i = 0; i < 6; i++) {
				if (i == currentStatus.ballOwner) {
					if (currentStatus.turn % 2 == 0) {
						move2goal(i, currentStatus.mine[i], resultOrder);
						//
						if (currentStatus.mine[i].getX() < _Game.WIDTH - 10) {
							resultOrder.dy[i] = getRandom(-1, 1);
						}
						//
					}
				} else {
					if (currentStatus.mine[i].getDistance(currentStatus.mine[currentStatus.ballOwner]) > 3) {
						move2(i, currentStatus.mine[i], currentStatus.mine[currentStatus.ballOwner], resultOrder);
					} else {
						move2goal(i, currentStatus.mine[i], resultOrder);
						//
						if (currentStatus.mine[i].getX() < _Game.WIDTH - 10) {
							resultOrder.dy[i] = getRandom(-1, 1);
						}
						//
					}
				}
			}
		} else {// 수비
			for (int i = 0; i < 6; i++) {
				move2(i, currentStatus.mine[i], currentStatus.enemy[i], resultOrder);
				resultOrder.dx[i]--;
			}
		}
	}

	public void move2(int number, Unit src, Unit dest, Order resultOrder) {

		int dx, dy;
		if (src.getX() == dest.getX()) {
			dx = 0;
		} else {
			dx = (dest.getX() - src.getX()) / Math.abs(dest.getX() - src.getX());
		}
		if (src.getY() == dest.getY()) {
			dy = 0;
		} else {
			dy = (dest.getY() - src.getY()) / Math.abs(dest.getY() - src.getY());
		}

		resultOrder.dx[number] = dx;
		resultOrder.dy[number] = dy;

	}

	public void runAway2(int number, Unit src, Unit dest, Order resultOrder) {

		int dx, dy;
		if (src.getX() == dest.getX()) {
			dx = 0;
		} else {
			dx = -(dest.getX() - src.getX()) / Math.abs(dest.getX() - src.getX());
		}
		if (src.getY() == dest.getY()) {
			dy = 0;
		} else {
			dy = -(dest.getY() - src.getY()) / Math.abs(dest.getY() - src.getY());
		}

		resultOrder.dx[number] = dx;
		resultOrder.dy[number] = dy;

	}

	public void move2goal(int number, Unit u, Order resultOrder) {

		resultOrder.dx[number]++;
		if (u.getY() >= _Game.HEIGHT / 2 + 5) {
			resultOrder.dy[number]--;
		} else if (u.getY() <= _Game.HEIGHT / 2 - 5) {
			resultOrder.dy[number]++;
		} else {
			resultOrder.dy[number] = 0;
		}

	}

	public void randomSpread() {
	}
}
