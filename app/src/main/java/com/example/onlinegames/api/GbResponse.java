package com.example.onlinegames.api;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GbResponse {

    @SerializedName("status_code")
    private int statusCode;

    @SerializedName("results")
    private List<GbGame> results;

    public int getStatusCode() {
        return statusCode;
    }

    public List<GbGame> getResults() {
        return results;
    }
}
