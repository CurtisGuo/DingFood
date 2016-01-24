package com.ding.dingfood.frontend;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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

        String openHours = "";
        int looper = 0;
        setContentView(R.layout.detail_layout);

        looper = MainActivity.looper;
        if(looper == 0){
            looper = MainActivity.geo.jsonDataNo;
        }
        else{
            looper--;
        }
//=======================================================================================NAME
        TextView detailText;
        detailText=(TextView) findViewById(R.id.detail_name);
        detailText.setText(MainActivity.geo.name.get(looper));

        //=======================================================================================PICS

        ImageView detailImage;
        detailImage=(ImageView) findViewById(R.id.detail_image);
        if(MainActivity.geo.image.get(looper).get(0) == null){
            detailImage.setImageResource(R.mipmap.nophoto);
        }
        else{
            detailImage.setImageBitmap(MainActivity.geo.image.get(looper).get(0));
        }

        //=======================================================================================CONTACT

        TextView open;
        open=(TextView) findViewById(R.id.open);
        String isOpen = MainActivity.geo.isOpen.get(looper);
        if(isOpen == "true"){
            open.setText("Open.");
        }
        else if(isOpen == "false"){
            open.setText("Closed.");
        }
        else {
            open.setText("Not Available.");
        }


        //=======================================================================================OPENING HOURS
        TextView detailOpen;
        Log.d("Open no:" ,MainActivity.geo.jsonOpenNo.get(looper).toString() );
        detailOpen=(TextView) findViewById(R.id.detail_open);
        if(MainActivity.geo.jsonOpenNo.get(looper) != 0){
            for(int i =0 ;  i < MainActivity.geo.jsonOpenNo.get(looper) ; i++){
                openHours += (MainActivity.geo.openingHours.get(looper).get(i)+ "\n");
            }
            detailOpen.setText(openHours);
        }
        else{
            detailOpen.setText("Not Available.");
        }


        //detailImage.setBackground(getResources().getDrawable(MainActivity.sendData.src));

        //=======================================================================================CONTACT

        TextView contact;
        contact=(TextView) findViewById(R.id.contactno);
        contact.setText(MainActivity.geo.telephoneNumber.get(looper));

        //=======================================================================================DISTANCE


        TextView distance;
        distance=(TextView) findViewById(R.id.distance);
        distance.setText(MainActivity.geo.distance.get(looper));

        //=======================================================================================DURATION


        TextView duration;
        duration=(TextView) findViewById(R.id.duration);
        duration.setText(MainActivity.geo.duration.get(looper));

    }
    public void goBack(View v)
    {
        finish();
    }
}
