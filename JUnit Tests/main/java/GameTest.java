package main.java;


import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test void checkLetterCryptogram(){
        Game g = new Game();
        //checking if the a letter cryptogram is generated when 1 is entered
        g.cryptogramChoice(1, "src/resources/testPhraseFile.txt");
        assertNotNull(g.getCurrentCryptogram());
        assertEquals(LetterCryptogram.class, g.getCurrentCryptogram().getClass());
    }
    @Test
    void playGameTest() throws UnsupportedEncodingException {
        Game g = new Game();

        //checking if the a letter cryptogram is generated when 1 is entered
        g.cryptogramChoice(2,"src/resources/testPhraseFile.txt");
        assertNotNull(g.getCurrentCryptogram());
        assertEquals(NumberCryptogram.class, g.getCurrentCryptogram().getClass());

        //checking if the number of cryptograms played increases
        assertEquals(g.getP().getNumberPlayed(),1);



        // check not complete- should return true
        assertTrue(g.getCurrentCryptogram().getWorkingPhrase().contains("#"));
        g.getCurrentCryptogram().generateCryptogram();


        //Entering a letter that is not in the phrase
        mimicInput("p");
        g.enterLetter(1);
        assertNotEquals(g.getCurrentCryptogram().getWorkingPhrase().charAt(1), 'p');
        assertEquals(1 , g.getP().getTotalNumGuesses());
        assertEquals(0 , g.getP().getNumberOfCorrectGuesses());

        //randomly filling the phrases with known letters
        mimicInput("e");
        g.enterLetter(0);
        mimicInput("s");
        g.enterLetter(1);
        // checking if the stats are updated
        assertEquals(3 , g.getP().getTotalNumGuesses());
        assertEquals(0 , g.getP().getNumberOfCorrectGuesses());
        //inputing at correct positon
        mimicInput("l");
        g.enterLetter(6);
        assertEquals(4 , g.getP().getTotalNumGuesses());
        assertEquals(1 , g.getP().getNumberOfCorrectGuesses());


        // trying to overWrite
        mimicInput("1");
        assertTrue(g.helpCheck(1));
        mimicInput("i");
        g.enterLetterHelper(1);
        assertEquals(g.getCurrentCryptogram().getWorkingPhrase().charAt(1), 'i');


        // Not overwriting
        mimicInput("2");
        assertFalse(g.helpCheck(2));
        g.enterLetterHelper(1);
        assertEquals(g.getCurrentCryptogram().getWorkingPhrase().charAt(1), 'i');


        //Entering a letter that is already enterred
        mimicInput("e");
        g.enterLetter(2);
        //Entering a letter that is already enterred
        assertTrue(g.getAlreadyInput().contains("e"));
        //Entering a letter taht is not already there
        assertFalse(g.getAlreadyInput().contains("f"));


        // undo
        g.undoLetter(0);
        assertEquals('#', g.getCurrentCryptogram().getWorkingPhrase().charAt(0));
    }

    private void mimicInput(String input) throws UnsupportedEncodingException {
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
    }
    @Test
    void checkComplete() throws UnsupportedEncodingException {
        Game g = new Game();
        g.cryptogramChoice(2, "src/resources/testPhraseFile.txt");
        g.getCurrentCryptogram().generateCryptogram();

        mimicInput("t");
        g.enterLetter(0);
        assertEquals(g.getCurrentCryptogram().getWorkingPhrase().charAt(0), 't');

        mimicInput("e");
        g.enterLetter(1);
        assertEquals(g.getCurrentCryptogram().getWorkingPhrase().charAt(1), 'e');

        mimicInput("s");
        g.enterLetter(2);

        mimicInput("f");
        g.enterLetter(4);

        mimicInput("i");
        g.enterLetter(5);

        mimicInput("l");
        g.enterLetter(6);

        //checking that the number of complete cryptograms increased.
        assertEquals(g.getCurrentCryptogram().getWorkingPhrase(), g.getCurrentCryptogram().getPhrase());
        assertTrue(g.checkComplete());
        assertEquals(g.getP().getNumberCompleted(),1);
    }


    @Test
    void NotcheckComplete() throws UnsupportedEncodingException {
        Game g = new Game();
        g.cryptogramChoice(2,"src/resources/testPhraseFile.txt");
        g.getCurrentCryptogram().generateCryptogram();

        mimicInput("t");
        g.enterLetter(0);
        assertEquals(g.getCurrentCryptogram().getWorkingPhrase().charAt(0), 't');

        mimicInput("e");
        g.enterLetter(1);
        assertEquals(g.getCurrentCryptogram().getWorkingPhrase().charAt(1), 'e');

        mimicInput("s");
        g.enterLetter(2);

        mimicInput("f");
        g.enterLetter(4);
// swapping 2 letters so the cryptogram doesnt complete
        mimicInput("l");
        g.enterLetter(5);

        mimicInput("i");
        g.enterLetter(6);

        //checking that the number of complete cryptograms hasnt increased.
        assertNotEquals(g.getCurrentCryptogram().getWorkingPhrase(), g.getCurrentCryptogram().getPhrase());
        assertFalse(g.checkComplete());
        assertEquals(g.getP().getNumberCompleted(),0);
    }

    @Test
    void saveCryptogramCheck() throws UnsupportedEncodingException {
        Game g = new Game();
        g.cryptogramChoice(1,"src/resources/testPhraseFile.txt");
        g.getCurrentCryptogram().generateCryptogram();
        mimicInput("e");
        g.enterLetter(1);
       g.saveGame("testSaveCryptogram2.txt", 0);
       File file = new File("testSaveCryptogram2.txt");
       assertTrue(file.exists());
       //testing if the file has contents
       assertEquals(file.length(), 53);
    }

    @Test
    void loadCryptogramCheck() throws FileNotFoundException {
        Game game = new Game();
        game.loadgame("testSaveCryptogram1.txt");
        File file = new File("testSaveCryptogram1.txt");
        assertEquals(file.length(), 62);
        assertTrue(file.exists());
        //checking if the character from the loaded file is t from the phrase
        Scanner reader = new Scanner(file);
        String input = reader.nextLine();
        assertEquals(input.charAt(42),'t');

        //checking if the number of cryptograms loaded doesnt increase.
        assertEquals(game.getP().getNumberPlayed(),0);

        //when the file doesnt have a previous game
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        game.loadgame("testSaveCryptogram4.txt");
        String expected = "!!!!!!Could not find previously saved game!!!!!!\n"
                .replaceAll("\n", System.getProperty("line.separator"));
        assertEquals(expected, outContent.toString());

        //when the file is corrupt
        final ByteArrayOutputStream outContent2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent2));
        game.loadgame("testSaveCryptogram3.txt");
        String expected1 = "Error loading cryptogram-corrupt\n"
                .replaceAll("\n", System.getProperty("line.separator"));
        assertEquals(expected1, outContent2.toString());
    }

    @Test
    void showSolutionCheck(){
        Game g = new Game();
        g.cryptogramChoice(1, "src/resources/testPhraseFile.txt");
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        g.showSolution();
        String expected = "testfile";
        assertEquals(expected, outContent.toString());


    }

}