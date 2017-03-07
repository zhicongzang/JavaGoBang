package GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import model.Piece;


public class BoardPanel extends JPanel {
	
	static private final ImageIcon BOARD_IMAGE_ICON = new ImageIcon("res/board.png");
	
	static private final int PIECES_GAP = 4;
	
	private int boardGap = 0; 
	private int lineGap = 0;
	private int pieceWidth = 0;
	private int frameLineDistance = 0;
	
	private List<Piece> pieces = new ArrayList<>();
	
	private boolean isTouchable = false;
	
	public BoardPanel() {
		super();
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		// width%46 == 0
		super.setBounds(x, y, width, height);
		lineGap = (width * 3) / 46;
		boardGap = lineGap / 3 * 2;
		pieceWidth = lineGap - PIECES_GAP;
		frameLineDistance = pieceWidth / 3;
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		drawBoard(g);
		drawPieces(g);
	}
	
	
	/*
	 *  Draw pieces
	 */
	private void drawPieces(Graphics g) {
		for(Piece p: pieces) {
			Point point = pieceCoordinateToPoint(p);
			g.drawImage(p.getColor().getPaint(), point.x, point.y, pieceWidth, pieceWidth, this);
		}
		if (pieces.size() > 0) {
			drawFrame(g, pieceCoordinateToPoint(pieces.get(pieces.size() - 1)));
		}
	}
	
	/*
	 * 	Draw board
	 */
	private void drawBoard(Graphics g) {
		g.drawImage(BOARD_IMAGE_ICON.getImage(), 0, 0, getWidth(), getHeight(), this);
	}
	
	/*
	 * Convert coordinate to point.
	 */
	private Point pieceCoordinateToPoint(Piece piece) {
		return new Point(boardGap + piece.getCol() * lineGap - pieceWidth / 2, boardGap + piece.getRow() * lineGap - pieceWidth / 2); 
	}
	
	private void drawFrame(Graphics g, Point point) { 
		g.setColor(Color.RED);
		g.drawLine(point.x, point.y, point.x + frameLineDistance, point.y);
		g.drawLine(point.x + pieceWidth - frameLineDistance, point.y, point.x + pieceWidth, point.y);
		g.drawLine(point.x, point.y + pieceWidth, point.x + frameLineDistance, point.y + pieceWidth);
		g.drawLine(point.x + pieceWidth - frameLineDistance, point.y + pieceWidth, point.x + pieceWidth, point.y + pieceWidth);
		g.drawLine(point.x, point.y, point.x, point.y + frameLineDistance);
		g.drawLine(point.x, point.y + pieceWidth - frameLineDistance, point.x, point.y + pieceWidth);
		g.drawLine(point.x + pieceWidth, point.y, point.x + pieceWidth, point.y + frameLineDistance);
		g.drawLine(point.x + pieceWidth, point.y + pieceWidth - frameLineDistance, point.x + pieceWidth, point.y + pieceWidth);
		
	}
	
	/*
	 * Convert point to col.
	 */
	public int pointToCol(Point point) {
		return (point.x - boardGap + pieceWidth / 2) / lineGap;
	}
	
	/*
	 * Convert point to row.
	 */
	public int pointToRow(Point point) {
		return (point.y - boardGap + pieceWidth / 2) / lineGap;
	}
	
	/*
	 * Add piece
	 */
	public void addPiece(Piece piece, Callable<Void> showResult) {
		pieces.add(piece);
		repaint();
		try {
			showResult.call();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Reset panel.
	 */
	public void reset() {
		pieces = new ArrayList<>();
		repaint();
		isTouchable = false;
	}

	public boolean isTouchable() {
		return isTouchable;
	}

	public void setTouchable(boolean isTouchable) {
		this.isTouchable = isTouchable;
	}
	
	public ArrayList<Piece> undo() {
		ArrayList<Piece> undoPieces = new ArrayList<>();
		for(int i=0;i<2;i++) {
			if (pieces.size() <= 0) {
				break;
			} 
			undoPieces.add(pieces.remove(pieces.size() - 1));
		}
		if (pieces.size() > 0) {
			undoPieces.add(pieces.get(pieces.size() - 1));
		}
		repaint();
		return undoPieces;
		
	}
	
	
}
