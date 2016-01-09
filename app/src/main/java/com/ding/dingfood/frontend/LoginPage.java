package com.ding.dingfood.frontend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ding.dingfood.MainActivity;
import com.ding.dingfood.R;

/**
 * Created by user on 2015/12/11.
 */
public class LoginPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_layout);

    }
    public void gotoMainActivity(View v)
    {
        startActivity(new Intent(this,MainActivity.class));
    }
}
