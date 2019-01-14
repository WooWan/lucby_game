package lugbygame;

import java.util.ArrayList;
import java.util.Scanner;

//Rule

//
//	                   ###initial position###
//     0123456789012345678901234567890123456789012345678901234567890123
//	  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% |
//	0 %                                                                % |
//	1 %                                                                % |
//	2 %                                                                % |
//	3 %                                                                % |
//	4 %                                                                % |
//	5 %                                                                % |
//	6 %                                                                % |
//	7                                                                    |
//	8                                                                    |
//	9                                                                    |
//	0                                                                    |
//	1    f    c                                                 |
//	2                                                                    |
//	3                                                                    |
//	4                                                                    |
//	5                                                                    |
//	6                                                                    |
//	7 %                                                                % |
//	8 %                                                                % |
//	9 %                                                                % |
//	0 %                                                                % |
//	1 %                                                                % |
//	2 %                                                                % |
//	3 %                                                                % |
//	  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% |

//main class
public class _Game {
	public static final int WIDTH = 64;
	public static final int HEIGHT = 24;
	public static final int NPLAYER = 6;

	private int normalWaitTime;
	private int eventWaitTime;
	private boolean eventTurn;
	private int nTurn;
	public static boolean DEBUGMODE = false;

	Strategy teamA;
	Strategy teamB;
	private int score_a;
	private int score_b;
	Player[] players;
	Player[] playerOfTeamA;
	Player[] playerOfTeamB;
	Ball ball;
	Player ballOwner;

	private Unit[][] screen;

	private Status statusA;
	private Status statusB;
	private Order orderA;
	private Order orderB;

	public _Game() {
		this(400, 1000);
	}

	public _Game(int normalWaitTime, int eventWaitTime) {
		this.normalWaitTime = normalWaitTime;
		this.eventWaitTime = eventWaitTime;

		eventTurn = false;
		nTurn = 0;

		teamA = new TeamA();
		teamB = new TeamB();
		score_a = 0;
		score_b = 0;
		statusA = new Status();
		statusB = new Status();
		orderA = new Order();
		orderB = new Order();
	}

	public void run() {
		initialize(0);
		// updateScr();
		printScr();
		// wait
		delay(1000);
		while (true) {
			nTurn++;
			clearScr();// clear screen

			onetick();// get order
			applyOrder(orderA, orderB);// apply order
			// updateScr();// set screen
			printScr();// print screen with print form

			int goalCheck = goalCheck();
			if (goalCheck == 1) {// if goal
				eventTurn = true;
				initialize(2);
			} else if (goalCheck == 2) {
				eventTurn = true;
				initialize(1);
			}

			if (endCheck() == 1) {// if end
				System.out.println("end game");
				return;
			}

			if (eventTurn) {
				delay(eventWaitTime);
				eventTurn = false;
			} else {
				delay(normalWaitTime);
			}
		}
	}

	private void initialize(int starter) {// 0 : 초기 1 : a팀 시작 2 : b팀 시작

		screen = new Unit[WIDTH][HEIGHT];

		playerOfTeamA = new Player[6];
		playerOfTeamA[0] = new Player(0, ((WIDTH - 1) / 2) - 25, HEIGHT / 2 - 4, 30, 30, 40);
		playerOfTeamA[1] = new Player(1, ((WIDTH - 1) / 2) - 24, HEIGHT / 2 - 2, 25, 40, 50);
		playerOfTeamA[2] = new Player(2, ((WIDTH - 1) / 2) - 23, HEIGHT / 2, 20, 50, 35);
		playerOfTeamA[3] = new Player(3, ((WIDTH - 1) / 2) - 24, HEIGHT / 2 + 2, 25, 40, 50);
		playerOfTeamA[4] = new Player(4, ((WIDTH - 1) / 2) - 25, HEIGHT / 2 + 4, 30, 30, 40);
		playerOfTeamA[5] = new Player(5, ((WIDTH - 1) / 2) - 28, HEIGHT / 2, 50, 50, 55);
		playerOfTeamB = new Player[6];
		playerOfTeamB[0] = new Player(6, ((WIDTH - 1) / 2) + 26, HEIGHT / 2 - 4, 30, 30, 40);
		playerOfTeamB[1] = new Player(7, ((WIDTH - 1) / 2) + 25, HEIGHT / 2 - 2, 25, 40, 50);
		playerOfTeamB[2] = new Player(8, ((WIDTH - 1) / 2) + 24, HEIGHT / 2, 20, 50, 35);
		playerOfTeamB[3] = new Player(9, ((WIDTH - 1) / 2) + 25, HEIGHT / 2 + 2, 25, 40, 50);
		playerOfTeamB[4] = new Player(10, ((WIDTH - 1) / 2) + 26, HEIGHT / 2 + 4, 30, 30, 40);
		playerOfTeamB[5] = new Player(11, ((WIDTH - 1) / 2) + 29, HEIGHT / 2, 50, 50, 55);
		players = new Player[12];
		for (int i = 0; i < players.length; i++) {
			if (i < 6)
				players[i] = playerOfTeamA[i];
			else
				players[i] = playerOfTeamB[i - 6];
		}

		if (starter == 0) {
			if (throwCoinWithProbability(50)) {
				ball = new Ball(playerOfTeamA[2].x, playerOfTeamA[2].y);
				ballOwner = playerOfTeamA[2];
				playerOfTeamA[2].getBall(ball);
			} else {
				ball = new Ball(playerOfTeamB[2].x, playerOfTeamB[2].y);
				ballOwner = playerOfTeamB[2];
				playerOfTeamB[2].getBall(ball);
			}
		} else if (starter == 1) {
			ball = new Ball(playerOfTeamA[2].x, playerOfTeamA[2].y);
			ballOwner = playerOfTeamA[2];
			playerOfTeamA[2].getBall(ball);
		} else if (starter == 2) {
			ball = new Ball(playerOfTeamB[2].x, playerOfTeamB[2].y);
			ballOwner = playerOfTeamB[2];
			playerOfTeamB[2].getBall(ball);
		}

		for (Player p : players) {
			screen[p.x][p.y] = p;
		}
	}

	public void onetick() {
		int ballOwner = (this.ballOwner == null ? -1 : this.ballOwner.getId());
		statusA.setStatus(generateUnitInfo(false), ballOwner, nTurn);
		teamA.execute(statusA, orderA);

		int ballOwnerForB;
		if (ballOwner == -1)
			ballOwnerForB = -1;
		else if (ballOwner < 6) {
			ballOwnerForB = ballOwner + 6;
		} else {
			ballOwnerForB = ballOwner - 6;
		}
		statusB.setStatus(generateUnitInfo(true), ballOwnerForB, nTurn);
		teamB.execute(statusB, orderB);
		// debug
		// for (int i = 0; i < 6; i++) {
		// System.out.println((char) ('a' + i) + ":(" + orderA.dx[i] + "," +
		// orderA.dy[i] + ")\t" + (char) ('1' + i)
		// + ":(" + orderB.dx[i] + "," + orderB.dy[i] + ")");
		// }
	}

	private Unit[] generateUnitInfo(boolean mirror) {
		Unit[] result = new Unit[13];

		if (mirror) {// for team b
			result[0] = new Unit(ball, true);
			for (int i = 1; i < 13; i++) {
				if (i <= 6) {
					result[i] = new Unit(playerOfTeamB[i - 1], true);
				} else {
					result[i] = new Unit(playerOfTeamA[i - 7], true);
				}
			}
		} else {// for team a
			result[0] = new Unit(ball);
			for (int i = 1; i < 13; i++) {
				if (i <= 6) {
					result[i] = new Unit(playerOfTeamA[i - 1]);
				} else {
					result[i] = new Unit(playerOfTeamB[i - 7]);
				}
			}
		}
		return result;
	}

	// ----------------------------------------------main_process----------------------------------------------------//
	private void applyOrder(Order oa, Order obr) { //
		Order ob = new Order(obr, true);

		for (Player p : playerOfTeamA) {
			p.move(oa.dx[p.getNumber()], oa.dy[p.getNumber()]);
			screen[p.x][p.y] = p;
		}
		for (Player p : playerOfTeamB) {
			p.move(ob.dx[p.getNumber()], ob.dy[p.getNumber()]);
			screen[p.x][p.y] = p;
		}
		contendCheck();
		passCheck();
		ballCheck();
	}

	private void contendCheck() {
		for (int i = 0; i < NPLAYER * 2; i++) {
			ArrayList<Player> contendingPlayers = new ArrayList<Player>();
			for (int j = i + 1; j < NPLAYER * 2; j++) {
				if (players[i].isSamePositionWith(players[j])) {
					contendingPlayers.add(players[j]);
				}
			}
			if (contendingPlayers.size() >= 1) {
				contendingPlayers.add(players[i]);
				contend(contendingPlayers);
			}
		}
	}

	private void contend(ArrayList<Player> players) {// 충돌
		// decide winner
		int rlength = 0, winner = -1;
		for (Player p : players) {
			rlength += p.getStrength();
		}
		if (rlength == 0) {
			winner = getRandom(0, players.size() - 1);
		} else {
			int r = getRandom(0, rlength - 1);
			int rm = 0, rM = 0;
			for (int i = 0; i < players.size(); i++) {
				rM += players.get(i).getStrength();
				if (r >= rm && r < rM) {
					winner = i;
					break;
				}
				rm = rM;
			}
		}

		// players.get(winner) -> winner
		int wx = players.get(winner).x;
		int wy = players.get(winner).y;
		for (int i = 0; i < players.size(); i++) {
			if (i == winner) {// 승자가 자리차지
				players.get(i).winContending();// apply stamina
				screen[wx][wy] = players.get(i);
			} else {
				if (players.get(i).isBallOwner()) {// 공 날라감
					int ix, iy;
					do {
						ix = wx + getRandom(-3, 3);
						iy = wy + getRandom(-3, 3);
					} while ((ix < 0 || ix >= WIDTH || iy < 0 || iy >= HEIGHT) || (screen[ix][iy] != null));
					ballOwner = null;
				}
				// 패배자 날라감
				int dx = 0, dy = 0;
				int ix, iy;
				int count = 0;
				do {
					count++;
					if (count > 100) {
						dx = getRandom(-2, 2);
						dy = getRandom(-2, 2);
					} else {
						dx = getRandom(-1, 1);
						dy = getRandom(-1, 1);
					}
					ix = wx + dx;
					iy = wy + dy;
				} while ((dx == 0 && dy == 0)// 범위 검사 할 것!(벽)
						|| (ix < 0 || ix >= WIDTH || iy < 0 || iy >= HEIGHT) || (screen[ix][iy] != null));
				screen[ix][iy] = players.get(i);
				if (DEBUGMODE)
					System.out.println("player " + i + " is intend to go to (" + ix + "," + iy + ")");
				players.get(i).loseContending(ix, iy);// apply stamina, set position, ballowner false

			}
		}

	}

	private void passCheck() {
		if (ballOwner != null) {
			Player passtarget = null;
			if ((ballOwner.getId() >= 0 && ballOwner.getId() < 6) && orderA.passto >= 0
					&& ballOwner != playerOfTeamA[orderA.passto])// a team case
				passtarget = playerOfTeamA[orderA.passto];
			else if ((ballOwner.getId() >= 6 && ballOwner.getId() < 12) && orderB.passto >= 0
					&& ballOwner != playerOfTeamB[orderB.passto])// b team case
				passtarget = playerOfTeamB[orderB.passto];
			else {
				return;
			}

			if (DEBUGMODE)
				System.out.println("pass from " + ballOwner.getId() + " to " + passtarget.getId());

			// ballOwner -> passtarget
			// 경로상에 플레이어 확인
			// 기울기와 y절편 (y = mx + n)
			double m, n;
			if (ballOwner.getX() == passtarget.getX()) {
				// 직선의 방정식이 | 인경우
				int yV = (passtarget.getY() - ballOwner.getY()) / Math.abs(passtarget.getY() - ballOwner.getY());
				for (int i = ballOwner.getY() + yV; i < passtarget.getY() - yV; i += yV) {
					if (screen[ballOwner.getX()][i] != null) {
						// intercepted by screen[ballOwner.x][i]
						System.out.println("intercepted!!!");
						ballOwner.throwBall();
						Player interceptPlayer = (Player) screen[ballOwner.getX()][i];
						interceptPlayer.getBall(ball);
						ballOwner = interceptPlayer;
						return;
					}
				}
			} else {
				m = (ballOwner.getY() - passtarget.getY()) / (ballOwner.getX() - passtarget.getX());
				n = ballOwner.getY() - m * ballOwner.getX();

				int xV = (passtarget.getX() - ballOwner.getX()) / Math.abs(passtarget.getX() - ballOwner.getX());

				if (DEBUGMODE)
					System.out.println("m:" + m + ",n:" + n);
				for (int i = ballOwner.getX() + xV; i != passtarget.getX() - xV; i += xV) {
					int fi = (int) (m * i + n + 0.4);
					if (DEBUGMODE) {
						System.out.println("src : (" + ballOwner.getX() + "," + ballOwner.getY() + ") dest : ("
								+ passtarget.getX() + "," + passtarget.getY() + ")");
						System.out.println("---" + i + "," + fi + "---");
					}
					if (screen[i][fi] != null) {
						// intercepted by screen[i][fi]
						System.out.println("intercepted!!!");
						ballOwner.throwBall();
						Player interceptPlayer = (Player) screen[i][fi];
						interceptPlayer.getBall(ball);
						ballOwner = interceptPlayer;
						return;
					}
				}
			}

			// 거리에 다라 패스 성공률 적용
			// 일단 인터셉트는 안당함
			int d = ballOwner.getDistance(passtarget);
			if (d > 5) {
				int r = getRandom(0, ballOwner.getPass() + d * 5 - 1);
				if (r < ballOwner.getPass()) {
					// pass success
					ballOwner.throwBall();
					passtarget.getBall(ball);
					ballOwner = passtarget;
				} else {
					// pass fail
					ballOwner.throwBall();
//					passtarget x,y로 공의 위치 재설정
					int ix, iy;
					do {
						ix = passtarget.getX() + getRandom(-2, 2);
						iy = passtarget.getY() + getRandom(-2, 2);
					} while ((ix == passtarget.getX() && iy == passtarget.getY())
							|| (ix < 0 || ix >= WIDTH || iy < 0 || iy >= HEIGHT));
					ball.fly2(ix, iy);
					ballOwner = null;
				}
			} else {
				ballOwner.throwBall();
				passtarget.getBall(ball);
				ballOwner = passtarget;
			}
//			// 패스 성공 시
//			ballOwner.throwBall();
//			passtarget.getBall(ball);
//			ballOwner = passtarget;
//			// 패스 실패 시
//			ballOwner.throwBall();
//			passtarget x,y로 공의 위치 재설정
//			ballOwner = null;
		}
	}

	private void ballCheck() {
		if (ballOwner != null) {
			ball.dribbled(ballOwner);
		} else {
			if (screen[ball.getX()][ball.getY()] != null) {
				ballOwner = (Player) screen[ball.getX()][ball.getY()];
				ballOwner.getBall(ball);
			}
		}
	}
	// -------------------------------------------------------------------------------------------------------------------------------//

	public int getRandom(int m, int n) {
		int d = Math.abs(m - n) + 1;
		return (int) (Math.random() * d) + m;
	}

	private boolean throwCoinWithProbability(int probability) {// true일 확률 probability%(0~100)
		if (probability < 0 || probability > 100) {
			System.out.println("probability error!!!!");
			return false;
		}

		int r = (int) (Math.random() * 100);
		if (r < probability) {
			return true;
		} else {
			return false;
		}
	}

	// -------------------------------------------------------------------------------------------------------------------------------//

	private void printScr() {

		// test section

		if (DEBUGMODE) {
			System.out.println(nTurn + "th Turn");
			for (int i = 0; i < 6; i++) {
				System.out.println((char) ('a' + i) + "(" + i + ")" + ":(" + playerOfTeamA[i].x + ","
						+ playerOfTeamA[i].y + ")  \t" + (char) ('a' + i) + "(" + i + ")" + ":(" + orderA.dx[i] + ","
						+ orderA.dy[i] + ")  \t" + (char) ('a' + i) + "(" + (i) + ")" + ":(str:"
						+ playerOfTeamA[i].getStrength() + ",pass:" + playerOfTeamA[i].getPass() + ",stm:"
						+ playerOfTeamA[i].getStamina() + ")");
			}
			for (int i = 0; i < 6; i++) {
				System.out.println((char) ('1' + i) + "(" + (i + 6) + ")" + ":(" + playerOfTeamB[i].x + ","
						+ playerOfTeamB[i].y + ")  \t" + (char) ('1' + i) + "(" + (i + 6) + ")" + ":(" + orderB.dx[i]
						+ "," + orderB.dy[i] + ")  \t" + (char) ('1' + i) + "(" + (i + 6) + ")" + ":(str:"
						+ playerOfTeamB[i].getStrength() + ",pass:" + playerOfTeamB[i].getPass() + ",stm:"
						+ playerOfTeamB[i].getStamina() + ")");

			}

			if (ballOwner != null)
				System.out.println("Ball owner : " + ballOwner.getId());
			else
				System.out.println("Ball owner : none");
		}

		if (ballOwner == null) {
			screen[ball.x][ball.y] = ball;
		}

		System.out.println("Score " + score_a + " : " + score_b);
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		for (int i = 0; i < HEIGHT; i++) {
			if (i < HEIGHT / 2 - 5 || i >= HEIGHT / 2 + 5)
				System.out.print("%");
			else
				System.out.print(" ");
			for (int j = 0; j < WIDTH; j++) {
				if (screen[j][i] == null) {
					System.out.print(" ");
				} else {
					System.out.print(screen[j][i]);
				}
			}
			if (i < HEIGHT / 2 - 5 || i >= HEIGHT / 2 + 5)
				System.out.println("%");
			else
				System.out.println(" ");
		}
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
	}

	// MARK debug mode = 5, release mode = 50
	private void clearScr() {
		int line = 5;
		for (int i = 0; i < line; i++) {
			System.out.println();
		}

		screen = new Unit[WIDTH][HEIGHT];
	}

	private int goalCheck() {// goal 아니면 -1, a팀의 골이면 1, b팀의 골이면 2
		if (ballOwner != null) {
			if (ballOwner.getId() >= 0 && ballOwner.getId() < 6) {
				if (ballOwner.getX() == WIDTH - 1
						&& (ballOwner.getY() >= HEIGHT / 2 - 5 && ballOwner.getY() < HEIGHT / 2 + 5)) {
					score_a++;
					return 1;
				}
			} else if (ballOwner.getId() >= 6 && ballOwner.getId() < 12) {
				if (ballOwner.getX() == 0
						&& (ballOwner.getY() >= HEIGHT / 2 - 5 && ballOwner.getY() < HEIGHT / 2 + 5)) {
					score_b++;
					return 2;
				}
			}
		}
		return -1;
	}

	private int endCheck() {
		if (nTurn == 1000)
			return 1;
		else
			return 0;
	}

	// MARK waiting time apply
	private void delay(int n) {
		try {
			Thread.sleep(n);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// waiting time parsing
		_Game game = new _Game();
		Scanner sc = new Scanner(System.in);
		sc.nextLine();
		game.run();
	}

}
