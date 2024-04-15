package p1;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AudioPlayer
{
    public String soundFolder = "src/main/resources" + File.separator;
    public String Congrat = soundFolder + "congrats.wav";      //congrats
    public String nbeeps = soundFolder + "negative_beeps.wav";
    public Clip CongratSound,nbeepsSound;
    public AudioPlayer(){
        try {
            CongratSound = AudioSystem.getClip();
            nbeepsSound = AudioSystem.getClip();

            CongratSound.open(AudioSystem.getAudioInputStream(new File(Congrat).getAbsoluteFile()));

            nbeepsSound.open(AudioSystem.getAudioInputStream(new File(nbeeps).getAbsoluteFile()));

        }
        catch (LineUnavailableException ex){
            Logger.getLogger(AudioPlayer.class.getName()).log(Level.SEVERE,null,ex);
        }
        catch (UnsupportedAudioFileException ex){
            Logger.getLogger(AudioPlayer.class.getName()).log(Level.SEVERE,null,ex);
        }
        catch (IOException ex){
            Logger.getLogger(AudioPlayer.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    public void playcongrat(){
        CongratSound.setFramePosition(0);
        CongratSound.start();
    }
    public void playnbeep(){
        nbeepsSound.setFramePosition(0);
        nbeepsSound.start();
    }
}
