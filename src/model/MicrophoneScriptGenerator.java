package model;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class MicrophoneScriptGenerator {

    public void makeBashScript() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("myscript.sh");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.println("#!/bin/bash");
        writer.println("cd ~");
        writer.println("ffmpeg -f alsa -i hw:0 -acodec libmp3lame -filter_complex showvolume=b=5:v=0:h=75:w=600[a] -map [a] -window_title \"Microphone Volume\" -f sdl - 2>/dev/null\n");
        writer.close();
    }
}
