package lugbygame;

public class TeamB implements Strategy {

	@Override
	public void execute(Status currentStatus, Order resultOrder) {
		resultOrder.clear();

		if (currentStatus.ballOwner == -1) {// 프리볼
			for (int i = 0; i < 6; i++) {
				move2(i, currentStatus.mine[i], currentStatus.ball, resultOrder);
			}
		} else if (currentStatus.ballOwner < 6) {// 공격
			for (int i = 0; i < 6; i++) {
				resultOrder.dx[i]++;
				if (currentStatus.mine[i].getX() < _Game.WIDTH / 2) {
					resultOrder.dy[i] = getRandom(-1, 1);
				} else {
					move2goal(i, currentStatus.mine[i], resultOrder);
				}
			}

			if (currentStatus.turn % 2 == 1) {
				int ballOwnersX = currentStatus.mine[currentStatus.ballOwner].getX();
				for (int j = 0; j < 6; j++) {
					if (ballOwnersX < currentStatus.enemy[j].getX()) {
						resultOrder.passto = getRandom(0, 5);
						break;
					}
				}

			}
		} else {// 수비
			for (int i = 0; i < 3; i++) {
				move2(i, currentStatus.mine[i], currentStatus.ball, resultOrder);
			}
			for (int i = 3; i < 6; i++) {// 공격 하다가 전환했을 때 조정해야 함
				if (currentStatus.mine[i].getX() < 20) {
					if (currentStatus.turn % 3 == 0) {
						move2(i, currentStatus.mine[i], currentStatus.ball, resultOrder);
					} else if (currentStatus.turn % 3 == 1) {
						resultOrder.dx[i] = 0;
						resultOrder.dy[i] = 0;
					} else {
						runAway2(i, currentStatus.mine[i], currentStatus.ball, resultOrder);
					}
				} else {
					resultOrder.dx[i]--;
					resultOrder.dy[i] = _Game.HEIGHT / 2 - currentStatus.mine[i].getY();
				}
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

}
