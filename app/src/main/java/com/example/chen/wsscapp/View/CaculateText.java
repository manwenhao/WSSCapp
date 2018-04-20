package com.example.chen.wsscapp.View;

/**
 * Created by xuxuxiao on 2018/4/16.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatTextView;


import com.example.chen.wsscapp.R;


public class CaculateText extends AppCompatTextView {


    private Paint mPaint;
    private int mViewWidth;
    private String[] mString;
    private String ellipsize = "...";
    private String mText;
    private int mMaxLine = 2;
    private int mCurrentLine = 0;
    public CaculateText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LimitLineTextView);

//因为获取maxline属性要sdk = 16及以上版本才支持,所以自定义一个获取最大行数属性，以适配所有机型
        mMaxLine = typedArray.getInt(R.styleable.LimitLineTextView_maxLine, 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint      = getPaint();
        int x = getPaddingLeft();
        int y = (int) (getPaddingTop() - mPaint.ascent());

        if(!TextUtils.isEmpty(mText)){
            mString = mText.split("\n");
            mViewWidth  = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
            for(int  i = 0; i < mString.length; i ++){
                String targetStr = mString[i];

                for(int j = 0; j < mMaxLine; j ++){

                    if(!TextUtils.isEmpty(targetStr)){
                        mCurrentLine = mCurrentLine + 1;
                        if(mCurrentLine == mMaxLine){
                            int secondCharCount = mPaint.breakText(ellipsize + targetStr, true, mViewWidth, null);
                            if(targetStr.length() > secondCharCount - 3){
                                targetStr = targetStr.substring(0, secondCharCount - 3) + ellipsize;
                            }
                            canvas.drawText(targetStr, x, y, mPaint);
                        }else{
                            int charCount = mPaint.breakText(targetStr, true, mViewWidth, null);

                            canvas.drawText(targetStr.substring(0, charCount), x, y, mPaint);
                            y = y + getLineHeight();
                            if(targetStr.length() > charCount){
                                String strLeave = targetStr.substring(charCount, targetStr.length());
                                if(strLeave.length() > 0){
                                    targetStr = strLeave;
                                }
                            }else{
                                targetStr = "";
                            }
                        }
                        if(mCurrentLine == mMaxLine) return;
                    }
                }
            }
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        mText = text.toString();
    }


}