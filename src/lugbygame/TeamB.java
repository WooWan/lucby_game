package lugbygame;

public class TeamB implements Strategy {

	@Override
	public void execute(Status currentStatus, Order resultOrder) {
		resultOrder.clear();

		resultOrder.dx[2]++;
		int i = 0, j, k;

		resultOrder.passto = -1;

		if (currentStatus.ballOwner >= 0 && currentStatus.ballOwner < 6) {

			i = currentStatus.ballOwner;
			for (j = 0; j < 6; j++) {
				if (i == 5 && currentStatus.mine[i].x - currentStatus.enemy[j].x <= 0
						&& currentStatus.mine[i].x - currentStatus.enemy[j].x >= -2
						&& currentStatus.mine[i].y - currentStatus.enemy[j].y < 2
						&& currentStatus.mine[i].y - currentStatus.enemy[j].y > -2) {
					// Pass
					if (currentStatus.mine[5].x <= currentStatus.mine[i].x) {
						resultOrder.passto = 5;
						break;
					}
					if (i == 5) {
						resultOrder.passto = getRandom(0, 4);
						break;
					}

					for (k = 0; k < 5; k++) {
						if (currentStatus.mine[k].x <= currentStatus.mine[i].x) {
							resultOrder.passto = k;
							break;
						}
					}

					if (k == 6) {
						resultOrder.dx[i] = -1;
						if (currentStatus.mine[i].x == 0) {
							resultOrder.dy[i] = (getRandom(0, 1)) == 0 ? -1 : 1;
						}
					}
					break;
				}
			}

			if (j == 6) {
				resultOrder.dx[i] = 1;
				if (currentStatus.mine[i].y < (_Game.HEIGHT - 1) / 2 - 5)
					resultOrder.dy[i] = 1;
				else if (currentStatus.mine[i].y > (_Game.HEIGHT - 1) / 2 + 5)
					resultOrder.dy[i] = -1;
			}

			for (j = 0; j < 6; j++) {

				if (j != i) {
					if (currentStatus.ballOwner != 5) {
						if (currentStatus.mine[j].x > currentStatus.mine[i].x)
							resultOrder.dx[j] = -1;
						else
							resultOrder.dx[j] = getRandom(0, 1);
					} else {
						resultOrder.dx[j] = getRandom(0, 1);
					}

					resultOrder.dy[j] = (getRandom(0, 2) - 1);
				}

			}

		}

		if (currentStatus.ballOwner == -1) {
			for (i = 0; i < 6; i++) {
				if (currentStatus.ball.x > currentStatus.mine[i].x)
					resultOrder.dx[i] = 1;
				else if (currentStatus.ball.x < currentStatus.mine[i].x)
					resultOrder.dx[i] = -1;
				else
					resultOrder.dx[i] = 0;

				if (currentStatus.ball.y > currentStatus.mine[i].y)
					resultOrder.dy[i] = 1;
				else if (currentStatus.ball.y < currentStatus.mine[i].y)
					resultOrder.dy[i] = -1;
				else
					resultOrder.dy[i] = 0;
			}

		} else if (currentStatus.ballOwner >= 6) {

			for (i = 0; i < 6; i++) {
				if (currentStatus.ball.x > currentStatus.mine[i].x + (getRandom(0, 2) - 1))
					resultOrder.dx[i] = 1;
				else if (currentStatus.ball.x < currentStatus.mine[i].x + (getRandom(0, 2) - 1))
					resultOrder.dx[i] = -1;
				else
					resultOrder.dx[i] = 0;

				if (currentStatus.ball.y > currentStatus.mine[i].y + (getRandom(0, 2) - 1))
					resultOrder.dy[i] = 1;
				else if (currentStatus.ball.y < currentStatus.mine[i].y + (getRandom(0, 2) - 1))
					resultOrder.dy[i] = -1;
				else
					resultOrder.dy[i] = 0;
			}

		}

		return;

	}

}
