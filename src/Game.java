import java.awt.Component;
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
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game {

	ArrayList<ArrayList<ArrayList<String>>> mapStorage = new ArrayList<>();
	ArrayList<ArrayList<String>> currentMap = new ArrayList<>();
	ArrayList<String> pipeImagePath = new ArrayList<>(Collections.nCopies(17, null));
	GameMap map = null;
	int nowMapIndex = 0;
	protected boolean rotateRight = true;
	boolean playerWin = false;

	Game() {
		mapStorage = readMapFile();
		nowMapIndex = 0;
		currentMap = mapStorage.get(nowMapIndex);
		map = new GameMap(currentMap);
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

	boolean isLastMap(int index) {
		return index == mapStorage.size() - 1;
	}

	void changeMap(int change) {
		nowMapIndex += change;
		currentMap = mapStorage.get(nowMapIndex);
	}

	boolean checkEnd(ArrayList<int[]> endPipes, ArrayList<ArrayList<Boolean>> waterMap) {
		boolean win = true;
		for (int i = 0; i < endPipes.size(); i++) {
			int pipeRow = endPipes.get(i)[0];
			int pipeCol = endPipes.get(i)[1];
			win &= waterMap.get(pipeRow).get(pipeCol);
			if (!win)
				return false;

		}
		return true;
	}

	void setRotate(JPanel gameMapPanel, boolean rotate) {
		Component[] components = gameMapPanel.getComponents();
		for (Component component : components) {
			if (component instanceof JLabel) {
				JLabel element = (JLabel) component;
				element.setDisabledIcon(element.getIcon());
				element.setEnabled(rotate);
			}
		}
	}

	void check(JPanel gameMapPanel) {

		final boolean[] noWasteWater = { true }; // 使用陣列將 noWasteWater 變成 effectively final 變數

		final int colMax = map.getWidth();
		final int rowMax = map.getHeight();

		final int[] controller = { 0 }; // 使用陣列將 controller 變成 effectively final 變數

		ArrayList<ArrayList<Boolean>> waterMap = new ArrayList<>();
		for (int i = 0; i < rowMax; i++) {
			ArrayList<Boolean> tmp = new ArrayList<>();
			for (int j = 0; j < colMax; j++) {
				tmp.add(false);
			}
			waterMap.add(tmp);
		}

		final ArrayList<int[]> waterPipes = new ArrayList<>(map.getStartPipe()); // 使用 ArrayList 初始化 waterPipes 變數
		final ArrayList<int[]> endPipes = map.getEndPipe();

		Timer timer = null;
		timer = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<int[]> tmpPipe = new ArrayList<>();
				for (int i = 0; i < waterPipes.size(); i++) {
					int pipeRow = waterPipes.get(i)[0];
					int pipeCol = waterPipes.get(i)[1];

					waterMap.get(pipeRow).set(pipeCol, true);
					changeImage(gameMapPanel, pipeRow, pipeCol);
					if (map.getUnitCode(pipeRow, pipeCol).equals("w"))
						continue;

					boolean stillFlow = true;
					boolean[] canFlow = map.canFlowDirections(pipeRow, pipeCol);

					if (canFlow[0] && pipeRow - 1 >= 0 && !map.getUnitCode(pipeRow - 1, pipeCol).equals("-")
							&& !waterMap.get(pipeRow - 1).get(pipeCol)) {
						if (map.canFlowDirections(pipeRow - 1, pipeCol)[2]) {
							tmpPipe.add(new int[] { pipeRow - 1, pipeCol });
							stillFlow = false;
						}
					}

					if (canFlow[1] && pipeCol + 1 < colMax && !map.getUnitCode(pipeRow, pipeCol + 1).equals("-")
							&& !waterMap.get(pipeRow).get(pipeCol + 1)) {
						if (map.canFlowDirections(pipeRow, pipeCol + 1)[3]) {
							tmpPipe.add(new int[] { pipeRow, pipeCol + 1 });
							stillFlow = false;
						}
					}

					if (canFlow[2] && pipeRow + 1 < rowMax && !map.getUnitCode(pipeRow + 1, pipeCol).equals("-")
							&& !waterMap.get(pipeRow + 1).get(pipeCol)) {
						if (map.canFlowDirections(pipeRow + 1, pipeCol)[0]) {
							tmpPipe.add(new int[] { pipeRow + 1, pipeCol });
							stillFlow = false;
						}
					}

					if (canFlow[3] && pipeCol - 1 >= 0 && !map.getUnitCode(pipeRow, pipeCol - 1).equals("-")
							&& !waterMap.get(pipeRow).get(pipeCol - 1)) {
						if (map.canFlowDirections(pipeRow, pipeCol - 1)[1]) {
							tmpPipe.add(new int[] { pipeRow, pipeCol - 1 });
							stillFlow = false;
						}
					}

					if (stillFlow)
						noWasteWater[0] = false; // 更新 noWasteWater 的值

					controller[0] += 1;
				}
				waterPipes.clear(); // 清空 waterPipes
				waterPipes.addAll(tmpPipe); // 將 tmpPipe 中的元素加入 waterPipes
				System.out.println("running");
				if (checkEnd(endPipes, waterMap) || (waterPipes.size() == 0) || (controller[0] >= colMax * rowMax)) {
					((Timer) e.getSource()).stop();
					for (int i = 0; i < endPipes.size(); i++) {
						int pipeRow = endPipes.get(i)[0];
						int pipeCol = endPipes.get(i)[1];
						if (waterMap.get(pipeRow).get(pipeCol))
							changeImage(gameMapPanel, pipeRow, pipeCol);
					}
				}
			}
		});
		timer.start();

		playerWin = checkEnd(endPipes, waterMap) && noWasteWater[0];

	}

	void changeImage(JPanel gameMapPanel, int pipeRow, int pipeCol) {
		int elementWidth = gameMapPanel.getWidth() / map.getWidth();
		int elementHeight = gameMapPanel.getHeight() / map.getHeight();
		Component[] components = gameMapPanel.getComponents();
		Component component = components[pipeRow * map.getWidth() + pipeCol];
		if (component instanceof JLabel) {
			JLabel element = (JLabel) component;
			ImageIcon image = getIconByPath(pipeImagePath.get(map.getImagePath(pipeRow, pipeCol, true, false)));
			if (!(map.getUnitCode(pipeRow, pipeCol).equals("w") || map.getUnitCode(pipeRow, pipeCol).equals("W"))) {
				image = rotateIcon(image, 90 * (map.getUnitAngle(pipeRow, pipeCol) - 1));
			}
			element.setDisabledIcon(scaledIcon(image, elementWidth, elementHeight));
		}
	}

	private ImageIcon getIconByPath(String imagePath) {
		BufferedImage originalImage = null;
		try {
			originalImage = ImageIO.read(getClass().getResourceAsStream(imagePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ImageIcon(originalImage);
	}

	private ImageIcon scaledIcon(ImageIcon icon, int width, int height) {
		// 從ImageIcon對象中獲取原始圖像
		Image img = icon.getImage();

		// 創建一個具有所需尺寸的新圖像
		Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

		// 創建一個新的ImageIcon對象，使用新的圖像作為參數
		return new ImageIcon(newImg);
	}

	private ImageIcon rotateIcon(ImageIcon icon, int angle) {
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

	protected void create(JPanel gameMapPanel, GameMap map) {
		this.map = map;
		gameMapPanel.removeAll();
		gameMapPanel.revalidate();
		int elementWidth = gameMapPanel.getWidth() / map.getWidth();
		int elementHeight = gameMapPanel.getHeight() / map.getHeight();

		for (int row = 0; row < map.getHeight(); row++) {
			for (int col = 0; col < map.getWidth(); col++) {
				JLabel element = new JLabel();
				element.setBounds(elementWidth * col, elementHeight * row, elementWidth, elementHeight);
				gameMapPanel.add(element);
				if (map.getImagePath(row, col, false, false) < 0) {
					continue;
				}
				int nowRow = row;
				int nowCol = col;

				if ((13 <= map.getImagePath(row, col, false, false) && map.getImagePath(row, col, false, false) <= 16)
						|| (4 <= map.getImagePath(row, col, false, false)
								&& map.getImagePath(row, col, false, false) <= 7)) {
					ImageIcon image = getIconByPath(pipeImagePath.get(map.getImagePath(row, col, false, false)));
					element.setIcon(scaledIcon(image, elementWidth, elementHeight));
				} else {
					ImageIcon image = getIconByPath(pipeImagePath.get(map.getImagePath(row, col, false, false)));
					image = rotateIcon(image, 90 * (map.getUnitAngle(row, col) - 1));
					element.setIcon(scaledIcon(image, elementWidth, elementHeight));
				}

				element.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						map.setUnitAngle(nowRow, nowCol, rotateRight);
						ImageIcon image = getIconByPath(
								pipeImagePath.get(map.getImagePath(nowRow, nowCol, false, false)));
						if (!(map.getUnitCode(nowRow, nowCol).equals("w")
								|| map.getUnitCode(nowRow, nowCol).equals("W"))) {
							image = rotateIcon(image, 90 * (map.getUnitAngle(nowRow, nowCol) - 1));
						}
						element.setIcon(scaledIcon(image, elementWidth, elementHeight));
					}
				});
			}
		}
	}

}
