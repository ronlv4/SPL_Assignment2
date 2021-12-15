package bgu.spl.mics.application.InputParsing;

import bgu.spl.mics.application.objects.Model;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ModelDeserializer implements JsonDeserializer<Model> {

    @Override
    public Model deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject modelAsJsonObject = jsonElement.getAsJsonObject();
        return new Model(modelAsJsonObject.get("name").getAsString(), modelAsJsonObject.get("type").getAsString(), modelAsJsonObject.get("size").getAsInt());
    }
}