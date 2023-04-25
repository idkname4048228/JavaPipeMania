import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {

	ArrayList<ArrayList<ArrayList<String>>> mapStorage = new ArrayList<>();
	ArrayList<ArrayList<String>> currentMap = new ArrayList<>();
	GameMap map = null;
	int nowMapIndex = 0;

	Game() {
		mapStorage = readMapFile();
		nowMapIndex = 0;
		currentMap = mapStorage.get(nowMapIndex);
		map = new GameMap(currentMap);
	}

	protected ArrayList<ArrayList<ArrayList<String>>> readMapFile() {
		File f = new File("./src/GameMapTxtFiles/map.txt");
		Scanner sc = null;
		ArrayList<String> tmpArray = new ArrayList<String>();
		ArrayList<ArrayList<ArrayList<String>>> returnList = new ArrayList<>();
		try {
			sc = new Scanner(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (sc.hasNext()) {
			String tmp = sc.nextLine();
			if (tmp.length() == 0) {
				ArrayList<ArrayList<String>> tmpStrArray = new ArrayList<>();
				for (int i = 0; i < tmpArray.size(); i++) {
					ArrayList<String> rowArray = new ArrayList<String>();
					for (String unit : tmpArray.get(i).split(" ")) {
						rowArray.add(unit);
					}
					tmpStrArray.add(rowArray);
				}
				tmpArray.clear();
				returnList.add(tmpStrArray);
			} else {
				tmpArray.add(tmp);
			}
		}
		if (tmpArray.size() != 0) {
			ArrayList<ArrayList<String>> tmpStrArray = new ArrayList<>();
			for (int i = 0; i < tmpArray.size(); i++) {
				ArrayList<String> rowArray = new ArrayList<String>();
				for (String unit : tmpArray.get(i).split(" ")) {
					rowArray.add(unit);
				}
				tmpStrArray.add(rowArray);
			}
			tmpArray.clear();
			returnList.add(tmpStrArray);
		}
		return returnList;
	}

	ArrayList<ArrayList<String>> getCurrentMap() {
		return currentMap;
	}

	void changeMap(int change) {
		nowMapIndex += change;
		currentMap = mapStorage.get(nowMapIndex);
	}

}
