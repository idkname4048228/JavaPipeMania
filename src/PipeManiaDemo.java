import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

public class PipeManiaDemo extends JFrame implements Observer {

	private JPanel contentPane; // 視窗的主畫面
	int game_map_index = 0; // 當前的遊戲地圖的索引
	Game game = null; // 當前的遊戲
	GameMap map = null; // 當前的遊戲地圖
	ArrayList<JPanel> game_maps = null; // 遊戲畫面渲染所需的面板
	ArrayList<String> game_maps_name = null; // 存放面板名字的 ArrayList
	// 意下四個是預先定義的原件，在 constructor 之外的地方會用到
	JButton lastRoundButton = null;
	JButton nextRoundButton = null;
	JLabel stepsLabel = null;
	JButton checkButton = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					PipeManiaDemo frame = new PipeManiaDemo();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PipeManiaDemo() {
		this.game = new Game();// 初始化遊戲
		game.addObserver(this); // 把現在的 method電城觀察者，去監聽 game 內的變數 notify
		this.map = new GameMap(game.getCurrentMap()); // 把遊戲內的現在的地圖取出來，放進遊戲地圖的建構子，進行遊戲地圖的初始化
		this.game_maps = new ArrayList<>();// 初始化放面板的 ArrayList
		this.game_maps_name = new ArrayList<>(); // 初始化放面板名字的 ArrayList

		// 視窗主畫面的一些設定
		setTitle("PipeMania");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(500, 200, 1000, 600);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 650, 325, 0 };
		gbl_contentPane.rowHeights = new int[] { 550, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		// 放「放遊戲主畫面面板」的面板及其設定
		JPanel GameMapPanel = new JPanel();
		GridBagConstraints gbc_GameMapPanel = new GridBagConstraints();
		gbc_GameMapPanel.fill = GridBagConstraints.BOTH;
		gbc_GameMapPanel.gridx = 0;
		gbc_GameMapPanel.gridy = 0;
		contentPane.add(GameMapPanel, gbc_GameMapPanel);
		GameMapPanel.setLayout(new CardLayout(0, 0));

		// 主要放遊戲主畫面的面板一及其設定，並把他的名字加進 game_map_name 裡面、把他加進 game_maps 裡面
		JPanel GameMap1 = new JPanel();
		GameMap1.setBackground(new Color(255, 255, 255));
		GameMapPanel.add(GameMap1, "firstMap");
		GameMap1.setLayout(null);
		GameMap1.setBounds(0, 0, 650, 550);
		game_maps.add(GameMap1);
		game_maps_name.add("firstMap");

		// 主要放遊戲主畫面的面板二及其設定，並把他的名字加進 game_map_name 裡面、把他加進 game_maps 裡面
		JPanel GameMap2 = new JPanel();
		GameMap2.setBackground(new Color(255, 255, 255));
		GameMapPanel.add(GameMap2, "secondMap");
		GameMap2.setLayout(null);
		GameMap2.setBounds(0, 0, 650, 550);
		game_maps.add(GameMap2);
		game_maps_name.add("secondMap");

		// 主要放遊戲主畫面的面板三及其設定，並把他的名字加進 game_map_name 裡面、把他加進 game_maps 裡面
		JPanel GameMap3 = new JPanel();
		GameMap3.setBackground(new Color(255, 255, 255));
		GameMapPanel.add(GameMap3, "thirdMap");
		GameMap3.setLayout(null);
		GameMap3.setBounds(0, 0, 650, 550);
		game_maps.add(GameMap3);
		game_maps_name.add("thirdMap");

		// 玩家操作介面的面板(放按鈕之類的東西)
		JPanel OperatePanel = new JPanel();
		OperatePanel.setLayout(null);
		GridBagConstraints gbc_OperatePanel = new GridBagConstraints();
		gbc_OperatePanel.fill = GridBagConstraints.BOTH;
		gbc_OperatePanel.gridx = 1;
		gbc_OperatePanel.gridy = 0;
		contentPane.add(OperatePanel, gbc_OperatePanel);

		// 以下的元件皆可看參考構子的文字，像下面的就是向左旋轉功能的按鈕
		JButton leftRotateButton = new JButton("向左旋轉");
		leftRotateButton.setFont(new Font("微軟正黑體", Font.BOLD, 24));
		leftRotateButton.setBounds(10, 30, 150, 70);
		OperatePanel.add(leftRotateButton);

		JButton rightRotateButton = new JButton("向右旋轉");
		rightRotateButton.setFont(new Font("微軟正黑體", Font.BOLD, 24));
		rightRotateButton.setBounds(164, 30, 150, 70);
		OperatePanel.add(rightRotateButton);

		JButton lastRoundButton = new JButton("上一關");
		lastRoundButton.setFont(new Font("微軟正黑體", Font.BOLD, 24));
		lastRoundButton.setBounds(10, 450, 150, 70);
		OperatePanel.add(lastRoundButton);

		JButton nextRoundButton = new JButton("下一關");
		nextRoundButton.setFont(new Font("微軟正黑體", Font.BOLD, 24));
		nextRoundButton.setBounds(164, 450, 150, 70);
		OperatePanel.add(nextRoundButton);

		JLabel stepsTip = new JLabel("當前步數/限制步數：");
		stepsTip.setVerticalAlignment(SwingConstants.TOP);
		stepsTip.setFont(new Font("微軟正黑體", Font.BOLD, 24));
		stepsTip.setBounds(10, 150, 304, 50);
		OperatePanel.add(stepsTip);

		checkButton = new JButton("確認");
		checkButton.setFont(new Font("微軟正黑體", Font.BOLD, 24));
		checkButton.setBounds(10, 370, 150, 70);
		OperatePanel.add(checkButton);

		JButton restrartButton = new JButton("重新開始");
		restrartButton.setFont(new Font("微軟正黑體", Font.BOLD, 24));
		restrartButton.setBounds(164, 370, 150, 70);
		OperatePanel.add(restrartButton);

		stepsLabel = new JLabel("--/100");
		stepsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		stepsLabel.setFont(new Font("微軟正黑體", Font.BOLD, 24));
		stepsLabel.setBounds(10, 180, 304, 50);
		OperatePanel.add(stepsLabel);

		JLabel currentDirectionTip = new JLabel("當前選擇旋轉方向：");
		currentDirectionTip.setVerticalAlignment(SwingConstants.TOP);
		currentDirectionTip.setFont(new Font("微軟正黑體", Font.BOLD, 24));
		currentDirectionTip.setBounds(10, 240, 304, 50);
		OperatePanel.add(currentDirectionTip);

		JLabel currentDirectionLabel = new JLabel("向右旋轉");
		currentDirectionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		currentDirectionLabel.setFont(new Font("微軟正黑體", Font.BOLD, 24));
		currentDirectionLabel.setBounds(10, 270, 304, 70);
		OperatePanel.add(currentDirectionLabel);

		// 以上是玩家操作遊戲所需的元件

		// 以下是按鈕的功能實作
		// 下面兩個的功能是類似的，把提示文字換成相對應的方位，以及去 set game 內的 rotateRight
		leftRotateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentDirectionLabel.setText("向左旋轉");
				game.rotateRight = false;
			}
		});

		rightRotateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentDirectionLabel.setText("向右旋轉");
				game.rotateRight = true;
			}
		});

		// 當玩家確認後， check Button 會去觸發 game 內的 check() 去確定玩家是否成功通關
		checkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.setRotate(game_maps.get(game_map_index), false); // 讓水管不能再轉
				checkButton.setEnabled(false);// 讓check Button不能再按

				boolean[] win = { game.playerLose };

				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {// 把 game 內的 check() 放在背景執行
					@Override
					protected Void doInBackground() throws Exception {
						win[0] = game.check(game_maps.get(game_map_index)); // 檢查遊戲是否成功通關
						JOptionPane.showMessageDialog(null, win[0] ? "你贏了" : "你失敗了");// 並顯示相關文字
						return null;
					}
				};
				worker.execute();

			}
		});

		// 當玩家選擇重新開始， map 會重新載入 game 的當前地圖，並讓 game 重新顯示畫面
		restrartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				map.init(game.getCurrentMap());// map 重新載入當前地圖
				game.bind(game_maps.get(game_map_index), game.getMapIndex(), 100); // game 重新顯示畫面
				// 以下是對 game 內的 attribute 做初始化
				checkButton.setEnabled(true);
				game.setRotate(game_maps.get(game_map_index), true);
				game.resetSteps();
			}
		});

		// 當玩家選擇上一關，程式會拿上一張卡片(cardLayout)，那張卡片上是已經渲染好的遊戲畫面
		lastRoundButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 顯示上一張卡片，也就是索引為 game_map_index - 1 的 game_maps_name 內的面板
				CardLayout cardLayout = (CardLayout) GameMapPanel.getLayout();
				game_map_index = (game.getMapIndex() - 1) + 3;
				game_map_index %= 3;
				cardLayout.show(GameMapPanel, game_maps_name.get(game_map_index));
				game.changeMap(-1);

				// 確認是否為最後一題或第一題，並對他們(上下關按鈕)做能否繼續使用的判斷
				lastRoundButton.setEnabled(!(game.getMapIndex() == 0));
				nextRoundButton.setEnabled(!game.isLastMap(game.getMapIndex()));
				checkButton.setEnabled(true);

				// 地圖初始化， game 的步數重新計算
				map.init(game.getCurrentMap());
				game.resetSteps();

				// 再上一張地圖的預先選染，放在背景執行
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						game.bind(game_maps.get((game_map_index + 2) % 3), game.getMapIndex() - 1, 100);
						return null;
					}
				};
				worker.execute();
			}
		});
		// 功能與上一關的按鈕類似
		nextRoundButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cardLayout = (CardLayout) GameMapPanel.getLayout();
				game_map_index = game.getMapIndex() + 1;
				game_map_index %= 3;
				cardLayout.show(GameMapPanel, game_maps_name.get(game_map_index));
				game.changeMap(1);

				lastRoundButton.setEnabled(!(game.getMapIndex() == 0));
				nextRoundButton.setEnabled(!game.isLastMap(game.getMapIndex()));
				checkButton.setEnabled(true);

				map.init(game.getCurrentMap());
				game.resetSteps();

				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						game.bind(game_maps.get((game_map_index + 1) % 3), game.getMapIndex() + 1, 100);
						return null;
					}
				};
				worker.execute();
			}
		});

		// 遊戲開始的預設值
		game_map_index = 0;
		map.init(game.getCurrentMap());
		game.bind(GameMap3, 2, 100);
		game.bind(GameMap2, 1, 100);
		game.bind(GameMap1, 0, 100);
		lastRoundButton.setEnabled(!(game_map_index == 0));
		nextRoundButton.setEnabled(!game.isLastMap(game_map_index));
	}

	// game 內的步數的監聽器，在發現玩家失敗後會顯示相關提示框
	@Override
	public void update(Observable o, Object arg) {
		int step = (int) arg;
		stepsLabel.setText(step + "/" + 100);
		if (game.playerLose) {
			JOptionPane.showMessageDialog(null, "你失敗了");
			checkButton.setEnabled(false);
			game.setRotate(game_maps.get(game_map_index), false);
		}
	}

}
