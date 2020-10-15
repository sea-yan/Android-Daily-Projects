package com.dwh.dialogdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDemo();
            }
        });
    }

    private void showDemo() {
        final Dialog mDemo = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.layout,null);
        mDemo.setContentView(view);
        TextView textView = mDemo.findViewById(R.id.textView);
        TextView textView2 = mDemo.findViewById(R.id.textView2);
        TextView textView3 = mDemo.findViewById(R.id.textView3);
        mDemo.show();

    }
}