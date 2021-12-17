package bgu.spl.mics.application.OutputWriting;

import bgu.spl.mics.application.objects.ConferenceInformation;
import bgu.spl.mics.application.objects.Student;

public class OutputFile {
    Student[] students;
    ConferenceInformation[] conferences;
    int gpuTimeUsed;
    int cpuTimeUsed;
    int batchesProcessed;

    public OutputFile(Student[] students, ConferenceInformation[] conferences ,int gpuTimeUsed, int cpuTimeUsed, int batchesProcessed){
        this.students = students;
        this.conferences = conferences;
        this.cpuTimeUsed = cpuTimeUsed;
        this.gpuTimeUsed = gpuTimeUsed;
        this.batchesProcessed = batchesProcessed;
    }

}
