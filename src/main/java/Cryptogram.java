package main.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public abstract class Cryptogram {
    private String phrase;
    private  String attempt;
    private final String BASE_QUOTES_FILE = "src/resources/phrases.txt";
    private List<Integer> keep;

    public Cryptogram() {
        attempt = "";
        keep = new ArrayList<>();
    }

    public Cryptogram(String fileName) {
        attempt = "";
    }

    /**
     * this is a method where it gets the phrases from the  file and randomises it and encrypts the strings
     * @param filename
     * @return String - which is the real string from the file
     * if file file is not found a catch is thrown
     */
    public String getPhraseForEncryption(String filename, int phraseNumber) {
        keep = new ArrayList<>();
        ArrayList<String> numberOfStrings = new ArrayList<>();
        try {
            if(phraseNumber>= 15){
                System.out.println("You have completed the game! Well done!");
                System.out.println("Exiting");
                System.exit(0);
            }
            else {
                File myObj = new File(filename);
                if (myObj.length() == 0) {
                    System.out.println("No phrases");
                    System.exit(0);
                }
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    numberOfStrings.add(myReader.nextLine());
                }
                phrase = numberOfStrings.get(phraseNumber);
                return phrase;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Not found!");
        }
        return null;
    }

    public String getPhrase(){
        return phrase;
    }

    /**
     *This method is called changing the working phrase when the user enters or deletes a letter
     * @param input
     * @param whatPlace
     */
    public void changePhrase(String input, int whatPlace) {
        int test = this.getPhrase().length();
        String change = "";
        for (int i = 0; i < this.getPhrase().length(); i++) {
            if (this.getPhrase().charAt(i) == this.getPhrase().charAt(whatPlace)) {
                change = change.concat(input);
            } else {
                change = change.concat(getWorkingPhrase().charAt(i) + "");
            }
        }
        this.setAttempt(change);
    }

    public void clear(){
      attempt = "";
       phrase = "";
       StringBuffer sbf = new StringBuffer("") ;
       sbf.delete(0, phrase.length());
    }

    public String getWorkingPhrase() {
        return this.getAttempt();
    }

    public String getAttempt() {
        return attempt;
    }
    public void setAttempt(String attempt) {
        this.attempt = attempt;
    }



    public abstract String generateCryptogram();
    public abstract String printMethod(String phrase);
    public abstract HashMap<?,?> getCrypto();
}