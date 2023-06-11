package com.example.futebolzueiras;

// import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;




public class UploadFragment extends Fragment {

    EditText et_description , et_tag;
    Button btn_send, btn_choose_file;
    ImageView imageView;

    private static final int REQUEST_PERMISSION = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    public static SQLiteHelper sqLiteHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        et_description = view.findViewById(R.id.et_description);
        et_tag = view.findViewById(R.id.et_tag);
        btn_send = view.findViewById(R.id.btn_send);
        btn_choose_file = view.findViewById(R.id.btn_choose_file);
        imageView = (ImageView) view.findViewById(R.id.image_meme);

        // Instanciado no main activity
        // MainActivity.sqLiteHelper = new SQLiteHelper(getActivity(), "MemeDB.sqlite" , null , 1);

        // Apenas para testes
        //sqLiteHelper.queryData("DROP TABLE IF EXISTS MEME");


        MainActivity.sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS MEME (Id INTEGER PRIMARY KEY AUTOINCREMENT, description VARCHAR ,tag VARCHAR,  image BLOB)");

        btn_choose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestStoragePermission();
                } else {
                    dispatchTakePictureIntent();
                }
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    MainActivity.sqLiteHelper.insertMeme(
                            et_description.getText().toString().trim(),
                            et_tag.getText().toString().trim(),
                            imageViewToByte(imageView)
                    );
                    Toast.makeText(getActivity(), "Added successfully!", Toast.LENGTH_SHORT).show();
                    et_description.setText("");
                    et_tag.setText("");
                    imageView.setImageResource(R.mipmap.ic_launcher);

                    // Mudar pro fragment de profile
                    ((MainActivity) requireActivity()).replaceFragment(new ProfileFragment());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });



        return view;
    }

    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
    }

    private void dispatchTakePictureIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(getActivity(), "You dont have permission" , Toast.LENGTH_LONG).show();
            }
            return ;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}