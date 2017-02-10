package model;

/*
 * 		x, y:		Location of piece(from 0 to 8)
 * 		color:		Piece color(white or black)
 * 
 */

public class Piece {
	
	private int col;
	private int row;
	private PieceColor color;

	public Piece(int col, int row, PieceColor color) {
		super();
		this.col = col;
		this.row = row;
		this.color = color;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public PieceColor getColor() {
		return color;
	}

	public void setColor(PieceColor color) {
		this.color = color;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return (new Integer(col*9 + row)).hashCode() + color.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj instanceof Piece) {
			Piece p = (Piece) obj;
			return this.col == p.getCol() && this.row == p.getRow() && this.color.equals(p.getColor());
		}
		return false;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "col: " + col + " row: " + row + " color: " + color.toString();
	}
	
	
	
}
