/**
 * @author Tahmid Khan
 * Holds the game audio and sound effect methods 
 */

package ultimate_tic_tac_toe.GUI;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import ultimate_tic_tac_toe.GUI.GameRunner.Mode;

public class GameAudio 
{
	// Sound effect constant enumeration
	public static enum SFX {MENU_HOVER, MENU_SELECT, P1_PING, P2_PING, P1_CLAIM, 
		P2_CLAIM, NULL_CLAIM, VICTORY_FANFARE, DRAW_FANFARE, COUNTDOWN};
	
	public Clip bgm;
	
	public GameAudio()
	{
		bgm = null;
	}
	
	/**
	 * Stops the current background music
	 */
	public void stopBGM()
	{
		if(bgm.isActive())
			bgm.stop();
	}
	
	/**
	 * Plays the appropriate BGM for the given game mode
	 * @param gameMode, the state of the game right now (in untimed game, frenzy game, or in menu)
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 * @throws LineUnavailableException 
	 */
	public void beginBGM(Mode gameMode) throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		// First select the appropriate file
		InputStream inputStream = null;
		if(gameMode == Mode.UNTIMED)
			inputStream = getClass().getClassLoader().getResourceAsStream("resources/bgm/untimedBGM.wav");
		else if(gameMode == Mode.FRENZY)
			inputStream = getClass().getClassLoader().getResourceAsStream("resources/bgm/frenzyBGM.wav");
		else if(gameMode == Mode.MENU)
			inputStream = getClass().getClassLoader().getResourceAsStream("resources/bgm/menuBGM.wav");
		else
			return;
		
		// Prepare player and loop the selected BGM
	    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);
	    bgm = AudioSystem.getClip();
	    bgm.open(audioInputStream);
	    bgm.loop(Clip.LOOP_CONTINUOUSLY);
	}

	/**
	 * Plays the give sound effect
	 * @param sfx the sound to play
	 */
	public void playSoundEffect(SFX sfx)
	{
		// Initialize the appropriate sound file
		InputStream in = null;
		switch(sfx)
		{
			case MENU_HOVER:
				in = getClass().getClassLoader().getResourceAsStream("resources/sfx/menuHover.wav");
				break;
			case MENU_SELECT:
				in = getClass().getClassLoader().getResourceAsStream("resources/sfx/menuSelect.wav");
				break;
			case P1_PING:
				in = getClass().getClassLoader().getResourceAsStream("resources/sfx/P1SE.wav");
				break;
			case P2_PING:
				in = getClass().getClassLoader().getResourceAsStream("resources/sfx/P2SE.wav");
				break;
			case P1_CLAIM:
				in = getClass().getClassLoader().getResourceAsStream("resources/sfx/P1Claim.wav");
				break;
			case P2_CLAIM:
				in = getClass().getClassLoader().getResourceAsStream("resources/sfx/P2Claim.wav");
				break;
			case NULL_CLAIM:
				in = getClass().getClassLoader().getResourceAsStream("resources/sfx/nullClaim.wav");
				break;
			case VICTORY_FANFARE:
				in = getClass().getClassLoader().getResourceAsStream("resources/sfx/victoryFanfare.wav");
				break;
			case DRAW_FANFARE:
				in = getClass().getClassLoader().getResourceAsStream("resources/sfx/tieFanfare.wav");
				break;
			case COUNTDOWN:
				in = getClass().getClassLoader().getResourceAsStream("resources/sfx/countdown.wav");
				break;
			default:
				return;
		}
		
		// Create an audiostream from the inputstream
		AudioStream audioStream = null;
		try {
			audioStream = new AudioStream(in);
		} catch (IOException e) {
			System.out.println("Error loading sound file");
			return;
		}
			 
		// Play the audio clip with the audioplayer class
		AudioPlayer.player.start(audioStream);
	}
}
