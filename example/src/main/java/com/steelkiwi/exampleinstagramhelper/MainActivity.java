package com.steelkiwi.exampleinstagramhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.steelkiwi.instagramhelper.InstagramHelper;
import com.steelkiwi.instagramhelper.InstagramHelperConstants;
import com.steelkiwi.instagramhelper.model.InstagramUser;

public class MainActivity extends Activity {
    private InstagramHelper instagramHelper;
    private LinearLayout    userInfoPanel;
    private ImageView       userPhoto;
    private TextView        userTextInfo;
    private Button          loginBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        instagramHelper = TestApplication.getInstagramHelper();

        userInfoPanel = (LinearLayout) findViewById(R.id.user_instagram_info);
        userPhoto = (ImageView) findViewById(R.id.user_photo);
        userTextInfo = (TextView) findViewById(R.id.user_text_info);
        loginBtn = (Button) findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instagramHelper.loginFromActivity(MainActivity.this);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == InstagramHelperConstants.INSTA_LOGIN && resultCode == RESULT_OK) {
            InstagramUser user = instagramHelper.getInstagramUser(this);
            loginBtn.setVisibility(View.GONE);
            userInfoPanel.setVisibility(View.VISIBLE);
            Picasso.with(this).load(user.getData().getProfilePicture()).into(userPhoto);
            userTextInfo.setText(user.getData().getUsername() + "\n"
                            + user.getData().getFullName() + "\n"
                            + user.getData().getWebsite()
            );

        } else {
            Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();
        }
    }
}
