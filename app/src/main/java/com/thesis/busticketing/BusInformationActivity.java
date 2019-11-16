package com.thesis.busticketing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.WriterException;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class BusInformationActivity extends AppCompatActivity {

    CarouselView carouselView;
    int[] sampleImages = {R.drawable.davao_bus1, R.drawable.davao_bus2, R.drawable.davao_bus3};
    RequestQueue queue;
    String URL_BUS_ROUTE = "http://bus-ticketing.herokuapp.com/bus_route.php";
    String origin,destination,driver,route_description,plate="";
    TextView tvRouteOrigin1, tvDriver1, tvPlate1, tvRouteDescription1;
    TextView tvRouteOrigin2, tvDriver2, tvPlate2, tvRouteDescription2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_information);

        assert getSupportActionBar()!=null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvRouteOrigin1 = findViewById(R.id.tvRouteOrigin1);
        tvDriver1 = findViewById(R.id.tvDriver1);
        tvPlate1 = findViewById(R.id.tvPlate1);
        tvRouteDescription1 = findViewById(R.id.tvRouteDescription1);

        tvRouteOrigin2 = findViewById(R.id.tvRouteOrigin2);
        tvDriver2 = findViewById(R.id.tvDriver2);
        tvPlate2 = findViewById(R.id.tvPlate2);
        tvRouteDescription2 = findViewById(R.id.tvRouteDescription2);

        queue = Volley.newRequestQueue(this);
        carouselView = findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);
            }
        });

        fetchRoute();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
    private void fetchRoute() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(false);
        pDialog.show();

        Log.d("Response2", "niagi");

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL_BUS_ROUTE, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response2", ""+response);

                        try {
                            JSONArray result = response.getJSONArray("route");
                            pDialog.dismissWithAnimation();
                            Log.d("Response2", ""+result);

                            for(int x=0;x<result.length();x++) {
                                JSONObject collegeData = result.getJSONObject(x);
                                origin = collegeData.getString("origin");
                                destination = collegeData.getString("destination");
                                driver = collegeData.getString("name");
                                route_description = collegeData.getString("route_description");
                                plate = collegeData.getString("plate#");

                                if(origin.equals("Toril")){

                                    tvRouteOrigin1.setText(origin+" - "+destination);
                                    tvDriver1.setText("Driver: "+driver);
                                    tvPlate1.setText("Plate #: "+plate);
                                    tvRouteDescription1.setText(route_description);
                                }

                                if(origin.equals("Catalunan")){

                                    tvRouteOrigin2.setText(origin+" - "+destination);
                                    tvDriver2.setText("Driver: "+driver);
                                    tvPlate2.setText("Plate #: "+plate);
                                    tvRouteDescription2.setText(route_description);

                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return super.getBody();
            }
        };

        queue.add(getRequest);

    }
}
