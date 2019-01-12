package lugbygame;

public class test {
	public static int getRandom(int m, int n) {
		int d = Math.abs(m - n) + 1;
		return (int) (Math.random() * d) + m;
	}

	private static boolean throwCoinWithProbability(int probability) {// trueÀÏ È®·ü probability%(0~100)
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

	public static void main(String[] args) {
		// for (int i = 0; i < 100000; i++) {
		// System.out.println(test.throwCoinWithProbability(50));
		// }
		// System.out.println(Math.pow(-2, 2));
		// int x = 5, y = 6;
		// int kx = 2, ky = 10;
		// int d = (int) Math.sqrt(Math.pow((x - kx), 2) + Math.pow(y - ky, 2));
		// System.out.println(d);

		int x1, y1;
		int x2, y2;
	}
}
