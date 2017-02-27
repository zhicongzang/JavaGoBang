package AI;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.AINode;
import model.Board;
import model.Piece;
import model.PieceColor;
import model.Score;

public class AIAgent{
	
	private int level = 3;
	private PieceColor pieceColor;
	private List<Piece> piecesWaitingList = new ArrayList<>();
	
	public AIAgent(PieceColor pieceColor) {
		// TODO Auto-generated constructor stub
		this.pieceColor = pieceColor;
		if (pieceColor.equals(PieceColor.Black)) {
			piecesWaitingList.add(new Piece(7, 7, pieceColor));
		} else {
			piecesWaitingList.add(randomNeighborPiece(7, 7, pieceColor));
		}
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
		if (piecesWaitingList.size() > 0) {
			return piecesWaitingList.remove(0);
		}
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
		if (depth <= 0 || !currentNode.isExplorable()) {
			return null;
		}
		AINode tempNode = null;
		for(AINode node: currentNode.exploreNodes()) {
			node.setSubNode(minimaxSearch(node, node.needMoreThink() ? depth :depth - 1, alpha, beta, !isMax));
			if (isMax) {
				if (tempNode == null || tempNode.getScore() < node.getScore()) {
					tempNode = node;
				}
				alpha = Math.max(alpha, node.getScore());
			} else {
				if (tempNode == null || tempNode.getScore() > node.getScore()) {
					tempNode = node;
				}
				beta = Math.min(beta, node.getScore());
			}
			if (alpha >= beta || alpha > Score.WIN_SCORE || beta < Score.LOSE_SCORE) {
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
