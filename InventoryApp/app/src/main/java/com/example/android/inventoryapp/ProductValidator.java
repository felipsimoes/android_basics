package com.example.android.inventoryapp;

import android.widget.EditText;

/**
 * Created by Felipe on 06/11/2016.
 */

public class ProductValidator {
    public boolean isInteger(EditText words) {
        try {
            Integer.parseInt(words.getText().toString());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean isFloat(EditText words) {
        try {
            Float.parseFloat(words.getText().toString());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean isEmpty(EditText words) {
        if (words.getText().toString().trim().length() > 0)
            return false;
        else
            return true;
    }
}
