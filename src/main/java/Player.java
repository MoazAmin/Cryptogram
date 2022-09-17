package main.java;

public class Player{
    private String username;
    private int totalNumGuesses;
    private int numberCompleted;
    private int numberPlayed;
    private int numCorrectGuesses;

    public Player(String name){
        username = name;
        totalNumGuesses = 0;
        numberCompleted = 0;
        numberPlayed = 0;
        numCorrectGuesses = 0;
    }

    //Method used to create a new player account.
    public Player(String name, int totalGuesses, int numberC, int numberP, int numCorrectGs){
        username = name;
        totalNumGuesses = totalGuesses;
        numberCompleted = numberC;
        numberPlayed = numberP;
        numCorrectGuesses = numCorrectGs;
    }

    // Get and set methods for Players used to increment, change and display all parameters for a Player
    public String getUsername(){return username;}
    public void setUsername(String name) {username = name;}
    public int getNumberCompleted(){return numberCompleted;}
    public int getNumberPlayed(){return numberPlayed;}
    public void incrementNumberPlayed(){numberPlayed++;}
    public void incrementTotalGuesses(){totalNumGuesses++;}
    public int getTotalNumGuesses(){return totalNumGuesses;}
    public int getNumberOfCorrectGuesses(){return numCorrectGuesses;}
    public void incrementNumberCompleted(){
        numberCompleted++;
        numberPlayed++;
    }
    public void incrementNumberOfCorrectGuesses(){
        numCorrectGuesses++;
        totalNumGuesses++;
    }

    // Calculates accuracy and returns it
    public double getAccuracy() {
        double d1 = numCorrectGuesses;
        double d2 = totalNumGuesses;
        if (totalNumGuesses <= 0) {
            return 0;
        } else {
            return (d1 / d2) * 100;
        }
    }

    // Displays all the current statistics of a player
    public void displayStats(){
        System.out.println("Username: " + getUsername());
        System.out.println("Number of guesses: " + getTotalNumGuesses());
        System.out.println("Number of correct guesses: "+ getNumberOfCorrectGuesses());
        System.out.println("Accuracy: " + getAccuracy() + "%");
        System.out.println("Number of games played: " + getNumberPlayed());
        System.out.println("Score : " + getNumberCompleted());
    }
}