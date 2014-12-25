package model;

import java.util.Observable;

public class Board extends Observable {
	
	public void printState() {
		System.out.print("test\n");
		for (int i=0; i<WIDTH; i++) {
			for (int j=0; j<HEIGHT; j++) {
				if (grid[i][j].getPlayer() != null) {
					System.out.print(getGrid()[i][j].getPlayer()+" ");
				} else {
				System.out.print(getGrid()[i][j].getOpponent()+" ");
				}
			}
			System.out.print("\n");
			
		}
	}
	
	// VARIABLES 
	
	final int WIDTH = 10;
	final int HEIGHT = 10;
	final int TOTAL_NUMBER_OF_ENEMIES = 4;
	final int TOTAL_NUMBER_OF_HELPERS = 4;	
	int DIFFICULTY_LEVEL = 1;
	final int initialXPosition = 0;
	final int initialYPosition = 0;
	final Position initialPosition = new Position(initialXPosition, initialYPosition);
	final int xTrophy = (int) (WIDTH*0.75);
	final int yTrophy = (int) (HEIGHT*0.75);
	
	private Player player;
	private Tile[][] grid = new Tile[WIDTH][HEIGHT];
	private Trophy trophy;
	private Position activePosition = initialPosition;
	private boolean gameFinished = false;
	
	
	// CONSTRUCTOR
	
	
	// METHODS 
	
	public void initBoard() {
		
		player = new Player(initialPosition, DIFFICULTY_LEVEL);
		trophy = new Trophy();
		activePosition = initialPosition;
		gameFinished = false;
		
		int numberOfHelpers = 0;
		int numberOfEnemies = 0;
		
		// create all the tiles
		for (int i=0; i<WIDTH; i++) {
			for (int j=0; j<HEIGHT; j++) {
				this.grid[i][j] = new Tile("grass");
			}
		}
		// add player
		grid[initialXPosition][initialYPosition].setPlayer(player);
		// add trophy
		grid[xTrophy][yTrophy].setOpponent(trophy);	
		// randomly add enemies (only on even coordinates to spread them)
		while (numberOfEnemies < TOTAL_NUMBER_OF_ENEMIES) {
			int x = 2*(1 + (int)(Math.random()*(WIDTH/2-1)));
			int y = 2*(1 + (int)(Math.random()*(HEIGHT/2-1)));
			if (grid[x][y].getOpponent() == null) {
				grid[x][y].setOpponent(new Enemy());
				numberOfEnemies ++;
			}
		}
		// randomly add helpers (only on even coordinates to spread them)
		while (numberOfHelpers < TOTAL_NUMBER_OF_HELPERS) {
			int x = 2*(1 + (int)(Math.random()*(WIDTH/2-1)));
			int y = 2*(1 + (int)(Math.random()*(HEIGHT/2-1)));
			if (grid[x][y].getOpponent() == null) {
				grid[x][y].setOpponent(new Helper());
				numberOfHelpers ++;
			}
		}
		this.setChanged();
		this.notifyObservers();
	}

	
	
	public boolean isGameOver() {
		return (player.getTimeLeft()==0);
	}
	
	public Position computeDestination(String direction) {
		Position position = player.getPosition();
		Position destination = new Position(-10,-10);
		switch(direction) {
		case "left" : 	destination = position.plus(0,-1);
						break;
		case "right" : 	destination = position.plus(0,+1);
						break;
		case "down" : 	destination = position.plus(+1,0);
						break;
		case "up" : 	destination = position.plus(-1,0);
						break;
		}
		
		return destination;
	}
	
	public boolean isWithinBounds(Position destination) {
		int x = destination.getX();
		int y = destination.getY();
		return (0 <= x 
				&& x < WIDTH 
				&& 0 <= y 
				&& y < HEIGHT);
	}
	
	public boolean isAdjacent(Position destination) {
		Position position = player.getPosition();
		return(position.plus(0, 1).equals(destination)
				|| position.plus(0, -1).equals(destination)
				|| position.plus(1, 0).equals(destination)
				|| position.plus(-1, 0).equals(destination)); 
	}
	
	public boolean isValidDestination(Position destination) {
		return (isAdjacent(destination) && isWithinBounds(destination)) ;
	}
	
	public void makeMove(Position destination) {
		if (isValidDestination(destination)) {
			int oldX = player.getXPosition();
			int oldY = player.getYPosition();
			int newX = destination.getX();
			int newY = destination.getY();
			grid[oldX][oldY].setPlayer(null);
			player.move(destination);
			grid[newX][newY].setPlayer(player);
			this.setChanged();
			this.notifyObservers(player.getPosition()); // sends new position
			activePosition = player.getPosition();
			if (this.isGameOver()) {
				gameFinished = true;
				this.setChanged();
				this.notifyObservers(gameFinished);
			}
		}
	}
	
	public void handleInteraction(Player player) {
		int x = player.getXPosition();
		int y = player.getYPosition();
		if (grid[x][y].isInteractionPossible()) {
			grid[x][y].handleInteraction(player);
		}
		this.setChanged();
		this.notifyObservers(player);
		
	}
	// GETTERS
	
	public Player getPlayer() {
		return player;
	}
	
	public Tile[][] getGrid() {
		return grid;
	}
	
	public Tile getTile(int i, int j) {
		return grid[i][j];
	}
	
	public Trophy getTrophy() {
		return trophy;
	}
	
	public Position getActivePosition() {
		return activePosition;
	}
	
	// SETTERS 
	
	
}
