package bgu.spl.mics.application.OutputWriting;

import bgu.spl.mics.application.InputParsing.InputFile;
import bgu.spl.mics.application.objects.CPU;
import bgu.spl.mics.application.objects.ConferenceInformation;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Student;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.io.FileWriter;
import java.io.Writer;

public class OutputFile {
    @Expose Student[] students;
    @Expose ConferenceInformation[] conferences;
    @Expose int gpuTimeUsed;
    @Expose int cpuTimeUsed;
    @Expose int batchesProcessed;

    public OutputFile(Student[] students, ConferenceInformation[] conferences ,int gpuTimeUsed, int cpuTimeUsed, int batchesProcessed){
        this.students = students;
        this.conferences = conferences;
        this.cpuTimeUsed = cpuTimeUsed;
        this.gpuTimeUsed = gpuTimeUsed;
        this.batchesProcessed = batchesProcessed;
    }


}
