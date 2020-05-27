package mainpackage;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class Controller implements GameModel.GameListener {


    @FXML
    Label lbState;

    GameModel model;

    Color brown = new Color(0.322, 0.216, 0.231, 1);
    Color red = new Color(0.839, 0.239, 0.255,1);
    Color orange = new Color(0.925, 0.337, 0.2,1);


    @FXML
    public void initialize() {

    }

    public void initialiseAfterLoading(){
        lbState.getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                model.onEnterPressed();
            }
        });
        model = new GameModel(this);
    }

    @Override
    public void onStateChanged(GameModel.State state) {
        switch (state) {
            case Running:
                lbState.setText("Focus");
                break;
            case WaitingForPlayerReaction:
                lbState.setBackground(getBackgroundFromColor(red));
                lbState.setTextFill(brown);
                lbState.setText("React");
                break;
            case WaitingForNextRound:
                lbState.setBackground(getBackgroundFromColor(brown));
                lbState.setTextFill(orange);
//                if(model != null){
//                    long score = model.getLastReactionTime();
//                    lbState.setText(score+" ms");
//                } else{
                lbState.setText("Play");
//                }
                break;
            case GameFinished:
                lbState.setText("Game Over");
                lbState.setBackground(getBackgroundFromColor(brown));
                lbState.setTextFill(orange);
                break;
        }
    }

    public Background getBackgroundFromColor(Color color){
        return new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY));
    }

    @Override
    public void onGameFinishedWithScore(long averageReactionTime, long[] individualScores) {
        String output = System.currentTimeMillis() + ";" + averageReactionTime + ";";
        for (long individualScore : individualScores) {
            output += individualScore + ";";
        }
        output = output.substring(0, output.length()-1) + "\n";
        System.out.println(output);
        File logFile = new File("log.txt");
        try {
            FileUtils.write(logFile, output, Charset.defaultCharset(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
