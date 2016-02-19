package com.ekuaizhi.library.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by livvym on 15-12-23.
 *
 */
public class BaseModel {

    private boolean result;
    private String message;


    public BaseModel(JSONObject jsonObject) throws JSONException {
        result = jsonObject.getBoolean("result");
        message = jsonObject.getString("message");
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
