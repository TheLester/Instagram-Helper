package com.steelkiwi.exampleinstagramhelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.steelkiwi.instagramhelper.InstagramHelper;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestApplication.getInstagramHelper().showAuthorizeDialog(MainActivity.this, new InstagramHelper.AuthorizeListener() {
                    @Override
                    public void onSuccess(String token) {

                    }

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
    }
}
