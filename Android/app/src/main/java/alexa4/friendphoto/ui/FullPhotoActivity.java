package alexa4.friendphoto.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import alexa4.friendphoto.R;

public class FullPhotoActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        Intent data = getIntent();
        String image = data.getStringExtra("image");
        String text = data.getStringExtra("text");

        TextView textView = findViewById(R.id.full_image_text);
        textView.setText(text);

        ImageView back = findViewById(R.id.back_button);
        back.setOnClickListener(v -> finish());

        ImageView imageView = findViewById(R.id.full_image);
        Picasso.get()
                .load(image)
                .into(imageView);
    }

    public static Intent getInstance(Context context, String text, String image) {
        Intent intent = new Intent(context, FullPhotoActivity.class);
        intent.putExtra("image", image);
        intent.putExtra("text", text);

        return intent;
    }
}
