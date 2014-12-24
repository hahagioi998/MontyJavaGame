package model;

public class Enemy extends Opponent{
	
	//  VARIABLES 
	public static int MAX_JOKING_THRESHOLD = 10;
	public static int MAX_FIGHTING_THRESHOLD = 10;
	private int jokeThreshold;
	private int fightThreshold;
	
	// CONSTRUCTOR
	
	public Enemy() {
		super(5);
		this.jokeThreshold = (int)(Math.random()*MAX_JOKING_THRESHOLD);
		this.fightThreshold = (int)(Math.random()*MAX_FIGHTING_THRESHOLD);
	}

	public Enemy(int bonus, int jokeThreshold, int fightThreshold) {
		super(bonus);
		this.jokeThreshold = jokeThreshold;
		this.fightThreshold = fightThreshold;
	}

	// METHODS
	
	public void interactWith(Player player) {
		String choice = player.getSkillChoice();
		if (choice=="fight" && player.getFightingSkill() > fightThreshold) {
			increaseFightingSkill(player);
			player.increaseScore(this.getBonus());
		} else if (choice=="joke" && player.getJokingSkill() > jokeThreshold) {
			this.increaseJokingSkill(player);
			player.increaseScore(this.getBonus());
		}
	}
	
	// GETTERS
	
	// SETTERS
	
	public String toString() {
		return "enem";
	}
	
}
