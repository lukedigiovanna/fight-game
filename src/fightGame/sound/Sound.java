package fightGame.sound;

import javax.sound.sampled.*;

import fightGame.utils.MathUtils;

import java.io.*;

public class Sound {
	
	//any sound lower than MIN_VOLUME should completely mute the sound.
	public static final float MIN_VOLUME_REAL = -50.0f, MAX_VOLUME_REAL = 0f;
	public static final float MIN_VOLUME_SCALED = 0f, MAX_VOLUME_SCALED = 100f;
	
	private File audioFile;
	private Clip clip;
	private FloatControl gainControl;
	private float relativeVolume; //how loudly the sound should be played relative to other sounds
	// this is useful for determining the strength of a sound based on distance
	// is on a scale from 0 to 50, where 50 is a very loud sound
	
	private boolean playing = false;
	
	private boolean mute = false;
	
	/**
	 * Creates a sound from a WAVE file
	 * @param path File path to the .wav file
	 */
	public Sound(String path, float relativeVolume) {
		this(new File(path), relativeVolume);
	}
	
	public Sound(File audioFile, float relativeVolume) {
		this.audioFile = audioFile;
		this.relativeVolume = relativeVolume;
		try {
			AudioInputStream audioInputStream = 
					AudioSystem.getAudioInputStream(
							audioFile
							);
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		} catch (Exception e) {
			
		}
	}
	
	public Sound copy() {
		return new Sound(this.audioFile,this.relativeVolume);
	}
	
	public void play() {
		if (mute)
			return;
		clip.start();
		playing = true;
	}
	
	public void pause() {
		clip.stop();
		playing = false;
	}
	
	public void toggle() {
		if (playing)
			pause();
		else
			play();
	}
	
	private float lastVolume; 
	public void mute() {
		this.mute = true;
		gainControl.setValue(-999999); //just set it to a very low value
	}
	
	public void unmute() {
		this.mute = false;
		setVolume(lastVolume);
	}
	
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	/**
	 * Sets the actual volume that this sound will be played at.
	 * @param value on the scaled range (not the actual range)
	 */
	public void setVolume(float value) {
		if (mute)
			return;
		lastVolume = value;
		value = MathUtils.clip(MIN_VOLUME_SCALED, MAX_VOLUME_SCALED, value);
		float percent = (value-MIN_VOLUME_SCALED)/(MAX_VOLUME_SCALED-MIN_VOLUME_SCALED);
		float realVolume = percent * (MAX_VOLUME_REAL-MIN_VOLUME_REAL)+MIN_VOLUME_REAL;
		gainControl.setValue(realVolume); 
	}
	
	/**
	 * Should be a value from 0 to 1
	 * @param strength
	 */
	public void setStrength(float strength) {
		this.setVolume(strength * this.relativeVolume);
	}
	
	public boolean dead() {
		return clip.getMicrosecondPosition() >= clip.getMicrosecondLength();
	}
}
