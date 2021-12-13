package bgu.spl.mics.application;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.services.*;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.junit.experimental.theories.Theories;
import sun.jvm.hotspot.debugger.cdbg.basic.BasicCDebugInfoDataBase;
import sun.jvm.hotspot.debugger.posix.elf.ELFSectionHeader;

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

    private static void buildStudentServices(InputFile inputJava) {
        int numOfStudents = inputJava.getNumOfStudents();
        Thread[] studentServicesThreads = new Thread[numOfStudents];
        int i = 1;
        for (Student student : inputJava.getStudents()) {
            studentServicesThreads[i] = new Thread(new StudentService("Student Service " + i));
            studentServicesThreads[i++].start();
        }
    }

    private static void buildGPUServices(GPU[] gpus) {
        Thread[] GPUServicesThreads = new Thread[gpus.length];
        int i = 1;
        for (GPU gpu: gpus){
            GPUServicesThreads[i] = new Thread(new GPUService("GPU Service " + i, gpu));
            GPUServicesThreads[i++].start();
        }
    }

    private static void buildCPUServices(CPU[] cpus) {
        Thread[] CPUServicesThreads = new Thread[cpus.length];
        int i = 1;
        for (CPU cpu: cpus){
            CPUServicesThreads[i] = new Thread(new CPUService("GPU Service " + i, cpu));
            CPUServicesThreads[i++].start();
        }
    }

    private static void buildConferenceServices(InputFile inputJava) {
        int numOfConferences = inputJava.getNumOfConferences();
        Thread[] conferencesServicesThreads = new Thread[numOfConferences];
        int i = 1;
        for(ConfrenceInformation conference: inputJava.getConferences()){
            conferencesServicesThreads[i] = new Thread(new ConferenceService("Conference Service " + i, conference));
            conferencesServicesThreads[i].start();
        }
    }

    private static void buildTimeService(InputFile inputJava) {
        Thread timeServiceThread = new Thread(TimeService.getInstance());
        timeServiceThread.start();
    }

    private static GPU[] parseAndConstructGPUS(String[] gpuStrings){
        GPU[] gpus = new GPU[gpuStrings.length];
        for (int i = 0; i < gpuStrings.length; i++){
            GPU.Type type = convertStringTypeToEnum(gpuStrings[i]);
            gpus[i] = new GPU(type);
        }
        return gpus;

    }

    private static GPU.Type convertStringTypeToEnum(String gpuString) {
        if (gpuString.equals("RTX3090"))
            return GPU.Type.RTX3090;
        else if (gpuString.equals("RTX2080"))
            return GPU.Type.RTX2080;
        else
            return GPU.Type.GTX1080;
    }

    private static CPU[] parseAndConstructCPUS(int[] cpuCores) {
        CPU[] cpus = new CPU[cpuCores.length];
        for (int i = 0; i < cpuCores.length; i++){
            cpus[i] = new CPU(cpuCores[i]);
        }
        return cpus;

    }

    public static void main(String[] args) {
        InputFile inputAsJavaObject = null;
        try {
            inputAsJavaObject = parseInputFile(args[0]);
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist");
            return;
        }
        MessageBusImpl messageBus = MessageBusImpl.getInstance();
        Cluster cluster = Cluster.getInstance();
        buildStudentServices(inputAsJavaObject);
        GPU[] gpus = parseAndConstructGPUS(inputAsJavaObject.getGPUS());
        CPU[] cpus = parseAndConstructCPUS(inputAsJavaObject.getCPUS());
        buildGPUServices(gpus);
        buildCPUServices(cpus);
//        updateCluster(gpus,cpus);
        buildConferenceServices(inputAsJavaObject);
        buildTimeService(inputAsJavaObject);
    }

//    private static void updateCluster(GPU[] gpus, CPU[] cpus) {
//        Cluster.setCPUS(cpus);
//        Cluster.setGPUS(gpus);
//    }
}
