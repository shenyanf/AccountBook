package com.shanshan.myaccountbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.apache.log4j.Logger;

public class AboutAuthor extends AppCompatActivity {
    private Logger myLogger = MyLogger.getMyLogger(AboutAuthor.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_author);
        TextView textView = (TextView) findViewById(R.id.about_author_text);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("    Authors: shenyf & hess\n\n");
        stringBuffer.append("First Publish Day: 2015-12-14\n\n");
        stringBuffer.append("Have a good day, guys!\n\n");
        stringBuffer.append("All Rights Reserved!\n\n");
        textView.setText(stringBuffer.toString());
        
        myLogger.debug("display author info...");
    }
}
