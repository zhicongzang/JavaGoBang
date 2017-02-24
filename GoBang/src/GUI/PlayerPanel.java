package GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import AI.AIAgent;
import model.PieceColor;
import model.PlayerType;

public class PlayerPanel extends JPanel {
	
	static private GridBagLayout gbl_puzzleTypePanel = new GridBagLayout()
	{{
		columnWidths = new int[]{0, 0, 0};
		rowHeights = new int[]{0, 0, 0};
		columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
	}};
	static private final int GAP = 1;
	
	private PieceColor pieceColor;
	
	private JPanel photoPanel;
	private JPanel piecePanel;
	private JPanel configurePanel;
	private JComboBox<PlayerType> playerTypeComboxBox;
	private Label lblAILevel;
	private TextField txtAILevel;
	
	public PlayerPanel(PieceColor pieceColor) {
		super();
		this.pieceColor = pieceColor;
		init();
	}
	
	private void init() {
		setLayout(null);
		photoPanel = new JPanel();
		piecePanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				super.paintComponent(g);
				g.drawImage(pieceColor.getPaint(), 0, 0, piecePanel.getWidth(), piecePanel.getHeight(), this);
			}
		};
		configurePanel = new JPanel();
		configurePanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Settings", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		configurePanel.setLayout(gbl_puzzleTypePanel);
		
		playerTypeComboxBox = new JComboBox<PlayerType>();
		playerTypeComboxBox.addItem(PlayerType.Player);
		playerTypeComboxBox.addItem(PlayerType.AI);
		GridBagConstraints gbc_playerTypeComboxBox = new GridBagConstraints();
		gbc_playerTypeComboxBox.anchor = GridBagConstraints.WEST;
		gbc_playerTypeComboxBox.insets = new Insets(0, 0, 5, 0);
		gbc_playerTypeComboxBox.gridx = 1;
		gbc_playerTypeComboxBox.gridy = 0;
		playerTypeComboxBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				setAIConfigurable(((PlayerType) e.getItem()).equals(PlayerType.AI));
			}
		});
		
		lblAILevel = new Label();
		lblAILevel.setText("AI Level: ");
		GridBagConstraints gbc_lblAILevel = new GridBagConstraints();
		gbc_lblAILevel.anchor = GridBagConstraints.WEST;
		gbc_lblAILevel.insets = new Insets(0, 0, 5, 0);
		gbc_lblAILevel.gridx = 0;
		gbc_lblAILevel.gridy = 1;
		
		txtAILevel = new TextField();
		txtAILevel.setText("3");
		GridBagConstraints gbc_txtAILevel = new GridBagConstraints();
		gbc_txtAILevel.anchor = GridBagConstraints.WEST;
		gbc_txtAILevel.insets = new Insets(0, 0, 5, 0);
		gbc_txtAILevel.gridx = 1;
		gbc_txtAILevel.gridy = 1;
		txtAILevel.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				int keyChar=e.getKeyChar();
				if (keyChar>=KeyEvent.VK_0 && keyChar<=KeyEvent.VK_9) {
					
				} else {
					e.consume();  
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
			
		
		add(photoPanel);
		add(piecePanel);
		add(configurePanel);
		configurePanel.add(playerTypeComboxBox, gbc_playerTypeComboxBox);
		configurePanel.add(lblAILevel, gbc_lblAILevel);
		configurePanel.add(txtAILevel, gbc_txtAILevel);
		
		setAIConfigurable(false);
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		super.setBounds(x, y, width, height);
		
		photoPanel.setBounds(GAP,GAP, width - 2 * GAP, width - 2 * GAP);
		photoPanel.setBackground(Color.blue);
		piecePanel.setBounds(width * 5 / 12, width + GAP, width / 6, width / 6);
		configurePanel.setBounds(GAP, width * 7 / 6 + 2 * GAP , width - 2 * GAP, height - width * 7 / 6 - 3 * GAP);
		
	}
	
	private AIAgent getAgent() {
		AIAgent agent = ((PlayerType) playerTypeComboxBox.getSelectedItem()).getAgent(pieceColor);
		if (agent != null) {
			agent.setLevel(Integer.parseInt(txtAILevel.getText()));
			return agent;
		}
		return null;
	}
	
	private void setAIConfigurable(boolean isConfigurable) {
		lblAILevel.setEnabled(isConfigurable);
		txtAILevel.setEnabled(isConfigurable);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		playerTypeComboxBox.setEnabled(enabled);
		setAIConfigurable(enabled && ((PlayerType) playerTypeComboxBox.getSelectedItem()).equals(PlayerType.AI));
	}
	
	public AIAgent start() {
		setEnabled(false);
		piecePanel.setVisible(false);
		return getAgent();
	}
	
	public void end() {
		setEnabled(true);
		piecePanel.setVisible(true);
	}
	
	public void setInTurns(PieceColor pieceColor) {
		piecePanel.setVisible(this.pieceColor.equals(pieceColor));
	}
}
