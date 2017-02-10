package GUI;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import model.Piece;


public class BoardPanel extends JPanel {
	
	static private final ImageIcon BOARD_IMAGE_ICON = new ImageIcon("res/board.png");
	
	static private final int PIECES_GAP = 4;
	
	private int boardGap = 0; 
	private int lineGap = 0;
	private int pieceWidth = 0;
	
	private List<Piece> pieces = new ArrayList<>();
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		// width%46 == 0
		super.setBounds(x, y, width, height);
		lineGap = (width * 3) / 46;
		boardGap = lineGap / 3 * 2;
		pieceWidth = lineGap - PIECES_GAP;
		
		
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
	public void addPiece(Piece piece) {
		pieces.add(piece);
		repaint();
	}
	
	/*
	 * Reset panel.
	 */
	public void reset() {
		pieces = new ArrayList<>();
		repaint();
	}
}
