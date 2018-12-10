
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import static javafx.scene.layout.AnchorPane.setLeftAnchor;
import static javafx.scene.layout.AnchorPane.setTopAnchor;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jiuntian
 */
public class Solver {
    private int n_color;
    public int nums[];
    private String code = "0000";
    private int index,guessNum;
    private String guess = "";
    String winMusic = "win.mp3";
    String roundMusic = "round.mp3";
    String playMusic = "playing.mp3";
    int count =0;
    
    Media roundSound = new Media(new File(roundMusic).toURI().toString());
    Media playSound = new Media(new File(playMusic).toURI().toString());
    MediaPlayer mediaPlayerPlay = new MediaPlayer(playSound);
    Media winSound = new Media(new File(winMusic).toURI().toString());
    MediaPlayer mediaPlayerWin = new MediaPlayer(winSound);
    
    public Solver(int n_color){
        this.n_color = n_color;
        index = 3;
        guessNum = 1;
        nums = new int[] {-1, -1, -1, -1};
        //Game game = new Game(n_color, "Computer");
    }

    /**
     *
     * @param color is the number of color for guess and code
     * @return
     */
    public List<String> generateInitial(int color){
        List<String> list = new ArrayList<>();
        //loop
        for(int i=0;i<color;i++){
            for(int j=0;j<color;j++){
                for(int k=0; k<color;k++){
                    for(int l=0;l<color;l++){
                        String a ="";
                        a+=""+i+j+k+l;
                        list.add(a);
                    }
                }
            }
        }
        return list;
    }
    public void setCode(String code){
        this.code = code;
    }
    public int[] getFeedback(String code, String guess){
        String tempCode;
        String tempGuess;
        tempCode = code;
        tempGuess = guess;
        
        int blackPegs = 0;
        for (int i=0; i < code.length(); i++) {
            if (tempCode.charAt(i)==tempGuess.charAt(i)) {
                blackPegs++;
                tempCode = tempCode.substring(0,i)+'x'+tempCode.substring(i+1);
                tempGuess = tempGuess.substring(0,i)+'x'+tempGuess.substring(i+1);
            }
        }
        
        int whitePegs = 0;
        for (int i=0; i < code.length(); i++) {
            if (tempCode.charAt(i)=='x') {
                continue;
            }
            for (int j=0;j < code.length(); j++) {
                if (tempGuess.charAt(i)=='x') {
                    continue;
                }
                if (tempCode.charAt(i)==tempGuess.charAt(i)) {
                    whitePegs++;
                    tempCode = tempCode.substring(0,i)+'x'+tempCode.substring(i+1);
                    tempGuess = tempGuess.substring(0,i)+'x'+tempGuess.substring(i+1);
                }
            }
        }
        int[] result = new int[2];
        result[0] = blackPegs;
        result[1] = whitePegs;
        return result;
    }
    public List<String> filter(List<String> list, String guess, int[] feedback){
        List<String> newList = new ArrayList<>();
        for(String possible : list) {
            if( !(possible.equals(guess))){
                //System.out.println("Debug--Not equal"); //pass
                if(feedback[0]==getFeedback(possible, guess)[0]){
                    if(feedback[1]==getFeedback(possible, guess)[1]){
                        //System.out.println("Debug -- compare feedback");
                        newList.add(possible);
                    }
                }
            }
        }
        return newList;
    }
    public String makeGuess(List<String> pool, int[] feedback){
        int min_length  = Integer.MAX_VALUE;
        String best = "0000";
        for(String possible:pool){
            int length = filter(pool,possible, feedback).size();
            if(min_length > length){
                min_length = length;
                best = possible;
            }
        }
        return best;
    }
    public void toggleColor(Circle c, int index, int n_color) {
        /*
        if (nums[index]==(n_color-1)) { 
            nums[index]=0;
        }
        else {
            nums[index]++;
        }
        */
        int numberIndex = Character.getNumericValue(guess.charAt(index));
        if (numberIndex==(n_color-1)) { 
            guess = guess.substring(0,index)+0+guess.substring(index+1);
            //nums[index]=0; //original
        }
        else {
            guess = guess.substring(0,index) + (numberIndex+1) + guess.substring(index+1);
        }
        //System.out.println("Toggle le");
        changeColor(c, index);
    }
    public void changeColor(Shape c, int index)  {
        int a=Character.getNumericValue(guess.charAt(index)); 
        switch (a) {
            case 0: {
                c.setFill(Color.BLUE); break;
            }
            case 1: {
                c.setFill(Color.GREEN); break;
            }
            case 2: {
                c.setFill(Color.ORANGE); break;
            }
            case 3: {
                c.setFill(Color.PURPLE); break;
            }
            case 4: {
                c.setFill(Color.RED); break;
            }
            case 5: {
                c.setFill(Color.YELLOW); break;
            }
            case 6: {
                c.setFill(Color.BROWN); break;
            }
            case 7: {
                c.setFill(Color.WHITE); break;
            }
        }
    }
    public void createGuessCircles(AnchorPane a, Stage stage, int n_color) throws FileNotFoundException {
        HBox box = new HBox();
        box.setSpacing(30.0);
        MediaPlayer mediaPlayerRound = new MediaPlayer(roundSound);
        mediaPlayerRound.play();
        Label whichGuess = new Label();
        whichGuess.setText("" + guessNum);
        guessNum++;
        box.getChildren().add(whichGuess);

        for (int i=0; i<4; i++) {
            Circle c = new Circle(20.0);
            changeColor(c, i);
            box.getChildren().add(c);
        }
        Separator s = new Separator();
        s.setOrientation(Orientation.VERTICAL);
        box.getChildren().add(s);
        
        int[] feedback = getFeedback(code, guess);
        /*
        Now, feedback[0] = number of black pegs
        feedback[1] = number of white pegs
         */
        if (feedback[0]==4) { //if player has won
            playAgainScreen(true, stage, n_color);
        }
        
        for (int i=0; i < 4; i++) {
            Rectangle r = new Rectangle(30.0, 30.0);
            box.getChildren().add(r);
            if (feedback[0] > 0) {  //first filling in any black feedback pegs
                r.setFill(Color.BLACK);
                feedback[0]--;
            }
            else if (feedback[1] > 0) {//then filling in any white feedback pegs
                r.setFill(Color.WHITE);
                feedback[1]--;
            }
            else {
                r.setFill(Color.PINK);
            }
        }

        if (guessNum>=11) { //This if-else block is purely for sake of alignment
                            //The 10th and final guess was not aligning with the previous guesses
            setLeftAnchor(box, 209.00);//220 
        }
        else {
            setLeftAnchor(box, 214.0);
        }
        index--; //I want the first guess to be at the bottom of the page
                    //and later guesses to move upwards
        setTopAnchor(box, index * 50.0);
        
        a.getChildren().add(box);

    }
    public void playAgainScreen(boolean won, Stage s, int n_color) throws FileNotFoundException {
        Stage stage = new Stage();
        AnchorPane playAgain = new AnchorPane();
        Scene scene = new Scene(playAgain, 300, 100);

        Label message = new Label();
        setTopAnchor(message, 10.0);
        setLeftAnchor(message, 75.0);
        if (won) {
            stage.setTitle("Computer Solved!!");
            message.setText("Congrats! Computer Win!\nWith "+ count + " tries!");
            mediaPlayerWin.play();
        }
        Button play = new Button("Play Again");
        setTopAnchor(play, 60.0);
        setLeftAnchor(play, 30.0);
        play.setPrefHeight(15.0);
        play.setPrefWidth(100.0);
        play.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                s.close();
                mediaPlayerPlay.stop();
                stage.close();
                (new GUI()).createWelcomeScreen();
            }
        });

        Button quit = new Button("Quit");
        setTopAnchor(quit, 60.0);
        setLeftAnchor(quit, 180.0);
        quit.setPrefHeight(15.0);
        quit.setPrefWidth(100.0);
        quit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
        playAgain.getChildren().addAll(message,play, quit);
        stage.setScene(scene);
        stage.show();
    }
    public void createGameScreen(String name, int n_guess, int n_color) throws FileNotFoundException {
        Stage stage;
        AnchorPane gameScreen;
        guess = "0000";
        stage = new Stage();
        stage.setTitle("Mastermind - " + "Computer");
        gameScreen = new AnchorPane();
        String image = "bg1.jpg";
        Image img = new Image(new FileInputStream(image));
        BackgroundImage bgImg = new BackgroundImage(img, 
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT);
        gameScreen.setBackground(new Background(bgImg));
        Scene scene = new Scene(gameScreen, 1200, 800);
        //gameScreen.setBackground(background);
        index += n_guess;
        /*
        This part of the code creates the circles which the player will use to select their guess
         */
        HBox options = new HBox();
        options.setSpacing(30.0);
        
        for (int i=0; i < 4; i++) {
            int x = i; //just a dummy variable. I cannot use i because if I access it
                       //from within inner class, i needs to be final or effectively final.
                       //this is an adequate workaround
            Circle c = new Circle(20.0);
            options.getChildren().add(c);
            c.setFill(Color.BLUE);
            c.setOnMouseClicked((MouseEvent event) -> {
                toggleColor(c, x, n_color);
            });
        }
        setTopAnchor(options, 30.0);
        setLeftAnchor(options, 270.0);

        /*
        This part of the code creates the "Submit Guess" and "Exit" buttons
         */
        Button submit = new Button("Start Guess");
        submit.setOnAction((ActionEvent event) -> {
            List<String> pool = generateInitial(n_color);
            //setCode("1122");
            code = guess;
            guess = "0011";
            while(true){
                try {
                    count++;
                    //log the guess
                    //System.out.println("I guess is "+guess);
                    int[] result = getFeedback(code, guess);
                    createGuessCircles(gameScreen, stage, n_color);
                    if(result[0] == 4)
                        break;
                    //refresh pool: Done // filter out
                    //show choices left
                    //System.out.println("Now filtering");
                    //System.out.println("Length before filter "+pool.size());
                    pool = filter(pool, guess, result);
                    //System.out.println("Length after filter "+pool.size());
                    //refresh guess: Done
                    guess= makeGuess(pool, result);
                    //for debug purpose
                    //break;
                } catch (FileNotFoundException ex) {
                }
            }
        });
        submit.setPrefHeight(50.0);
        submit.setPrefWidth(120.0);
        setTopAnchor(submit, 25.0);
        setLeftAnchor(submit, 560.0);

        Button exit = new Button("Exit");
        exit.setOnAction((ActionEvent event) -> {
            System.exit(0);
        });
        exit.setPrefHeight(50.0);
        exit.setPrefWidth(120.0);
        setTopAnchor(exit, 25.0);
        setLeftAnchor(exit, 860.0);
        
        Label by = new Label();
        setTopAnchor(by, 630.0);
        setLeftAnchor(by, 880.0);
        by.setFont(new Font("Hannotate SC", 17));
        by.setText("Created by:\n1. WID180019 HOE JIUN TIAN\n" +
                "2. WID180026 MELISSA KOK YUAN AI\n" + 
"3. WID180024 MAERZAD MOHAMED\n    NA'EEM BIN MOHAMED NASIR\n" +
"4. WID180022 LAW SIAO HUI");
        
        gameScreen.getChildren().addAll(options, submit, exit, by);
        stage.setScene(scene);
        stage.show();
        mediaPlayerPlay.setOnEndOfMedia(new Runnable() {
        @Override
        public void run() {
            mediaPlayerPlay.seek(Duration.ZERO);
            mediaPlayerPlay.play();
        }
        });
        mediaPlayerPlay.play();

    }
}
