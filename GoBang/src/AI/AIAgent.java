package AI;


import model.AINode;
import model.Board;
import model.Piece;
import model.PieceColor;
import model.Score;

public class AIAgent{
	
	private int level = 4;
	private PieceColor pieceColor = PieceColor.White;
	
	public Piece run(Board board) {
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
			node.setSubNode(minimaxSearch(node, depth - 1, alpha, beta, !isMax));
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
