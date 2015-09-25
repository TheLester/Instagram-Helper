package com.steelkiwi.instagramhelper;

import android.app.AlertDialog;
import android.content.Context;

public class MaterialDialog extends AlertDialog {
    protected MaterialDialog(Context context) {
        super(context);
    }

    protected MaterialDialog(Context context, int theme) {
        super(context, theme);
    }

    protected MaterialDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void show() {
        super.show();
        setContentView(R.layout.material_progress);
    }
}
