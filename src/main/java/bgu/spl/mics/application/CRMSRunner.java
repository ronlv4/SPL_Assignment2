package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.CPU;
import bgu.spl.mics.application.objects.ConfrenceInformation;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Student;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */

class InputFile{
    private Student[] Students;
    private String[] GPUS;
    private int[] CPUS;
    private ConfrenceInformation[] Conferences;
    private int TickTime;
    private int Duration;
}
public class CRMSRunner {
    public static void main(String[] args) {
        InputFile inputJson = null;
        Gson gson = new Gson();
        try{
            JsonReader reader = new JsonReader(new FileReader(args[0]));
            inputJson= gson.fromJson(reader, InputFile.class);
        }
        catch (FileNotFoundException e){
            System.out.println("File does not exist");
        }
    }
}
