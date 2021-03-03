package fr.alexandrine.uneapplinulle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button createImage;
    private Button readJokes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.createImage = (Button) findViewById(R.id.create_image_btn);
        this.readJokes = (Button) findViewById(R.id.jokes_btn);

        createImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent otherActivity = new Intent(getApplicationContext(), ImageActivity.class);
                startActivity(otherActivity);
            }
        });

        readJokes.setOnClickListener(view -> {
            Intent otherActivity = new Intent(getApplicationContext(), JokeActivity.class);
            startActivity(otherActivity);
        });
    }
}