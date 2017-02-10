package GUI;

import java.awt.Color;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import model.PlayerType;

public class PlayerPanel extends JPanel {
	
	static private final int GAP = 1;
	
	private JPanel photoPanel;
	private JComboBox<PlayerType> playerTypeComboxBox;
	
	public PlayerPanel() {
		// TODO Auto-generated constructor stub
		super();
		setLayout(null);
		photoPanel = new JPanel();
		playerTypeComboxBox = new JComboBox<PlayerType>();
		playerTypeComboxBox.addItem(PlayerType.Player);
		playerTypeComboxBox.addItem(PlayerType.AI);
		add(photoPanel);
		add(playerTypeComboxBox);
	}
	
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		super.setBounds(x, y, width, height);
		
		photoPanel.setBounds(GAP,GAP, width - 2 * GAP, width - 2 * GAP);
		photoPanel.setBackground(Color.blue);
		playerTypeComboxBox.setBounds(width / 4, width, width / 2, width / 4);
	}
}
