import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("deprecation")
public class Game extends Observable {
// 遊戲的相關屬性
	ArrayList<ArrayList<ArrayList<String>>> mapStorage = new ArrayList<>();// 存放地圖原檔的 ArrayList
	ArrayList<ArrayList<String>> currentMap = new ArrayList<>(); // 當前的地圖原檔
	ArrayList<String> pipeImagePath = new ArrayList<>(Collections.nCopies(17, null));// 存放圖片路徑的 ArrayList
	ArrayList<GameMap> gameMaps = new ArrayList<>();// 存放 GamaMap 的 ArrayList
	int nowMapIndex = 0;// 現在的 GameMap 在 gameMaps 內的 index
	protected boolean rotateRight = true;// 是否向右旋轉
	boolean playerLose = false;// 玩家是否落敗
	int currentSteps = 0;// 玩家當前的步數

	Game() {
		mapStorage = readMapFile();// 使用讀檔的方式取得全部的地圖原檔
		nowMapIndex = 0;// 初始化
		currentMap = mapStorage.get(nowMapIndex);// 取得當前的地圖原檔
		gameMaps = new ArrayList<>(Collections.nCopies(mapStorage.size(), null));// 有幾個題目(地圖檔用空行分開)，就鄉初始化幾個空間，等等要放
																					// GameMap 的
		// 以下是圖片檔路徑的初始化
		pipeImagePath.set(0, "./img/pipeImage/straightPipe.png");
		pipeImagePath.set(1, "./img/pipeImage/bentPipe.png");
		pipeImagePath.set(2, "./img/pipeImage/tPipe.png");
		pipeImagePath.set(3, "./img/pipeImage/crossPipe.png");
		pipeImagePath.set(4, "./img/pipeImage/upInWaterStore.png");
		pipeImagePath.set(5, "./img/pipeImage/rightInWaterStore.png");
		pipeImagePath.set(6, "./img/pipeImage/downInWaterStore.png");
		pipeImagePath.set(7, "./img/pipeImage/leftInWaterStore.png");
		pipeImagePath.set(8, "./img/pipeImage/straightPipeWithWater.png");
		pipeImagePath.set(9, "./img/pipeImage/bentPipeWithWater.png");
		pipeImagePath.set(10, "./img/pipeImage/tPipeWithWater.png");
		pipeImagePath.set(11, "./img/pipeImage/crossPipeWithoneWater.png");
		pipeImagePath.set(12, "./img/pipeImage/crossPipeWithtwoWater.png");
		pipeImagePath.set(13, "./img/pipeImage/upInWaterStoreWithWater.png");
		pipeImagePath.set(14, "./img/pipeImage/rightInWaterStoreWithWater.png");
		pipeImagePath.set(15, "./img/pipeImage/downInWaterStoreWithWater.png");
		pipeImagePath.set(16, "./img/pipeImage/leftInWaterStoreWithWater.png");
	}

	protected ArrayList<ArrayList<ArrayList<String>>> readMapFile() {// 讀地圖檔，並把他們都放到 mapStorage 裡面
		File f = new File("./GameMapTxtFiles/map.txt");
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

	ArrayList<ArrayList<String>> getCurrentMap() {// 取得當前地圖文字檔
		return currentMap;
	}

	int nowSteps() {// 取得當前步數
		return currentSteps;
	}

	@SuppressWarnings("deprecation")
	void resetSteps() {// 重置玩家的步數
		playerLose = false;// 重置玩家的勝負狀態
		currentSteps = 0;// 重置步數
		setChanged();
		notifyObservers(currentSteps);// 並 notify 讓 Demo class 可以更新步數
	}

	void checkSteps(int limit) {// 檢查玩家的步數是否超過限制
		playerLose = (currentSteps >= limit);
	}

	boolean isPlayerWin() {// 回傳玩家是否勝利的布林值
		return !playerLose;
	}

	boolean isLastMap(int index) {// 回傳是否傳進來的索引是最後一張地圖的布林值
		return index == mapStorage.size() - 1;
	}

	void changeMap(int change) {// 更新地圖文字檔，參數是正負一，代表往上一個或往後一個
		// 更伈索引，並通過計算防止超出邊界
		nowMapIndex += change;
		nowMapIndex += mapStorage.size();
		nowMapIndex %= mapStorage.size();
		currentMap = mapStorage.get(nowMapIndex);// 更新當前地圖文字檔
	}

	int getMapIndex() {// 回傳當前地圖所在的索引值
		return nowMapIndex;
	}

	boolean checkEnd(ArrayList<int[]> endPipes, ArrayList<ArrayList<Boolean>> waterMap) {// 檢查是否終點都有水，第一個參數是終點的座標，第二個是有水的水管的二維地圖
		boolean win = true;// 預設是全都有水，也就是獲勝的條件
		for (int i = 0; i < endPipes.size(); i++) {
			int pipeRow = endPipes.get(i)[0];
			int pipeCol = endPipes.get(i)[1];
			win &= waterMap.get(pipeRow).get(pipeCol);// 去檢查 waterMap 內終點的座標是否有睡
			if (!win)
				return false;// 如果有終點是沒有水的，那就是失敗，並回傳

		}
		return true;// 代表成功
	}

	void setRotate(JPanel gameMapPanel, boolean rotate) {// 設定水管是否可以旋轉，參數 rotate 是控制旋轉的布林值
		Component[] components = gameMapPanel.getComponents();
		for (Component component : components) {
			if (component instanceof JLabel) {
				JLabel element = (JLabel) component;
				element.setDisabledIcon(element.getIcon());
				element.setEnabled(rotate);// 停止或啟動水管的旋轉功能
			}
		}
	}

	boolean check(JPanel gameMapPanel) {// 檢查玩家是否解題成功

		GameMap map = gameMaps.get(nowMapIndex);// 取得需要確認的遊戲地圖

		// 取得行跟列的最大值
		final int colMax = map.getWidth();
		final int rowMax = map.getHeight();

		final int[] controller = { 0 }; // 使用陣列將 controller 變成 effectively final 變數

		ArrayList<ArrayList<Boolean>> waterMap = new ArrayList<>();// 初始化 waterMap 也就是有水的水管的 ArrayList
		for (int i = 0; i < rowMax; i++) {
			ArrayList<Boolean> tmp = new ArrayList<>();
			for (int j = 0; j < colMax; j++) {
				tmp.add(false);
			}
			waterMap.add(tmp);
		}

		final ArrayList<int[]> waterPipes = new ArrayList<>(map.getStartPipe()); // 使用 ArrayList 初始化 waterPipes 變數
		final ArrayList<int[]> endPipes = map.getEndPipe();// 取得終點的座標
		final boolean[] noWasteWater = { true }; // 成功的條件，不能浪費水，有水的水管的每個出口都要有其他的水管去接

		Timer timer = null;
		CountDownLatch latch = new CountDownLatch(1);// 等待計時器執行完畢，才能回傳玩家是否成功
		timer = new Timer(500, new ActionListener() {// 設定計時器，每 0.5 秒執行一次，以此模仿水流的效果
			@Override
			public void actionPerformed(ActionEvent e) {// 使用 BFS 進行每次水管流向的確認
				ArrayList<int[]> tmpPipe = new ArrayList<>();// 等等會有水的水管
				for (int i = 0; i < waterPipes.size(); i++) {// 遍歷 waterPipe 內的每個水管，裡面的每個水關都是有水流過的
					// 取得座標
					int pipeRow = waterPipes.get(i)[0];
					int pipeCol = waterPipes.get(i)[1];

					waterMap.get(pipeRow).set(pipeCol, true);// 在 waterMap 內標記該座有水流過
					changeImage(gameMapPanel, pipeRow, pipeCol, map);// 更改座標的圖片
					if (map.getUnitCode(pipeRow, pipeCol).equals("w"))// 如果是空的水庫，代表他不會有其他方向可以流
						continue;

					boolean stillFlow = true;// 檢查水管能流動的方位是否都有可以相接的水管
					boolean[] canFlow = map.canFlowDirections(pipeRow, pipeCol);// 獲取當前座標可流動的方向

					if (map.getUnitCode(pipeRow, pipeCol).equals("c")) {// 如果當前座標是交叉水管
						if (map.getUnitAngle(pipeRow, pipeCol) == 1 || map.getUnitAngle(pipeRow, pipeCol) == 3) {// 並且水是左右流動

							if (pipeCol - 1 >= 0 && map.canFlowDirections(pipeRow, pipeCol - 1)[1]
									&& !waterMap.get(pipeRow).get(pipeCol - 1)) {// 檢查水能不能往左邊流動(左邊可以接水以及左邊沒水)
								tmpPipe.add(new int[] { pipeRow, pipeCol - 1 });// 加進下次需要遍歷的 ArrayList
								stillFlow = false;// 已把水傳下去
							}
							/*
							 * 下面以此類推
							 * 
							 * 
							 * 
							 */
							if (pipeCol + 1 < colMax && map.canFlowDirections(pipeRow, pipeCol + 1)[3]
									&& !waterMap.get(pipeRow).get(pipeCol + 1)) {
								tmpPipe.add(new int[] { pipeRow, pipeCol + 1 });
								stillFlow = false;
							}
						}
						if (map.getUnitAngle(pipeRow, pipeCol) == 2 || map.getUnitAngle(pipeRow, pipeCol) == 3) {
							if (pipeRow - 1 >= 0 && map.canFlowDirections(pipeRow - 1, pipeCol)[2]
									&& !waterMap.get(pipeRow - 1).get(pipeCol)) {
								tmpPipe.add(new int[] { pipeRow - 1, pipeCol });
								stillFlow = false;
							}
							if (pipeRow + 1 < rowMax && map.canFlowDirections(pipeRow + 1, pipeCol)[0]
									&& !waterMap.get(pipeRow + 1).get(pipeCol)) {
								tmpPipe.add(new int[] { pipeRow + 1, pipeCol });
								stillFlow = false;
							}
						}

					}

					if (canFlow[0] && pipeRow - 1 >= 0 && !(map.getUnitCode(pipeRow - 1, pipeCol).equals("-")
							|| map.getUnitCode(pipeRow, pipeCol).equals("c"))) {// 如果上面還有格子，而且不是交叉水管，也不是空的
						if (!waterMap.get(pipeRow - 1).get(pipeCol) && map.canFlowDirections(pipeRow - 1, pipeCol)[2]) { // 上面沒水，且可以從下面流上去
							if (map.getUnitCode(pipeRow - 1, pipeCol).equals("c")) // 上面沒水，但是是交叉水管
								map.setCrossAngle(pipeRow - 1, pipeCol, 2);// 把交叉水管通水的角度調整為 90 度，以期符合水上下流的視覺效果
							tmpPipe.add(new int[] { pipeRow - 1, pipeCol });// 加進下次需要遍歷的 ArrayList
							stillFlow = false;// 已把水傳下去
						} else if (waterMap.get(pipeRow - 1).get(pipeCol)
								&& map.getUnitCode(pipeRow - 1, pipeCol).equals("c")) { // 如果上面是交叉水管且有水，那他必定是 1(水平流動)
																						// 2(垂直流動) 3(交叉流動)
							if (map.getUnitAngle(pipeRow - 1, pipeCol) == 1) { // 將 1(水平流動) 的交叉水管改為 3(交叉流動)
								map.setCrossAngle(pipeRow - 1, pipeCol, 3);// 把交叉水管通水的角度代號調整為 3 ，以期符合水交叉流的視覺效果
								tmpPipe.add(new int[] { pipeRow - 1, pipeCol });// 加進下次需要遍歷的 ArrayList
								stillFlow = false;// 已把水傳下去
							}
						} else if (waterMap.get(pipeRow - 1).get(pipeCol)) {// 如果上面有水了
							stillFlow = false;// 代表沒有浪費水
						}
					}
					/*
					 * 下面以此類推
					 * 
					 * 
					 * 
					 */

					if (canFlow[1] && pipeCol + 1 < colMax && !(map.getUnitCode(pipeRow, pipeCol + 1).equals("-")
							|| map.getUnitCode(pipeRow, pipeCol).equals("c"))) {
						if (!waterMap.get(pipeRow).get(pipeCol + 1) && map.canFlowDirections(pipeRow, pipeCol + 1)[3]) {
							if (map.getUnitCode(pipeRow, pipeCol + 1).equals("c"))
								map.setCrossAngle(pipeRow, pipeCol + 1, 1);
							tmpPipe.add(new int[] { pipeRow, pipeCol + 1 });
							stillFlow = false;
						} else if (waterMap.get(pipeRow).get(pipeCol + 1)
								&& map.getUnitCode(pipeRow, pipeCol + 1).equals("c")) {
							if (map.getUnitAngle(pipeRow, pipeCol + 1) == 2) {
								map.setCrossAngle(pipeRow, pipeCol + 1, 3);
								tmpPipe.add(new int[] { pipeRow, pipeCol + 1 });
								stillFlow = false;
							}
						} else if (waterMap.get(pipeRow).get(pipeCol + 1)) {
							stillFlow = false;
						}
					}

					if (canFlow[2] && pipeRow + 1 < rowMax && !(map.getUnitCode(pipeRow + 1, pipeCol).equals("-")
							|| map.getUnitCode(pipeRow, pipeCol).equals("c"))) {
						if (!waterMap.get(pipeRow + 1).get(pipeCol) && map.canFlowDirections(pipeRow + 1, pipeCol)[0]) {
							if (map.getUnitCode(pipeRow + 1, pipeCol).equals("c"))
								map.setCrossAngle(pipeRow + 1, pipeCol, 2);
							tmpPipe.add(new int[] { pipeRow + 1, pipeCol });
							stillFlow = false;
						} else if (waterMap.get(pipeRow + 1).get(pipeCol)
								&& map.getUnitCode(pipeRow + 1, pipeCol).equals("c")) {
							if (map.getUnitAngle(pipeRow + 1, pipeCol) == 1) {
								map.setCrossAngle(pipeRow + 1, pipeCol, 3);
								tmpPipe.add(new int[] { pipeRow + 1, pipeCol });
								stillFlow = false;
							}
						} else if (waterMap.get(pipeRow + 1).get(pipeCol)) {
							stillFlow = false;
						}
					}

					if (canFlow[3] && pipeCol - 1 >= 0 && !(map.getUnitCode(pipeRow, pipeCol - 1).equals("-")
							|| map.getUnitCode(pipeRow, pipeCol).equals("c"))) {
						if (!waterMap.get(pipeRow).get(pipeCol - 1) && map.canFlowDirections(pipeRow, pipeCol - 1)[1]) {
							if (map.getUnitCode(pipeRow, pipeCol - 1).equals("c"))
								map.setCrossAngle(pipeRow, pipeCol - 1, 1);
							tmpPipe.add(new int[] { pipeRow, pipeCol - 1 });
							stillFlow = false;
						} else if (waterMap.get(pipeRow).get(pipeCol - 1)
								&& map.getUnitCode(pipeRow, pipeCol - 1).equals("c")) {
							if (map.getUnitAngle(pipeRow, pipeCol - 1) == 2) {
								map.setCrossAngle(pipeRow, pipeCol - 1, 3);
								tmpPipe.add(new int[] { pipeRow, pipeCol - 1 });
								stillFlow = false;
							}
						} else if (waterMap.get(pipeRow).get(pipeCol - 1)) {
							stillFlow = false;
						}
					}

					if (map.getUnitCode(pipeRow, pipeCol).equals("t")) {// 如果它是 T 水管
						boolean[] flow = map.canFlowDirections(pipeRow, pipeCol);// 取得可流動方向
						int[][] directions = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } }; // 上、右、下、左
						for (int dir = 0; dir < 4; dir++) {// 對於每個方向
							// 計算對應座標
							int nextRow = pipeRow + directions[dir][0];
							int nextCol = pipeCol + directions[dir][1];
							if (flow[dir] && nextRow >= 0 && nextRow < map.getHeight() && nextCol >= 0// 如果水可以往某個方向流，且沒有超出地圖邊界
									&& nextCol < map.getWidth()) {
								if (map.canFlowDirections(nextRow, nextCol)[(dir + 2) % 4])// 如果對應的座標可以接水管
									flow[dir] = false;
							}
						}
						stillFlow = flow[0] | flow[1] | flow[2] | flow[3];// 如果有方向沒更改到，那就是還在流動
					}

					if (stillFlow) {
						noWasteWater[0] = false;
					}

					controller[0] += 1;
				}
				waterPipes.clear(); // 清空 waterPipes
				waterPipes.addAll(tmpPipe); // 將 tmpPipe 中的元素加入 waterPipes
				tmpPipe.clear();

				if (checkEnd(endPipes, waterMap) || (waterPipes.size() == 0) || (controller[0] >= colMax * rowMax)) {// 檢查是否還要再繼續流動
					((Timer) e.getSource()).stop();// 停止計時器
					for (int i = 0; i < endPipes.size(); i++) {
						// 把可能沒換圖片的終點座標更團片
						int pipeRow = endPipes.get(i)[0];
						int pipeCol = endPipes.get(i)[1];
						if (waterMap.get(pipeRow).get(pipeCol))
							changeImage(gameMapPanel, pipeRow, pipeCol, map);
						latch.countDown();
					}
				}
			}
		});
		timer.start();// 開始計時器
		try {
			latch.await();// 等待計時器結束
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println(checkEnd(endPipes, waterMap));
		System.out.println(noWasteWater[0]);

		return checkEnd(endPipes, waterMap) && noWasteWater[0];// 回傳玩家是否成功解題

	}

	void changeImage(JPanel gameMapPanel, int pipeRow, int pipeCol, GameMap map) {// 更換水管的圖片
		// 取得圖片應有的高及寬
		int elementWidth = gameMapPanel.getWidth() / map.getWidth();
		int elementHeight = gameMapPanel.getHeight() / map.getHeight();
		Component[] components = gameMapPanel.getComponents();
		Component component = components[pipeRow * map.getWidth() + pipeCol];
		if (component instanceof JLabel) {
			JLabel element = (JLabel) component;
			ImageIcon image = getIconByPath(pipeImagePath.get(map.getImagePath(pipeRow, pipeCol, true)));
			if (!(map.getUnitCode(pipeRow, pipeCol).equals("w") || map.getUnitCode(pipeRow, pipeCol).equals("W"))) {// 如果他不是水庫
				image = rotateIcon(image, 90 * (map.getUnitAngle(pipeRow, pipeCol) - 1));// 旋轉圖片
			}
			element.setDisabledIcon(scaledIcon(image, elementWidth, elementHeight));// 設定圖片
		}
	}

	private ImageIcon getIconByPath(String imagePath) {// 從圖檔路徑取得圖片
		BufferedImage originalImage = null;
		try {
			originalImage = ImageIO.read(getClass().getResourceAsStream(imagePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ImageIcon(originalImage);
	}

	private ImageIcon scaledIcon(ImageIcon icon, int width, int height) {// 縮放圖片直到特定大小
		// 從ImageIcon對象中獲取原始圖像
		Image img = icon.getImage();

		// 創建一個具有所需尺寸的新圖像
		Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

		// 創建一個新的ImageIcon對象，使用新的圖像作為參數
		return new ImageIcon(newImg);
	}

	private ImageIcon rotateIcon(ImageIcon icon, int angle) {// 旋轉涂燕直到特定角度
		// 創建一個 BufferedImage 來儲存旋轉後的圖像
		BufferedImage rotatedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(),
				BufferedImage.TYPE_INT_ARGB);

		// 創建一個 Graphics2D 對象，用於繪製旋轉後的圖像
		Graphics2D g2d = rotatedImage.createGraphics();

		// 設置繪圖質量，以達到更好的旋轉效果
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		// 計算旋轉的中心點
		int centerX = icon.getIconWidth() / 2;
		int centerY = icon.getIconHeight() / 2;

		// 創建一個 AffineTransform 對象，用於旋轉圖像
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(angle), centerX, centerY);

		// 繪製旋轉後的圖像
		g2d.drawImage(icon.getImage(), transform, null);
		g2d.dispose();

		// 返回旋轉後的 ImageIcon
		return new ImageIcon(rotatedImage);
	}

	void printMap(GameMap map) {// 除錯用，不用關注
		for (int i = 0; i < map.getHeight(); i++) {
			for (int j = 0; j < map.getWidth(); j++) {
				System.out.print(map.getUnitCode(i, j) + "" + map.getUnitAngle(i, j) + " ");
			}
			System.out.println();
		}
	}

	protected void bind(JPanel gameMapPanel, int map_index, int stepLimit) {// 對參數提供的面板及地圖索引和提供的度數限制進行綁定

		if (map_index >= mapStorage.size() || map_index < 0)// 如果 index 超出邊界則不做
			return;

		GameMap nowMap = new GameMap(mapStorage.get(map_index));// 取得地圖文字檔，並進行 GameMap 的初始化
		nowMap.init(mapStorage.get(map_index));// 地圖初始化
		gameMaps.set(map_index, nowMap);// 把存放 gameMap 的 ArrayList 相關的 index set 成 nowMap
		gameMapPanel.removeAll();// 把面板上的 JLabl 全部移除
		gameMapPanel.revalidate();// 並讓它重新可用

		// 計算圖片的寬及高
		int elementWidth = gameMapPanel.getWidth() / nowMap.getWidth();
		int elementHeight = gameMapPanel.getHeight() / nowMap.getHeight();

		// 遍歷 nowMap 內的每個單位
		for (int row = 0; row < nowMap.getHeight(); row++) {
			for (int col = 0; col < nowMap.getWidth(); col++) {
				JLabel element = new JLabel();// 創建新的 JLabel
				element.setBounds(elementWidth * col, elementHeight * row, elementWidth, elementHeight);// 計算 JLabel
																										// 在面板應所處的座標，及給定寬度及高度
				gameMapPanel.add(element);// 把 JLabel 加進面板
				if (nowMap.getImagePath(row, col, false) < 0) {// 如果他不是水管或水庫，是空的，那繼續做下一個
					continue;
				}

				element.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// 設定畫數在水管圖片上的樣式

				// 計算當前列及行
				int nowRow = row;
				int nowCol = col;

				if ((nowMap.getUnitCode(nowRow, nowCol).equals("w")
						|| nowMap.getUnitCode(nowRow, nowCol).equals("W"))) {// 如果它是水庫
					ImageIcon image = getIconByPath(pipeImagePath.get(nowMap.getImagePath(row, col, false)));// 那他要取得水庫的圖片
					element.setIcon(scaledIcon(image, elementWidth, elementHeight));// 並將其縮放置特定大小，並設定它為 JLabel 的 icon
				} else {// 如果它是水管
					ImageIcon image = getIconByPath(pipeImagePath.get(nowMap.getImagePath(row, col, false)));// 那它要取得相關的圖片
					image = rotateIcon(image, 90 * (nowMap.getUnitAngle(row, col) - 1));// 並旋轉
					element.setIcon(scaledIcon(image, elementWidth, elementHeight));// 再縮放，並設定它為 JLabel 的 icon
				}

				element.addMouseListener(new MouseAdapter() {// 對每個 JLabel 設定點擊的事件監聽器
					// 取得相關於綁定的面板及步數限制
					int relativeMapIndex = map_index;
					GameMap nowMap = gameMaps.get(relativeMapIndex);
					int relativeLimit = stepLimit;

					@SuppressWarnings("deprecation")
					@Override
					public void mouseClicked(MouseEvent e) {
						if (playerLose)// 如果玩家已經輸，不做任何事
							return;
						nowMap.rotateUnit(nowRow, nowCol, rotateRight);// 旋轉，如果 rotateRight 是 true ，那就是往右轉
						ImageIcon image = getIconByPath(pipeImagePath.get(nowMap.getImagePath(nowRow, nowCol, false)));// 取得相關圖片
						if (!(nowMap.getUnitCode(nowRow, nowCol).equals("w")
								|| nowMap.getUnitCode(nowRow, nowCol).equals("W"))) {// 如果不是水庫
							image = rotateIcon(image, 90 * (nowMap.getUnitAngle(nowRow, nowCol) - 1));// 旋轉圖片
						}
						element.setIcon(scaledIcon(image, elementWidth, elementHeight));// 更改圖片
						checkSteps(relativeLimit);// 檢查步數是否達到限制
						currentSteps += 1;// 部署加一
						setChanged();
						notifyObservers(currentSteps);// 通知 Demo
					}
				});
			}
		}
	}

}
