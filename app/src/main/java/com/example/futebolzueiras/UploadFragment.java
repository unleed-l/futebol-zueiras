package com.example.futebolzueiras;

// import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.io.IOException;

public class UploadFragment extends Fragment {

    EditText et_description , et_tag;
    Button btn_send, btn_choose_file;
    ImageView imageView;

    private static SharedPreferences upload_preferences;

    private static final int REQUEST_PERMISSION = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    public static SQLiteHelper sqLiteHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        upload_preferences = requireContext().getSharedPreferences("upload_preferences", Context.MODE_PRIVATE);
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
        imageView = (ImageView) view.findViewById(R.id.image_profile);

        //MainActivity.sqLiteHelper.queryData("DROP TABLE IF EXISTS MEME");
        //MainActivity.sqLiteHelper.queryData("DELETE FROM MEME");

        // Criação da tabela (*pode ser encapsulado)
        MainActivity.sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS MEME (Id INTEGER PRIMARY KEY AUTOINCREMENT, description VARCHAR ,tag VARCHAR,  image BLOB)");

        // Carrega os memes das preferências
        loadMemeData();

        // Seta o botão para pedir
        btn_choose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Verifica se a permissão READ_EXTERNAL_STORAGE não foi concedida
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Caso não tenha sido concedida, solicita a permissão
                    requestStoragePermission();
                } else {
                    // Caso a permissão tenha sido concedida, inicia a intenção de escolher uma imagem
                    dispatchTakePictureIntent();
                }
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Insere registro do meme na tabela MEME
                try{
                    MainActivity.sqLiteHelper.insertMeme(
                            et_description.getText().toString().trim(),
                            et_tag.getText().toString().trim(),
                            imageViewToByte(imageView)
                    );


                    Toast.makeText(getActivity(), "Added successfully!", Toast.LENGTH_SHORT).show();

                    // Após registrar seta os campos para default
                    et_description.setText("");
                    et_tag.setText("");
                    imageView.setImageResource(R.drawable.random_guest);

                    // Muda pro fragment de profile
                    ((MainActivity) requireActivity()).replaceFragment(new ProfileFragment());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private void saveMemeData() {
        // Obter os valores dos formulários
        String descriptionValue = et_description.getText().toString().trim();
        String tagValue = et_tag.getText().toString().trim();

        // Salvar a descrição e a tag nas SharedPreferences
        SharedPreferences.Editor editor = upload_preferences.edit();
        editor.putString("description", descriptionValue);
        editor.putString("tag", tagValue);

        editor.commit();
    }

    private void loadMemeData() {

        // Carregar a descrição e a tag das SharedPreferences
        String savedDescription = upload_preferences.getString("description", "");
        String savedTag = upload_preferences.getString("tag", "");

        if(savedDescription != null){
            et_description.setText(savedDescription);
        }

        if(savedTag != null) {
            et_tag.setText(savedTag);
        }
    }
    @Override
    public void onPause() {
        super.onPause();

        // Salvar os dados do formulário quando o fragmento estiver pausado
        saveMemeData();
    }

    // Conversão de ImageView para byte
    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return resizeImage(byteArray);
    }

    // Função para evitar problemas com tamanho de imagem
    private byte[] resizeImage(byte[] imagem_img){
        while (imagem_img.length > 500000){
            Bitmap bitmap = BitmapFactory.decodeByteArray(imagem_img, 0, imagem_img.length);
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*0.8), (int)(bitmap.getHeight()*0.8), true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imagem_img = stream.toByteArray();
        }
        return imagem_img;
    }

    // Pede permissão de armazenamento
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
    }

    private void dispatchTakePictureIntent() {
        // Cria uma intenção para selecionar uma imagem da galeria externa
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Inicia a atividade esperando um resultado, ou seja, uma imagem selecionada
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Verifica se a solicitação de permissão corresponde à constante REQUEST_PERMISSION
        if(requestCode == REQUEST_PERMISSION){
            // Verifica se há pelo menos um resultado e se a permissão foi concedida
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Se a permissão foi concedida, chama a função para selecionar uma imagem
                dispatchTakePictureIntent();
            } else {
                // Se a permissão não foi concedida, exibe um Toast informando ao usuário
                Toast.makeText(getActivity(), "You dont have permission" , Toast.LENGTH_LONG).show();
            }
            return ;
        }

        // Chama a implementação padrão do método para lidar com outras solicitações de permissão
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Verifica se o resultado foi obtido a partir da solicitação de captura de imagem
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK && data != null) {
            // Obtém a imagem selecionada a partir dos dados retornados pela atividade
            Uri selectedImage = data.getData();
            try {
                // Converte para bitmap e seta no imageview
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}