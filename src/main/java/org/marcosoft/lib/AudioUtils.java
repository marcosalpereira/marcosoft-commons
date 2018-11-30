package org.marcosoft.lib;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioUtils {

//    public static void play(InputStream stream) {
//        try {
//            if (stream == null) {
//                System.out.println("Play what???!!");
//            } else {
//                playClip(stream);
//            }
//
//        } catch (final IOException e) {
//            e.printStackTrace();
//        } catch (final UnsupportedAudioFileException e) {
//            e.printStackTrace();
//        } catch (final LineUnavailableException e) {
//            e.printStackTrace();
//        } catch (final InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    public static void playClip(InputStream stream) throws IOException,
            UnsupportedAudioFileException, LineUnavailableException, InterruptedException {

        class AudioListener implements LineListener {
            private boolean done = false;

            public synchronized void update(LineEvent event) {
                final Type eventType = event.getType();
                if (eventType == Type.STOP || eventType == Type.CLOSE) {
                    done = true;
                    notifyAll();
                }
            }

            public synchronized void waitUntilDone() throws InterruptedException {
                while (!done) {
                    wait();
                }
            }
        }

        final AudioListener listener = new AudioListener();
        final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(stream);
        try {
            final Clip clip = AudioSystem.getClip();
            clip.addLineListener(listener);
            clip.open(audioInputStream);
            try {
                clip.start();
                listener.waitUntilDone();
            } finally {
                clip.close();
            }
        } finally {
            audioInputStream.close();
        }
    }

    public static void main(String[] args) throws Exception {
        final ByteArrayInputStream stream = new ByteArrayInputStream(IOUtils.readContent(
                new FileInputStream("/usr/share/sounds/purple/alert.wav")));
        playClip(stream);
        stream.reset();
        playClip(stream);

    }

}
