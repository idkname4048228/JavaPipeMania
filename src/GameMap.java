
import java.util.ArrayList;
import java.util.Random;

public class GameMap {
	// 遊戲地圖相關的屬性
	ArrayList<ArrayList<String>> currentMap = null;// 當前地圖(文字檔)
	ArrayList<int[]> start = new ArrayList<>();// 地圖的起點(有水的水庫)
	ArrayList<int[]> end = new ArrayList<>();// 地圖的終點(沒水的水庫)

	int width, height;// 高跟寬

	protected int getWidth() {// 回傳寬度
		return this.width;
	}

	protected int getHeight() {// 回傳高度
		return this.height;
	}

	GameMap(ArrayList<ArrayList<String>> map) {// 建構子，參數是二維的 String 的 ArrayList
		this.init(map);// 初始化
	}

	protected void init(ArrayList<ArrayList<String>> map) {// 初始化用的 method ，參數是二維的 String 的 ArrayList
		width = map.get(0).size();// 取得寬度
		height = map.size();// 取得高度
		this.start = new ArrayList<>();// 初始化起點
		this.end = new ArrayList<>();// 初始化終點
		this.currentMap = new ArrayList<>();// 初始化當前地圖

		// 把參數的二維矩陣複製一份，並把起點及終點的座標儲存起來
		for (int row = 0; row < height; row++) {
			ArrayList<String> tmp = new ArrayList<>();
			for (int col = 0; col < width; col++) {

				if (map.get(row).get(col).charAt(0) == 'W') {// 儲存起點座標
					start.add(new int[] { row, col });
				}
				if (map.get(row).get(col).charAt(0) == 'w') {// 儲存終點座標
					end.add(new int[] { row, col });
				}

				if (map.get(row).get(col).charAt(1) == '5') {// 如果角度是隨機的，隨機生成一個合法的角度代號
					int randAngle = new Random().nextInt(4) + 1;
					tmp.add(map.get(row).get(col).substring(0, 1) + Integer.toString(randAngle));// 加進暫時的 ArrayList
				} else
					tmp.add(map.get(row).get(col));// 加進暫時的 ArrayList
			}
			currentMap.add(tmp);// 把暫時的 ArrayList 加進當前的地圖，完成複製的工作
		}
	}

	protected int getImagePath(int row, int col, boolean withWater) {// 針對特定座標的水管代號及角度，回傳圖片路徑的 index
		switch (currentMap.get(row).get(col).charAt(0)) {
		case 's':
			return withWater ? 8 : 0;
		case 'b':
			return withWater ? 9 : 1;
		case 't':
			return withWater ? 10 : 2;
		case 'c':
			if (withWater) {
				if (getUnitAngle(row, col) == 3)
					return 12;
				else
					return 11;
			} else {
				return 3;
			}
		case 'W':
			return 12 + getUnitAngle(row, col);
		case 'w':
			return 3 + getUnitAngle(row, col) + (withWater ? 9 : 0);
		default:
			return -1;
		}
	}

	protected String getUnitCode(int row, int col) {// 回傳特定座標的水管代號
		return currentMap.get(row).get(col).substring(0, 1);
	}

	protected int getUnitAngle(int row, int col) {// 回傳角度代號，如果是空的則是 -1
		if (currentMap.get(row).get(col).charAt(1) == '-')
			return -1;
		return currentMap.get(row).get(col).charAt(1) - '0';
	}

	protected void rotateUnit(int row, int col, boolean right) {// 針對特定方向(左右)，更改特定座標的角度代號
		String unit = getUnitCode(row, col);
		int originalAngle = getUnitAngle(row, col);

		currentMap.get(row).set(col, unit + Integer.toString((originalAngle - 1 + (right ? 1 : -1) + 4) % 4 + 1));
	}

	protected void setCrossAngle(int row, int col, int angle) {// 設定特定座標的角度
		currentMap.get(row).set(col, getUnitCode(row, col) + Integer.toString(angle));
	}

	protected ArrayList<int[]> getStartPipe() {// 回傳起點的座標 ArrayList
		return start;
	}

	protected ArrayList<int[]> getEndPipe() {// 回傳終點的座標 ArrayList
		return end;
	}

	protected boolean[] canFlowDirections(int row, int col) {// 判斷並回傳特定座標的是否可流動的方位(上下左右)陣列
		boolean[] directions = new boolean[] { false, false, false, false }; // up, right, down, left
		String pipeCode = getUnitCode(row, col);
		int degree = getUnitAngle(row, col);

		switch (pipeCode) {
		case "s":
			directions[degree - 1] = true;
			directions[(degree + 1) % 4] = true;
			break;
		case "b":
			directions[degree - 1] = true;
			directions[degree % 4] = true;
			break;
		case "t":
			directions[degree - 1] = true;
			directions[degree % 4] = true;
			directions[(degree + 2) % 4] = true;
			break;
		case "c":
			directions[0] = true;
			directions[1] = true;
			directions[2] = true;
			directions[3] = true;
			break;
		default:
			directions[degree - 1] = true;
		}

		return directions;
	}

}