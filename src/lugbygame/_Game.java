package lugbygame;

//Rule
//
//

//main class
public class _Game {
	private int normalWaitTime;
	private int eventWaitTime;
	
	private int score_a;
	private int score_b;
	
	public _Game() {
		this.normalWaitTime = 1000;
		this.eventWaitTime = 1000;
	}
	
	public _Game(int normalWaitTime,int eventWaitTime) {
		this.normalWaitTime = normalWaitTime;
		this.eventWaitTime = eventWaitTime;
	}
	
	public void run(int normalWaitTime, int eventWaitTime) {
		this.initialize();
		while(true) {
			
			
		}
		
	}
	
	private void initialize() {
		
	}
	
	private void applyStroke(Stroke sa, Stroke sb) {
		
	}
	
	private int goalCheck() {
		return 0;
	}
	
	private void printScr() {
		
	}
	
	private void clearScr() {
		
	}

	
	
	
	public static void main(String[] args) {
		// waiting time 적용할 것!
		_Game game = new _Game();
		game.run();
	}

}
