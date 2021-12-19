package bgu.spl.mics.application.OutputWriting;

import bgu.spl.mics.application.objects.Model;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class ModelSerializer implements JsonSerializer<Model> {
    public JsonElement serialize(Model model, Type type, JsonSerializationContext context) {
        JsonElement val = context.serialize(model).getAsJsonObject();
//        val.getAsJsonObject().entrySet();
        return new JsonPrimitive(model.getName());
    }
}