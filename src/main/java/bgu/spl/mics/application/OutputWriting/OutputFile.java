package bgu.spl.mics.application.OutputWriting;

import bgu.spl.mics.application.InputParsing.InputFile;
import bgu.spl.mics.application.objects.CPU;
import bgu.spl.mics.application.objects.ConferenceInformation;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Student;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.Writer;

public class OutputFile {
    Student[] students;
    ConferenceInformation[] conferences;
    int gpuTimeUsed;
    int cpuTimeUsed;
    int batchesProcessed;

    public OutputFile(Student[] students, ConferenceInformation[] conferences, int gpuTimeUsed, int cpuTimeUsed, int batchesProcessed) {
        this.students = students;
        this.conferences = conferences;
        this.gpuTimeUsed = gpuTimeUsed;
        this.cpuTimeUsed = cpuTimeUsed;
        this.batchesProcessed = batchesProcessed;
    }
}
