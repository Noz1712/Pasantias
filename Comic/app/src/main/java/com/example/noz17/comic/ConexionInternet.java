package com.example.noz17.comic;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

/**
 * Created by Noz17 on 29/08/2017.
 */

public class ConexionInternet {
    public static boolean verificaConexion(Context ctx) {
        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        for (int i = 0; i < 2; i++) {
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        return bConectado;
    }

    public boolean conectadoAInternet(String Dir) throws InterruptedException, IOException
    {
        String comando = "ping -c 1" + Dir;
        return (Runtime.getRuntime().exec (comando).waitFor() == 0);
    }
}
