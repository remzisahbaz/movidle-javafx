package com.example.game;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class SoundUtil {
    public static void playFindMovieItemsSound() {
        try {
            String filePath = "./sounds/good.wav";
            File soundFile = new File(filePath).getAbsoluteFile();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    public static void playGotGuessSound() {
        try {
            String filePath = "./sounds/win.wav";
            File soundFile = new File(filePath).getAbsoluteFile();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}

