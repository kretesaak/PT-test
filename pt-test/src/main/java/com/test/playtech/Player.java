package com.test.playtech;

import java.util.ArrayList;

public class Player {
    private String id;
    private long balance;
    private int wins;
    private boolean illegitimate;
    private ArrayList<String> bets = new ArrayList<String>();

    Player(String playerId) {
        this.id = playerId;
        this.balance = 0;
        this.wins = 0;
        this.illegitimate = false;
    }

    public String getId() {
        return this.id;
    }

    public long getBalance() {
        return this.balance;
    }

    public String getPlayerInfo() {
        String info = id + " " + balance + " " + wins/bets.size() + "\n";
        return info;
    }

    public boolean isIllegitimate() {
        return illegitimate;
    }

    public boolean hasEnoughCoins(int coins) {
        if (this.balance < coins) {
            this.setIllegitimate(true);
            return false;
        }
        return true;
    }

    public boolean hasNotBetted(String match) {
        if (bets.indexOf(match) < 0) {
            bets.add(match);
            return true;
        }
        this.setIllegitimate(true);
        return false;
    }

    public void setIllegitimate(boolean value) {
        this.illegitimate = value;
    }

    public void deposit(int coins) {
        this.balance += coins;
    }

    public void withdraw(int coins) {
        if (coins > this.balance) {
            this.setIllegitimate(true);
        } else {
            this.balance -= coins;
        }
    }

    public void win(int coins) {
        this.balance += coins;
        this.wins++;
    }

    public void lose(int coins) {
        this.balance -= coins;
    }
}
