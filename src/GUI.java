import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import static javafx.scene.layout.AnchorPane.setLeftAnchor;
import static javafx.scene.layout.AnchorPane.setTopAnchor;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import static javafx.scene.paint.Color.RED;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class GUI {

    public int[] nums;//the corresponding indeces of the colors chosen
    int index, minutes, seconds;      //required to set vertical location for corresponding guess circles
    int guessNum;//this is to display the number of the current guess
    Game game;
    String name;
    int[] code;  //to store the secret code in integer form (i.e. converting colors to their indeces)
    long startTime;
    String winMusic = "win.mp3";
    String loseMusic = "lose.mp3";
    String roundMusic = "round.mp3";
    String bgmMusic = "bgm.mp3";
    String playMusic = "playing.mp3";
    String[] phrase3 = {"Are you sure? Hehehe...", 
        "Getting confident ay...you'll regret it haha!", 
        "Excellent! 3 corrects, one more and HALLELUJAH!",
        "Hmm...it doesn't seem right...hehe",
        "I entered ten puns into a contest to see which one "
            + "would win. No pun in ten did :P Funny right? "
            + "HAHAHAHAHAHA!!! Okay, fine one more correct guess to go, go go go!",
        "Let me tell you an interesting fact...Stephan hawking always wins "
            + "Good luck, one more correct guess to go!"
    };
    String[] phrase2 = {"Why didn't the astronaut come home to his wife? "
            + "--------- He needed his space...Hey! Don't space out!", 
        "Can a kangaroo jump higher than a house? Of course, a house doesn't "
            + "jump at all...you shouldn't be thinking\n" +
" about this joke! You're losing the game!", 
        "Just 2!? ARE YOU KIDDING ME! I'm so disappointed "
            + "with you Jiun Tian...oh wait you're someone else...\n" +
" whatever",
        "Just 2!? ARE YOU KIDDING ME! I'm so disappointed "
            + "with you Sia...oh wait you're someone else...whatever",
        "Just 2!? ARE YOU KIDDING ME! I'm so disappointed "
            + "with you Desmond...oh wait you're someone else...whatever",
        "Just 2!? ARE YOU KIDDING ME! I'm so disappointed "
            + "with you Melissa...oh wait you're someone else...whatever",
        "Why did the tomato turn red? ------ Because it saw the salad dressing! "
            + "HAHHAHAHA!"
    };
    String[] phrase1 = {"Why don't they play poker in the jungle? "
            + "------------ Too many cheetahs...You better not be one hehe!", 
        "What is Malaysians' favourite city? ---------- "
            + "SITI NURHALIZA !!! HAHAHAHAHAHAv", 
        "GGWP. Where is your brain?",
        "Did your brain wires get connected wrongly?",
        "It's amazing how you just got one correct answer! "
            + "I'm impressed! U NEED THOUSANDS OF APPLAUSES!!!",
        "Let's have a moment of silence for your disfunctioning brain...",
        "Do you know what is a cat's favourite color? "
            + "------------ Purrrrrrrrrple! HAHAHAHA! Okay, it's cold "
            + "around here ",
        "When do you go at red and stop at green? When you're eating "
            + "a watermelon. LMAO!",
        "A driving student arrives at a busy intersection, he wasn't sure of "
            + "the direction so he asked \"Turn left?\" the instructor \n" +
" said, \"Right.\". Then they died. I guess you're right"
    };
    String[] phrase0 = {"Roses are red, Violets are blue. Garbage is dumped, "
            + "now so are you!", 
        "Mario is Red. Sonic is blue. Press give up button because you're going "
            + "to lose HAHA!", 
        "Roses are red, violets are blue, if you love Star Wars, may the force "
            + "be with you.",
        "Why did the traffic light turn red? You would be too at the next "
            + "guess ;) ",
        "I think it's time for you to dial 991, something is very wrong with "
            + "your brain!",
        "NOOB.",
        "What is yellow and solves your problems? ----------- What? "
    };
    
    Random rd = new Random();
    
    Map<String, Player> list = new HashMap<>();
    Media playSound = new Media(new File(playMusic).toURI().toString());
    MediaPlayer mediaPlayerPlay = new MediaPlayer(playSound);
    
    Media loseSound = new Media(new File(loseMusic).toURI().toString());
    MediaPlayer mediaPlayerLose = new MediaPlayer(loseSound);
    Media roundSound = new Media(new File(roundMusic).toURI().toString());
    
    Media bgmSound = new Media(new File(bgmMusic).toURI().toString());
    MediaPlayer mediaPlayerbgm = new MediaPlayer(bgmSound);
    Media winSound = new Media(new File(winMusic).toURI().toString());
    MediaPlayer mediaPlayerWin = new MediaPlayer(winSound);
    //in player.txt each line is <pname> <score>
    GUI() {
        try (Scanner playerListin = new Scanner(new FileInputStream("player.txt"))) {
            while(playerListin.hasNextLine()){
                String[] line = playerListin.nextLine().split("~");
                //update 29/11 make sure no duplicate or overlap in list
                if(list.containsKey(line[0])){
                    //get highest score only for single name
                    if(list.get(line[0]).getScore()<Double.parseDouble(line[1])){
                        list.get(line[0]).updateScore(Double.parseDouble(line[1]));
                    }
                }
                else{
                    list.put(line[0], new Player(line[0], Double.parseDouble(line[1])));
                }
            }
        }catch(FileNotFoundException e){
            System.out.println("Player record file not found!");
        }
        nums = new int[] {-1, -1, -1, -1}; //These have to be -1 because in my changeColor function,
                                           //I update the color index and then display the color
        index = 3; //this is 13 only because it worked well with the spacing
        //game = new Game(n_color);
        //code = game.codeToInts(game.generateSecretCode());
        guessNum = 1;
        
        }
    /**
     * Creates and displays welcome screen. This screen will show the rules of the game
     * and will have a "Start game" button so the user can start playing.
     * It will also have an "Exit" button so the user can quit instead
     * @param chat input is the label name chat which will show the hint
     */
    public void updateChat(Label chat){
        int[] feedback = game.getFeedback(code, nums);
        int correct = feedback[0];
        int close = feedback[1];
        if(correct == 3){
            chat.setText(phrase3[rd.nextInt(phrase3.length)]);
        }else if(correct == 2){
            chat.setText(phrase2[rd.nextInt(phrase2.length)]);
        }else if(correct == 1){
            chat.setText(phrase1[rd.nextInt(phrase1.length)]);
        }else if(correct == 0){
            chat.setText(phrase0[rd.nextInt(phrase0.length)]);
        }
        //chat.setText(phrase[rd.nextInt(phrase.length)]);
    }
    boolean isNumeric(String someString){
        return someString.chars().allMatch(Character::isDigit);
    }
    public void createWelcomeScreen() {
        Stage stage = new Stage();
        stage.setTitle("Welcome to Mastermind by GGWP");
        
        AnchorPane welcomeScreen = new AnchorPane();
        Scene scene = new Scene(welcomeScreen, 520, 650);
        Label rules = new Label();
        setTopAnchor(rules, 15.0);
        setLeftAnchor(rules, 25.0);
        rules.setText("Welcome to Mastermind.\n" +
                "\n" +
                "The objective of the game is to correctly guess a secret code consisting\n" +
                "of a series of 4 colored pegs.\n\n" +
                "Each peg will be of one of 2 to 8 colors – Blue, Green, Orange, Purple, Red,\n Yellow, Brown and White.\n" +
                "More than one peg in the secret code could be of the same color.\nYou must guess the correct color " +
                "and order of the code.\n\n" +
                "You can set chances to correctly guess the code.\n\nAfter every guess, the computer " +
                "will provide you feedback in the\nform of 0 to 4 colored pegs. A black peg indicates " +
                "that a peg in your guess is\nof the correct color and is in the correct position.\n" +
                "A white peg indicates that a peg in your guess is of the correct color\n" +
                "but is not in the correct position.\n\n" +
                "NOTE: The order of the feedback pegs does not correspond to either the\npegs in the code " +
                "or the pegs in your guess. " +
                "\nIn other words, the color of the pegs is important, not the order they are in.\n\n" +
                "Are you ready?\n"+
                "Please enter your name, number of guess and number of color.\n"+
                "There is 1-12 number of guess and 2-8 number of color.");
        rules.setFont(new Font("SansSerif", 14));
        
        welcomeScreen.getChildren().add(rules);
        /* Text area */
        TextField nameInput = new TextField();
        setTopAnchor(nameInput, 480.0+50.0);
        setLeftAnchor(nameInput, 100.0);
        nameInput.setPrefHeight(35.0);
        nameInput.setPrefWidth(140.0);
        nameInput.setPromptText("Name");
        
        TextField guessInput = new TextField();
        setTopAnchor(guessInput, 480.0+50.0);
        setLeftAnchor(guessInput, 260.0);
        guessInput.setPrefHeight(35.0);
        guessInput.setPrefWidth(60.0);
        guessInput.setPromptText("Guess");
        
        TextField colorInput = new TextField();
        setTopAnchor(colorInput, 480.0+50.0);
        setLeftAnchor(colorInput, 330.0);
        colorInput.setPrefHeight(35.0);
        colorInput.setPrefWidth(60.0);
        colorInput.setPromptText("Color");
        
        Label warning = new Label();
        setTopAnchor(warning, 513.0);
        setLeftAnchor(warning, 25.0);
        warning.setFont(new Font("SansSerif", 14));
        warning.setTextFill(RED);
        /*Buttons */
        Button play = new Button("Start Game");
        setTopAnchor(play, 530.0+50.0);
        setLeftAnchor(play, 40.0);
        play.setPrefHeight(50.0);
        play.setPrefWidth(120.0);
        
    

        play.setOnAction((ActionEvent event) -> {
            //make sure input is valid
            if("".equals(nameInput.getText())){
                warning.setText("Name cannot be empty!");
            }
            else if("".equals(guessInput.getText())){
                warning.setText("Number of guess cannot be empty!");
            }
            else if("".equals(colorInput.getText())){
                warning.setText("Number of color cannot be empty!");
            }
            else if(!isNumeric(guessInput.getText())){
                warning.setText("Number of guess must be integer");
            }
            else if(!isNumeric(colorInput.getText())){
                warning.setText("Number of color must be integer");
            }
            else if(Integer.parseInt(guessInput.getText()) >12){
                warning.setText("Number of guess cannot more than 12");
            
            }
            else if(Integer.parseInt(guessInput.getText())==0){
                warning.setText("Number of guess cannot be 0");
            
            }
            else if(Integer.parseInt(colorInput.getText()) >8 ||Integer.parseInt(colorInput.getText())<2){
                warning.setText("Number of color must in range 2 to 8");
            }
            //only run when input is valid
            else{
                try {
                    stage.close();
                    mediaPlayerbgm.stop();
                    name = nameInput.getText();
                    createGameScreen(nameInput.getText(), Integer.parseInt(guessInput.getText()), Integer.parseInt(colorInput.getText()));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        Button codemaker = new Button("Code Maker");
        setTopAnchor(codemaker, 530.0+50.0);
        setLeftAnchor(codemaker, 185.0);
        codemaker.setPrefHeight(50.0);
        codemaker.setPrefWidth(120.0);
        
    

        codemaker.setOnAction((ActionEvent event) -> {
            //make sure input is valid
            if("".equals(colorInput.getText())){
                warning.setText("Number of color cannot be empty!");
            }
            else if(!isNumeric(colorInput.getText())){
                warning.setText("Number of color must be integer");
            }
            else if(Integer.parseInt(colorInput.getText()) >8 ||Integer.parseInt(colorInput.getText())<2){
                warning.setText("Number of color must in range 2 to 8");
            }
            //only run when input is valid
            else{
                try {
                    stage.close();
                    mediaPlayerbgm.stop();
                    name = nameInput.getText();
                    Solver ai = new Solver(Integer.parseInt(colorInput.getText()));
                    ai.createGameScreen("Computer", 12, Integer.parseInt(colorInput.getText()));
                } catch (FileNotFoundException ex) {
                    System.out.println("File not found");
                }
            }
        });
        
        
        Button quit = new Button("Quit");
        setTopAnchor(quit, 530.0+50.0);
        setLeftAnchor(quit, 330.0);
        quit.setPrefWidth(120.0);
        quit.setPrefHeight(50.0);
        quit.setOnAction((ActionEvent event) -> {
            stage.close();
            System.exit(0);
        });
        welcomeScreen.getChildren().addAll(play,quit,nameInput, guessInput, colorInput,  warning, codemaker);
        
        mediaPlayerbgm.play();
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Creates and displays game screen
     * @param name 
     * @param n_guess 
     * @param n_color 
     * @throws java.io.FileNotFoundException 
     */
    public void createGameScreen(String name, int n_guess, int n_color) throws FileNotFoundException {
        Stage stage = new Stage();
        stage.setTitle("Mastermind - " + name);
        AnchorPane gameScreen = new AnchorPane();
        
        String image = "bg1.jpg";
        Image img = new Image(new FileInputStream(image));
        BackgroundImage bgImg = new BackgroundImage(img, 
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT);
        gameScreen.setBackground(new Background(bgImg));
        Scene scene = new Scene(gameScreen, 1200, 800);
        
        Image dialog = new Image(new FileInputStream("dialog.png"));
        ImageView dialogImage = new ImageView(dialog);
        setTopAnchor(dialogImage, 150.0);
        setLeftAnchor(dialogImage, 10.0);
        dialogImage.setFitHeight(150);
        dialogImage.setFitWidth(200);
        
        Image robot = new Image(new FileInputStream("robot.png"));
        ImageView robotImage = new ImageView(robot);
        setTopAnchor(robotImage, 280.0);
        setLeftAnchor(robotImage, -10.0);
        robotImage.setFitHeight(300);
        robotImage.setFitWidth(240);
        
        Image tips = new Image(new FileInputStream("tips.png"));
        ImageView tipsImage = new ImageView(tips);
        setTopAnchor(tipsImage, 45.0);
        setLeftAnchor(tipsImage, 38.0);
        tipsImage.setFitHeight(80);
        tipsImage.setFitWidth(160);
        
        Label chat = new Label("I am your AI chat bot\nI will give you tips\nthat might work or not");
        setTopAnchor(chat, 170.0);
        setLeftAnchor(chat, 20.0);
        chat.setFont(new Font("SansSerif", 12));
        chat.setMaxWidth(180);
        //chat.setAlignment(Pos.CENTER);
        chat.setWrapText(true);
        //chat.setTextFill(RED);
        
        //gameScreen.setBackground(background);
        index += n_guess;
        startTime = System.currentTimeMillis();
        /*
        This part of the code creates the circles which the player will use to select their guess
         */
        HBox options = new HBox();
        options.setSpacing(30.0);
        
        game = new Game(n_color, name);
        code = game.codeToInts(game.generateSecretCode());
        
        for (int i=0; i < 4; i++) {
            int x = i; //just a dummy variable. I cannot use i because if I access it
                       //from within inner class, i needs to be final or effectively final.
                       //this is an adequate workaround
            Circle c = new Circle(20.0);
            options.getChildren().add(c);
            c.setOnMouseClicked((MouseEvent event) -> {
                toggleColor(c, x, n_color);
            });
        }
        setTopAnchor(options, 30.0);
        setLeftAnchor(options, 270.0);

        /*
        This part of the code creates the "Submit Guess" and "Exit" buttons
         */
        Button submit = new Button("Submit Guess");
        submit.setOnAction((ActionEvent event) -> {
            if(index>4){ try {
                //this will ensure that player only has 10 guesses
                /*
                This is an ugly way to do this. I might work on this later
                */
                updateChat(chat);
                
                createGuessCircles(gameScreen, stage, n_color);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else {
                try {
                    createGuessCircles(gameScreen, stage, n_color);
                    if(game.getFeedback(code, nums)[0]!=4){
                        long endTime = System.currentTimeMillis();
                        long elapsedMilliSeconds = endTime - startTime;
                        int elapsedSeconds = (int) (elapsedMilliSeconds / 1000.0);//first argument is true means that player has won
                        minutes = elapsedSeconds / 60;
                        seconds = elapsedSeconds % 60;
                        //mediaPlayerLose.play();
                        playAgainScreen(false, stage, n_color); //first argument is false means that player has lost the game
                        /*
                        For debugging purposes. Will replace this with playAgainScreen displaying code
                        */
                        for (int i=0; i < 4; i++) {
                            System.out.print(code[i]);
                        }
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
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
        
        Button giveup = new Button("Give Up");
        giveup.setOnAction((ActionEvent event) -> {
            try {
                playAgainScreen(false, stage, n_color);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        giveup.setPrefHeight(50.0);
        giveup.setPrefWidth(120.0);
        setTopAnchor(giveup, 25.0);
        setLeftAnchor(giveup, 1010.0);
        //Restart
        Button restart = new Button("Restart");
        restart.setOnAction((ActionEvent event) -> {
            try {
                index =3;
                guessNum = 1;
                createGameScreen(name, n_guess, n_color);
                stage.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        restart.setPrefHeight(50.0);
        restart.setPrefWidth(120.0);
        setTopAnchor(restart, 25.0);
        setLeftAnchor(restart, 710.0);
        //Score Board
        
        //input all the data first to do sorting
        TableData[] tf = new TableData[list.size()];
        for(int i=0;i<list.size();i++){
            tf[i] = new TableData(list.get(list.keySet().toArray()[i]).getName(),(int)(list.get(list.keySet().toArray()[i]).getScore()));
        }//too long get warning nia, no problem here
        Arrays.sort(tf);
        int sizeList;
        if(list.size()<10){
            sizeList = list.size();
        }else{
            sizeList = 10;
        }
        //Add the top 10 only after sort
        TableData[] td = new TableData[sizeList];
        for(int i =0;i<sizeList;i++){
            td[i]= tf[i];
        }//ignore the warning, its not problem
        final ObservableList<TableData> data = FXCollections.observableArrayList(td);
        
        TableView table = new TableView();
        TableColumn Cname = new TableColumn("Name");
        TableColumn Cscore = new TableColumn("Score");
        Cname.setMinWidth(200);
        Cname.setCellValueFactory(
            new PropertyValueFactory<TableData,String>("name")
        );//no error here actually
        Cscore.setMinWidth(80);
        Cscore.setCellValueFactory(
            new PropertyValueFactory<TableData,Integer>("score")
        );//no error here actually
        table.setItems(data);
        table.setEditable(false);
        table.getColumns().addAll(Cname, Cscore);
        Cscore.setSortType(TableColumn.SortType.DESCENDING);
        table.getSortOrder().setAll(Cscore);
        table.setFixedCellSize(37.5);
        setTopAnchor(table, 200.0);
        setLeftAnchor(table, 880.0);
        table.setPrefHeight(420.0);
        
        Label scoreboard = new Label("Scoreboard");
        setTopAnchor(scoreboard, 140.0);
        setLeftAnchor(scoreboard, 940.0);
        scoreboard.setFont(new Font("Comic Sans MS", 30));
        
        Label by = new Label();
        setTopAnchor(by, 630.0);
        setLeftAnchor(by, 880.0);
        by.setFont(new Font("Hannotate SC", 17));
        by.setText("Created by:\n1. WID180019 HOE JIUN TIAN\n" +
                "2. WID180026 MELISSA KOK YUAN AI\n" + 
"3. WID180024 MAERZAD MOHAMED\n    NA'EEM BIN MOHAMED NASIR\n" +
"4. WID180022 LAW SIAO HUI");
        
        gameScreen.getChildren().addAll(options, submit, exit, table, restart, 
                scoreboard, giveup, by, dialogImage, robotImage, tipsImage, 
                chat);
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
    
    /**
     * This function is responsible for the different colors that the guess circles can have.
     * Calls changeColor within which is the function actually responsible for assigning colors to circles
     * @param c Circle whose color needs to be changed
     * @param index denotes which color the circle currently has. Order of colors corresponds
     *              to order in colors[] array
     *              i.e. 0-Blue, 1-Green, 2-Orange, 3-Purple, 4-Red, 5-Yellow
     * @param n_color
     */
    public void toggleColor(Circle c, int index, int n_color) {
        if (nums[index]==(n_color-1)) { //there are only 6 color options
            nums[index]=0;
        }
        else {
            nums[index]++;
        }
        changeColor(c, index);
    }

    /**
     * Assigns colors to each circle
     * @param c shape object whose color needs to be changed
     * @param index denotes which color the shape currently has. Order of colors corresponds
     *              to order in colors[] array
     *              i.e. 0-Blue, 1-Green, 2-Orange, 3-Purple, 4-Red, 5-Yellow
     */
    public void changeColor(Shape c, int index)  {
        switch (nums[index]) {
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

    /**
     * Creates row of circles and feedback squares for each guess
     * @param a the AnchorPane created in createGameScreen to which I need to add the guess circles and squares
     * @param stage the stage created in createGameScreen. This is required only to pass
     *              to playAgainScreen() as that may need to close the game screen stage
     * @param n_color
     * @throws java.io.FileNotFoundException
     */
    public void createGuessCircles(AnchorPane a, Stage stage, int n_color) throws FileNotFoundException {
        HBox guess = new HBox();
        guess.setSpacing(30.0);
        MediaPlayer mediaPlayerRound = new MediaPlayer(roundSound);
        mediaPlayerRound.play();
        Label whichGuess = new Label();
        whichGuess.setText("" + guessNum);
        guessNum++;
        guess.getChildren().add(whichGuess);

        for (int i=0; i<4; i++) {
            Circle c = new Circle(20.0);
            changeColor(c, i);
            guess.getChildren().add(c);
        }

        Separator s = new Separator();
        s.setOrientation(Orientation.VERTICAL);
        guess.getChildren().add(s);

        int[] feedback = game.getFeedback(code, nums);
        /*
        Now, feedback[0] = number of black pegs
        feedback[1] = number of white pegs
         */

        if (feedback[0]==4) { //if player has won
            long endTime = System.currentTimeMillis();
            long elapsedMilliSeconds = endTime - startTime;
            int elapsedSeconds = (int) (elapsedMilliSeconds / 1000.0);//first argument is true means that player has won
            minutes = elapsedSeconds / 60;
            seconds = elapsedSeconds % 60;
            
            playAgainScreen(true, stage, n_color);
        }
        
        for (int i=0; i < 4; i++) {
            Rectangle r = new Rectangle(30.0, 30.0);
            guess.getChildren().add(r);
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
            setLeftAnchor(guess, 209.00);//220 
        }
        else {
            setLeftAnchor(guess, 214.0);
        }
        index--; //I want the first guess to be at the bottom of the page
                    //and later guesses to move upwards
        setTopAnchor(guess, index * 50.0);
        
        a.getChildren().add(guess);

    }

    /**
     * Creates the screen after the player either wins or loses the game.
     * If player has lost the game, screen will display what the code was for that game
     * @param won true if player has won. false if player has lost. Useful to modify text, title, etc.. to situation
     * @param s if player chooses to play again, I want to close the current gameScreen. Need this stage for that
     * @param n_color
     * @throws java.io.FileNotFoundException
     */
    public void playAgainScreen(boolean won, Stage s, int n_color) throws FileNotFoundException {
        Stage stage = new Stage();
        AnchorPane playAgain = new AnchorPane();
        Scene scene = new Scene(playAgain, 300, 100);

        Label message = new Label();
        setTopAnchor(message, 10.0);
        setLeftAnchor(message, 75.0);
        Label showCode = new Label(); //only used if player has lost
        showCode.setVisible(false);
        if (won) {
            stage.setTitle("You Win!");
            double score = 1.0/(1.0+( ((double)guessNum-1.0) * ((double)minutes * 60.0 + (double)(seconds) ) ) ) * 100000.0 * (double)n_color;
            //System.out.printf("%.4f %d %d %d", score, n_color,guessNum, (minutes*60 + seconds));
            message.setText("Congrats! You Win!\nTime: "+minutes+" minutes and "+seconds+" seconds.\nScore: "+(int)score);
            if(!list.containsKey(name))
                list.put(name,new Player(name, score));
            else if(list.get(name).getScore()<score)
                list.get(name).updateScore(score);
            
            try (PrintWriter playerListout = new PrintWriter(new FileOutputStream(new File("player.txt"),true))) {
                playerListout.println(name+"~"+score);
            }
            game.log.println("Player Win with score "+score);
            mediaPlayerWin.play();
        }
        else {
            stage.setTitle("You Lose!");
            message.setText("Sorry! You lose!\n");
            showCode.setText("The code was " + game.codeForPlayAgain(code) );
            showCode.setVisible(true);
            setTopAnchor(showCode, 28.0);
            setLeftAnchor(showCode, 30.0);
            game.log.println("Player lose");
            mediaPlayerLose.play();// play lose music
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
        playAgain.getChildren().addAll(message, showCode, play, quit);
        stage.setScene(scene);
        stage.show();
        game.log.close();
    }
}