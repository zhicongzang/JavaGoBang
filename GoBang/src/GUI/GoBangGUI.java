package GUI;

import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	private Board board = new Board();
	private AIAgent agent = new AIAgent();
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
				Point point = e.getPoint();
				Piece piece = board.addPiece(boardPanel.pointToCol(point), boardPanel.pointToRow(point));
				if (piece != null) {
					boardPanel.addPiece(piece);
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		blackPlayerPanel = new PlayerPanel();
		blackPlayerPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		blackPlayerPanel.setBounds(10, 10, 200, 400);
		frame.getContentPane().add(blackPlayerPanel);
		
		whitePlayerPanel = new PlayerPanel();
		whitePlayerPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		whitePlayerPanel.setBounds(680, 10, 200, 400);
		frame.getContentPane().add(whitePlayerPanel);
		
		board.addObserver(this);
		
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
	
	/*
	 * 	Game start
	 */
	private void startGame(){
		boardPanel.reset();
		board.reset();
	}
	
	/*
	 *	Game end 
	 */
	private void endGame() {
		startGame();
	}
}
