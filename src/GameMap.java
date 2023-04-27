
import java.util.ArrayList;

public class GameMap {
	ArrayList<ArrayList<String>> currentMap = null;
	ArrayList<int[]> start = new ArrayList<>();
	ArrayList<int[]> end = new ArrayList<>();

	int width, height;
	boolean check = false;
	boolean win = false;

	ArrayList<int[]> changes = null;

	protected ArrayList<int[]> getChanges() {
		return this.changes;
	}

	protected int getWidth() {
		return this.width;
	}

	protected int getHeight() {
		return this.height;
	}

	GameMap(ArrayList<ArrayList<String>> map) {
		this.init(map);
	}

	protected void init(ArrayList<ArrayList<String>> map) {
		width = map.get(0).size();
		height = map.size();
		win = false;
		changes = null;
		this.start = new ArrayList<>();
		this.end = new ArrayList<>();
		this.currentMap = new ArrayList<>();
		for (int row = 0; row < height; row++) {
			ArrayList<String> tmp = new ArrayList<>();
			for (int col = 0; col < width; col++) {
				tmp.add(map.get(row).get(col));
				if (map.get(row).get(col).charAt(0) == 'W') {
					start.add(new int[] { row, col });
				}
				if (map.get(row).get(col).charAt(0) == 'w') {
					end.add(new int[] { row, col });
				}
			}
			currentMap.add(tmp);
		}
	}

	protected int getImagePath(int row, int col, boolean withWater, boolean withSecondWater) {
		switch (currentMap.get(row).get(col).charAt(0)) {
		case 's':
			return withWater ? 8 : 0;
		case 'b':
			return withWater ? 9 : 1;
		case 't':
			return withWater ? 10 : 2;
		case 'c':
			if (withWater && withSecondWater) {
				return 12;
			} else if (withWater || withSecondWater) {
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

	protected String getUnitCode(int row, int col) {
		return currentMap.get(row).get(col).substring(0, 1);
	}

	protected int getUnitAngle(int row, int col) {
		if (currentMap.get(row).get(col).charAt(1) == '-')
			return -1;
		return currentMap.get(row).get(col).charAt(1) - '0';
	}

	protected void setUnitAngle(int row, int col, boolean right) {
		String unit = getUnitCode(row, col);
		int originalAngle = getUnitAngle(row, col);

		currentMap.get(row).set(col, unit + Integer.toString((originalAngle - 1 + (right ? 1 : -1) + 4) % 4 + 1));
	}

	protected ArrayList<int[]> getStartPipe() {
		return start;
	}

	protected ArrayList<int[]> getEndPipe() {
		return end;
	}

	protected boolean[] canFlowDirections(int row, int col) {
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
			directions[degree] = true;
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