package fr.alexandrine.uneapplinulle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.io.File;


public class ImageActivity extends AppCompatActivity {
    private static final String TAG = ImageActivity.class.getSimpleName();
    private ImageView doggoImageView;
    private Button newImage;
    private Button downloadImage;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        loadImage();

        newImage = (Button) findViewById(R.id.new_image_btn);
        downloadImage = (Button) findViewById(R.id.download_image_btn);

        newImage.setOnClickListener(view -> {
            loadImage();
        });

        downloadImage.setOnClickListener(view -> {
            downloadImageNew(imageUrl);
        });

    }

    private void loadImage() {
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

                String url = "https://dog.ceo/api/breeds/image/random";

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response)
                            {
                                Log.d(TAG, "Response" + response);

                                try {
                                    JSONObject listOfJokesObject = new JSONObject(response);
                                    imageUrl = listOfJokesObject.getString("message");
                                    TextView loadingTextView = findViewById(R.id.loadingTextView);

                                    imageLoader.displayImage(imageUrl, doggoImageView, new ImageLoadingListener() {

                                        @Override
                                        public void onLoadingStarted(String imageUri, View view) {
                                            loadingTextView.setVisibility(View.VISIBLE);
                                            loadingTextView.setText(R.string.loading);
                                        }

                                        @Override
                                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                            loadingTextView.setText(R.string.error_msg);
                                            Toast.makeText(getApplicationContext(), R.string.loading_error_msg, Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

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
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.error_query, Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
                queue.add(stringRequest);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.error_network, Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }

        } catch (Exception e) {
            Log.e(TAG, "Erreur", e);
        }
    }

    private void downloadImageNew(String downloadUrlOfImage){
        try{
            DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(downloadUrlOfImage);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle("woof")
                    .setMimeType("image/jpeg")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + "woof.jpg");
            dm.enqueue(request);
            Toast.makeText(getApplicationContext(), R.string.downloading_starting_msg, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), R.string.downloading_error_msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}