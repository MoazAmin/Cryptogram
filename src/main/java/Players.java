package main.java;
import java.io.*;
import java.util.*;

public class Players{
    private List<Player> players;
    private int playersCount;
    File file;

    public Players(){
        file = new File("src/resources/accounts.txt");
        players = new LinkedList<>();
        players = load();
        playersCount = players.size();
    }

    public Players(String str){
        file = new File(str);
        players = new LinkedList<>();
        playersCount = players.size();
    }


    //Adds a new player to the list of players
    public void addPlayer(String newName) {
        //Ensures new players name does not have a space in it
        if(newName.contains(" ")) {
            System.out.println("Names cannot contain a space.");
            return;
        }
        //Doesn't allow repeated usernames so the username can be used to identify each individual player
        for(Player pl : players) {
            if(pl.getUsername().equals(newName)) {
                System.out.println("Player already exists.");
                return;
            }
        }
        //Creates a new player with default statistics and adds them to the list of all players. Then increases the player count by 1.
        players.add(new Player(newName, 0, 0, 0, 0));
        playersCount++;
    }

    public void addExhistingPlayer(Player p){
        players.add(p);
    }

    public void removePlayer(String name) {
        for(int i = 0; i< players.size(); i++) {
            if(players.get(i).getUsername().equals(name)) {
                players.remove(i);
                playersCount--;
                return;
            }
        }
    }

    //Returns the details of a specific player using their username. Returns null if player doesn't exist.
    public Player findPlayer(String name) {
        for(Player player : players) {
            if(player.getUsername().equals(name)) {
                return player;
            }
        }
        System.out.println("Adding new player!");
        Player newPlayer = new Player(name);
        return newPlayer;
    }

    public int getIndex(String name){
        int index = 0;
        for(Player player : players) {
            if(player.getUsername().equals(name)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    //Returns number of players
    public int getPlayerCount()
    {
        return  playersCount;
    }
    //Returns all the players and their details
    public List<Player> getPlayers()
    {
        return players;
    }

    public void savePlayers() {
        try {
            FileWriter fw = new FileWriter(file, false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            int length = 0;
            for(Player pl : players) {
                out.print(pl.getUsername() + " " + pl.getTotalNumGuesses() + " " + pl.getNumberCompleted() + " " + pl.getNumberPlayed() + " " + pl.getNumberOfCorrectGuesses());
                if(length < players.size()) out.print("\n");
            }
            out.close();
            bw.close();
            fw.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Player> load() {
         try {
             List<Player> players = new LinkedList<>();
             BufferedReader reader = new BufferedReader(new FileReader(file));
             ArrayList<String> lines = new ArrayList<>();
             while(true){
                 String line = reader.readLine();
                 if(line == null) break;
                 lines.add(line);
             }
             for(int i = 0; i < lines.size(); i++){
                 String[] splitParts = lines.get(i).split(" ");
                 Player newPlayer = new Player(splitParts[0], Integer.parseInt(splitParts[1]), Integer.parseInt(splitParts[2]), Integer.parseInt(splitParts[3]), Integer.parseInt(splitParts[4]));
                 players.add(newPlayer);
             }
             return players;
         } catch (IOException e) {
             e.printStackTrace();
             return null;
         }
         catch (Exception e) {
             System.out.println("Error Reading file!");
             return null;
         }
    }
    //Displays the top 10 players
    public void showTop10() {
        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                if(p1.getNumberCompleted() - p2.getAccuracy() > 0) {
                    return 1;
                }
                else if(p1.getNumberCompleted() - p2.getAccuracy() > 0){
                    return 0;
                }
                else {
                    return -1;
                }
            }
        });
        for(int count = 0;count < 10 && count< playersCount; count++) {
            System.out.print(players.get(count).getUsername());
            System.out.print(": ");
            players.get(count).displayStats();
        }
    }






}
