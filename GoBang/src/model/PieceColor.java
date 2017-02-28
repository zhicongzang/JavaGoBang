package model;

import java.awt.Image;

import javax.swing.ImageIcon;



public enum PieceColor {
	White, Black;
	
	static private final ImageIcon BLACK_IMAGE_ICON = new ImageIcon("res/stone_black.png");
	static private final ImageIcon WHITE_IMAGE_ICON = new ImageIcon("res/stone_white.png"); 
	
	/*
	 * 	return the Image of piece.
	 */
	public Image getPaint() {
		switch (this) {
		case White:
			return WHITE_IMAGE_ICON.getImage();
		case Black:
			
			return BLACK_IMAGE_ICON.getImage();	
		default:
			return null;
		}
	}
	
	/*
	 *	White to Black, Black to White. 
	 */
	public PieceColor changeColor() {
		switch (this) {
		case White:
			return Black;
		case Black:
			return White;	
		default:
			return null;
		}
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		switch (this) {
		case White:
			return "white";
		case Black:
			
			return "black";	
		default:
			return null;
		}
	}
	
}
