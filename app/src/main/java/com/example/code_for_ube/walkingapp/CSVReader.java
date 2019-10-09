package com.example.code_for_ube.walkingapp;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    List<ListData> objects = new ArrayList<>();

    public void reader(Context context) {
        AssetManager assetManager = context.getResources().getAssets();
        try {
            // CSVファイルの読み込み
            InputStream inputStream = assetManager.open("kankospot.csv");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferReader.readLine()) != null) {

                //カンマ区切りで１つづつ配列に入れる
                ListData data = new ListData();
                String[] RowData = line.split(",");

                //CSVの左([0]番目)から順番にセット
                data.setId(RowData[0]);
                data.setTitle(RowData[1]);
                data.setText(RowData[2]);
                data.setCategories(RowData[3]);
                data.setTags(RowData[4]);
                data.setBasename(RowData[5]);
                data.setAdress(RowData[6]);
                data.setArtimg(RowData[7]);
                data.setEigyoujikan(RowData[8]);
                data.setHoliday(RowData[9]);
                data.setLatitude(RowData[10]);
                data.setLongitude(RowData[11]);
                data.setParking(RowData[12]);
                data.setPhone(RowData[13]);
                data.setPrice(RowData[14]);
                data.setTrafficdata(RowData[15]);

                objects.add(data);
            }
            bufferReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}