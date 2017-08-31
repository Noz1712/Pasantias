package com.example.noz17.comic;

import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class Espera extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_espera);
        new Verificando().execute();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private class Verificando extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            while (!ConexionInternet.verificaConexion(getApplicationContext()));
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                finish();
            }
        }

    }

}
