package com.lightspeed.logic;

/**
 * Created by Ricky Jones Jr on 6/24/2016.
 */
public class Scoreboard {

    private int player1score = 0;
    private int player2score = 0;

    public void incrementScoreCount(int playerID) {
        if(playerID == 0) {
            player1score++;
        } else if(playerID == 1) {
            player2score++;
        }
    }

    public int getPlayerScore(int playerID) {
        int playerScore = 0;
        if(playerID == 0) {
            playerScore = player1score;
        } else if(playerID == 1) {
            playerScore = player2score;
        }

        return playerScore;
    }
}
