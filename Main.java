/*
Name: Zoe Baker
Date: 10/30/2024
Description: A simple Othello version of Othello, which can be played human vs ai or can be
utilized to determine the optimal next move based on what is given. This program
uses mini-max and alpha-beta pruning ai algorithms to determine the moves the ai will use.
*/
import java.util.*;
import java.io.*;

//a "node" which contains a possible move, information behind it, and links to other nodes
class Node{
	//The i and j of Square which is to be given the new piece
	public int[] IJ;
	//List of the Squares which get flipped because of the move being played
	public int[][] flipSquares;
	//The potential amount of points a move will give (heuristic)
	public int potentialValue;
	public Node(int[] takeSquare, char color){
		//set the square we're taking
		IJ = takeSquare;
		//evaluate what squares will be flipped
		flipSquares = determineFlips(color);
		//evaluate the potential amount of points that would be earned by this move
		potentialValue = determineValue();
	}
	
	//determines the squares that will be flipped during a move
	public int[][] determineFlips(char color){
		ArrayList<int[]> toFlip = new ArrayList<>();
		char oppColor = ' ';
		if(color == Square.WHITE){oppColor = Square.BLACK;}
		else if(color == Square.BLACK){oppColor = Square.WHITE;}
		//check around the piece for a piece of the opposite color
		//N
		if(inBounds(IJ[0]-1, IJ[1]) && Othello.board[IJ[0]-1][IJ[1]].getColor() == oppColor){
			//search away from move piece
			int k = IJ[0];
			do{
				k--;
			} while(inBounds(k-1, IJ[1]) && Othello.board[k][IJ[1]].getColor() == oppColor);
			//if we've reached a match, record what's flipped
			if(Othello.board[k][IJ[1]].getColor() == color){
				do{
					toFlip.add(new int[]{k, IJ[1]});
					k++;
				} while(inBounds(k+1, IJ[1]) && Othello.board[k+1][IJ[1]].getColor() == oppColor);
			}
		}
		//NE
		if(inBounds(IJ[0]-1, IJ[1]+1) && Othello.board[IJ[0]-1][IJ[1]+1].getColor() == oppColor){
			//search away from move piece
			int k = IJ[0];
			int m = IJ[1];
			do{
				k--;
				m++;
			}while(inBounds(k-1, m+1) && Othello.board[k][m].getColor() == oppColor);
			//if we've reached a match, record what's flipped
			if(Othello.board[k][m].getColor() == color){
				do{
					toFlip.add(new int[]{k, m});
					k++;
					m--;
				}while(inBounds(k+1, m-1) && Othello.board[k+1][m-1].getColor() == oppColor);
			}
		}
		//E
		if(inBounds(IJ[0], IJ[1]+1) && Othello.board[IJ[0]][IJ[1]+1].getColor() == oppColor){
			//search away from move piece
			int m = IJ[1];
			do{
				m++;
			}while(inBounds(IJ[0], m+1) && Othello.board[IJ[0]][m].getColor() == oppColor);
			//if we've reached a match, record what's flipped
			if(Othello.board[IJ[0]][m].getColor() == color){
				do{
					toFlip.add(new int[]{IJ[0], m});
					m--;
				}while(inBounds(IJ[0], m-1) && Othello.board[IJ[0]][m-1].getColor() == oppColor);
			}
		}
		//SE
		if(inBounds(IJ[0]+1, IJ[1]+1) && Othello.board[IJ[0]+1][IJ[1]+1].getColor() == oppColor){
			//search away from move piece
			int k = IJ[0];
			int m = IJ[1];
			do{
				k++;
				m++;
			}while(inBounds(k+1, m+1) && Othello.board[k][m].getColor() == oppColor);
			//if we've reached a match, record what's flipped
			if(Othello.board[k][m].getColor() == color){
				do{
					toFlip.add(new int[]{k, m});
					k--;
					m--;
				}while(inBounds(k-1, m-1) && Othello.board[k-1][m-1].getColor() == oppColor);
			}
		}
		//S
		if(inBounds(IJ[0]+1, IJ[1]) && Othello.board[IJ[0]+1][IJ[1]].getColor() == oppColor){
			//search away from move piece
			int k = IJ[0];
			do{
				k++;
			}while(inBounds(k+1, IJ[1]) && Othello.board[k][IJ[1]].getColor() == oppColor);
			//if we've reached a match, record what's flipped
			if(Othello.board[k][IJ[1]].getColor() == color){
				do{
					toFlip.add(new int[]{k, IJ[1]});
					k--;
				}while(inBounds(k-1, IJ[1]) && Othello.board[k-1][IJ[1]].getColor() == oppColor);
			}
		}
		//SW
		
		if(inBounds(IJ[0]+1, IJ[1]-1) && Othello.board[IJ[0]+1][IJ[1]-1].getColor() == oppColor){
			//search away from move piece
			int k = IJ[0];
			int m = IJ[1];
			do{
				k++;
				m--;
			}while(inBounds(k+1, m-1) && Othello.board[k][m].getColor() == oppColor);
			//if we've reached a match, record what's flipped
			if(Othello.board[k][m].getColor() == color){
				do{
					toFlip.add(new int[]{k, m});
					k--;
					m++;
				}while(inBounds(k-1, m+1) && Othello.board[k-1][m+1].getColor() == oppColor);
			}
		}
		//W
		if(inBounds(IJ[0], IJ[1]-1) && Othello.board[IJ[0]][IJ[1]-1].getColor() == oppColor){
			//search away from move piece
			int m = IJ[1];
			do {
				m--;
			} while(inBounds(IJ[0], m-1) && Othello.board[IJ[0]][m].getColor() == oppColor);
			//if we've reached a match, record what's flipped
			if(Othello.board[IJ[0]][m].getColor() == color){
				do {
					toFlip.add(new int[]{IJ[0], m});
					m++;
				} while(inBounds(IJ[0], m+1) && Othello.board[IJ[0]][m+1].getColor() == oppColor);
			}
		}
		//NW
		if(inBounds(IJ[0]-1, IJ[1]-1) && Othello.board[IJ[0]-1][IJ[1]-1].getColor() == oppColor){
			//search away from move piece
			int k = IJ[0];
			int m = IJ[1];
			do{
				k--;
				m--;
			}while(inBounds(k-1, m-1) && Othello.board[k][m].getColor() == oppColor);
			//if we've reached a match, record what's flipped
			if(Othello.board[k][m].getColor() == color){
				do{
					toFlip.add(new int[]{k, m});
					k++;
					m++;
				}while(inBounds(k+1, m+1) && Othello.board[k+1][m+1].getColor() == oppColor);
			}
		}
		//convert to array manually
		int[][] flipArr = new int[toFlip.size()][2];
		for(int i = 0; i < toFlip.size(); i++){
			flipArr[i] = toFlip.get(i);
		}
		return flipArr;
	}
	
	//determines the amount of potential points this move would earn
	public int determineValue(){
		int val = flipSquares.length + 1;
		return val;
	}
	
	//helper function which returns whether something is within bounds of board
	public static boolean inBounds(int i, int j){
		boolean isInBounds = true;
		if(i < 0 || i >= Othello.board.length){isInBounds = false;}
		if(j < 0 || j >= Othello.board[0].length){isInBounds = false;}
		
		return isInBounds;
	}
}

//A square can have a piece of either black or white value
class Square{
	//display of the pieces
	public static final char BLACK = 'X';
	public static final char WHITE = 'O';
	//the current state of the square; initialized at ' '
	public char state = ' ';
	public Square(){
	}
	
	//returns the square state
	public char getColor(){
		return this.state;
	}
	
	//sets the state of the square as either black or white
	public void setColor(char newState){
		this.state = newState;
	}
	
	//sets the color of the square to the opposite color
	public void swapColor(){
		if(this.state == BLACK){this.state = WHITE;}
		else if(this.state == WHITE){this.state = BLACK;}
	}
}


//the ai algorithm (implements mini-max and alpha-beta pruning)
class MiniMax{
	//depth of mini-max search
	public static int searchDepth;
	public static int NEGINF = -10000;
	public static int POSINF = 10000;
	public static int statesExamined;
	public MiniMax(){
		return;
	}
	
	//find possible moves
	public static Node[] getLegalMoves(char color){
		char oppColor = ' ';
		if(color == Square.WHITE){oppColor = Square.BLACK;}
		else if(color == Square.BLACK){oppColor = Square.WHITE;}
		//list of Nodes of possible moves
		ArrayList<Node> nodes = new ArrayList<>();
		//parse board of each piece of color
		for(int i = 0; i < Othello.board.length; i++){
			for(int j = 0; j < Othello.board[0].length; j++){
				if(Othello.board[i][j].getColor() == color){
					//check around the piece for a piece of the opposite color
					//N
					if(Node.inBounds(i-1, j) && Othello.board[i-1][j].getColor() == oppColor){
					//record that space as available node
						int k = i-1;
						while(Node.inBounds(k-1, j) && Othello.board[k][j].getColor() == oppColor){
							k--;
						}
						//for each direction one is found, go to first ' ' space in that direction
						if(Othello.board[k][j].getColor() == ' '){
							nodes.add(new Node(new int[]{k,j}, color));
						}
					}
					//NE
					if(Node.inBounds(i-1, j+1) && Othello.board[i-1][j+1].getColor() == oppColor){
					//record that space as available node
						int k = i-1;
						int m = j+1;
						while(Node.inBounds(k-1,m+1) && Othello.board[k][m].getColor() == oppColor){
							k--;
							m++;
						}
						//for each direction one is found, go to first ' ' space in that direction
						if(Othello.board[k][m].getColor() == ' '){
							nodes.add(new Node(new int[]{k,m}, color));
						}
					}
					//E
					if(Node.inBounds(i, j+1) && Othello.board[i][j+1].getColor() == oppColor){
					//record that space as available node
						int m = j+1;
						while(Node.inBounds(i, m+1) && Othello.board[i][m].getColor() == oppColor){
							m++;
						}
						//for each direction one is found, go to first ' ' space in that direction
						if(Othello.board[i][m].getColor() == ' '){
							nodes.add(new Node(new int[]{i,m}, color));
						}
					}
					//SE
					if(Node.inBounds(i+1, j+1) && Othello.board[i+1][j+1].getColor() == oppColor){
					//record that space as available node
						int k = i+1;
						int m = j+1;
						while(Node.inBounds(k+1,m+1) && Othello.board[k][m].getColor() == oppColor){
							k++;
							m++;
						}
						//for each direction one is found, go to first ' ' space in that direction
						if(Othello.board[k][m].getColor() == ' '){
							nodes.add(new Node(new int[]{k,m}, color));
						}
					}
					//S
					if(Node.inBounds(i+1, j) && Othello.board[i+1][j].getColor() == oppColor){
					//record that space as available node
						int k = i+1;
						while(Node.inBounds(k+1, j) && Othello.board[k][j].getColor() == oppColor){
							k++;
						}
						//for each direction one is found, go to first ' ' space in that direction
						if(Othello.board[k][j].getColor() == ' '){
							nodes.add(new Node(new int[]{k,j}, color));
						}
					}
					//SW
					
					if(Node.inBounds(i+1, j-1) && Othello.board[i+1][j-1].getColor() == oppColor){
					//record that space as available node
						int k = i+1;
						int m = j-1;
						while(Node.inBounds(k+1,m-1) && Othello.board[k][m].getColor() == oppColor){
							k++;
							m--;
						}
						//for each direction one is found, go to first ' ' space in that direction
						if(Othello.board[k][j].getColor() == ' '){
							nodes.add(new Node(new int[]{k,m}, color));
						}
					}
					//W
					if(Node.inBounds(i, j-1) && Othello.board[i][j-1].getColor() == oppColor){
					//record that space as available node
						int m = j-1;
						while(Node.inBounds(i, m-1) && Othello.board[i][m].getColor() == oppColor){
							m--;
						}
						//for each direction one is found, go to first ' ' space in that direction
						if(Othello.board[i][m].getColor() == ' '){
							nodes.add(new Node(new int[]{i,m}, color));
						}
					}
					//NW
					if(Node.inBounds(i-1, j-1) && Othello.board[i-1][j-1].getColor() == oppColor){
					//record that space as available node
						int k = i-1;
						int m = j-1;
						while(Node.inBounds(k-1,m-1) && Othello.board[k][m].getColor() == oppColor){
							k--;
							m--;
						}
						//for each direction one is found, go to first ' ' space in that direction
						if(Othello.board[k][m].getColor() == ' '){
							nodes.add(new Node(new int[]{k,m}, color));
						}
					}
				}
			}
		}
		//convert to array manually
		Node[] nodeArr = new Node[nodes.size()];
		for(int i = 0; i < nodes.size(); i++){
			nodeArr[i] = nodes.get(i);
		}
		
		return nodeArr;
	}
	
	//finds best of the options given using alpha-beta pruning
	public static Node performMiniMax(Node[] moves){
		//reset states examined
		statesExamined = 0;
		//call helper function with starting values
		int bestValue = NEGINF;
		if(Othello.alphaBetaEnabled){
			bestValue = getBestAB(moves, NEGINF, POSINF, 0, 0, true);
		}else{
			bestValue = getBestNoAB(moves, 0, 0, true);
		}
		//parse moves list to find move with corresponding best value.
		//returns first node with value if two have same value
		for(int i = 0; i < moves.length; i++){
			if(moves[i].potentialValue == bestValue){
				return moves[i];
			}
		}
		System.out.println("An error has occurred in performMiniMax.");
		return new Node(new int[]{-1, -1}, 'j');
	}
	
	//helper function that is used recursively to find the best move using alpha-beta
	//some code/pseudocode sited from GeeksForGeeks
	public static int getBestAB(Node[] moves, int alpha, int beta, int depth, int nodeIndex, boolean maximizing){
		//if leaf node is reached return node
		searchDepth = (int)Math.round(Math.sqrt(moves.length));
		if(depth >= searchDepth){
			//Keep track of state
			statesExamined++;
			if(Othello.debugEnabled){
				System.out.println("\nLeaf node reached: "
				+ Othello.getString(moves[nodeIndex].IJ)
				+ " for " + moves[nodeIndex].potentialValue + " points.");
			System.out.println("Alpha: " + alpha + "\nBeta: " + beta + "\nDepth: " + depth);
			}
			return moves[nodeIndex].potentialValue;
		}
		
		//if the node's move is beneficial (and not leaf node) adjust alpha
		if(maximizing){
			int bestValue = NEGINF;
			
			//left child recurse
			int leftValue = getBestAB(moves, alpha, beta, depth+1, nodeIndex*2, false);
			if(Othello.debugEnabled){
				System.out.println("Calculating left child: "
				+ Othello.getString(moves[nodeIndex*2].IJ)
				+ " for " + moves[nodeIndex*2].potentialValue + " points.");
			}
			bestValue = Math.max(bestValue, leftValue);
			alpha = Math.max(alpha, bestValue);
			//if beta becomes less than alpha, skip forward
			if(beta <= alpha){return bestValue;}
			
			/* right child recurse
			if moves is odd, the last will not have a right child.
			check if child in bounds */
			if(nodeIndex*2+1 < moves.length){
				int rightValue = getBestAB(moves, alpha, beta, depth+1, nodeIndex*2+1, false);
				if(Othello.debugEnabled){
					System.out.println("Calculating right child: "
					+ Othello.getString(moves[nodeIndex*2+1].IJ)
					+ " for " + moves[nodeIndex*2+1].potentialValue + " points.");
				}
				bestValue = Math.max(bestValue, rightValue);
				alpha = Math.max(alpha, bestValue);
			}
			
			return bestValue;
		}
		
		//if not a beneficial move (and not leaf node) adjust beta
		else{
			int bestValue = POSINF;
			
			//left child recurse
			int leftValue = getBestAB(moves, alpha, beta, depth+1, nodeIndex*2, true);
			if(Othello.debugEnabled){
				System.out.println("Calculating left child: "
				+ Othello.getString(moves[nodeIndex*2].IJ)
				+ " for " + moves[nodeIndex*2].potentialValue + " points.");
			}
			bestValue = Math.min(bestValue, leftValue);
			beta = Math.min(beta, bestValue);
			//if beta becomes less than than alpha, skip forward
			if(beta <= alpha){return bestValue;}
			
			/* right child recurse
			if moves is odd, the last will not have a right child.
			check if child in bounds */
			if(nodeIndex*2+1 < moves.length){
				int rightValue = getBestAB(moves, alpha, beta, depth+1, nodeIndex*2+1, true);
				if(Othello.debugEnabled){
					System.out.println("Calculating right child: "
					+ Othello.getString(moves[nodeIndex*2+1].IJ)
					+ " for " + moves[nodeIndex*2+1].potentialValue + " points.");
				}
				bestValue = Math.min(bestValue, rightValue);
				beta = Math.min(beta, bestValue);
			}
		
			return bestValue;
		}
	}
	
	//helper function that is used recursively to find the best move w/o alpha-beta
	//some code/pseudocode sited from GeeksForGeeks
	public static int getBestNoAB(Node[] moves, int depth, int nodeIndex, boolean maximizing){
		
		//if leaf node is reached return node
		searchDepth = (int)Math.floor(Math.sqrt(moves.length));
		if(depth >= searchDepth){
			//Keep track of state
			statesExamined++;
			if(Othello.debugEnabled){
				System.out.println("\nLeaf node reached: "
				+ Othello.getString(moves[nodeIndex].IJ)
				+ " for " + moves[nodeIndex].potentialValue + " points.");
			System.out.println("Alpha-Beta disabled." + "\nDepth: " + depth);
			}
			return moves[nodeIndex].potentialValue;
		}
		
		//if the node's move is beneficial (and not leaf node) adjust alpha
		if(maximizing){
			int bestValue = NEGINF;
			
			//left child recurse
			int leftValue = getBestNoAB(moves, depth+1, nodeIndex*2, false);
			if(Othello.debugEnabled){
				System.out.println("Calculating left child: "
				+ Othello.getString(moves[nodeIndex*2].IJ)
				+ " for " + moves[nodeIndex*2].potentialValue + " points.");
			}
			bestValue = Math.max(bestValue, leftValue);
			
			/* right child recurse
			if moves is odd, the last will not have a right child.
			check if child in bounds */
			if(nodeIndex*2+1 < moves.length){
				int rightValue = getBestNoAB(moves, depth+1, nodeIndex*2+1, false);
				if(Othello.debugEnabled){
					System.out.println("Calculating right child: "
					+ Othello.getString(moves[nodeIndex*2+1].IJ)
					+ " for " + moves[nodeIndex*2+1].potentialValue + " points.");
				}
				bestValue = Math.max(bestValue, rightValue);
			}
			
			return bestValue;
		}
		
		//if not a beneficial move (and not leaf node) adjust beta
		else{
			int bestValue = POSINF;
			
			//left child recurse
			int leftValue = getBestNoAB(moves, depth+1, nodeIndex*2, true);
			if(Othello.debugEnabled){
				System.out.println("Calculating left child: "
				+ Othello.getString(moves[nodeIndex*2].IJ)
				+ " for " + moves[nodeIndex*2].potentialValue + " points.");
			}
			bestValue = Math.min(bestValue, leftValue);
			
			/* right child recurse
			if moves is odd, the last will not have a right child.
			check if child in bounds */
			if(nodeIndex*2+1 < moves.length){
				int rightValue = getBestNoAB(moves, depth+1, nodeIndex*2+1, true);
				if(Othello.debugEnabled){
					System.out.println("Calculating right child: "
					+ Othello.getString(moves[nodeIndex*2+1].IJ)
					+ " for " + moves[nodeIndex*2+1].potentialValue + " points.");
				}
				bestValue = Math.min(bestValue, rightValue);
			}
		
			return bestValue;
		}
	}
}


//the command line interface
class Othello{
	//debug is off by default
	public static boolean debugEnabled = false;
	//alpha-beta is off by default
	public static boolean alphaBetaEnabled = false;
	//keep track of scores. Heuristic is what gives the most points
	public static int AIScore = 0;
	public static int playerScore = 0;
	public static boolean gameInProgress = false;
	public static Square[][] board = new Square[8][8];
	//changes w/ each turn - global for ease of access
	public static Node[] options;
	public static Node choice;
	public Othello(){
		return;
	}
	
	//resets board to starting state
	public static void resetBoard(){
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[0].length; j++){
				if(board[i][j] == null){
					board[i][j] = new Square();
				}
				board[i][j].setColor(' ');
			}
		}
		board[3][3].setColor(Square.WHITE);
		board[4][3].setColor(Square.BLACK);
		board[3][4].setColor(Square.BLACK);
		board[4][4].setColor(Square.WHITE);
	}
	
	//updates display of b/w pieces on board
	public static void updateBoard(){
		System.out.println("\n");
		//upper border of "board"
		System.out.println("     A  B  C  D  E  F  G  H  ");
		System.out.println("  ----------------------------");
		//pieces
		for(int i = 0; i < board.length; i++){
			System.out.print(i+1 + " |  ");
			for(int j = 0; j < board[0].length; j++){
				System.out.print(board[i][j].getColor());
				System.out.print("  ");
			}
			System.out.println("  |");
		}
		//lower border of "board"
		System.out.println("  ----------------------------");
		
		//scores
		System.out.println("Player: " + playerScore + "\tAI: " + AIScore + "\n\n");
		
	}
	
	//play move given
	//calls MiniMax to figure out the next possible moves and, if necessary, displaying to CLI
	//user is either "AI" or "player"
	public static void selectMove(String user, char color){
		Scanner scanner = new Scanner(System.in);
		//calls MiniMax
		if(user.equals("AI") && debugEnabled){System.out.println("AI's Turn: Debug Enabled.\n");}

		//get options and, if AI, choice
		options = MiniMax.getLegalMoves(color);
		//game ending criteria - no moves left
		if(options.length == 0){
			gameInProgress = false;
			return;
		}
		if(user.equals("AI")){
		choice = MiniMax.performMiniMax(options);
		}
		
		//debug
		if(user.equals("AI") && debugEnabled){
			System.out.print("\nStates: " + options.length);
			System.out.print("\nStates examined:" + MiniMax.statesExamined);
			System.out.println("\nPossible options:");
			for(Node o : options){
				String str = Othello.getString(o.IJ);
				System.out.println(str + " - " + o.potentialValue + " points");
			}
			System.out.println("\nChosen move: " + Othello.getString(choice.IJ));
			System.out.println("Executing . . .");
		}
		//allows user to decide next move or give choice to AI
		else if(user.equals("player")){
			System.out.println("Your options: \n" + 
				"0: Let AI decide\n" + 
				"1: Toggle Debug Mode\n" + 
				"2: Toggle Alpha-Beta"
			);
			for(int i = 3; i < options.length+3; i++){
				System.out.println(i + ": " + Othello.getString(options[i-3].IJ));
			}
			
			//get user input
			System.out.print(">> ");
			int userInput = -1;
			while(userInput < 0 || userInput >= options.length+3){
				try{
					userInput = Integer.parseInt(scanner.next());
				} catch(Exception e){
					System.out.println("Unrecognized input.");
					System.out.print(">> ");
					continue;
				}
				if(userInput < 0 || userInput >= options.length+3){
					System.out.println("Unrecognized input.");
					System.out.print(">> ");
				}
			}
			
			//evaluate selection
			if(userInput == 0){
				//recursive call/make AI decide
				selectMove("AI", Square.BLACK);
			}else if(userInput == 1){
				//toggle debug and recursive call
				Othello.debugEnabled = !Othello.debugEnabled;
				System.out.println("\nDebug changed to " + debugEnabled + "\n");
				selectMove("player", Square.BLACK);
			}else if(userInput == 2){
				//toggle alpha-beta and recursive call
				Othello.alphaBetaEnabled = !Othello.alphaBetaEnabled;
				System.out.println("\nAlpha-Beta changed to " + alphaBetaEnabled + "\n");
				selectMove("player", Square.BLACK);
			}else{
				//choice is move that was selected
				choice = options[userInput-3];
			}
			
		}
		//play move selected by either AI or player
		playMove(getString(choice.IJ), color);
		
		//update points
		Othello.updatePoints();
	}
	
	//updates selected square with color and affected Squares
	public static void playMove(String move, char color){
		char oppColor = ' ';
		if(color == Square.WHITE){oppColor = Square.BLACK;}
		else if(color == Square.BLACK){oppColor = Square.WHITE;}
		//find i and j value for given string
		int[] IJ = getIJ(move);
		board[IJ[0]][IJ[1]].setColor(color);
		//update affected squares' colors
		ArrayList<int[]> toFlip = new ArrayList<>();
		
		//check around the piece for a piece of the opposite color
		//N
		if(Node.inBounds(IJ[0]-1, IJ[1]) && Othello.board[IJ[0]-1][IJ[1]].getColor() == oppColor){
			//search away from move piece
			int k = IJ[0]-1;
			while(Node.inBounds(k-1, IJ[1]) && Othello.board[k][IJ[1]].getColor() == oppColor){
				k--;
			}
			//if we've reached a match, record it
			if(Othello.board[k][IJ[1]].getColor() == color){
				toFlip.add(new int[]{k, IJ[1]});
			}
		}
		//NE
		if(Node.inBounds(IJ[0]-1, IJ[1]+1) && Othello.board[IJ[0]-1][IJ[1]+1].getColor() == oppColor){
			//search away from move piece
			int k = IJ[0]-1;
			int m = IJ[1]+1;
			while(Node.inBounds(k-1, m+1) && Othello.board[k][m].getColor() == oppColor){
				k--;
				m++;
			}
			//if we've reached a match, record it
			if(Othello.board[k][m].getColor() == color){
				toFlip.add(new int[]{k, m});
			}
		}
		//E
		if(Node.inBounds(IJ[0], IJ[1]+1) && Othello.board[IJ[0]][IJ[1]+1].getColor() == oppColor){
			//search away from move piece
			int m = IJ[1]+1;
			while(Node.inBounds(IJ[0], m+1) && Othello.board[IJ[0]][m].getColor() == oppColor){
				m++;
			}
			//if we've reached a match, record it
			if(Othello.board[IJ[0]][m].getColor() == color){
				toFlip.add(new int[]{IJ[0], m});
			}
		}
		//SE
		if(Node.inBounds(IJ[0]+1, IJ[1]+1) && Othello.board[IJ[0]+1][IJ[1]+1].getColor() == oppColor){
			//search away from move piece
			int k = IJ[0]+1;
			int m = IJ[1]+1;
			while(Node.inBounds(k+1,m+1) && Othello.board[k][m].getColor() == oppColor){
				k++;
				m++;
			}
			//if we've reached a match, record it
			if(Othello.board[k][m].getColor() == color){
				toFlip.add(new int[]{k, m});
			}
		}
		//S
		if(Node.inBounds(IJ[0]+1, IJ[1]) && Othello.board[IJ[0]+1][IJ[1]].getColor() == oppColor){
			//search away from move piece
			int k = IJ[0]+1;
			while(Node.inBounds(k+1, IJ[1]) && Othello.board[k][IJ[1]].getColor() == oppColor){
				k++;
			}
			//if we've reached a match, record it
			if(Othello.board[k][IJ[1]].getColor() == color){
				toFlip.add(new int[]{k, IJ[1]});
			}
		}
		//SW
		
		if(Node.inBounds(IJ[0]+1, IJ[1]-1) && Othello.board[IJ[0]+1][IJ[1]-1].getColor() == oppColor){
			//search away from move piece
			int k = IJ[0]+1;
			int m = IJ[1]-1;
			while(Node.inBounds(k+1, m-1) && Othello.board[k][m].getColor() == oppColor){
				k++;
				m--;
			}
			//if we've reached a match, record it
			if(Othello.board[k][m].getColor() == color){
				toFlip.add(new int[]{k, m});
			}
		}
		//W
		if(Node.inBounds(IJ[0], IJ[1]-1) && Othello.board[IJ[0]][IJ[1]-1].getColor() == oppColor){
			//search away from move piece
			int m = IJ[1]-1;
			while(Node.inBounds(IJ[0], m-1) && Othello.board[IJ[0]][m].getColor() == oppColor){
				m--;
			}
			//if we've reached a match, record it
			if(Othello.board[IJ[0]][m].getColor() == color){
				toFlip.add(new int[]{IJ[0], m});
			}
		}
		//NW
		if(Node.inBounds(IJ[0]-1, IJ[1]-1) && Othello.board[IJ[0]-1][IJ[1]-1].getColor() == oppColor){
			//search away from move piece
			int k = IJ[0]-1;
			int m = IJ[1]-1;
			while(Node.inBounds(k-1, m-1) && Othello.board[k][m].getColor() == oppColor){
				k--;
				m--;
			}
			//if we've reached a match, record it
			if(Othello.board[k][m].getColor() == color){
				toFlip.add(new int[]{k, m});
			}
		}
		
		//parse found matches
		for(int i = 0; i < toFlip.size(); i++){
			//parse from move to match
			//N
			for(int n = IJ[0]-1; n > toFlip.get(i)[0]; n--){
				board[n][IJ[1]].swapColor();
			}
			//S
			for(int n = IJ[0]+1; n < toFlip.get(i)[0]; n++){
				board[n][IJ[1]].swapColor();
			}
			//W
			for(int n = IJ[1]-1; n > toFlip.get(i)[1]; n--){
				board[IJ[0]][n].swapColor();
			}
			//E
			for(int n = IJ[1]+1; n < toFlip.get(i)[1]; n++){
				board[IJ[0]][n].swapColor();
			}
			//NW
			for(int n = IJ[0]-1, m = IJ[1]-1; n > toFlip.get(i)[0] && m > toFlip.get(i)[1]; n--, m--){
				board[n][m].swapColor();
			}
			//NE
			for(int n = IJ[0]-1, m = IJ[1]+1; n > toFlip.get(i)[0] && m < toFlip.get(i)[1]; n--, m++){
				board[n][m].swapColor();
			}
			//SE
			for(int n = IJ[0]+1, m = IJ[1]+1; n < toFlip.get(i)[0] && m < toFlip.get(i)[1]; n++, m++){
				board[n][m].swapColor();
			}
			//SW
			for(int n = IJ[0]+1, m = IJ[1]-1; n < toFlip.get(i)[0] && m > toFlip.get(i)[1]; n++, m--){
				board[n][m].swapColor();
			}
			
		}
	}
	
	//converts a string to an i and j value on the board
	public static int[] getIJ(String squareCode){
		//i value is vertical (number), j value is horizontal (letter)
		int[] IJ = new int[2];
		char number = squareCode.charAt(1);
		IJ[0] = Character.getNumericValue(number) - 1;
		char letter = squareCode.charAt(0);
		switch(letter){
			case 'A':
				IJ[1] = 0;
				break;
			case 'B':
				IJ[1] = 1;
				break;
			case 'C':
				IJ[1] = 2;
				break;
			case 'D':
				IJ[1] = 3;
				break;
			case 'E':
				IJ[1] = 4;
				break;
			case 'F':
				IJ[1] = 5;
				break;
			case 'G':
				IJ[1] = 6;
				break;
			case 'H':
				IJ[1] = 7;
				break;
			default:
				break;
		}
		return IJ;
	}
	
	//converts an i and j value array into a string value on the board
	public static String getString(int[] IJ){
		//i value is vertical (number), j value is horizontal (letter)
		String s = "";
		int letter = IJ[1];
		switch(letter){
			case 0:
				s = "A";
				break;
			case 1:
				s = "B";
				break;
			case 2:
				s = "C";
				break;
			case 3:
				s = "D";
				break;
			case 4:
				s = "E";
				break;
			case 5:
				s = "F";
				break;
			case 6:
				s = "G";
				break;
			case 7:
				s = "H";
				break;
			default:
				break;
		}
		int num = IJ[0] + 1;
		s += Integer.toString(num);
		return s;
	}
	
	//updates scores
	public static void updatePoints(){
		//reset
		AIScore = playerScore = 0;
		//calculate
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[0].length; j++){
				if(board[i][j].getColor() == Square.WHITE){AIScore++;}
				else if(board[i][j].getColor() == Square.BLACK){playerScore++;}
			}
		}
	}
	
	//message of game over displayed
	public static void displayGameOver(){
		//determine winner
		String winner;
		if(AIScore > playerScore){winner = "AI";}
		else if(playerScore > AIScore){winner = "PLAYER";}
		else{winner = "DRAW";}
		
		//print in CLI
		System.out.println("~~~~~GAME OVER!!~~~~~");
		System.out.println("\n    WINNER: " + winner);
		System.out.println("\n\n Thanks for playing!");
		System.out.println("\n~~~~~~~~~~~~~~~~~~~~~");
	}
	
	//exits the program
	public static void exitNetwork(){
		System.out.println("Understood: exiting. Have a good day!");
		System.exit(0);
	}
}


//Main class
class Main{
	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		while(true){
			//Starting screen upon running program
			System.out.println("\n\nWelcome to Othello. Please select your game mode by typing a number:\n" +
				"0: Exit\n" +
				"1: Play solo Othello without alpha-beta\n" +
				"2: Play solo Othello with alpha-beta\n"
			);
			System.out.print(">> ");
			//get selection
			int option1 = -1;
			try{
				option1 = Integer.parseInt(scanner.next());
			} catch(Exception e){
				System.out.println("Unrecognized input.");
				continue;
			}
			//evaluate selection
			switch (option1){
				case 0:
					Othello.exitNetwork();
					break;
				case 1:
					System.out.println("Your selection: " + option1 + "\n");
					Othello.gameInProgress = true;
					Othello.alphaBetaEnabled = false;
					break;
				case 2:
					System.out.println("Your selection: " + option1 + "\n");
					Othello.gameInProgress = true;
					Othello.alphaBetaEnabled = true;
					break;
				default:
					System.out.println("Unrecognized input.");
					continue;
			}
			
			//Play the game
			System.out.println("You play BLACK");
			Othello.resetBoard();
			Othello.updateBoard();
			while(Othello.gameInProgress){
				//player's turn
				System.out.println("Your move . . .\n");
				//find legal moves
				Othello.selectMove("player", Square.BLACK);
				//update Othello
				Othello.updateBoard();
				
				//check if the game is over
				if(!Othello.gameInProgress){
					Othello.displayGameOver();
					break;
				}
				
				//pause to simulate AI thinking and give
				//user time to process move
				try{
					Thread.sleep(3000);
				}catch(InterruptedException e){
					Thread.currentThread().interrupt();
				}
				
				//if game is still on, continue to AI's turn
				System.out.println("AI's turn . . .");
				//find legal moves (only displayed in debug mode)
				Othello.selectMove("AI", Square.WHITE);
				//update Othello
				Othello.updateBoard();
				
				//check if the game is over
				if(!Othello.gameInProgress){
					Othello.displayGameOver();
					break;
				}
			}
		}
	}
}