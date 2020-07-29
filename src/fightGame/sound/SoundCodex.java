package fightGame.sound;

public class SoundCodex {
	public static Sound SONG = new Sound("res/sounds/fightsong.wav", Sound.MAX_VOLUME_SCALED),
				        JUMP = new Sound("res/sounds/jump.wav", Sound.MAX_VOLUME_SCALED/2),
	                    POWERUP = new Sound("res/sounds/powerup.wav", Sound.MAX_VOLUME_SCALED/2),
	                    HIT = new Sound("res/sounds/hit.wav", Sound.MAX_VOLUME_SCALED/2),
	                    THROW = new Sound("res/sounds/throw.wav", Sound.MAX_VOLUME_SCALED/2);
	
	public static void play(Sound sound) {
		sound.copy().play();
	}
	
	public static void loop(Sound sound) {
		sound.copy().loop();
	}
}
