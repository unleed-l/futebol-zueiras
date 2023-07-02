package com.example.futebolzueiras;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.ByteArrayOutputStream;

public class MemeInteractions extends AppCompatActivity {
    ImageView imgview;
    String meme_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_interactions);

        // Obtendo a imagem (meme)
        meme_image = getIntent().getExtras().getString("Image");

        // Encontrando o ImageView (fundo da tela)
        imgview = findViewById(R.id.backgroundMeme);

        // Carregando a imagem utilizando uma biblioteca de terceiros, como Glide
        Glide.with(this).load(meme_image).into(imgview);

        // Configurando o botão de compartilhamento
        ImageButton shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage();
            }
        });
    }

    // Método responsável por compartilhar a imagem
    private void shareImage() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");

        // Obtendo os bytes da imagem do ImageView
        byte[] imageBytes = getBytesFromImageView(imgview);

        // Convertendo os bytes em URI para compartilhamento
        Uri imageUri = getImageUriFromBytes(imageBytes);

        // Adicionando a imagem e texto extra à intent de compartilhamento
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.putExtra(Intent.EXTRA_TEXT , "Link do app");

        // Iniciando a atividade de compartilhamento
        startActivity(Intent.createChooser(intent, "Compartilhar imagem via"));
    }

    // Método para converter bytes em URI
    private Uri getImageUriFromBytes(byte[] imageBytes) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        String title = ("Image_" + System.currentTimeMillis());
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, title, null);
        return Uri.parse(path);
    }

    // Método para obter bytes de um ImageView
    private byte[] getBytesFromImageView(ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
}
