
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

	protected int getUnitCode(int row, int col) {
		switch (currentMap.get(row).get(col).charAt(0)) {
		case 's':
			return 0;
		case 'b':
			return 1;
		case 't':
			return 2;
		case 'c':
			return 3;
		case 'W':
			return 12 + getUnitAngle(row, col);
		case 'w':
			return 3 + getUnitAngle(row, col);
		default:
			return -1;
		}
	}

	protected int getUnitAngle(int row, int col) {
		if (currentMap.get(row).get(col).charAt(1) == '-')
			return -1;
		return currentMap.get(row).get(col).charAt(1) - '0';
	}

}