package fr.alexandrine.uneapplinulle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.alexandrine.uneapplinulle.adapter.AdapterForJokes;
import fr.alexandrine.uneapplinulle.model.Joke;

public class JokeActivity extends AppCompatActivity {
    private static final String TAG = JokeActivity.class.getSimpleName();
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);

        List<Joke> jokes = new ArrayList<>(0);

        RequestQueue queue = Volley.newRequestQueue(this);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
        {
            Log.d(TAG, "Connected to internet");

            String url = "https://v2.jokeapi.dev/joke/Any?blacklistFlags=nsfw,religious,political,racist,sexist,explicit&type=single&amount=10";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    (Response.Listener<String>) response -> {
                        Log.d(TAG, "Response" + response);

                        try {
                            JSONObject listOfJokesObject = new JSONObject(response);
                            JSONArray listOfJokes = new JSONArray(listOfJokesObject.getString("jokes"));

                            JSONObject jsonItem;
                            Joke joke;
                            for (int i = 0; i < listOfJokes.length(); i++)
                            {
                                jsonItem = listOfJokes.getJSONObject(i);

                                joke = new Joke(
                                        jsonItem.getInt("id"),
                                        jsonItem.getString("category"),
                                        jsonItem.getString("joke")
                                );
                                jokes.add(joke);
                            }

                            ListView listView = (ListView) findViewById(R.id.listView);
                            AdapterForJokes adapterForJokes = new AdapterForJokes(JokeActivity.this, jokes);
                            listView.setAdapter(adapterForJokes);
                        } catch (JSONException e) {
                            Log.e(TAG, "Error while parsing jokes result", e);
                        }
                    }, (Response.ErrorListener) error -> {
                        Toast toast = Toast.makeText(getApplicationContext(), "Erreur de requête", Toast.LENGTH_LONG);
                        toast.show();
                    });
            queue.add(stringRequest);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Non connecté à internet", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }


    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 17) {
                recreate();

            }
        }


        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

}