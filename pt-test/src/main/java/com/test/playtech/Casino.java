package com.test.playtech;

import java.util.HashMap;
import java.util.List;

public class Casino {
    private int casinoBalance;
    private int lastLegitimateCasinoBalance;
    private HashMap<String, Player> players = new HashMap<String, Player>();
    private String illegalActions;

    Casino() {
        casinoBalance = 0;
        lastLegitimateCasinoBalance = 0;
        illegalActions = "";
    }

    public int getBalance() {
        return casinoBalance;
    }

    public String getIllegalActions() {
        return illegalActions;
    }

    public HashMap<String, Player> getPlayers() {
        return players;
    }

    public void bet(Player currentPlayer, String matchId, int coins, String side, List<String[]> matchData) {
        for (String[] match : matchData) {
            if (!matchId.equals(match[0])) {
                continue;
            }
            double returnRateA = Double.parseDouble(match[1]);
            double returnRateB = Double.parseDouble(match[2]);
            String result = match[3];

            if (result.equals("DRAW")) {
                continue;
            } else if (result.equals(side)) {
                int coinsWon = 0;
                if (side.equals("A")) {
                    coinsWon = (int) Math.floor(coins * returnRateA);
                } else if (side.equals("B")) {
                    coinsWon = (int) Math.floor(coins * returnRateB);
                } else {
                    currentPlayer.setIllegitimate(true);
                    continue;
                }

                this.casinoBalance -= coinsWon;
                currentPlayer.win(coinsWon);
            } else {
                if (side.equals("A") || side.equals("B")) {
                    this.casinoBalance += coins;
                    currentPlayer.lose(coins);
                } else {
                    currentPlayer.setIllegitimate(true);
                    continue;
                }
            }
        }
    }

    public void performAction(String[] action, List<String[]> matchData) {
        // Set action parameters
        String id = action[0];
        String operation = action[1];
        String matchId = action[2];
        if (matchId.equals("")) {
            matchId = null;
        }
        int coins = Integer.parseInt(action[3]);
        String side = null;
        if (action.length > 4) {
            side = action[4];
        }

        // Set player or create new one
        Player currentPlayer;
        if (players.get(id) == null) {
            lastLegitimateCasinoBalance = this.casinoBalance;
            currentPlayer = new Player(id);
            players.put(id, currentPlayer);
        } else {
            currentPlayer = players.get(id);
        }

        // If player is not illegitimate, perform action
        if (!currentPlayer.isIllegitimate()) {
            // Perform action
            switch (operation) {
                case "DEPOSIT":
                    currentPlayer.deposit(coins);
                    break;
                case "BET":
                    if (currentPlayer.hasEnoughCoins(coins) && currentPlayer.hasNotBetted(matchId)) {
                        this.bet(currentPlayer, matchId, coins, side, matchData);
                        break;
                    } else {
                        break;
                    }
                case "WITHDRAW":
                    currentPlayer.withdraw(coins);
                    break;
                default:
            }

            // If player is out, set the action to illegal ones
            if (currentPlayer.isIllegitimate()) {
                this.casinoBalance = this.lastLegitimateCasinoBalance;
                this.illegalActions += id + " " + operation + " " + matchId + " " + coins + " " + side + "\n";
                players.remove(id);
            }
        }
    }
}
