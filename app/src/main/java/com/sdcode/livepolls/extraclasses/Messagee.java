package com.sdcode.livepolls.extraclasses;

import android.content.Context;
import android.widget.Toast;

public class Messagee {
    public static void message(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}