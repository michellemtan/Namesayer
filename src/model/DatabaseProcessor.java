package model;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DatabaseProcessor {

    private String pathToDB;

    public DatabaseProcessor(String path) {
        pathToDB = path;
    }

    /**Iterates through files in database directory and converts to readable name. Creates folders of said names, and places
     audio files into corresponding folder. If 2 of same name found, they go into the same folder. Trims audio at same time,
     and places original uncut files in uncut_files folder.**/
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

                    //If finalName already has a similar folder (e.g finalName = Jonothan, and jonothan exists), make same name
                    List<File> fileList = Arrays.asList(dir.listFiles());
                    List<String> fileNames = new ArrayList<>();
                    for(File file1 : fileList) {
                        fileNames.add(file1.getName()); //Create list of string of existing files
                    }
                    //Make first letter capitilised.
                    String upcased = finalName.substring(0, 1).toUpperCase() + finalName.substring(1);
                    if(fileNames.contains(finalName.toLowerCase()) || fileNames.contains(upcased)) {
                        //This code runs if name being processed already has a similar folder
                        if(fileNames.contains(upcased)) {
                            File parentDir = new File(file.getParent() + "/" + upcased);
                            //Create File object for trimmed audio file
                            File trimFile = new File(file.getPath().substring(0, file.getPath().length() - 4) + "_TRIM.wav");
                            trimFile.renameTo(new File(pathToDB + "/" + upcased + "/" + upcased + "(" + Objects.requireNonNull(parentDir.listFiles()).length + ").wav"));
                            //Save uncut files into uncut_files folder
                            boolean resultMoveUncut = file.renameTo(new File(pathToDB + "/uncut_files/" + file.getName()));
                        }
                    } else {
                        //Create directory of name
                        boolean resultMkdir = new File(pathToDB + "/" + upcased).mkdir();

                        //Create File object for trimmed audio file
                        File trimFile = new File(file.getPath().substring(0, file.getPath().length() - 4) + "_TRIM.wav");

                        File parentDir = new File(file.getParent() + "/" + upcased);
                        //Move trimmed audio file into it's directory, rename to correct file if there is more than 1 of them
                        if(parentDir.isDirectory() && Objects.requireNonNull(parentDir.list()).length == 0) {
                            boolean resultMove = trimFile.renameTo(new File(pathToDB + "/" + upcased + "/" + upcased + ".wav"));
                        } else {
                            boolean resultMove = trimFile.renameTo(new File(pathToDB + "/" + upcased + "/" + upcased + "(" + Objects.requireNonNull(parentDir.list()).length + ").wav"));
                        }

                        //Save uncut files into uncut_files folder
                        boolean resultMoveUncut = file.renameTo(new File(pathToDB + "/uncut_files/" + file.getName()));
                    }
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
    //ffmpeg -y -i path/to/in.wav -af silenceremove=1:0:-35dB path/to/out.wav for removing silence
}



