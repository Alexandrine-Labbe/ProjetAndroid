package fr.alexandrine.uneapplinulle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button createImage;
    private Button readJokes;
    private Button createEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.createImage = (Button) findViewById(R.id.create_image_btn);
        this.readJokes = (Button) findViewById(R.id.jokes_btn);
        this.createEvent = (Button) findViewById(R.id.create_event_btn);

        createImage.setOnClickListener(view -> {
            Intent otherActivity = new Intent(getApplicationContext(), ImageActivity.class);
            startActivity(otherActivity);
        });

        readJokes.setOnClickListener(view -> {
            Intent otherActivity = new Intent(getApplicationContext(), JokeActivity.class);
            startActivity(otherActivity);
        });

        createEvent.setOnClickListener(view -> {
            Intent otherActivity = new Intent(getApplicationContext(), EventActivity.class);
            startActivity(otherActivity);
        });
    }
}