package main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public abstract class Launcher {
    
    private static final String CROP_DATA_FILENAME = "crops.dat";
    private static final String FIELD_DATA_FILENAME = "fields.dat";
    
    protected List<Crop> crops;
    protected List<Field> fields;
    
    public Launcher() {
        
        try {
            crops = readCropData(CROP_DATA_FILENAME);
            fields = readFieldData(FIELD_DATA_FILENAME);
        } catch (JsonIOException |
                JsonSyntaxException |
                FileNotFoundException e) {
            e.printStackTrace();
        }
        
    }
    
    /**
     * Reads the crop data from the given file.
     * 
     * @param filename
     * @return
     * @throws JsonIOException
     * @throws JsonSyntaxException
     * @throws FileNotFoundException
     */
    private List<Crop> readCropData(String filename) 
            throws JsonIOException, JsonSyntaxException, FileNotFoundException {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Crop>>(){}.getType();
        return gson.fromJson(new JsonReader(
                new FileReader(filename)), type);
    }
    
    /**
     * Reads the field data from the given file.
     * 
     * @param filename
     * @return
     * @throws JsonIOException
     * @throws JsonSyntaxException
     * @throws FileNotFoundException
     */
    private List<Field> readFieldData(String filename)
            throws JsonIOException, JsonSyntaxException, FileNotFoundException {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Field>>(){}.getType();
        return gson.fromJson(new JsonReader(
                new FileReader(filename)), type);
    }
    
}
