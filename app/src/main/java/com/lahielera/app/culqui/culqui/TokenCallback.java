package com.lahielera.app.culqui.culqui;

import org.json.JSONObject;

public interface TokenCallback {
    public void onSuccess(JSONObject token);

    public void onError(Exception error);
}
