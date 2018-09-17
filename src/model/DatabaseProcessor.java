package model;

import java.io.*;

public class DatabaseProcessor {

    private String pathToDB;

    public DatabaseProcessor(String path) {
        pathToDB = path;
    }

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
                    String pathToFile = file.getPath().substring(0, file.getPath().length() - 4);
                    //-35dB seems to work on MOST files, but a few (zkon) don't work
                    String trimCommand = "ffmpeg -y -i " + file.getPath() + " -af silenceremove=1:0:-35dB " + pathToFile + "_TRIM.wav";
                    this.trimAudio(trimCommand);

                    String fileTrim = file.getName().replaceAll("[^A-Za-z]", "");
                    //Get just name from file name
                    String finalName = fileTrim.substring(2, fileTrim.length() - 3);
                    //Create directory of name
                    boolean resultMkdir = new File(pathToDB + "/" + finalName).mkdir();
                    //Create File object for trimmed audio file
                    File trimFile = new File(file.getPath().substring(0, file.getPath().length() - 4) + "_TRIM.wav");
                    //Move trimmed audio file into it's directory & move old file into hidden /.uncut folder
                    boolean resultMove = trimFile.renameTo(new File(pathToDB + "/" + finalName + "/" + trimFile.getName()));
                    file.renameTo(new File(pathToDB + "/uncut_files/" + file.getName()));
                    System.out.println(finalName + " created: " + resultMkdir + " moved: " + resultMove);
                }
            }
        }
    }

    /**
     * Code to trim audio's silence via bash ffmpeg command
     * @param command bash command to be run
     */
    private void trimAudio(String command) {
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
            System.out.println("Error trimming audio");
        }
    }

    //TODO: trim silence and normalise volume and then add to tree view in database menu.
    //TODO: fix onJonothan
    //ffmpeg -y -i path/to/in.wav -af silenceremove=1:0:-35dB path/to/out.wav for removing silence
}
