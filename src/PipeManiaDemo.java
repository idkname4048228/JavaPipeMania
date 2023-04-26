import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class PipeManiaDemo extends JFrame {

	private JPanel contentPane;

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
		Game game = new Game();
		GameMap map = new GameMap(game.getCurrentMap());
		ArrayList<JPanel> game_maps = new ArrayList<>();
		int game_map_index = 0;

		setTitle("PipeMania");
		setResizable(false);
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

		JPanel GameMapPanel = new JPanel();
		GridBagConstraints gbc_GameMapPanel = new GridBagConstraints();
		gbc_GameMapPanel.fill = GridBagConstraints.BOTH;
		gbc_GameMapPanel.gridx = 0;
		gbc_GameMapPanel.gridy = 0;
		contentPane.add(GameMapPanel, gbc_GameMapPanel);
		GameMapPanel.setLayout(new CardLayout(0, 0));

		JPanel GameMap1 = new JPanel();
		GameMap1.setBackground(new Color(255, 255, 255));
		GameMapPanel.add(GameMap1, "name_937573585471600");
		GameMap1.setLayout(null);
		GameMap1.setBounds(0, 0, 650, 550);
		game_maps.add(GameMap1);

		JPanel GameMap2 = new JPanel();
		GameMap2.setBackground(new Color(255, 255, 255));
		GameMapPanel.add(GameMap2, "name_937573591616900");
		GameMap2.setLayout(null);
		GameMap2.setBounds(0, 0, 650, 550);
		game_maps.add(GameMap2);

		JPanel OperatePanel = new JPanel();
		OperatePanel.setLayout(null);
		GridBagConstraints gbc_OperatePanel = new GridBagConstraints();
		gbc_OperatePanel.fill = GridBagConstraints.BOTH;
		gbc_OperatePanel.gridx = 1;
		gbc_OperatePanel.gridy = 0;
		contentPane.add(OperatePanel, gbc_OperatePanel);

		JButton leftRotateButton = new JButton("向左旋轉");
		leftRotateButton.setFont(new Font("微軟正黑體", Font.BOLD, 24));
		leftRotateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		leftRotateButton.setBounds(10, 30, 150, 70);
		OperatePanel.add(leftRotateButton);

		JButton righttRotateButton = new JButton("向右旋轉");
		righttRotateButton.setFont(new Font("微軟正黑體", Font.BOLD, 24));
		righttRotateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		righttRotateButton.setBounds(164, 30, 150, 70);
		OperatePanel.add(righttRotateButton);

		JButton lastRoundButton = new JButton("上一關");
		lastRoundButton.setFont(new Font("微軟正黑體", Font.BOLD, 24));
		lastRoundButton.setBounds(10, 450, 150, 70);
		OperatePanel.add(lastRoundButton);

		JButton nextRoundButton = new JButton("下一關");
		nextRoundButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		nextRoundButton.setFont(new Font("微軟正黑體", Font.BOLD, 24));
		nextRoundButton.setBounds(164, 450, 150, 70);
		OperatePanel.add(nextRoundButton);

		JLabel stepsTip = new JLabel("當前步數/限制步數：");
		stepsTip.setVerticalAlignment(SwingConstants.TOP);
		stepsTip.setFont(new Font("微軟正黑體", Font.BOLD, 24));
		stepsTip.setBounds(10, 150, 304, 50);
		OperatePanel.add(stepsTip);

		JButton checkButton = new JButton("確認");
		checkButton.setFont(new Font("微軟正黑體", Font.BOLD, 24));
		checkButton.setBounds(10, 370, 150, 70);
		OperatePanel.add(checkButton);

		JButton restrartButton = new JButton("重新開始");
		restrartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		restrartButton.setFont(new Font("微軟正黑體", Font.BOLD, 24));
		restrartButton.setBounds(164, 370, 150, 70);
		OperatePanel.add(restrartButton);

		JLabel stepsLabel = new JLabel("--/--");
		stepsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		stepsLabel.setFont(new Font("微軟正黑體", Font.BOLD, 24));
		stepsLabel.setBounds(10, 180, 304, 50);
		OperatePanel.add(stepsLabel);

		JLabel currentBlockTip = new JLabel("當前選擇格子：");
		currentBlockTip.setVerticalAlignment(SwingConstants.TOP);
		currentBlockTip.setFont(new Font("微軟正黑體", Font.BOLD, 24));
		currentBlockTip.setBounds(10, 240, 304, 50);
		OperatePanel.add(currentBlockTip);

		JLabel currentBlockLabel = new JLabel("row: - col: - ");
		currentBlockLabel.setHorizontalAlignment(SwingConstants.CENTER);
		currentBlockLabel.setFont(new Font("微軟正黑體", Font.BOLD, 24));
		currentBlockLabel.setBounds(10, 270, 304, 70);
		OperatePanel.add(currentBlockLabel);

		game_map_index = 0;
		map.init(game.getCurrentMap());
		game.create(GameMap1, map);
	}
}
