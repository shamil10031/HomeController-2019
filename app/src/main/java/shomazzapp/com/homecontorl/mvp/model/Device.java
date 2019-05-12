package shomazzapp.com.homecontorl.mvp.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

public class Device {

    @SerializedName("id")
    private int id;
    @SerializedName("item_name")
    private String name;
    @SerializedName("item_description")
    private String description;
    @SerializedName("item_photo")
    private String photoAddress;

    public Device(int id, String name, String description, String photoAddress) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.photoAddress = photoAddress;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPhotoAddress() {
        return photoAddress;
    }

    public static ArrayList<Device> getDevicesFromJson(String json) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Type collectionType = new TypeToken<Collection<Device>>() {
        }.getType();
        return new ArrayList<Device>(gson.fromJson(json, collectionType));
    }

}
