package bgu.spl.mics.application.objects;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU {

    private Model model;
    private Cluster cluster;

    /**
     * Enum representing the type of the GPU.
     */
    enum Type {RTX3090, RTX2080, GTX1080}

    private Type type;

    public GPU(Model model, Cluster cluster, String type){
//        this.model = model;
//        this.cluster = cluster;
//        if (type.equals("RTX3090")){
//            this.type=Type.RTX3090;
//        }
//        else if (type.equals("RTX2080")){
//            this.type = Type.RTX2080;
//        }
//        else if (type.equals("GTX1080")){
//            this.type = Type.GTX1080;
//        }
//        else{
//        }
    }



}
