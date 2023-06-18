package com.example.futebolzueiras;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;

public class MemeInteractions extends AppCompatActivity {

    ImageView imgview;
    byte [] meme_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_interactions);

        // Pega o array de bytes da imagem(meme)
        meme_image = getIntent().getExtras().getByteArray("meme_image");

        // Acha o imageView(Background da tela)
        imgview = findViewById(R.id.backgroundMeme);

        // Decodifica a imagem e seta no imageView
        Bitmap bitmap = BitmapFactory.decodeByteArray(meme_image, 0, meme_image.length);
        imgview.setImageBitmap(bitmap);

        ImageButton shareButton = (ImageButton) findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*

                Intent intent = new Intent(Intent.ACTION_SEND);
              intent.setType("text/plain");
              intent.putExtra(Intent.EXTRA_SUBJECT , "Check out this");
              intent.putExtra(Intent.EXTRA_TEXT , "yOUR APLICAition link here");
              startActivity(Intent.createChooser(intent , "Share via"));

                */

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                byte[] imageBytes = meme_image;
                intent.putExtra(Intent.EXTRA_STREAM, getImageUriFromBytes(imageBytes));
                startActivity(Intent.createChooser(intent, "Compartilhar imagem via"));

                // Log.d("ImageButton", "O botão foi tocado!");
            }
        });
    }
    private Uri getImageUriFromBytes(byte[] imageBytes) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Image", null);
        return Uri.parse(path);
    }




}