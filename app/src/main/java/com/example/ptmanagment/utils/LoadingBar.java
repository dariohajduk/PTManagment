package com.example.ptmanagment.utils;

import android.app.AlertDialog;
import android.content.Context;
import com.example.ptmanagment.R;

public class LoadingBar {
    private AlertDialog dialog;

    public void LoadingBarShow(Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);// if you want user to wait for some process to finish,
        builder.setView(R.layout.progress);
        dialog = builder.create();
        dialog.show();

    }
    public void EndLoadingBar()
    {
        dialog.dismiss();
    }
}
