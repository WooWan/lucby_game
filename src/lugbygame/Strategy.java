package lugbygame;

public interface Strategy {
	// 현재 상태(currentStatus)에 따라서 다른 전략(Order)을 생성
	// 전략(Order)은 각 플레이어의 다음 이동 벡터와 패스 대상에 대한 정보를 포함
	public abstract void execute(Status currentStatus, Order resultOrder);

	public default int getRandom(int m, int n) {
		int d = Math.abs(m - n) + 1;
		return (int) (Math.random() * d) + m;
	}
}