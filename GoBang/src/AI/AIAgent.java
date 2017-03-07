package AI;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.AINode;
import model.Board;
import model.BoardData;
import model.OpeningBook;
import model.Piece;
import model.PieceColor;
import model.Score;

public class AIAgent{
	
	private int level = 4;
	private PieceColor pieceColor;
	
	public AIAgent(PieceColor pieceColor) {
		// TODO Auto-generated constructor stub
		this.pieceColor = pieceColor;
	}
	
	private Piece randomNeighborPiece(int col, int row, PieceColor pieceColor) {
		Random random = new Random();
		int c,r;
		do {
			c = col + (random.nextInt(3) - 1);
			r = row + (random.nextInt(3) - 1);
		} while (c==col && r==row);
		return new Piece(c, r, pieceColor);
	}
	
	public Piece run(Board board) {
		Piece p = OpeningBook.getInstance().getPiece(board.getBoardData().getHashValue());
		if (p != null) {
			return p;
		}
		int score = Score.getScore(board.getBoardData());
		System.out.println("Current: " + score);
//		int searchDepth = ((pieceColor.equals(PieceColor.Black) && score >= 0) || (pieceColor.equals(PieceColor.White) && score < 0)) ? level + 1 : level;
		AINode node = minimaxSearch(new AINode(board), level, Integer.MIN_VALUE, Integer.MAX_VALUE, pieceColor.equals(PieceColor.Black));
		System.out.println(node.toString());
		return node.getPiece();
	}
	
	/*
	 * 	Main function to do MiniMax Search.
	 * 	currentNode:	current state
	 * 	depth:			search depth
	 * 	alpha:			minimum value of max
	 * 	beta:			maximum value of min
	 * 	isMax:			Ture -> do Max		False -> do Min
	 * 	Alpha-beta pruning:		if alpha >= beta, pruning
	 */	
	private AINode minimaxSearch(AINode currentNode, int depth, int alpha, int beta, boolean isMax){
		if (depth <= 0 || !currentNode.isExplorable() || currentNode.isOnlyNode()) {
			return null;
		}
		currentNode.generatePossiblePositions();
		AINode tempNode = (depth%2 == level%2) ? currentNode.unstoppableSubNode(level * 4): currentNode.unstoppableSubNode(level * 2);
		if (tempNode != null) {
			return tempNode;
		}
		for(AINode node: currentNode.exploreNodes()) {
			node.setSubNode(minimaxSearch(node, depth - 1, alpha, beta, !isMax));
			if (isMax) {
				if (tempNode == null || tempNode.getSubNodeScore() < node.getSubNodeScore()) {
					tempNode = node;
				}
				alpha = Math.max(alpha, node.getSubNodeScore());
			} else {
				if (tempNode == null || tempNode.getSubNodeScore() > node.getSubNodeScore()) {
					tempNode = node;
				}
				beta = Math.min(beta, node.getSubNodeScore());
			}
			if (alpha >= beta || alpha > Score.WIN_SCORE || beta < -Score.WIN_SCORE) {
				break;
			}
		}
		return tempNode;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public PieceColor getPieceColor() {
		return pieceColor;
	}

	public void setPieceColor(PieceColor pieceColor) {
		this.pieceColor = pieceColor;
	}
	
	
	
}
