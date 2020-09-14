package me.niotgg.configuration;


import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonFileManager {


    File file;

    public JsonFileManager() {

        File theDir = new File("C:\\Program Files\\OCRLenguages\\");


        if (!theDir.exists()) {
            try{
                theDir.mkdir();
            }
            catch(SecurityException se){
                se.printStackTrace();
            }
        }


        File file = new File("C:\\Program Files\\OCRLenguages\\config.json");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }


            PrintWriter printWriter = null;
            try {
                printWriter = new PrintWriter(new FileWriter(file));
            } catch (IOException e) {
                e.printStackTrace();
            }




            printWriter.print("{ \"input\": \"\", \"output\": \"\" }");

            printWriter.flush();
            printWriter.close();
        }




        this.file = file;
    }

    public void setConfig(Config config) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("input", config.getInput());
        jsonObject.put("output", config.getOutput());

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        printWriter.print(jsonObject.toString());

        printWriter.flush();
        printWriter.close();
    }

    public Config getConfig() {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        JSONTokener jsonTokener = new JSONTokener(stream);
        JSONObject jsonObject = new JSONObject(jsonTokener);
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Config(jsonObject.getString("input"), jsonObject.getString("output"));
    }

}
