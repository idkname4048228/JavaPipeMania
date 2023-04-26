import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
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
		gbl_contentPane.columnWidths = new int[] { 600, 375, 0 };
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
		GameMap1.setBounds(0, 0, 600, 550);
		game_maps.add(GameMap1);

		JPanel GameMap2 = new JPanel();
		GameMap2.setBackground(new Color(255, 255, 255));
		GameMapPanel.add(GameMap2, "name_937573591616900");
		GameMap2.setLayout(null);
		GameMap2.setBounds(0, 0, 600, 550);
		game_maps.add(GameMap2);

		JPanel OperatePanel = new JPanel();
		GridBagConstraints gbc_OperatePanel = new GridBagConstraints();
		gbc_OperatePanel.fill = GridBagConstraints.BOTH;
		gbc_OperatePanel.gridx = 1;
		gbc_OperatePanel.gridy = 0;
		contentPane.add(OperatePanel, gbc_OperatePanel);

		game_map_index = 0;
		map.init(game.getCurrentMap());
		game.create(GameMap1, map);
	}

}
