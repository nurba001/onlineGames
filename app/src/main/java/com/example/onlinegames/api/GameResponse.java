package com.example.onlinegames.api;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GameResponse {

    @SerializedName("results")
    private List<ApiGame> results;

    public List<ApiGame> getResults() {
        return results;
    }
}