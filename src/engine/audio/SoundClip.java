package engine.audio;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Esta clase representa un sónido, o un clip de audio
 * para poder ser reproducido dentro de una aplicación.
 *
 * @class: SoundClip.
 * @autor: Sergio Martí Torregrosa. sMartiTo
 * @version: 0.0.01 pre-alpha.
 * @date: 2020-07-06
 */
public class SoundClip {

    private Clip clip = null;

    private FloatControl gainControl;

    /**
     * El constructor de la clase SoundClip.
     *
     * @param path Es la ruta absoluta o relativa de la cual se extrae el clip de audio.
     */
    public SoundClip(String path) {
        try {
            InputStream audioSrc = SoundClip.class.getResourceAsStream(path);
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedIn);
            AudioFormat baseFormat = ais.getFormat();
            AudioFormat decodeFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false
            );
            AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
            clip = AudioSystem.getClip();
            clip.open(dais);
            gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch ( UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Este método permite que el clip se reproduzca una vez.
     */
    public void play() {
        if ( clip == null ) {
            return;
        }
        stop();
        clip.setFramePosition(0);
        while ( !clip.isRunning() ) {
            clip.start();
        }
    }

    /**
     * Este método hace que el clip de sonido pare.
     */
    public void stop() {
        if ( clip.isRunning() ) {
            clip.stop();
        }
    }

    /**
     * Este método cierra el clip de sonido. Previamente,
     * llama al método <method>stop</method> de esta misma clase
     * por si el clip se esta reproduciendo.
     */
    public void close() {
        stop();
        clip.drain();
        clip.close();
    }

    /**
     * Este método permite que el clip de sonido se repita.
     */
    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        play();
    }

    /**
     * Permite cambiar el volumen al cual se reproduce el clip.
     */
    public void setVolume(float value) {
        gainControl.setValue(value);
    }

    /**
     * Si el clip esta reproduciendose o no.
     */
    public boolean isRunning() {
        return clip.isRunning();
    }

}
