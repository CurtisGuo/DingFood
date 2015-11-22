package com.ding.dingfood.frontend.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ding.dingfood.R;

/**
 * Created by Kent on 2015/11/14.
 */
public class DingFoodFragment extends Fragment {

    private ScrollView mScrollView;
    private ImageView shakeResultImageView;
    private TextView shakeResultTextView;
    private int shakeCount = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        View chatView = inflater.inflate(R.layout.fragment_dingfood, container, false);
        mScrollView =  (ScrollView)chatView.findViewById(R.id.shake_scrollView);
        mScrollView.setOnTouchListener(new TouchListenerImpl());
        shakeResultImageView = (ImageView)chatView.findViewById(R.id.shake_result_imageView);
        //shakeResultTextView = (TextView)chatView.findViewById(R.id.shake_result_textView);
        return chatView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }

    private class TouchListenerImpl implements View.OnTouchListener {

        int hitNum = 0;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    hitNum = 0;
                    Log.i("flag", "滑動ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    int scrollY=view.getScrollY();
                    int height=view.getHeight();
                    int scrollViewMeasuredHeight=mScrollView.getChildAt(0).getMeasuredHeight();
                    if(scrollY==0){
                        Log.i("flag", "滑動到了頂端");
                        System.out.println("滑動到了頂端 view.getScrollY()=" + scrollY);
                        hitNum ++;
                        if(hitNum > 30){
                            shakeCount--;
                            if(shakeCount == 2){
                                shakeResultImageView.setImageResource(R.drawable.run2);
                            } else if(shakeCount ==1){
                                shakeResultImageView.setImageResource(R.drawable.run1);
                            }else{
                                shakeResultImageView.setImageResource(R.drawable.run0);
                            }
                            //shakeResultImageView.setImageDrawable(null);
                            //shakeResultTextView.setText("");
                            hitNum = 0;
                        }

                    }
                    if((scrollY+height)==scrollViewMeasuredHeight){
                        System.out.println("滑動到了底部 scrollY="+scrollY);
                        System.out.println("滑動到了底部 height="+height);
                        System.out.println("滑動到了底部 scrollViewMeasuredHeight="+scrollViewMeasuredHeight);
                    }
                    break;

                default:
                    break;
            }
            return false;
        }

    };

}
