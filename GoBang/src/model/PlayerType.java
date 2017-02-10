package model;

import AI.AIAgent;

public enum PlayerType {
	Player, AI;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		switch (this) {
		case Player:
			return "Player";
		case AI:
			return "AI";
		default:
			return super.toString();
		}
	}
	
	public AIAgent getAgent(PieceColor pieceColor) {
		switch (this) {
		case AI:
			return new AIAgent(pieceColor);
		default:
			return null;
		}
	}
	
}
