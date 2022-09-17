package main.java;
import java.io.*;
import java.sql.SQLOutput;
import java.util.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Game {
    private Cryptogram currentCryptogram;
    private Scanner input;
    private String currentLetter;
    private ArrayList<String> alreadyInput;
    private Players playerList;
    private Player player;
    private String printCrypto;
    private final String BASE_QUOTES_FILE = "src/resources/phrases.txt";
    private int phraseNumber;


    public Game(String save) {
        input = new Scanner(System.in);
        currentLetter = null;
        alreadyInput = new ArrayList<>();
        printCrypto = "";
        playerList = new Players(save);
        player = new Player("Player1");

    }

    public Game() {
        input = new Scanner(System.in);
        currentLetter = null;
        alreadyInput = new ArrayList<>();
        printCrypto = "";
        playerList = new Players();
        player = new Player("Player1");
    }


    public Game(String playerName, String fileName) {
        input = new Scanner(System.in);
        currentLetter = null;
        alreadyInput = new ArrayList<>();
        printCrypto = "";
        playerList = new Players();
        player = playerList.findPlayer(playerName);
        phraseNumber = 0;
    }

    public Game(Player player) {
        input = new Scanner(System.in);
        currentLetter = null;
        alreadyInput = new ArrayList<>();
        this.player = player;
        printCrypto = "";
    }

    /*
    Play game method which is the main method for playing the game
    @param int type
     */
    public void playGame(int type) {
        int whatPlace = 0;
        if (currentCryptogram == null)
            currentCryptogram = cryptogramChoice(type, BASE_QUOTES_FILE);
        printCrypto = currentCryptogram.generateCryptogram();
        int choice;
        while (true) {
            try {
                if (checkComplete()) {
                    break;
                }
                System.out.println(printCrypto);
                System.out.println("=======================");
                printDisplay(currentCryptogram.getWorkingPhrase(), whatPlace);
                help();
                Scanner reader = new Scanner(System.in);
                choice = reader.nextInt();
                System.out.println("You have chosen " + choice);

                switch (choice) {
                    case 1:
                        this.enterLetter(whatPlace);
                        break;
                    case 2:
                        undoLetter(whatPlace);
                        break;
                    case 3:
                        help();
                    case 4: // Move the cursor Forward
                        whatPlace = moveCursorForward(whatPlace);
                        break;
                    case 5: // Move the cursor backwards
                        whatPlace = moveCursorBackward(whatPlace);
                        break;
                    case 6:
                        System.out.println("Thanks for playing! GoodBye!");
                        player.incrementNumberPlayed();
                        playerList.removePlayer(player.getUsername());
                        playerList.addExhistingPlayer(player);
                        playerList.savePlayers();
                        System.exit(0);
                        break;
                    case 7:
                        saveGame(player.getUsername(), phraseNumber);
                        if (!continuePlaying()) {
                            currentCryptogram = null;
                            return;
                        }
                        break;
                    case 8:
                        loadgame(player.getUsername());
                        break;
                    case 9:
                        displayStats();
                        break;
                    case 10:
                        playerList.showTop10();
                        break;
                    case 11:
                        showSolution();
                        System.out.println();
                        System.out.println("Thanks for playing! GoodBye!");
                        player.incrementNumberPlayed();
                        playerList.removePlayer(player.getUsername());
                        playerList.addExhistingPlayer(player);
                        playerList.savePlayers();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid option. Please try again!");
                        break;
                }
            } catch (Exception e) {
                System.out.println();
                System.out.println("=================Invalid option. Please try again!==============================");
            }

        }
        System.out.println();
        System.out.println("Would you like to play another game. Enter 1 for yes, anything else for no.");
        Scanner reader = new Scanner(System.in);
        if (reader.nextInt() != 1) {
            playerList.removePlayer(player.getUsername());
            playerList.addExhistingPlayer(player);
            playerList.savePlayers();
            System.exit(0);
        }
        currentCryptogram = null;
    }

    public void reset() {
        currentLetter = null;
        alreadyInput = new ArrayList<>();
        printCrypto = "";
    }

    /*
    This method prints out the cursor
    @param String phrase the whole phrase
    @param int what place - the position of the cursor
     */
    public static void printDisplay(String phrase, int whatPlace) {
        System.out.println(phrase);
        for (int i = 0; i < whatPlace; i++) {
            System.out.print(" ");
        }
        System.out.println("^");
    }

    public Boolean checkComplete() {
        boolean check = false;
        if (!currentCryptogram.getWorkingPhrase().contains("#")) {
            if (currentCryptogram.getWorkingPhrase().equals(currentCryptogram.getPhrase())) {
                System.out.println();
                System.out.println("Well done");
                player.incrementNumberCompleted();
                reset();
                check = true;
            } else {
                System.out.println("You got the phrase wrong.");
                check = false;
            }
        }
        return check;
    }

    public boolean continuePlaying() {
        System.out.println("Would you like to continue playing?");
        System.out.println("Type 1 for yes or 2 for No!");
        Scanner in = new Scanner(System.in);
        if (in.hasNextInt()) {
            if (in.nextInt() == 1) {
                return true;
            } else {
                return false;
            }
        } else {
            return continuePlaying();
        }
    }

    public void saveGame(String fileName, int phraseNumber) {
        Scanner in = new Scanner(System.in);
        File f = new File(fileName);
        if (f.exists()) {
            System.out.println("Would you like to overwrite?");
            System.out.println("Type 1 for yes or 2 for no");
            if (in.hasNextInt()) {
                if (in.nextInt() == 1) {
                    saveGameHelper(f, phraseNumber);
                } else {
                    System.out.println("Not overwritten");
                }
            } else {
                in.next();
                saveGame(fileName, phraseNumber);
            }
        } else {
            saveGameHelper(f, phraseNumber);
        }
    }

    public void saveGameHelper(File f, int phraseNumber) {
        BufferedWriter bw;
        String toSave = "", crypto = "";
        // Form output
        toSave += phraseNumber + "|";
        toSave += currentCryptogram.getCrypto() + "|";
        toSave += currentCryptogram.getPhrase() + "|";
        toSave += currentCryptogram.getAttempt() + "|";
        toSave += getPrintCrypto() + "|";
        toSave += "\n";
        // Rewrite all saves to file
        try {
            bw = new BufferedWriter(new FileWriter(f, true));
            bw.write(toSave);
            bw.close();
        } catch (Exception e) {
            System.out.println("Issue saving file");
        }

    }


    public void loadgame(String filename) {
        try {
            File file = new File(filename);
            String input = "";
            if (file.exists() && (file.length() > 0)) {
                Scanner reader = new Scanner(file);
                ArrayList<String> individualFileStrings = new ArrayList();
                while (reader.hasNextLine()) {
                    input = reader.nextLine();
                }
                reader.close();
                StringTokenizer stringTokenizer = new StringTokenizer(input, "|");
                while (stringTokenizer.hasMoreElements()) {
                    individualFileStrings.add(stringTokenizer.nextToken());
                }
                int pharseNum = Integer.parseInt(individualFileStrings.get(0));
                if (currentCryptogram != null) {
                    currentCryptogram.clear();

                    currentCryptogram.getPhraseForEncryption(BASE_QUOTES_FILE, pharseNum - 1);

                    currentCryptogram.setAttempt(individualFileStrings.get(3));
                }
                printCrypto = individualFileStrings.get(4);
            } else {
                System.out.println("!!!!!!Could not find previously saved game!!!!!!");
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found ");
        } catch (Exception e) {
            System.out.println("Error loading cryptogram-corrupt");
        }

    }

    public int moveCursorForward(int whatPlace) {
        int place;
        if (whatPlace >= currentCryptogram.getWorkingPhrase().length() - 1) {
            place = 0;
        } else {
            if (currentCryptogram.getWorkingPhrase().charAt(whatPlace + 1) == ' ') {
                place = whatPlace + 2;
            } else {
                place = whatPlace + 1;
            }
        }
        return place;
    }

    /*
   This method moves the cursor backwards
   @param int whatPlace the position of the cursor
   @return int which is the previous position on the string
    */
    public int moveCursorBackward(int whatPlace) {
        int place;
        if (whatPlace == 0) {
            place = (currentCryptogram.getWorkingPhrase().length() - 1);
        } else {
            if (currentCryptogram.getWorkingPhrase().charAt(whatPlace - 1) == ' ') {
                place = whatPlace - 2;
            } else {
                place = whatPlace - 1;
            }
        }
        return place;
    }

    /* Gives the user the choice between either a number or a letter cryptogram
    @param int userInput -  the user's choice
    @param String fileName -  the name of the file the phrases are located at
    @return Cryptogram - returns a cryptogram based on the users choice
    * */
    public Cryptogram cryptogramChoice(int userInput, String fileName) {
        player.incrementNumberPlayed();
        boolean input_done = false;
        while (!input_done) {
            try {

                if (userInput == 1) {
                    currentCryptogram = new LetterCryptogram(fileName, phraseNumber);
                    currentCryptogram.setAttempt(currentCryptogram.getPhrase().toLowerCase().replaceAll("[a-z]", "#"));
                    phraseNumber++;
                    input_done = true;
                } else if (userInput == 2) {
                    currentCryptogram = new NumberCryptogram(fileName, phraseNumber);
                    currentCryptogram.setAttempt(currentCryptogram.getPhrase().toLowerCase().replaceAll("[a-z]", "#"));
                    phraseNumber++;
                    input_done = true;
                } else {
                    System.out.println("What you entered was neither 1 or 2, please input a valid answer.");
                    return cryptogramChoice(userInput, fileName);
                }
            } catch (InputMismatchException e) {
                System.out.println();
                System.out.println("=================Invalid option. Please try again!==============================");
                break;
            }
        }
        return currentCryptogram;
    }

    /*Lets the user choose which letter in the cryptogram they would like to overwrite
    @param int whatLetter - enters a letter based on the position of the cursor
    * */
    public void enterLetter(int whatLetter) {
        if (((currentCryptogram.getWorkingPhrase().charAt(whatLetter) > 'z') || (currentCryptogram.getWorkingPhrase().charAt(whatLetter) < 'a')) && (!(currentCryptogram.getWorkingPhrase().charAt(whatLetter) == '#'))) {
            System.out.println("------------------");
            System.out.println("Keep moving along....Nothing to see here");
            System.out.println();
        } else {
            boolean check = helpCheck(whatLetter);
            if (check) {
                System.out.println("What would you like to overwrite with?");
                enterLetterHelper(whatLetter);
                checkCorrect(whatLetter);
            } else if ((currentCryptogram.getWorkingPhrase().charAt(whatLetter) == '#')) {
                System.out.println("What letter would you like to enter?");
                enterLetterHelper(whatLetter);
                checkCorrect(whatLetter);
            }
        }
    }

    /*
    This method check if the entered letter is correct to the letter compared to the corresponding phrase
    @param int whatLetter - the position of the letter
     */
    public void checkCorrect(int whatLetter) {
        if ((currentCryptogram.getWorkingPhrase().charAt(whatLetter)) == currentCryptogram.getPhrase().charAt(whatLetter)) {
            player.incrementNumberOfCorrectGuesses();
        } else if ((currentCryptogram.getWorkingPhrase().contains(currentLetter))) {
            player.incrementTotalGuesses();
        }
    }

    public String getPrintCrypto() {
        return printCrypto;
    }

    /*Overwrites the chosen letter with a new letter chosen by the user
            @param what letter- position of the letter
            * */
    public void enterLetterHelper(int whatLetter) {
        Scanner reader = new Scanner(System.in);
        currentLetter = reader.next().toLowerCase();
        char c = currentLetter.charAt(0);
        System.out.println("current letter = " + currentLetter);
        if (currentCryptogram.getCrypto().containsKey(c)) {
            if (currentLetter.length() == 1) {
                if (!letterCheck()) {
                    checkCorrect(whatLetter);
                    alreadyInput.add(currentLetter);
                    currentCryptogram.changePhrase(currentLetter, whatLetter);
                }
            } else {
                System.out.println("You did not enter a single letter.");
            }
        } else {
            player.incrementTotalGuesses();
            System.out.println();
            System.out.println("That letter is not in the phrase! Try Again!");
            System.out.println();
        }
    }

    /*Removes a chosen already guessed letter and replaced it with a #
    @param int whatLetter - position of the letter
    * */
    public void undoLetter(int whatLetter) {
        currentCryptogram.changePhrase("#", whatLetter);
        alreadyInput.remove(currentLetter);
    }

    public void displayStats() {
        player.displayStats();
    }

    public void showSolution() {
        System.out.print(currentCryptogram.getPhrase());
    }


    /*This method gives a choice of overwriting an input or moving on to the next int
    @param int whatLetter - checks to see if the user wants to overwrite or not

    @return Boolean - returns true if the user wants to overwrite else false.
    * */
    public Boolean helpCheck(int whatLetter) {
        boolean check = false;
        try {
            if (!(currentCryptogram.getWorkingPhrase().charAt(whatLetter) == '#')) {
                System.out.println("Would you like to overwrite? type 1 for yes or 2 for no");
                Scanner i = new Scanner(System.in);
                int a = Integer.parseInt(i.nextLine());
                if (a == 1) {
                    check = true;
                } else {
                    check = false;
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Enter 1 or 2 please");
            return helpCheck(whatLetter);
        }
        return check;
    }

    /*
    Gives the user help by showing them the options they have
     */
    private void help() {
        System.out.println("Help Section");
        System.out.println("Number description: ");
        System.out.println("Typing 1 - to enter a letter.");
        System.out.println("Typing 2 - to removing a letter.");
        System.out.println("Typing 4 - to move forward");
        System.out.println("Typing 5 - to move backwards.");
        System.out.println("Typing 6 - to quit.");
        System.out.println("Typing 7 - to save a game.");
        System.out.println("Typing 8 - to load a game.");
        System.out.println("Typing 9 - to display your stats.");
        System.out.println("Typing 10 - to display top 10 players.");
        System.out.println("Typing 11 - to show cryptogram solution.");
        System.out.println();
        System.out.println("Which option would you like.");
        System.out.println();
    }

    /*Used to check if the user has already entered a specific letter so that they can't enter that letter again
    @return Boolean - checks if the letter to entered
    * */
    public boolean letterCheck() {
        boolean alreadyEntered = false;
        for (int i = 0; i < alreadyInput.size(); i++) {
            if (alreadyInput.get(i).contains(currentLetter)) {
                alreadyEntered = true;
                System.out.println("You have already entered this letter, please try again");
            } else {
                alreadyEntered = false;
            }
        }
        return alreadyEntered;
    }

    public ArrayList<String> getAlreadyInput() {
        return alreadyInput;
    }

    public Cryptogram getCurrentCryptogram() {
        return currentCryptogram;
    }

    public Player getP() {
        return player;
    }

    /*
    runs the Game
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter your username, if it does not exist a new user will be made: ");
        Game newGame = new Game(input.nextLine(), "src/resources/phrases.txt");
        System.out.println("Hello");
        boolean exit = false;
        int i;

        while (!exit) {
            System.out.println("Do you want a number or a letter cryptogram. Enter 1 for letter, or 2 for number.");
            if (input.hasNextInt()) {
                i = input.nextInt();
                if (i == 1 || i == 2) {
                    newGame.playGame(i);
                } else {
                    input.next();
                }
            }


        }
    }
}