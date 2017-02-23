package GUI;

import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import AI.AIAgent;
import model.Board;
import model.Piece;
import model.PieceColor;
import model.Score;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class GoBangGUI implements Observer{

	private JFrame frame;
	private BoardPanel boardPanel;
	private PlayerPanel blackPlayerPanel;
	private PlayerPanel whitePlayerPanel;
	private JButton goButton;
	private Board board = new Board();
	private List<AIAgent> agents = new ArrayList<>();
	private ExecutorService executor = Executors.newSingleThreadExecutor();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GoBangGUI window = new GoBangGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GoBangGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 890, 510);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		boardPanel = new BoardPanel();
		boardPanel.setBounds(215, 10, 460, 460);
		frame.getContentPane().add(boardPanel);
		boardPanel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				if (!boardPanel.isTouchable()) {
					return;
				}
				Point point = e.getPoint();
				Piece piece = board.addPiece(boardPanel.pointToCol(point), boardPanel.pointToRow(point));
				if (piece != null) {
					boardPanel.addPiece(piece);
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) { }
			
			@Override
			public void mouseExited(MouseEvent e) { }
			
			@Override
			public void mouseEntered(MouseEvent e) { }
			
			@Override
			public void mouseClicked(MouseEvent e) { }
		});
		
		blackPlayerPanel = new PlayerPanel(PieceColor.Black);
		blackPlayerPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		blackPlayerPanel.setBounds(10, 10, 200, 400);
		frame.getContentPane().add(blackPlayerPanel);
		
		whitePlayerPanel = new PlayerPanel(PieceColor.White);
		whitePlayerPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		whitePlayerPanel.setBounds(680, 10, 200, 400);
		frame.getContentPane().add(whitePlayerPanel);
		
		goButton = new JButton("Go");
		goButton.setBounds(60,420, 100, 40);
		frame.getContentPane().add(goButton);
		goButton.addActionListener(ae -> {
			startGame();
		});
		
		board.addObserver(this);
		
		endGame();
		
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		if (o == board) {
			changeSide(board.getCurrentPieceColor());
		}
	}
	
	/*
	 * 	Update UI.
	 */
	private void changeSide(PieceColor pieceColor) {
		System.out.println("Now: " + Score.getScore(board.getBoardData()));
		System.out.println("================");
		if (board.isEnd()) {
			System.out.println("Game Over   " + board.getLatestPiece().getColor().toString());
			endGame();
			return;
		}
		switch (agents.size()) {
		case 0:
			boardPanel.setTouchable(true);
			break;
		case 1:
			boardPanel.setTouchable(!pieceColor.equals(agents.get(0).getPieceColor()));
			break;
		default:
			boardPanel.setTouchable(false);
			break;
		}
		whitePlayerPanel.setInTurns(pieceColor);
		blackPlayerPanel.setInTurns(pieceColor);
		for (AIAgent agent: agents) {
			if (pieceColor.equals(agent.getPieceColor())) {
				executor.execute( () -> {
					Piece piece = agent.run(board);
					piece = board.addPiece(piece.getCol(), piece.getRow());
					if (piece != null) {
						boardPanel.addPiece(piece);
					}
				});
			}
		}
	}
	
	/*
	 * 	Game start
	 */
	private void startGame(){
		AIAgent whiteAgent = whitePlayerPanel.start();
		AIAgent blackAgent = blackPlayerPanel.start();
		goButton.setEnabled(false);
		if (blackAgent != null) {
			agents.add(blackAgent);
		}
		if (whiteAgent != null) {
			agents.add(whiteAgent);
		}
		boardPanel.reset();
		board.reset();
		
	}
	
	/*
	 *	Game end 
	 */
	private void endGame() {
		boardPanel.setTouchable(false);
		goButton.setEnabled(true);
		whitePlayerPanel.end();
		blackPlayerPanel.end();
		agents.clear();
	}
}
