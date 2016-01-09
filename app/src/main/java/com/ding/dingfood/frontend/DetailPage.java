package com.ding.dingfood.frontend;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ding.dingfood.MainActivity;
import com.ding.dingfood.R;

/**
 * Created by user on 2015/12/12.
 */
public class DetailPage extends Activity {
    private MainActivity.Restaurant dataReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView detailText;
        ImageView detailImage;

        setContentView(R.layout.detail_layout);

        detailText=(TextView) findViewById(R.id.detail_name);
        detailText.setText(MainActivity.sendData.Name);
        detailImage=(ImageView) findViewById(R.id.detail_image);
        detailImage.setBackground(getResources().getDrawable(MainActivity.sendData.src));

    }
    public void goBack(View v)
    {
        finish();
    }
}
