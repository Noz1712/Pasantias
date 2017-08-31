package com.example.noz17.comic;

/**
 * Created by Noz17 on 30/08/2017.
 */

import android.app.Activity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;


public class ExisteURL{
    public String getConnectionStatus () {
        String conStatus = null;
        try {
            URL u = new URL("https://www.google.es/");
            HttpsURLConnection huc = (HttpsURLConnection) u.openConnection();
            huc.connect();
            conStatus = "Online";
        } catch (Exception e) {
            conStatus = "Offline";
        }
        return conStatus;
    }
}

