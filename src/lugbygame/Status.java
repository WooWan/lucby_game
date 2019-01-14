package lugbygame;

//current status
public class Status {
	int ballOwner;
	int turn;
	// 플레이어 위치정보
	// -1 : 공 //0~5 : 우리팀 //6~11 : 상대팀
	Unit[] mine;
	Unit[] enemy;
	Unit ball;

	public Status() {
		mine = new Unit[6];
		enemy = new Unit[6];
	}

	public void setStatus(Unit[] info, int ballOwner, int turn) {
		ball = info[0];
		System.arraycopy(info, 1, mine, 0, 6);
		System.arraycopy(info, 7, enemy, 0, 6);

		this.ballOwner = ballOwner;
		
		this.turn = turn;
	}
}
