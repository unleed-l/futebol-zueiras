package com.example.futebolzueiras;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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
        }
}