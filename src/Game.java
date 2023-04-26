import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
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

public class Game {

	ArrayList<ArrayList<ArrayList<String>>> mapStorage = new ArrayList<>();
	ArrayList<ArrayList<String>> currentMap = new ArrayList<>();
	ArrayList<String> pipeImagePath = new ArrayList<>(Collections.nCopies(17, null));
	GameMap map = null;
	int nowMapIndex = 0;
	protected boolean rotateRight = true;

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
		gameMapPanel.removeAll();
		gameMapPanel.revalidate();
		int elementWidth = gameMapPanel.getWidth() / map.getWidth();
		int elementHeight = gameMapPanel.getHeight() / map.getHeight();

		for (int row = 0; row < map.getHeight(); row++) {
			for (int col = 0; col < map.getWidth(); col++) {
				JLabel element = new JLabel();
				element.setBounds(elementWidth * col, elementHeight * row, elementWidth, elementHeight);
				gameMapPanel.add(element);
				if (map.getUnitCode(row, col) < 0) {
					continue;
				}

				if ((13 <= map.getUnitCode(row, col) && map.getUnitCode(row, col) <= 16)
						|| (4 <= map.getUnitCode(row, col) && map.getUnitCode(row, col) <= 7)) {
					ImageIcon image = getIconByPath(pipeImagePath.get(map.getUnitCode(row, col)));
					element.setIcon(scaledIcon(image, elementWidth, elementHeight));
				} else {
					ImageIcon image = getIconByPath(pipeImagePath.get(map.getUnitCode(row, col)));
					image = rotateIcon(image, 90 * (map.getUnitAngle(row, col) - 1));
					element.setIcon(scaledIcon(image, elementWidth, elementHeight));
				}
			}
		}
	}
}
