package bgu.spl.mics.application;

import bgu.spl.mics.application.services.InputFile;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.FileNotFoundException;

/**
 * This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */

public class CRMSRunner {

    private static InputFile parseInputFile(String jsonPath) throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(jsonPath));
        return gson.fromJson(reader, InputFile.class);
    }

    public static void main(String[] args) {
        InputFile inputJson;
        try {
            inputJson = parseInputFile(args[0]);
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist");
        }




    }
}
