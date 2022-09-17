package main.java;

import org.junit.jupiter.api.Test;
import org.junit.Before;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayersTest {

    @Before
    public void before() throws IOException {
        File file = new File("src/resources/playersTest.txt");
        FileWriter fw = new FileWriter(file, false);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
        out.print("");
        out.close();
        bw.close();
        fw.close();
    }

    @Test
    void addPlayerTest(){
        Players players = new Players("src/resources/playersTest.txt");
        assertNotNull(players);
        players.addPlayer("John");
        List<Player> compare = new LinkedList<>();
        Player player = new Player("John", 0, 0, 0, 0);
        compare.add(player);
        assertEquals(players.getPlayers().get(0).getUsername(), compare.get(0).getUsername());
        assertEquals(players.getPlayers().get(0).getNumberCompleted(), compare.get(0).getNumberCompleted());
        assertEquals(players.getPlayers().get(0).getNumberOfCorrectGuesses(), compare.get(0).getNumberOfCorrectGuesses());
        assertEquals(players.getPlayers().get(0).getTotalNumGuesses(), compare.get(0).getTotalNumGuesses());
        assertEquals(players.getPlayers().get(0).getNumberPlayed(), compare.get(0).getNumberPlayed());
    }

    @Test
    public void saveAndLoadStatsTest() {
        Players players = new Players("src/resources/playersTest.txt");
        players.addPlayer("John");
        players.savePlayers();
        List<Player> load = new LinkedList<>();
        load = players.load();
        Player player = new Player("John", 0, 0, 0, 0);
        assertEquals(load.get(0).getUsername(), player.getUsername());
        assertEquals(load.get(0).getNumberCompleted(), player.getNumberCompleted());
        assertEquals(load.get(0).getNumberOfCorrectGuesses(), player.getNumberOfCorrectGuesses());
        assertEquals(load.get(0).getTotalNumGuesses(), player.getTotalNumGuesses());
        assertEquals(load.get(0).getNumberPlayed(), player.getNumberPlayed());


        //error loading player details
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String expected = "Error Reading file!\n"
                .replaceAll("\n", System.getProperty("line.separator"));
        Players playersErr = new Players("src/resources/playersTestError.txt");
        playersErr.load();
        assertEquals(expected, outContent.toString());

    }
    private void mimicInput(String input) throws UnsupportedEncodingException {
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
    }
    @Test
    public void cryptogramsCorrectGuesses() throws UnsupportedEncodingException {
        Player p = new Player("test",0,0,0,0);
        Game g = new Game(p);
        g.cryptogramChoice(2,"src/resources/testPhraseFile.txt");
        g.getCurrentCryptogram().generateCryptogram();
        mimicInput("t");
        g.enterLetter(0);
        assertEquals(p.getNumberOfCorrectGuesses(), 1);
    }



    @Test
    public void cryptogramsPlayedTest() throws UnsupportedEncodingException {
        Player p = new Player("test",0,0,0,0);
        Game g = new Game(p);
        g.cryptogramChoice(2,"src/resources/testPhraseFile.txt");
        g.getCurrentCryptogram().generateCryptogram();
        mimicInput("t");
        g.enterLetter(0);
        assertEquals(p.getNumberPlayed(), 1);
    }

}