package bgu.spl.mics.application.OutputWriting;

import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Set;


public class StudentSerializer implements JsonSerializer<Student> {
    public JsonElement serialize(Student src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray ja = (JsonArray) context.serialize(src.getModels());
        ja.forEach(ja::remove);
        for (JsonElement je: ja){
        }
        JsonObject jo = new JsonObject();
        jo.add("trainedModels", ja);
        return jo;
    }
}

