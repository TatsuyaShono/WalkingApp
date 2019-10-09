package com.example.code_for_ube.walkingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    MapView map = null;
    ListData listData = null;

    //Map初期値
    private double mapStart = 17.0d;
    private double mapLat = 33.937366d;
    private double mapLon = 131.258893d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //load/initialize the osmdroid configuration
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        //inflate and create the map
        setContentView(R.layout.activity_main);

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        IMapController mapController =  map.getController();
        mapController.setZoom(mapStart);
        GeoPoint startPoint = new GeoPoint(mapLat, mapLon);
        mapController.setCenter(startPoint);

        //your items
        ArrayList<OverlayItem> items = new ArrayList<>();
        listData = new ListData();

        //CSVファイルを読み込んで地図にマッピング
        //AssetManager assetManager = ctx.getResources().getAssets();
        try {
            // CSVファイルの読み込み
            AssetManager as = getResources().getAssets();
            InputStream is = as.open("kankospot.csv");
            InputStreamReader isr = new InputStreamReader(is,"UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {

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

                items.add(new OverlayItem(data.getTitle(), data.getText(),
                        new GeoPoint(Double.parseDouble(data.getLatitude()),
                                Double.parseDouble(data.getLongitude()))));
                //objects.add(data);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        items.add(new OverlayItem("チョーコくん", "宇部市の彫刻の妖精。どうせなら美少女に擬人化したかった…",
                new GeoPoint(33.937366d,131.258893d)));
        items.add(new OverlayItem("チョーコくん1", "宇部市の彫刻の妖精。どうせなら美少女に擬人化したかった…",
                new GeoPoint(35.937366d,133.258893d)));

        //the overlay
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        //do something
                        //Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
                        return true;
                    }
                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        //Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
                        return false;
                    }
                }, ctx);
        mOverlay.setFocusItemsOnTap(true);
        map.getOverlays().add(mOverlay);
    }

    public void onResume(){
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }
    public void onPause(){
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }
}

