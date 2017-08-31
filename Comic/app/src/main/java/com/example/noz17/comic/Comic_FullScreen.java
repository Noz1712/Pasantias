package com.example.noz17.comic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

@SuppressWarnings("ConstantConditions")
public class Comic_FullScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide(); //Hace que se elimine la barra de titulo
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Oculta la ActionBar
        setContentView(R.layout.activity_comic__full_screen);
        ImageView Comic = (ImageView) findViewById(R.id.Comic);
        String S = getIntent().getExtras().getString("URL");
        Picasso.with(Comic_FullScreen.this).load(S).into(Comic);

    }
}
