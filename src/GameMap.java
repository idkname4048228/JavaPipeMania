
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

	private ImageIcon scaledIcon(String imagePath, int width, int height) {
		BufferedImage originalImage = null;
		try {
			originalImage = ImageIO.read(getClass().getResourceAsStream(imagePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = resizedImage.createGraphics();
		g2d.drawImage(originalImage, 0, 0, width, height, null);
		g2d.dispose();

		return new ImageIcon(resizedImage);
	}

	protected void create(JPanel gameMapPanel, GameMap map) {
		gameMapPanel.removeAll();
		gameMapPanel.revalidate();
		int elementWidth = gameMapPanel.getWidth() / map.getWidth();
		int elementHeight = gameMapPanel.getHeight() / map.getHeight();

		ImageIcon bentPipe = scaledIcon("./img/pipeImage/bentPipe.png", elementWidth, elementHeight);
		ImageIcon bentPipeWithWater = scaledIcon("./img/pipeImage/bentPipeWithWater.png", elementWidth, elementHeight);
		ImageIcon crossPipe = scaledIcon("./img/pipeImage/crossPipe.png", elementWidth, elementHeight);
		ImageIcon crossPipeWithOneWater = scaledIcon("./img/pipeImage/crossPipeWithOneWater.png", elementWidth,
				elementHeight);
		ImageIcon crossPipeWithTwoWater = scaledIcon("./img/pipeImage/crossPipeWithTwoWater.png", elementWidth,
				elementHeight);
		ImageIcon downInWaterStore = scaledIcon("./img/pipeImage/downInWaterStore.png", elementWidth, elementHeight);
		ImageIcon downInWaterStoreWithWater = scaledIcon("./img/pipeImage/downInWaterStoreWithWater.png", elementWidth,
				elementHeight);
		ImageIcon leftInWaterStore = scaledIcon("./img/pipeImage/leftInWaterStore.png", elementWidth, elementHeight);
		ImageIcon leftInWaterStoreWithWater = scaledIcon("./img/pipeImage/leftInWaterStoreWithWater.png", elementWidth,
				elementHeight);
		ImageIcon rightInWaterStore = scaledIcon("./img/pipeImage/rightInWaterStore.png", elementWidth, elementHeight);
		ImageIcon rightInWaterStoreWithWater = scaledIcon("./img/pipeImage/rightInWaterStoreWithWater.png",
				elementWidth, elementHeight);
		ImageIcon straightPipe = scaledIcon("./img/pipeImage/straightPipe.png", elementWidth, elementHeight);
		ImageIcon straightPipeWithWater = scaledIcon("./img/pipeImage/straightPipeWithWater.png", elementWidth,
				elementHeight);
		ImageIcon tPipe = scaledIcon("./img/pipeImage/tPipe.png", elementWidth, elementHeight);
		ImageIcon tPipeWithWater = scaledIcon("./img/pipeImage/tPipeWithWater.png", elementWidth, elementHeight);
		ImageIcon upInWaterStore = scaledIcon("./img/pipeImage/upInWaterStore.png", elementWidth, elementHeight);
		ImageIcon upInWaterStoreWithWater = scaledIcon("./img/pipeImage/upInWaterStoreWithWater.png", elementWidth,
				elementHeight);

		for (int row = 0; row < map.getHeight(); row++) {
			for (int col = 0; col < map.getWidth(); col++) {
				JLabel element = new JLabel();
				element.setBounds(elementWidth * col, elementHeight * row, elementWidth, elementHeight);
				gameMapPanel.add(element);
				switch (map.getUnitNum(row, col)) {
				case 0:
					element.setIcon(pathIcon);
					break;
				case 1:
					element.setIcon(wallIcon);
					break;
				case 2:
					element.setIcon(roleIcon);
					break;
				case 3:
					element.setIcon(boxIcon);
					break;
				case 4:
					element.setIcon(finishIcon);
					break;
				case 5:
					element.setIcon(button1Icon);
					break;
				case 6:
					element.setIcon(door1Icon);
					break;
				case 7:
					element.setIcon(button2Icon);
					break;
				case 8:
					element.setIcon(door2Icon);
					break;
				}
			}
		}
	}

}