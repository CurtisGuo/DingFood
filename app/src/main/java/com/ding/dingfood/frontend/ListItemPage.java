package com.ding.dingfood.frontend;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.ding.dingfood.R;

/**
 * Created by user on 2015/12/21.
 */
public class ListItemPage extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.itemlist_layout);
/*
        detailText=(TextView) findViewById(R.id.detail_name);
        detailText.setText(MainActivity2.sendData.Name);
        detailImage=(ImageView) findViewById(R.id.detail_image);
        detailImage.setBackground(getResources().getDrawable(MainActivity2.sendData.src));
*/
    }
    public void goBack(View v)
    {
        finish();
    }
}
