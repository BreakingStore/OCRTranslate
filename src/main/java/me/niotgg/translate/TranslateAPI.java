package me.niotgg.translate;

import me.niotgg.request.Requester;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;


/**
 * Created by hello on 16-Aug-18.
 */

public class TranslateAPI {

    String resp = null;
    String url = null;
    String langFrom = null;
    String langTo = null;
    String word = null;

    public TranslateAPI(String langFrom, String langTo, String text){
        this.langFrom=langFrom;
        this.langTo=langTo;
        this.word=text;



    }

    public String translate() {

        try {
            url = "https://translate.googleapis.com/translate_a/single?"+"client=gtx&"+"sl="+
                    langFrom +"&tl=" + langTo +"&dt=t&q=" + URLEncoder.encode(word, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            resp = new Requester().post(url);
        } catch (IOException e) {
            e.printStackTrace();
        }


        String temp = "";

        if(resp==null){System.out.println("SEM CONEXÃO");}else {
            try {
                JSONArray main = new JSONArray(resp);
                JSONArray total = (JSONArray) main.get(0);
                for (int i = 0; i < total.length(); i++) {
                    JSONArray currentLine = (JSONArray) total.get(i);
                    temp = temp + currentLine.get(0).toString();
                }


                if(temp.length()>2)
                {
                    return temp;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }






}