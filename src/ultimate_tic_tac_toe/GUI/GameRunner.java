/**
 * @author 	Tahmid Khan
 * @PID		A12650032
 * This class contains the main method and the entire GUI. 
 * It will initialize and run the game
 */

package ultimate_tic_tac_toe.GUI;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import ultimate_tic_tac_toe.main.BigBoard;
import ultimate_tic_tac_toe.main.GenericBoard;

@SuppressWarnings({"rawtypes", "unchecked"})
public class GameRunner extends Application
{
	// Color and style constants
	public static final String MAIN_FONT = "Raleway Thin";
	public static final Color MENU_BG_COLOR = Color.rgb(24, 27, 28);
	public static final Color GAME_BG_COLOR = Color.rgb(13, 13, 13);
	public static final Color CELL_COLOR = Color.rgb(255, 255, 255, 0.1);
	public static final Color SMALL_LINE_COLOR = Color.rgb(255, 255, 255, 0.3);
	public static final Color P1_COLOR = Color.ORANGE;
	public static final Color P2_COLOR = Color.rgb(64, 210, 255);//Color.AQUAMARINE;
	
	// Game constants
	public static final int NUM_BOARDS = 9;
	public static final int NUM_CELLS_PER_BOARD = 9;
	public static final int TIME_PER_TURN = 3500;
	public static enum Mode {UNTIMED, FRENZY, MENU};
	
	// Background constants
	public static final BackgroundFill[] MENU_FILL = {new BackgroundFill(MENU_BG_COLOR, 
			CornerRadii.EMPTY, Insets.EMPTY)};
	public static final BackgroundFill[] BLACK_FILL = {new BackgroundFill(Color.BLACK, 
			CornerRadii.EMPTY, Insets.EMPTY)};
	public static final BackgroundFill[] GAME_FILL = {new BackgroundFill(GAME_BG_COLOR, 
			CornerRadii.EMPTY, Insets.EMPTY)};
	public static final BackgroundFill[] CELL_FILL = {new BackgroundFill(CELL_COLOR, 
			CornerRadii.EMPTY, Insets.EMPTY)};
	public static final Background MENU_BG = new Background(MENU_FILL);
	public static final Background BAR_BG = new Background(BLACK_FILL);
	public static final Background GAME_BG = new Background(GAME_FILL);
	public static final Background CELL_BG = new Background(CELL_FILL);
	
	// Size constants
	public static final int FRAME_WIDTH = 780;
	public static final int FRAME_HEIGHT = 520;
	public static final int BAR_HEIGHT = 70;
	public static final int PLAYER_BAR_HEIGHT = 3;
	public static final int BIG_BOARD_SIZE = FRAME_HEIGHT-2*BAR_HEIGHT;
	public static final int SMALL_BOARD_SIZE = BIG_BOARD_SIZE/3;
	public static final int SMALL_CELL_SIZE = SMALL_BOARD_SIZE/3;
	public static final int LINE_WIDTH = 2;
	
	// Holds the current state of the game (playing or in main menu) as
	// a scene object
	private Scene gameState;
	private Stage window;
	// Board spaces carries an array of buttons associated with each of the
	// smaller boards
	// Button[i][j] refers to the button in cell #j of the ith board
	private Button[][] boardButtons;
	// Will contain the "lines" (Rectangle objects) of a given grid
	// Rectangle[i][j] refers to the jth rectangle of the ith board
	private Rectangle[][] smallGrids;
	// This object indicates whose turn it is (by color) and, if in frenzy mode
	// indicates the time left (by it's horizontal scale)
	private Rectangle playerBarTop;
	private Rectangle playerBarBottom;
	// Universal animation instances
	private GaussianBlur blur = new GaussianBlur(0);
	private ParallelTransition playerBarAnim;
	// Holds the game logic over the course of a game
	private BigBoard game;
	private Mode gameMode;
	// Holds the audio data
	private GameAudio audio = new GameAudio();
	
	// Launch here
	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage window) throws Exception 
	{
		this.window = window;
		gameState = buildMainMenuScene();
		
		window.setResizable(false);
		window.setTitle("Ultimate TTT Frenzy");
		window.setScene(gameState);
		window.show();
	}
	
	/**
	 * Builds, animates and returns the main menu GUI
	 * @return the main menu GUI scene
	 */
	public Scene buildMainMenuScene()
	{
		// Main menu scene
		final BorderPane main = new BorderPane();
		// Set node to draw from cache to optimize animations
		main.setCache(true);
		main.setCacheHint(CacheHint.SPEED);
		Scene mainMenu = new Scene(main, FRAME_WIDTH, FRAME_HEIGHT);
		main.setBackground(MENU_BG);
			
		// Make horizontal black bars
		Pane topPane = new Pane();
		topPane.setBackground(BAR_BG);
		topPane.setPrefSize(FRAME_WIDTH, FRAME_WIDTH/20);
		main.setTop(topPane);
		Pane bottomPane = new Pane();
		bottomPane.setBackground(BAR_BG);
		bottomPane.setPrefSize(FRAME_WIDTH, FRAME_WIDTH/20);
		main.setBottom(bottomPane);
			
		// Make a VBox for game title (stacked on top)and game 
		// selections (listed below)
		VBox subMenuPane = new VBox();
		subMenuPane.setAlignment(Pos.CENTER);
		main.setCenter(subMenuPane);
				
		// Next add the actual title label to the top of the 
		// subMenuPane
		Label title = new Label("Ultimate TTT Frenzy");
		title.setFont(new Font(MAIN_FONT, 60));
		title.setTextFill(Color.rgb(222, 233, 255));
		title.setAlignment(Pos.CENTER);
		title.setMinHeight(FRAME_HEIGHT/3);
		title.setVisible(false);
		subMenuPane.getChildren().add(title);
			
		// Make an HBox for selections
		HBox selectionPane = new HBox(60);
		selectionPane.setAlignment(Pos.CENTER);
		subMenuPane.getChildren().add(selectionPane);
				
		// Make the selection labels and finall add all to the
		// selection pane
		Text untimedBttn = new Text("Untimed");
		untimedBttn.setFont(new Font(MAIN_FONT, 30));
		untimedBttn.setFill(Color.WHITE);
		untimedBttn.setOnMouseEntered(menuButtonAnimation);
		untimedBttn.setOnMouseExited(menuButtonAnimation);
		untimedBttn.setOnMouseClicked(menuButtonLogic);
		
		Text frenzyBttn = new Text("Frenzy");
		frenzyBttn.setFont(new Font(MAIN_FONT, 30));
		frenzyBttn.setFill(Color.WHITE);
		frenzyBttn.setOnMouseEntered(menuButtonAnimation);
		frenzyBttn.setOnMouseExited(menuButtonAnimation);
		frenzyBttn.setOnMouseClicked(menuButtonLogic);
				
		Text quitBttn = new Text("Quit");
		quitBttn.setFont(new Font(MAIN_FONT, 30));
		quitBttn.setFill(Color.RED);
		quitBttn.setOnMouseEntered(menuButtonAnimation);
		quitBttn.setOnMouseExited(menuButtonAnimation);
		quitBttn.setOnMouseClicked(menuButtonLogic);
				
		selectionPane.getChildren().addAll(untimedBttn, frenzyBttn, quitBttn);
		selectionPane.setVisible(false);
				
		// Fade in title and options
		double length = FRAME_WIDTH - 100;
		double start_x = (FRAME_WIDTH - length)/2;
		double start_y = FRAME_HEIGHT/2 + 25;
		
		Rectangle line = new Rectangle(start_x, start_y, 
				length, LINE_WIDTH);
		line.setFill(Color.WHITE);
		
		ScaleTransition lineAnim = new ScaleTransition(Duration.millis(3000), line);
		lineAnim.setFromX(0.0);
		lineAnim.setByX(1.0);
		lineAnim.play();
		
		main.getChildren().add(line);
		
		FadeTransition fadeInTitle = new FadeTransition(Duration.millis(1000), title);
		fadeInTitle.setDelay(Duration.millis(1000));
		fadeInTitle.setFromValue(0.0);
		fadeInTitle.setToValue(1.0);
		FadeTransition fadeInSelections = new FadeTransition(Duration.millis(1000), selectionPane);
		fadeInSelections.setFromValue(0.0);
		fadeInSelections.setDelay(new Duration(1500));
		fadeInSelections.setToValue(1.0);
		ParallelTransition parallel = new ParallelTransition();
		parallel.getChildren().addAll(fadeInTitle, fadeInSelections);
		parallel.play();
		title.setVisible(true);
		selectionPane.setVisible(true);
		
		// Play menu bgm
		try {
			audio.beginBGM(Mode.MENU);
		} catch (Exception e){
			System.out.println("Problem loading BGM");
		}
		
		return mainMenu;
	}
	
	/**
	 * Builds, animates and returns the untimed GUI
	 * @return the untimed GUI scene
	 * @throws LineUnavailableException 
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 */
	public Scene buildGameScene()
	{
		// Reset any gaussian blurring
		blur.setRadius(0.0);
		
		BorderPane main = new BorderPane();
		Scene untimedScene = new Scene(main, FRAME_WIDTH, FRAME_HEIGHT);
		
		// Make horizontal black bars and colored player bars
		Pane topPane = new Pane();
		topPane.setBackground(BAR_BG);
		topPane.setPrefSize(FRAME_WIDTH, BAR_HEIGHT);
		playerBarTop = new Rectangle(0, BAR_HEIGHT - PLAYER_BAR_HEIGHT, 
				FRAME_WIDTH + 10, PLAYER_BAR_HEIGHT);
		playerBarTop.setFill(P1_COLOR);
		topPane.getChildren().add(playerBarTop);
		main.setTop(topPane);
		
		Pane bottomPane = new Pane();
		bottomPane.setBackground(BAR_BG);
		bottomPane.setPrefSize(FRAME_WIDTH, BAR_HEIGHT);
		playerBarBottom = new Rectangle(0, 0, FRAME_WIDTH + 10, PLAYER_BAR_HEIGHT);
		playerBarBottom.setFill(P1_COLOR);
		bottomPane.getChildren().add(playerBarBottom);
		main.setBottom(bottomPane);
		
		// Initialize animation sequence for top and bottom playerBars
		ScaleTransition topAnim = new ScaleTransition(
				Duration.millis(TIME_PER_TURN), playerBarTop);
		topAnim.setFromX(1.0);
		topAnim.setByX(-1.0);
		ScaleTransition bottomAnim = new ScaleTransition(
				Duration.millis(TIME_PER_TURN), playerBarBottom);
		bottomAnim.setFromX(1.0);
		bottomAnim.setByX(-1.0);
		
		playerBarAnim = new ParallelTransition(topAnim, bottomAnim);
		// The player bars act as a timer so if its animation reaches the end
		// the current player loses and the other player wins
		playerBarAnim.setOnFinished(new EventHandler(){
			public void handle(Event e)
			{
				audio.stopBGM();
				audio.playSoundEffect(GameAudio.SFX.VICTORY_FANFARE);
				switch(game.getPlayerTurn())
				{
					// The bar represents a sort of timer. Once the "timer has
					// run" (playerBar animation complete) for a player in Frenzy
					// mode, the other player automatically wins
					case GenericBoard.P1_MARKER:
						displayVictory(GenericBoard.P2_MARKER);
						break;
					case GenericBoard.P2_MARKER:
						displayVictory(GenericBoard.P1_MARKER);
						break;
					default:
						break;
				}
			}
		});
		
		// Make the main board grid
		GridPane centerPane = new GridPane();
		centerPane.setAlignment(Pos.CENTER);
		centerPane.setBackground(GAME_BG);
		
		// Initialize the smaller boards (as gridpanes) inside each of 
		// centerPane's 9 cells
		boardButtons = new Button[NUM_BOARDS][NUM_CELLS_PER_BOARD];
		for(int row = 0; row <= 2; row++)
			for(int col = 0; col <= 2; col++)
			{
				GridPane newBoard = new GridPane();
				// Initialize invisible buttons in each of the 9 cells of 
				// the smaller gridpane
				for(int newRow = 0; newRow <= 2; newRow++)
					for(int newCol = 0; newCol <= 2; newCol++)
					{
						Button b = new Button();
						// Make b a square size of SMALL_CELL_SIZE
						b.setPrefSize(SMALL_CELL_SIZE, SMALL_CELL_SIZE);
						// Make b invisible
						b.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,
								CornerRadii.EMPTY, Insets.EMPTY)));
						b.setEffect(blur);
						b.setDisable(true);
						b.setOnMouseEntered(cellAnimation);
						b.setOnMouseExited(cellAnimation);
						b.setOnMouseClicked(cellLogic);
						newBoard.add(b, newCol, newRow);
						
						// Save button into the board buttons array
						int boardNum = 3*row + col;
						int buttonNum = 3*newRow + newCol;
						boardButtons[boardNum][buttonNum] = b;
					}
				// add the new board (gridpane) to the appropriate cell of the main
				// gridpane board, centerPane
				centerPane.add(newBoard, col, row);
			}
		//animateBigGrid(centerPane);
		main.setCenter(centerPane);
		
		// Begin animation of the board
		animateBigGrid(main);
		animateSmallGrids(main);
		
		// Play bgm for untimed mode at this point
		if(gameMode == Mode.UNTIMED)
			try {
				audio.beginBGM(Mode.UNTIMED);
			} catch (Exception e){
				System.out.println("Problem loading BGM");
			}
		
		return untimedScene;
	}
	
	/**
	 * Draws and animates the big grid onto a given pane
	 * @param pane the pane to draw on
	 */
	public void animateBigGrid(Pane pane)
	{
		/*	Board lines:
		 * 			|			|
		 * 			|			|
		 * 			|			|
		 * ---------+-----------+---------	Line 3
		 * 			|			|
		 * 			|			|
		 * 			|			|
		 * ---------+-----------+---------	Line 4
		 * 			|			|
		 * 			|			|
		 * 			|			|
		 * 		Line 1		Line 2
		 */
		double start_x = (FRAME_WIDTH - BIG_BOARD_SIZE)/2;
		double start_y = BAR_HEIGHT;
		
		Rectangle line1 = new Rectangle(start_x + BIG_BOARD_SIZE/3, 
				start_y, LINE_WIDTH, BIG_BOARD_SIZE);
		line1.setFill(Color.WHITE);
		line1.setEffect(blur);
		Rectangle line2 = new Rectangle(start_x + BIG_BOARD_SIZE*2/3, 
				start_y, LINE_WIDTH, BIG_BOARD_SIZE);
		line2.setFill(Color.WHITE);
		line2.setEffect(blur);
		Rectangle line3 = new Rectangle(start_x, start_y + BIG_BOARD_SIZE/3, 
				BIG_BOARD_SIZE, LINE_WIDTH);
		line3.setFill(Color.WHITE);
		line3.setEffect(blur);
		Rectangle line4 = new Rectangle(start_x, start_y + BIG_BOARD_SIZE*2/3, 
				BIG_BOARD_SIZE, LINE_WIDTH);
		line4.setFill(Color.WHITE);
		line4.setEffect(blur);

		pane.getChildren().addAll(line1, line2, line3, line4);
		
		// The animation
	    ScaleTransition line1Anim = new ScaleTransition(Duration.millis(1500), line1);
	    line1Anim.setFromY(0);
	    line1Anim.setByY(1.0);
	    FadeTransition line1Fade = new FadeTransition(Duration.millis(1500), line1);
	    line1Fade.setFromValue(0.0);
	    line1Fade.setToValue(1.0);
	    ScaleTransition line2Anim = new ScaleTransition(Duration.millis(1500), line2);
	    line2Anim.setFromY(0);
	    line2Anim.setByY(1.0);
	    FadeTransition line2Fade = new FadeTransition(Duration.millis(1500), line2);
	    line2Fade.setFromValue(0.0);
	    line2Fade.setToValue(1.0);
	    ScaleTransition line3Anim = new ScaleTransition(Duration.millis(1500), line3);
	    line3Anim.setFromX(0);
	    line3Anim.setByX(1.0);
	    FadeTransition line3Fade = new FadeTransition(Duration.millis(1500), line3);
	    line3Fade.setFromValue(0.0);
	    line3Fade.setToValue(1.0);
	    ScaleTransition line4Anim = new ScaleTransition(Duration.millis(1500), line4);
	    line4Anim.setFromX(0);
	    line4Anim.setByX(1.0);
	    FadeTransition line4Fade = new FadeTransition(Duration.millis(1500), line4);
	    line4Fade.setFromValue(0.0);
	    line4Fade.setToValue(1.0);
	    
	    ParallelTransition lineAnimations = new ParallelTransition(
	    		line1Anim, line2Anim, line3Anim, line4Anim, 
	    		line1Fade, line2Fade, line3Fade, line4Fade);
	    lineAnimations.setDelay(new Duration(1000));
	    lineAnimations.setOnFinished(new EventHandler(){
	    	public void handle(Event e)
	    	{
	    		if(gameMode == Mode.UNTIMED)
	    		{
    				// Make cells interactable
    				for(int boardNum = 0; boardNum < NUM_BOARDS; boardNum++)
    					for(Button b : boardButtons[boardNum])
    						b.setDisable(false);
    				return;
	    		}
	    		// In frenzy mode, there will be a "3, 2, 1, Go!" notification that tells 
	    		// the players to start and prompts the player bar timers to begin ticking
	    		int correction_x = 25;
	    		int correction_y = 65;
	    		int size = 100;
	    		Color countDownColor = Color.WHITE;
	    		Glow glow = new Glow(0.6);
	    		
	    		final Text count3 = new Text("3");
	    		count3.setFill(countDownColor);
	    		count3.setFont(new Font("Arial", size));
	    		count3.relocate(FRAME_WIDTH/2 - correction_x, FRAME_HEIGHT/2 - correction_y);
	    		count3.setEffect(glow);
	    		((Pane) gameState.getRoot()).getChildren().add(count3);
	    		final Text count2 = new Text("2");
	    		count2.setFill(countDownColor);
	    		count2.setFont(new Font("Arial", size));
	    		count2.relocate(FRAME_WIDTH/2 - correction_x, FRAME_HEIGHT/2 - correction_y);
	    		count2.setEffect(glow);
	    		count2.setVisible(false);
	    		((Pane) gameState.getRoot()).getChildren().add(count2);
	    		final Text count1 = new Text("1");
	    		count1.setFill(countDownColor);
	    		count1.setFont(new Font("Arial", size));
	    		count1.relocate(FRAME_WIDTH/2 - correction_x, FRAME_HEIGHT/2 - correction_y);
	    		count1.setEffect(glow);
	    		count1.setVisible(false);
	    		((Pane) gameState.getRoot()).getChildren().add(count1);
	    		final Text go = new Text("GO");
	    		go.setFill(Color.GREENYELLOW);
	    		go.setFont(new Font("Arial", size));
	    		go.relocate(FRAME_WIDTH/2 - 3*correction_x, FRAME_HEIGHT/2 - correction_y);
	    		go.setEffect(glow);
	    		go.setVisible(false);
	    		((Pane) gameState.getRoot()).getChildren().add(go);
	    		
	    		final FadeTransition fadeIn3 = new FadeTransition(
	    				Duration.millis(1000), count3);
	    		fadeIn3.setFromValue(1.0);
	    		fadeIn3.setToValue(1.0);
	    		final FadeTransition fadeIn2 = new FadeTransition(
	    				Duration.millis(1000), count2);
	    		fadeIn2.setFromValue(1.0);
	    		fadeIn2.setToValue(1.0);
	    		final FadeTransition fadeIn1 = new FadeTransition(
	    				Duration.millis(1000), count1);
	    		fadeIn1.setFromValue(1.0);
	    		fadeIn1.setToValue(1.0);
	    		final FadeTransition fadeInGo = new FadeTransition(
	    				Duration.millis(1000), go);
	    		fadeInGo.setFromValue(1.0);
	    		fadeInGo.setToValue(1.0);
	    		
	    		audio.playSoundEffect(GameAudio.SFX.COUNTDOWN);
	    		fadeIn3.play();
	    		fadeIn3.setOnFinished(new EventHandler(){
	    			public void handle(Event e){
	    				count3.setVisible(false);
	    				count2.setVisible(true);
	    				fadeIn2.play();
	    			}
	    		});
	    		fadeIn2.setOnFinished(new EventHandler(){
	    			public void handle(Event e){
	    				count2.setVisible(false);
	    				count1.setVisible(true);
	    				fadeIn1.play();
	    			}
	    		});
	    		fadeIn1.setOnFinished(new EventHandler(){
	    			public void handle(Event e){
	    				count1.setVisible(false);
	    				go.setVisible(true);
	    				fadeInGo.play();
	    			}
	    		});
	    		fadeInGo.setOnFinished(new EventHandler(){
	    			public void handle(Event e){
	    				go.setVisible(false);
	    				// Make cells interactable
	    				for(int boardNum = 0; boardNum < NUM_BOARDS; boardNum++)
	    					for(Button b : boardButtons[boardNum])
	    						b.setDisable(false);
	    				// Start "timer"
	    				playerBarAnim.play();
	    				// Begin bgm music
	    				try {
	    					audio.beginBGM(Mode.FRENZY);
	    				} catch (Exception exception){
	    					System.out.println("Problem loading BGM");
	    				}
	    			}
	    		});
	    	}
	    });
	    lineAnimations.play();
	    
	}
	
	/**
	 * Animates the 9 smaller grids and saves the Rectangle
	 * lines of each grid in the smallGrids array
	 * @param pane the pane to animate on 
	 */
	public void animateSmallGrids(Pane pane)
	{
		double start_x = (FRAME_WIDTH - BIG_BOARD_SIZE)/2;
		double start_y = BAR_HEIGHT - SMALL_BOARD_SIZE;
		
		// Holds the 4 rectangle lines of each grid for future refrence
		smallGrids = new Rectangle[NUM_BOARDS][4];
		
		// Initialize boards
		for(int row = 0; row <= 2; row++)
			for(int col = 0; col <= 2; col++)
			{
				// Update origin (x, y) of rectangles
				if(col % 3 == 0)
				{
					start_x = (FRAME_WIDTH - BIG_BOARD_SIZE)/2;
					start_y += SMALL_BOARD_SIZE;
				}
				else
					start_x += SMALL_BOARD_SIZE;
				
				// Initialize Rectangle lines
				Rectangle line1 = new Rectangle(start_x + SMALL_BOARD_SIZE/3, 
						start_y, LINE_WIDTH/2, SMALL_BOARD_SIZE);
				line1.setFill(SMALL_LINE_COLOR);
				line1.setEffect(blur);
				Rectangle line2 = new Rectangle(start_x + SMALL_BOARD_SIZE*2/3, 
						start_y, LINE_WIDTH/2, SMALL_BOARD_SIZE);
				line2.setFill(SMALL_LINE_COLOR);
				line2.setEffect(blur);
				Rectangle line3 = new Rectangle(start_x, start_y + SMALL_BOARD_SIZE/3, 
						SMALL_BOARD_SIZE, LINE_WIDTH/2);
				line3.setFill(SMALL_LINE_COLOR);
				line3.setEffect(blur);
				Rectangle line4 = new Rectangle(start_x, start_y + SMALL_BOARD_SIZE*2/3, 
						SMALL_BOARD_SIZE, LINE_WIDTH/2);
				line4.setFill(SMALL_LINE_COLOR);
				line4.setEffect(blur);
				
				// Add to the pane and 
				pane.getChildren().addAll(line1, line2, line3, line4);
				int boardNum = 3*row + col;
				smallGrids[boardNum][0] = line1;
				smallGrids[boardNum][1] = line2;
				smallGrids[boardNum][2] = line3;
				smallGrids[boardNum][3] = line4;
				
				// Add animations to the lines
			    ScaleTransition line1Anim = new ScaleTransition(Duration.millis(1500), line1);
			    line1Anim.setFromY(0);
			    line1Anim.setByY(1.0);
			    FadeTransition line1Fade = new FadeTransition(Duration.millis(1500), line1);
			    line1Fade.setFromValue(0.0);
			    line1Fade.setToValue(1.0);
			    ScaleTransition line2Anim = new ScaleTransition(Duration.millis(1500), line2);
			    line2Anim.setFromY(0);
			    line2Anim.setByY(1.0);
			    FadeTransition line2Fade = new FadeTransition(Duration.millis(1500), line2);
			    line2Fade.setFromValue(0.0);
			    line2Fade.setToValue(1.0);
			    ScaleTransition line3Anim = new ScaleTransition(Duration.millis(1500), line3);
			    line3Anim.setFromX(0);
			    line3Anim.setByX(1.0);
			    FadeTransition line3Fade = new FadeTransition(Duration.millis(1500), line3);
			    line3Fade.setFromValue(0.0);
			    line3Fade.setToValue(1.0);
			    ScaleTransition line4Anim = new ScaleTransition(Duration.millis(1500), line4);
			    line4Anim.setFromX(0);
			    line4Anim.setByX(1.0);
			    FadeTransition line4Fade = new FadeTransition(Duration.millis(1500), line4);
			    line4Fade.setFromValue(0.0);
			    line4Fade.setToValue(1.0);
				
			    ParallelTransition lineAnimations = new ParallelTransition(
			    		line1Anim, line2Anim, line3Anim, line4Anim, 
			    		line1Fade, line2Fade, line3Fade, line4Fade);
			    lineAnimations.setDelay(Duration.millis(1500));
			    lineAnimations.play();
			}
	}
	
	/**
	 * Sets and highlights the working board
	 */
	private void setWorkingBoard()
	{
		// First re-enable all buttons and reset grids to default color
		for(int boardNum = 0; boardNum < NUM_BOARDS; boardNum++)
		{
			for(Button b : boardButtons[boardNum])
				b.setDisable(false);
			for(Rectangle rect : smallGrids[boardNum])
				rect.setFill(SMALL_LINE_COLOR);
		}
		
		int workingBoard = game.getWorkingBoard();
		// Dont highlight any grids if there is no specific working 
		// board defined
		if(workingBoard == -1)
			return;
		
		// Then Disable any cell (aka buttons) not associated with the
		// working board
		for(int boardNum = 0; boardNum < NUM_BOARDS; boardNum++)
			if(boardNum == workingBoard)
				continue;
			else
				for(Button b : boardButtons[boardNum])
					b.setDisable(true);

		// Then highlight the working board's grid in the color of
		// the current player
		for(Rectangle rect : smallGrids[workingBoard])
			switch(game.getPlayerTurn())
			{
				case 1: 
					rect.setFill(P1_COLOR);
					break;
				case 2:
					rect.setFill(P2_COLOR);
					break;
				default:
					break;
			}
		
	}
	
	/**
	 * Replaces a given small board with a large text displaying
	 * the given player's marker
	 * @param claimedBoard the gridpane of the claimed board
	 * @param boardNum the numerical index of the claimed board (0-9)
	 * @param marker the victory marker (1 if claimed by p1, 2 by p2, or -1
	 * 		  if it's a tie board)
	 */
	private void replaceGrid(GridPane claimedBoard, int boardNum, int marker) 
	{
		// If the grid is a tie grid, make it all gray
		if(marker == GenericBoard.TIE_MARKER)
		{
			// Turn all button texts (X's and O's) gray
			for(Button b : boardButtons[boardNum])
				b.setTextFill(Color.GRAY);
			// Turn grid lines gray
			for(Rectangle r : smallGrids[boardNum])
				r.setFill(Color.GRAY);
			
			return;
		}
		
		// When removing a node from grid pane, grid pane automatically
		// updates the succeeding nodes' indices so the removing the given
		// board may actually remove a different board accidentally. To fix
		// this, fill the gap in the grid pane with a dummy node to preserve
		// the indices of the other boards
		claimedBoard.getChildren().remove(boardNum);
		Text dummy = new Text();
		claimedBoard.getChildren().add(boardNum, dummy);
		
		
		int row = boardNum / GenericBoard.BOARD_SIZE;
		int col = boardNum % GenericBoard.BOARD_SIZE;
		
		Label markerLabel = new Label();
		markerLabel.setPrefSize(SMALL_BOARD_SIZE, SMALL_BOARD_SIZE);
		markerLabel.setStyle("-fx-font: 48 arial;");
		markerLabel.setAlignment(Pos.CENTER);
		markerLabel.setEffect(blur);
		if(marker == GenericBoard.P1_MARKER)
		{
			markerLabel.setText("X");
			markerLabel.setTextFill(P1_COLOR);
		}
		else
		{
			markerLabel.setText("O");
			markerLabel.setTextFill(P2_COLOR);
		}

		claimedBoard.add(markerLabel, col, row);
		
		// Remove the grid of that board
		for(Rectangle r : smallGrids[boardNum])
			r.setVisible(false);
	}
	
	/**
	 * Blurs the background
	 * Precondition: ONLY called when gameState is not main menu
	 */
	private void blurBackground()
	{
		// First make the background untouchable by disabling all 
		// cells
		for(int boardNum = 0; boardNum < NUM_BOARDS; boardNum++)
			for(Button b : boardButtons[boardNum])
				b.setDisable(true);
			
		// Set up the animation
		Timeline animation = new Timeline();
		KeyValue kv = new KeyValue(blur.radiusProperty(), 20.0);
		KeyFrame kf = new KeyFrame(Duration.millis(2000), kv);
		animation.getKeyFrames().add(kf);
		animation.setDelay(Duration.millis(500));
		
		// Play
		animation.play();
	}
	
	/**
	 * Displays the victoy screen
	 * @param victoriousPlayer the player number of the victorious
	 * player
	 */
	private void displayVictory(int victoriousPlayer)
	{
		Pane mainPane = (Pane) gameState.getRoot();
		
		// First blur the background
		blurBackground();
		
		// Display (and animate) the appropriate texts
		Text player = new Text("PLAYER " + victoriousPlayer);
		player.setFill(Color.WHITE);
		player.setFont(new Font(MAIN_FONT, 50));
		Text wins = new Text(" WINS");
		if(victoriousPlayer == 1)
		{
			wins.setFill(P1_COLOR);
			playerBarTop.setFill(P1_COLOR);
			playerBarBottom.setFill(P1_COLOR);
		}
		else
		{
			wins.setFill(P2_COLOR);
			playerBarTop.setFill(P2_COLOR);
			playerBarBottom.setFill(P2_COLOR);
		}
		wins.setFont(new Font(MAIN_FONT, 50));
		
		TextFlow message = new TextFlow(player, wins);
		message.setEffect(new Glow(0.5));
		message.setTextAlignment(TextAlignment.CENTER);
		message.relocate(FRAME_WIDTH/2, FRAME_HEIGHT/1.5);
		
		// After a while, display the return to main menu button
		final Text returnLabel = new Text("Return");
		returnLabel.setFill(Color.WHITE);
		returnLabel.setFont(new Font(MAIN_FONT, 25));
		returnLabel.relocate(message.getLayoutX() + 285, 
				message.getLayoutY() + 50);
		returnLabel.setVisible(false);
		returnLabel.setOnMouseEntered(menuButtonAnimation);
		returnLabel.setOnMouseExited(menuButtonAnimation);
		returnLabel.setOnMouseClicked(new EventHandler(){
			public void handle(Event e){
				audio.stopBGM();
				audio.playSoundEffect(GameAudio.SFX.MENU_SELECT);
				toMainMenu();
			}
		});
		
		FadeTransition fade = new FadeTransition(Duration.millis(1500), message);
		fade.setFromValue(0.0);
		fade.setToValue(1.0);
		fade.play();
		fade.setOnFinished(new EventHandler(){
			public void handle(Event e) 
				{returnLabel.setVisible(true);}
		});
		
		mainPane.getChildren().addAll(message, returnLabel);
	}
	
	/**
	 * Displays the tie result screen
	 */
	private void displayTie()
	{
		Pane mainPane = (Pane) gameState.getRoot();
		
		// First blur the background
		blurBackground();
		
		Text tie = new Text("TIE");
		tie.setFill(Color.rgb(81, 255, 0));
		playerBarTop.setFill(Color.rgb(81, 255, 0));
		playerBarBottom.setFill(Color.rgb(81, 255, 0));
		tie.setEffect(new Glow(0.5));
		tie.setFont(new Font(MAIN_FONT, 50));
		tie.relocate(FRAME_WIDTH/2 + 300, FRAME_HEIGHT/1.5);
		
		final Text returnLabel = new Text("Return");
		returnLabel.setFill(Color.WHITE);
		returnLabel.setFont(new Font(MAIN_FONT, 25));
		returnLabel.relocate(tie.getLayoutX() - 10, 
				tie.getLayoutY() + 20);
		returnLabel.setVisible(false);
		returnLabel.setOnMouseEntered(menuButtonAnimation);
		returnLabel.setOnMouseExited(menuButtonAnimation);
		returnLabel.setOnMouseClicked(new EventHandler(){
			public void handle(Event e){
				audio.stopBGM();
				audio.playSoundEffect(GameAudio.SFX.MENU_SELECT);
				toMainMenu();
			}
		});
		
		FadeTransition fade = new FadeTransition(Duration.millis(1500), tie);
		fade.setFromValue(0.0);
		fade.setToValue(1.0);
		fade.play();
		fade.setOnFinished(new EventHandler(){
			public void handle(Event e) 
				{returnLabel.setVisible(true);}
		});
		
		mainPane.getChildren().addAll(tie, returnLabel);
	}
	
	/**
	 * Moves the player to the game screen
	 * @param mode the game mode (either Untimed or Frenzy)
	 */
	private void toGame(final Mode mode)
	{
		FadeTransition fadeOut = new FadeTransition(Duration.millis(1000), gameState.getRoot());
		fadeOut.setFromValue(1.0);
		fadeOut.setToValue(0.0);
		fadeOut.play();
		fadeOut.setOnFinished(new EventHandler(){
			public void handle(Event e){
				gameMode = mode; 
				gameState = buildGameScene();
				window.setScene(gameState);
			}
		});
	}	
	
	/**
	 * Moves the player to the main menu
	 */
	private void toMainMenu()
	{
		gameMode = Mode.MENU;
		FadeTransition fadeOut = new FadeTransition(Duration.millis(1000), gameState.getRoot());
		fadeOut.setFromValue(1.0);
		fadeOut.setToValue(0.0);
		fadeOut.play();
		fadeOut.setOnFinished(new EventHandler(){
			public void handle(Event e){
				gameState = buildMainMenuScene();
				window.setScene(gameState);
			}
		});
	}
	
	public EventHandler menuButtonAnimation = new EventHandler(){
		// event handler for menu option animations
		public void handle(Event e){				
			String event = e.getEventType().toString();
			Text menuSelection = (Text) e.getSource(); 
			switch(event)
			{
				case "MOUSE_ENTERED":
					menuSelection.setEffect(new Glow(0.7));
					audio.playSoundEffect(GameAudio.SFX.MENU_HOVER);
					break;
				case "MOUSE_EXITED":
					menuSelection.setEffect(null);
					break;
				default:
					break;
			}
		}
	};
	public EventHandler menuButtonLogic = new EventHandler(){
		// event handler for menu option logic
		public void handle(Event e) {
			String event = e.getEventType().toString();
			Text menuSelection = (Text) e.getSource();
			if(event.equals("MOUSE_CLICKED"))
			{
				audio.stopBGM();
				audio.playSoundEffect(GameAudio.SFX.MENU_SELECT);
				switch(menuSelection.getText())
				{
					case "Untimed":
						game = new BigBoard();
						toGame(Mode.UNTIMED);
						break;
					case "Frenzy":
						game = new BigBoard();
						toGame(Mode.FRENZY);
						break;
					case "Quit":
						window.close();
						break;
					default:
						break;
				}
			}
			
		}
		
	};
	public EventHandler cellAnimation = new EventHandler(){
		// event handler for game cell animations
		public void handle(Event e) {
			String event = e.getEventType().toString();
			Button object = (Button) e.getSource();
			switch(event)
			{
				case "MOUSE_ENTERED":
					object.setBackground(CELL_BG);
					switch(game.getPlayerTurn())
					{
						// Display a transparent version of the player's marker 
						// while a player hovers over a cell 
						case GenericBoard.P1_MARKER:
							object.setText("X");
							object.setTextFill(P1_COLOR.desaturate().desaturate());
							break;
						case GenericBoard.P2_MARKER:
							object.setText("O");
							object.setTextFill(P2_COLOR.desaturate().desaturate());
							break;
					}
					break;
				case "MOUSE_EXITED":
					object.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,
							CornerRadii.EMPTY, Insets.EMPTY)));
					// Take away transparent marker upon exit  
					object.setText("");
					
					break;
				default:
					break;
			}
		}
	};
	public EventHandler cellLogic = new EventHandler(){
		// event handler for game cell logic
		public void handle(Event e) {
			Button object = (Button) e.getSource();
			
			// Get object's index in the boarButtons array
			int boardNum = -1, cellNum = -1;
			for(int b = 0; b < NUM_BOARDS; b++)
				for(int c = 0; c < NUM_CELLS_PER_BOARD; c++)
					if(object == boardButtons[b][c])
					{
						boardNum = b;
						cellNum = c;
						// Break out of nested for loop
						b = NUM_BOARDS;
						break;
					}
			
			// Send GUI data to the logic of the game and update
			// GUI accordingly
			if(boardNum != -1 && cellNum != -1)
			{
				if(gameMode == Mode.FRENZY)
					playerBarAnim.stop();
				
				int boardVictoryState = game.makeMove(boardNum, cellNum);
				switch(boardVictoryState)
				{
					case GenericBoard.NEUTRAL_MARKER:
						switch(game.getPlayerTurn())
						{
							case 1:
								object.setText("O");
								object.setTextFill(P2_COLOR);
								playerBarTop.setFill(P1_COLOR);
								playerBarBottom.setFill(P1_COLOR);
								audio.playSoundEffect(GameAudio.SFX.P1_PING);
								break;
							case 2:	
								object.setText("X");
								object.setTextFill(P1_COLOR);
								playerBarTop.setFill(P2_COLOR);
								playerBarBottom.setFill(P2_COLOR);
								audio.playSoundEffect(GameAudio.SFX.P2_PING);
								break;
						}
						break;
					// Remove this board from the main board grid pane
					// And replace it with a larger label of the player
					// who claimed this board
					case GenericBoard.P1_MARKER:
						GridPane p1ClaimedBoard = ((GridPane) object.getParent().getParent());
						replaceGrid(p1ClaimedBoard, boardNum, GenericBoard.P1_MARKER);
						playerBarTop.setFill(P2_COLOR);
						playerBarBottom.setFill(P2_COLOR);
						audio.playSoundEffect(GameAudio.SFX.P1_CLAIM);
						break;
					case GenericBoard.P2_MARKER:
						GridPane p2ClaimedBoard = ((GridPane) object.getParent().getParent()); 
						replaceGrid(p2ClaimedBoard, boardNum, GenericBoard.P2_MARKER);
						playerBarTop.setFill(P1_COLOR);
						playerBarBottom.setFill(P1_COLOR);
						audio.playSoundEffect(GameAudio.SFX.P2_CLAIM);
						break;
					case GenericBoard.TIE_MARKER:
						switch(game.getPlayerTurn())
						{
							case 1:
								object.setText("O");
								object.setTextFill(P2_COLOR);
								playerBarTop.setFill(P1_COLOR);
								playerBarBottom.setFill(P1_COLOR);
								audio.playSoundEffect(GameAudio.SFX.P1_PING);
								break;
							case 2:	
								object.setText("X");
								object.setTextFill(P1_COLOR);
								playerBarTop.setFill(P2_COLOR);
								playerBarBottom.setFill(P2_COLOR);
								audio.playSoundEffect(GameAudio.SFX.P2_PING);
								break;
						}
						GridPane tieBoard = ((GridPane) object.getParent().getParent());
						replaceGrid(tieBoard, boardNum, GenericBoard.TIE_MARKER);
						audio.playSoundEffect(GameAudio.SFX.NULL_CLAIM);
						break;
					default:
						break;
				}
				// Resume the bar timers
				if(gameMode == Mode.FRENZY)
					playerBarAnim.play();
				
				// Update the working board
				setWorkingBoard();
				
				// This cell can no longer be interacted with (players can no longer
				// put a maerker here because it's now occupied) so remove its 
				// click event handlers and hover animations
				object.setOnMouseClicked(null);
				object.setOnMouseEntered(null);
				object.setOnMouseExited(null);
				object.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,
						CornerRadii.EMPTY, Insets.EMPTY)));

				// Lastly check if the victory status of the board has changed from 
				// neutral (meaning either players have won with this move or it's 
				// a tie game)
				if(game.getVictory() != 0)
				{
					playerBarAnim.stop();
					audio.stopBGM();
					switch(game.getVictory())
					{
						case GenericBoard.P1_MARKER:
							audio.playSoundEffect(GameAudio.SFX.VICTORY_FANFARE);
							displayVictory(GenericBoard.P1_MARKER);
							break;
						case GenericBoard.P2_MARKER:
							audio.playSoundEffect(GameAudio.SFX.VICTORY_FANFARE);
							displayVictory(GenericBoard.P2_MARKER);
							break;
						case GenericBoard.TIE_MARKER:
							audio.playSoundEffect(GameAudio.SFX.DRAW_FANFARE);
							displayTie();
							break;
						default: 
							break;
					}
				}
			}
		}
	};
	
	
	
	
	
}
