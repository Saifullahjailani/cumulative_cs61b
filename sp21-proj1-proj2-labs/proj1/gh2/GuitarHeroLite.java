package gh2;

import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

/**
 * A client that uses the synthesizer package to replicate a plucked guitar string sound
 */
public class GuitarHeroLite {
    public static final double CONCERT_A = 440.0;
    public static final double CONCERT_C = CONCERT_A * Math.pow(2, 3.0 / 12.0);
    private static final String KEYBOARD = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

    public static void main(String[] args) {
        /* create two guitar strings, for concert A and C */
        GuitarHeroLite g = new GuitarHeroLite();
        GuitarString[] notes = g.notes();
        while (true) {
            double sample = 0;
            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                for (int i = 0; i < KEYBOARD.length(); i++) {
                    char c = KEYBOARD.charAt(i);
                    if (c == key) {
                        notes[i].pluck();
                    }
                }


            }
            double sum = 0;
            for (GuitarString a : notes) {
                sum += a.sample();
                a.tic();
            }

            StdAudio.play(sum);
        }
    }


    public GuitarString[] notes() {
        GuitarString[] notes = new GuitarString[37];
        for (int i = 0; i < KEYBOARD.length(); i++) {
            int index = KEYBOARD.indexOf(KEYBOARD.charAt(i)) + 1;
            double note = CONCERT_A * Math.pow(2, (index - 24) / 12);
            notes[i] = new GuitarString(note);
        }
        return notes;
    }
}

