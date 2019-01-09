package lugbygame;

public class TeamA implements Strategy {

	@Override
	public void execute(Status currentStatus, Order resultOrder) {
		resultOrder.clear();

		int i = 0, j, k;

		resultOrder.passto = -1;

		for (i = 0; i < 6; i++) {

			resultOrder.dx[i] = getRandom(0, 2) - 1;
			resultOrder.dy[i] = getRandom(0, 2) - 1;

			if (currentStatus.ballOwner == i) {

				for (j = 0; j < 6; j++) {
					if (currentStatus.mine[i].x - currentStatus.enemy[j].x < 0
							&& currentStatus.mine[i].x - currentStatus.enemy[j].x >= -2
							&& currentStatus.mine[i].y - currentStatus.enemy[j].y <= 2
							&& currentStatus.mine[i].y - currentStatus.enemy[j].y >= -2) {
						// Pass
						k = 0;
						if (i == 5) {
							resultOrder.passto = getRandom(0, 4);
						} else if (currentStatus.mine[5].x <= currentStatus.mine[i].x) {
							resultOrder.passto = 5;
						} else
							for (k = 0; k < 5; k++) {
								if (currentStatus.mine[k].x <= currentStatus.mine[i].x) {
									resultOrder.passto = k;
									break;
								}
							}

						if (k == 5) {
							resultOrder.dx[i] = -1;
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
					if (j != i && i != 5) {
						if (currentStatus.mine[i].x < currentStatus.mine[j].x) {
							resultOrder.dx[j] = -1;
						} else if (currentStatus.mine[i].x > currentStatus.mine[j].x) {
							resultOrder.dx[j] = 1;
						}
					}

				}

			}
		}

		if (currentStatus.ballOwner == -1 || currentStatus.ballOwner >= 6) {

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

		}

		return;
	}
}
