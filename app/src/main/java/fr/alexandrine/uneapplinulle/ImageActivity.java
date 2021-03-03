package fr.alexandrine.uneapplinulle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


import org.json.JSONException;
import org.json.JSONObject;


public class ImageActivity extends AppCompatActivity {
    private static final String TAG = ImageActivity.class.getSimpleName();
    private ImageView doggoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        try {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();

            doggoImageView = findViewById(R.id.doggo_image);

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(config);

            RequestQueue queue = Volley.newRequestQueue(this);

            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected())
            {
                Log.d(TAG, "Connected to internet");

                String url = "https://random.dog/woof.json";

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response)
                            {
                                Log.d(TAG, "Response" + response);

                                try {
                                    JSONObject listOfJokesObject = new JSONObject(response);
                                    String urlDog = listOfJokesObject.getString("url");



                                    imageLoader.displayImage(urlDog, doggoImageView, new ImageLoadingListener() {

                                        @Override
                                        public void onLoadingStarted(String imageUri, View view) {

                                        }

                                        @Override
                                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                                        }

                                        @Override
                                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                            TextView loadingTextView = findViewById(R.id.loadingTextView);

                                            loadingTextView.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onLoadingCancelled(String imageUri, View view) {

                                        }
                                    });



                                } catch (JSONException e) {
                                    Log.e(TAG, "Error while parsing jokes result", e);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Erreur de requête", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
                queue.add(stringRequest);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Non connecté à internet", Toast.LENGTH_LONG);
                toast.show();
                finish();
            }

        } catch (Exception e) {
            Log.e(TAG, "Erreur", e);
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }


}