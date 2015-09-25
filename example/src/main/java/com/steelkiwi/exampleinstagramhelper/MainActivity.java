package com.steelkiwi.exampleinstagramhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.steelkiwi.instagramhelper.InstagramHelperConstants;
import com.steelkiwi.instagramhelper.model.InstagramUser;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestApplication.getInstagramHelper().loginFromActivity(MainActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == InstagramHelperConstants.INSTA_LOGIN && resultCode == RESULT_OK) {
            Toast.makeText(this,"OK",Toast.LENGTH_LONG).show();
            InstagramUser user = TestApplication.getInstagramHelper().getInstagramUser(this);
            Log.i("Test", "onActivityResult "+user.getData().getUsername());
        } else {
            Toast.makeText(this,"FAIL",Toast.LENGTH_LONG).show();
        }
    }
}
