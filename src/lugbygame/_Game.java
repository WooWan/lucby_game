package lugbygame;

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

	TeamA teamA;
	TeamB teamB;
	private int score_a;
	private int score_b;
	Player[] playerOfTeamA;
	Player[] playerOfTeamB;

	private char[][] screen;
	
	private Status statusA;
	private Status statusB;
	private Order orderA;
	private Order orderB;
	
	public _Game() {
		this.normalWaitTime = 1000;
		this.eventWaitTime = 1000;
	}

	public _Game(int normalWaitTime, int eventWaitTime) {
		this.normalWaitTime = normalWaitTime;
		this.eventWaitTime = eventWaitTime;
	}

	public void run() {
		initialize();
		updateScr();
		printScr();
		//wait
		while (true) {
			clearScr();
			
			
		}
	}

	private void initialize() {
		teamA = new TeamA();
		teamB = new TeamB();
		score_a = 0;
		score_b = 0;
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
	}

	private void applyOrder(Order sa, Order sb) { //main process

	}

	private int goalCheck() {
		return 0;
	}

	private void updateScr() {
		for (int i = 0; i < NPLAYER; i++) {
			screen[playerOfTeamA[i].x][playerOfTeamA[i].y] = (char) ('a' + i);
			screen[playerOfTeamB[i].x][playerOfTeamB[i].y] = (char) ('1' + i);
		}
	}

	private void printScr() {
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

	// MARK debug mode = 3, release mode = 50
	private void clearScr() {
		int line = 3;
		for (int i = 0; i < line; i++) {
			System.out.println();
		}
	}

	// MARK waiting time apply
	public static void main(String[] args) {
		// waiting time �쟻�슜�븷 寃�!
		_Game game = new _Game();
		game.run();
	}

}
