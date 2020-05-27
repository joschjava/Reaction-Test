package mainpackage;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;

import java.util.Random;

public class GameModel {

    private float minimumTimeS = 3f;
    private float maximumTimeS = 6f;

    private int curRound = 0;
    private int numRounds = 3;

    private long[] score = new long[numRounds];
    private long startTimeStampStop;

    enum State {Running, WaitingForPlayerReaction, WaitingForNextRound, GameFinished}
    private State state;

    private GameListener gameListener;

    GameModel(GameListener gameListener) {
        this.gameListener = gameListener;
        onStateChanged(State.WaitingForNextRound);
    }

    public void onEnterPressed() {
        switch (state) {
            case Running:
                break;
            case WaitingForPlayerReaction:
                playerReacted();
                break;
            case WaitingForNextRound:
                startRound();
                break;
            case GameFinished:
                Platform.exit();
                break;
        }

    }

    private void startRound() {
        float randomWaitTime = getRandomWaitTime();
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(randomWaitTime),
                ae -> setStopPoint()));
        timeline.play();
        onStateChanged(State.Running);
    }

    private void setStopPoint() {
        onStateChanged(State.WaitingForPlayerReaction);
        startTimeStampStop = System.currentTimeMillis();
    }

    private void playerReacted() {
        long reactionTime = getCalculateReactionTime();
        score[curRound] = reactionTime;
//        System.out.println("Reaction time: "+reactionTime);
        curRound++;
        if (curRound >= numRounds) {
            onGameFinished();
        } else {
            onStateChanged(State.WaitingForNextRound);
        }
    }

    public void onGameFinished() {
        onStateChanged(State.GameFinished);
        long averageReactionTime = getAverageReactionTime();
        gameListener.onGameFinishedWithScore(averageReactionTime, score);
    }

    public void onStateChanged(State state) {
        this.state = state;
        gameListener.onStateChanged(state);
    }

    public long getLastReactionTime(){
        if(curRound != 0){
            return score[curRound-1];
        } else {
            return -1;
        }
    }

    private float getRandomWaitTime() {
        Random rand = new Random();
        return rand.nextFloat() * (maximumTimeS - minimumTimeS) + minimumTimeS;
    }

    public void setStartTimeStampStop() {
        startTimeStampStop = System.currentTimeMillis();
    }

    public long getCalculateReactionTime() {
        return System.currentTimeMillis() - startTimeStampStop;
    }

    public long getAverageReactionTime() {
        long totalReactionTime = 0;
        for (long curScore : score) {
            if (curScore != 0) {
                totalReactionTime += curScore;
            } else {
                System.err.println("Score shouldn't be 0");
            }
        }
        return totalReactionTime / ((long) numRounds);
    }

    public interface GameListener {
        void onStateChanged(State state);

        void onGameFinishedWithScore(long averageReactionTime, long[] individualScores);
    }

}
