package com.example.android.musicapp;

import android.content.Intent;
import android.view.View;

/**
 * Created by Felipe on 24/10/2016.
 */

public class MainScreenClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), MainScreenActivity.class);
        v.getContext().startActivity(intent);
    }
}
