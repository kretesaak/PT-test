package com.test.playtech;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

    private static List<String[]> readFile(String pathString) {
        List<String> allLines = new ArrayList<String>();
        List<String[]> result = new ArrayList<String[]>();
        try {
            allLines = Files.readAllLines(Paths.get(pathString));

        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String line : allLines) {
            result.add(line.split(","));
        }
        return result;
    }

    private static void writeFile(String data) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("pt-test/src/main/java/com/test/playtech/Results.txt"));
        writer.write(data);

        writer.close();
    }

    public static void main(String[] args) {
        // Read data from files
        List<String[]> playerData = readFile("pt-test/src/main/resources/player_data.txt");
        List<String[]> matchData = readFile("pt-test/src/main/resources/match_data.txt");
        String data = "";
        HashMap<String, Player> players;

        Casino myCasino = new Casino();

        for (String[] action : playerData) {
            myCasino.performAction(action, matchData);
        }
        players = myCasino.getPlayers();
        for (Player player : players.values()) {
            data += player.getPlayerInfo();
        }
        data += myCasino.getIllegalActions() + "\n";
        data += myCasino.getBalance() + "\n";

        try {
            writeFile(data);
        } catch (IOException err) {
            System.out.println(err);
        }
    }
}