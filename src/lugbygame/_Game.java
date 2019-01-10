package lugbygame;

import java.util.ArrayList;

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

	Strategy teamA;
	Strategy teamB;
	private int score_a;
	private int score_b;
	Player[] players;
	Player[] playerOfTeamA;
	Player[] playerOfTeamB;
	Ball ball;

	private char[][] screen;

	private Status statusA;
	private Status statusB;
	private Order orderA;
	private Order orderB;

	public _Game() {
		this(1000, 1000);
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
		updateScr();
		printScr();
		// wait
		while (true) {
			nTurn++;
			clearScr();// clear screen

			onetick();// get order
			applyOrder(orderA, orderB);// apply order
			updateScr();// set screen
			printScr();// print screen with print form

			if (goalCheck() == 1) {// if goal
				eventTurn = true;
				initialize(1);
			} else if (goalCheck() == 2) {
				eventTurn = true;
				initialize(2);
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

		screen = new char[WIDTH][HEIGHT];

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
			} else {
				ball = new Ball(playerOfTeamB[2].x, playerOfTeamB[2].y);
			}
		} else if (starter == 1) {
			ball = new Ball(playerOfTeamA[2].x, playerOfTeamA[2].y);
		} else if (starter == 2) {
			ball = new Ball(playerOfTeamB[2].x, playerOfTeamB[2].y);
		}

	}

	public void onetick() {
		statusA.setStatus(generateUnitInfo(false), nTurn);
		teamA.execute(statusA, orderA);
		statusB.setStatus(generateUnitInfo(true), nTurn);
		teamB.execute(statusB, orderB);
		for (int i = 0; i < 6; i++) {
			System.out.println((char) ('a' + i) + ":(" + orderA.dx[i] + "," + orderA.dy[i] + ")\t" + (char) ('1' + i)
					+ ":(" + orderB.dx[i] + "," + orderB.dy[i] + ")");
		}
	}

	private Unit[] generateUnitInfo(boolean mirror) {
		Unit[] result = new Unit[13];

		if (mirror) {// for team b
			result[0] = new Unit(ball);
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
		}
		for (Player p : playerOfTeamB) {
			p.move(ob.dx[p.getNumber()], ob.dy[p.getNumber()]);
		}
		contendCheck();
	}

	private void contendCheck() {// true:all contend resolved //false:there is a contend
		for (int i = 0; i < NPLAYER * 2; i++) {
			ArrayList<Player> contendingPlayers = new ArrayList<Player>();
			for (int j = 0; j < NPLAYER * 2; j++) {
				if (i != j && players[i].isSamePositionWith(players[j])) {
					contendingPlayers.add(players[j]);
				}
			}
			if (contendingPlayers.size() >= 1) {
				contend(contendingPlayers);
			}
		}
	}

	private void contend(ArrayList<Player> players) {// 충돌
		System.out.println("Contend!!");
		int mother = 0;
		for (Player p : players) {
			mother += p.getStrength();
		}
		for (int i = 0; i < players.size(); i++) {
			if (mother != 0) {
				if (i == players.size() - 1) {
					players.get(i).winContending();
				}
				if (throwCoinWithProbability(players.get(i).getStrength() / mother)) {
					players.get(i).winContending();
				} else {
					players.get(i).loseContending();
				}
			} else {
				players.get(i).loseContending();
			}
		}

	}

	public int getRandom(int m, int n) {
		int d = Math.abs(m - n + 1);
		return (int) (Math.random() * d) + 1;
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

	private void updateScr() {
		for (int i = 0; i < NPLAYER; i++) {
			screen[playerOfTeamA[i].x][playerOfTeamA[i].y] = (char) ('a' + i);
			screen[playerOfTeamB[i].x][playerOfTeamB[i].y] = (char) ('1' + i);
		}
	}

	private void printScr() {
		System.out.println(nTurn + "th Turn");
		// test section start
		for (int i = 0; i < 6; i++) {
			System.out.println((char) ('a' + i) + ":(" + playerOfTeamA[i].x + "," + playerOfTeamA[i].y + ")\t"
					+ (char) ('1' + i) + ":(" + playerOfTeamB[i].x + "," + playerOfTeamB[i].y + ")");
		}
		// test section end
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		for (int i = 0; i < HEIGHT; i++) {
			if (i < HEIGHT / 2 - 5 || i >= HEIGHT / 2 + 5)
				System.out.print("%");
			else
				System.out.print(" ");
			for (int j = 0; j < WIDTH; j++) {
				if (screen[j][i] == 0) {
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

		screen = new char[WIDTH][HEIGHT];
	}

	private int goalCheck() {// goal 아니면 -1, a팀의 골이면 1, b팀의 골이면 2
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
		// waiting time �쟻�슜�븷 寃�!
		_Game game = new _Game();
		game.run();
	}

}
