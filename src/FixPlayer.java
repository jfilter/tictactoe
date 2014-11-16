import java.util.ArrayList;
import java.util.List;

import de.ovgu.dke.teaching.ml.tictactoe.api.IBoard;
import de.ovgu.dke.teaching.ml.tictactoe.api.IMove;
import de.ovgu.dke.teaching.ml.tictactoe.api.IPlayer;
import de.ovgu.dke.teaching.ml.tictactoe.api.IllegalMoveException;
import de.ovgu.dke.teaching.ml.tictactoe.game.Move;

/**
 * The 
 * 
 * @author Johannes Filter
 */
public class FixPlayer implements IPlayer {
	
	private double adaptRate = 0.001;
	private double[] weights;	
	private IBoard oldBoard;
	
	// Overloaded, this is used, when the round is still ongoing.
	private void adaptWeights(IBoard oldBoard, IBoard newBoard){
		double vApprox = getScore(oldBoard);
		double vTrain = getScore(newBoard);
		int[] x = getFeatures(oldBoard);
		
		for(int i = 1; i < weights.length; i++) {
			weights[i] += adaptRate * (vTrain - vApprox) * x[i - 1]; // weil X ein Wert weniger hat.
		}
	}
	
	// Overloaded, this is used, when it a Game is finished.
	private void adaptWeights(IBoard oldBoard, double vTrain){
		double vApprox = getScore(oldBoard);
		int[] x = getFeatures(oldBoard);
		
		for(int i = 1; i < weights.length; i++) {
			weights[i] += adaptRate * (vTrain - vApprox) * x[i - 1]; // weil X ein Wert weniger hat.
		}
	}
	
	// Calculating the Features of the Board.
	// WARNING! Horrible Code! Don't look into it. ;)
	// Basic Idea:
	// weight[1]: 1 Cross in a row with no enemy cross.
	// weight[2]: 2 Crosses in a row with no enemy cross.
	// etc.
	// weight[5]: 1 Cross in a row from enemy and no cross from me
	// etc.
	// Doing it with for loops is a pain.
	// I would suggests doing it with more math.
	private int[] getFeatures(IBoard b){ 
		int[] features = new int[8];
		
		// 1
		for(int i = 0; i < 5; i++){
			for(int ii = 0; ii < 5; ii++){
				String name = null;
				int count = 0;
				boolean useful = true;
				
				for(int iii = 0; iii < 5; iii++){
					int[] a = {i,ii,iii};
					IPlayer p = b.getFieldValue(a);
					
					if(p != null){
						if(count == 0){
							count++;
							name = p.getName();					
						} else {
							if (name == p.getName()) {
								count++;
							} else {
								useful = false;
							}
						}
					}
				}
				if (name != null && useful){
					if (name == this.getName()) {
						features[count - 1]++;
					} else {
						features[count - 1 + 4]++;
					}
				}
			}
		}
		
		// 2
		for(int i = 0; i < 5; i++){
			for(int ii = 0; ii < 5; ii++){
				String name = null;
				int count = 0;
				boolean useful = true;
				
				for(int iii = 0; iii < 5; iii++){
					int[] a = {i,iii,ii};
					IPlayer p = b.getFieldValue(a);
					
					if(p != null){
						if(count == 0){
							count++;
							name = p.getName();					
						} else {
							if (name == p.getName()) {
								count++;
							} else {
								useful = false;
							}
						}
					}
				}
				if (name != null && useful){
					if (name == this.getName()) {
						features[count - 1]++;
					} else {
						features[count - 1 + 4]++;
					}
				}
			}
		}
		
		// 3
		for(int i = 0; i < 5; i++){
			for(int ii = 0; ii < 5; ii++){
				String name = null;
				int count = 0;
				boolean useful = true;
				
				for(int iii = 0; iii < 5; iii++){
					int[] a = {iii,ii,i};
					IPlayer p = b.getFieldValue(a);
					
					if(p != null){
						if(count == 0){
							count++;
							name = p.getName();					
						} else {
							if (name == p.getName()) {
								count++;
							} else {
								useful = false;
							}
						}
					}
				}
				if (name != null && useful){
					if (name == this.getName()) {
						features[count - 1]++;
					} else {
						features[count - 1 + 4]++;
					}
				}
			}
		}
		
		// Schrägen
		
		for(int i = 0; i < 5; i++){
				String name = null;
				int count = 0;
				boolean useful = true;
				for(int ii = 0; ii < 5; ii++){
					int[] a = {i,ii,ii};
					IPlayer p = b.getFieldValue(a);
					
					if(p != null){
						if(count == 0){
							count++;
							name = p.getName();					
						} else {
							if (name == p.getName()) {
								count++;
							} else {
								useful = false;
							}
						}
					}
				}
				if (name != null && useful){
					if (name == this.getName()) {
						features[count - 1]++;
					} else {
						features[count - 1 + 4]++;
					}
				}
			}
	
		for(int i = 0; i < 5; i++){
			String name = null;
			int count = 0;
			boolean useful = true;
			for(int ii = 0; ii < 5; ii++){
				int[] a = {4 - ii,ii,i};
				IPlayer p = b.getFieldValue(a);
				
				if(p != null){
					if(count == 0){
						count++;
						name = p.getName();					
					} else {
						if (name == p.getName()) {
							count++;
						} else {
							useful = false;
						}
					}
				}
			}
			if (name != null && useful){
				if (name == this.getName()) {
					features[count - 1]++;
				} else {
					features[count - 1 + 4]++;
				}
			}
		}
	
		// Diagonale
		String name = null;
		int count = 0;
		boolean useful = true;	
		
		for(int i = 0; i < 5; i++) {
			int[] a = {i,i,i};
			IPlayer p = b.getFieldValue(a);
			
			if(p != null){
				if(count == 0){
					count++;
					name = p.getName();					
				} else {
					if (name == p.getName()) {
						count++;
					} else {
						useful = false;
					}
				}
			}	
		}
		
		if (name != null && useful){
			if (name == this.getName()) {
				features[count - 1]++;
			} else {
				features[count - 1 + 4]++;
			}
		}
		
		name = null;
		count = 0;
		useful = true;	
		
		for(int i = 0; i < 5; i++) {
			int[] a = {4 - i,i,i};
			IPlayer p = b.getFieldValue(a);
			
			if(p != null){
				if(count == 0){
					count++;
					name = p.getName();					
				} else {
					if (name == p.getName()) {
						count++;
					} else {
						useful = false;
					}
				}
			}	
		}
		
		if (name != null && useful){
			if (name == this.getName()) {
				features[count - 1]++;
			} else {
				features[count - 1 + 4]++;
			}
		}
		
		name = null;
		count = 0;
		useful = true;	
		
		for(int i = 0; i < 5; i++) {
			int[] a = {i,4 - i,i};
			IPlayer p = b.getFieldValue(a);
			
			if(p != null){
				if(count == 0){
					count++;
					name = p.getName();					
				} else {
					if (name == p.getName()) {
						count++;
					} else {
						useful = false;
					}
				}
			}	
			
		}
		
		if (name != null && useful){
			if (name == this.getName()) {
				features[count - 1]++;
			} else {
				features[count - 1 + 4]++;
			}
		}
		
		name = null;
		count = 0;
		useful = true;	
		
		for(int i = 0; i < 5; i++) {
			int[] a = {i ,i , 4 - i};
			IPlayer p = b.getFieldValue(a);
			
			if(p != null){
				if(count == 0){
					count++;
					name = p.getName();					
				} else {
					if (name == p.getName()) {
						count++;
					} else {
						useful = false;
					}
				}
			}	
			
		}
		
		if (name != null && useful){
			if (name == this.getName()) {
				features[count - 1]++;
			} else {
				features[count - 1 + 4]++;
			}
		}
		
		return features;
	}
	
	// getting all possible moves
	private ArrayList<IBoard> allMoves(IBoard b){
		ArrayList<IBoard> moves = new ArrayList<IBoard>();
		
		for(int i = 0; i < 5; i++){
			for(int ii = 0; ii < 5; ii++){				
				for(int iii = 0; iii < 5; iii++){
					int[] a = {i, ii, iii};
					if(b.getFieldValue(a) == null){
						IBoard copy = b.clone();
						try {
							copy.makeMove(new Move(this, a));
						} catch (IllegalMoveException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						moves.add(copy);
					}
				}
			}
		}
		return moves;
	}

	// calculating all scores from possibile moves(future boards) and going for the best
	private int[] chooseMove(IBoard b){
		ArrayList<IBoard> allMoves = allMoves(b);
		
		IBoard bestMove = null;
		double bestScore = -1;
		
		for(IBoard move: allMoves) {
			double scoreMove = getScore(move);
			if(bestMove == null || scoreMove > bestScore){
				bestMove = move;
				bestScore = scoreMove;
			}
		}
		
		return getMoveFromDif(b, bestMove);
	}
	
	// getting last move from the difference of 2 boards
	private int[] getMoveFromDif(IBoard now, IBoard future){
		for(int i = 0; i < 5; i++){
			for(int ii = 0; ii < 5; ii++){				
				for(int iii = 0; iii < 5; iii++){
					int[] a = {i, ii, iii};
					if(now.getFieldValue(a) != future.getFieldValue(a)){
						return a;
					}
				}
			}
		}
		return null;
	}
	
	// calculating the target function
	private double getScore(IBoard b){
		int[] x = getFeatures(b);
		double res = weights[0];
		for(int i = 0; i < 8; i++) {
			res += weights[i + 1] * x[i];
		}
		return res;
	}
	
	public String getName() {
		return "Steve Jobs";
	}

	public int[] makeMove(IBoard board) {
		if(weights == null) { // initalizing.
			weights = new double[9];
			weights[0] = 0;
			weights[1] = 1.7092784021281027;
			weights[2] = -2.6883819757441305;
			weights[3] = -1.7634492191191449;
			weights[4] = -0.1924649552445665;
			weights[5] = 8.236948521098762;
			weights[6] = -5.003354163742613;
			weights[7] = -2.527633041571055;
			weights[8] = -0.20747909321800015;
		} else {
			if(board.getMoveHistory().size() > 2){ // not in first round
//				adaptWeights(oldBoard, board);
			}
		}
		oldBoard = board.clone(); // used for adapting the weights
		
//		 for(int i = 0; i<weights.length; i++){
//		 	System.out.print(weights[i] + " | ");
//		 }
//		 System.out.println();
		return chooseMove(board);
	}

	public void onMatchEnds(IBoard board) {
//		if(board.getWinner().getName() == this.getName()){
//			adaptWeights(oldBoard, 100);
//		} else {
//			adaptWeights(oldBoard, -100);			
//		}
		return;
	}
}
