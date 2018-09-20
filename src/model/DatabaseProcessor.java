package model;

import java.io.*;
import java.util.Objects;

public class DatabaseProcessor {

    private String pathToDB;

    public DatabaseProcessor(String path) {
        pathToDB = path;
    }

    //TODO: Possible functionality to add: if jonothan and Jonothan, ask user if they want to merge?
    /**Iterates through files in database directory and converts to readable name. Creates folders of said names, and places
    audio files into corresponding folder. If 2 of same name found, they go into the same folder. Trims audio at same time,
    overwriting, original file with trimmed one **/
    public void processDB() {
        File dir = new File(pathToDB);
        File[] directoryListing = dir.listFiles();
        //Create folder for untrimmed videos
        new File(pathToDB + "/uncut_files").mkdir();
        if(directoryListing != null) {
            for(File file : directoryListing) {
                if(!file.isDirectory()) {
                    String originalName = file.getPath().substring(0, file.getPath().length() - 4);
                    //-35dB seems to work on MOST files, but a few (zkon) don't work
                    String trimCommand = "ffmpeg -y -i " + file.getPath() + " -af silenceremove=1:0:-35dB " + originalName + "_TRIM.wav";
                    trimAudio(trimCommand);

                    String fileTrim = file.getName().substring(file.getName().lastIndexOf("_") + 1); //Trims down to 'example.wav'
                    String finalName = fileTrim.substring(0, fileTrim.length() - 4); //Trims down to 'example'

                    //Create directory of name
                    boolean resultMkdir = new File(pathToDB + "/" + finalName).mkdir();

                    //Create File object for trimmed audio file
                    File trimFile = new File(file.getPath().substring(0, file.getPath().length() - 4) + "_TRIM.wav");

                    File parentDir = new File(file.getParent() + "/" + finalName);
                    //Move trimmed audio file into it's directory, rename to correct file if there is more than 1 of them
                    if(parentDir.isDirectory() && Objects.requireNonNull(parentDir.list()).length == 0) {
                        boolean resultMove = trimFile.renameTo(new File(pathToDB + "/" + finalName + "/" + finalName));
                    } else {
                        boolean resultMove = trimFile.renameTo(new File(pathToDB + "/" + finalName + "/" + finalName + "(" + Objects.requireNonNull(parentDir.list()).length + ")"));
                    }

                    //Save uncut files into uncut_files folder
                    boolean resultMoveUncut = file.renameTo(new File(pathToDB + "/uncut_files/" + file.getName()));
                }
            }
        }
    }

    /**
     * Code to trim audio's silence via bash ffmpeg command
     * @param command bash command to be run
     */
    public void trimAudio(String command) {
        try {
            ProcessBuilder builderRecord = new ProcessBuilder("/bin/bash", "-c", command);
            Process process = builderRecord.start();

            InputStream stdout = process.getInputStream();
            InputStream stderr = process.getErrorStream();
            BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
            String line;
            while ((line = stdoutBuffered.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
        }
    }

    //TODO: normalise volume
    //TODO: fix onJonothan
    //ffmpeg -y -i path/to/in.wav -af silenceremove=1:0:-35dB path/to/out.wav for removing silence

}



