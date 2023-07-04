package com.omnicoffee.futebolzueiras;

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
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadFragment extends Fragment {

    // Componentes de interface do usuário
    EditText et_description, et_tag;
    Button btn_send, btn_choose_file;
    ImageView imageView;
    Uri selectedImage;
    String imageURL;

    // Variáveis para armazenar a descrição e a tag
    String description, tag;

    // SharedPreferences para salvar dados do formulário
    private static SharedPreferences upload_preferences;

    private static final int REQUEST_PERMISSION = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicialização das SharedPreferences
        upload_preferences = requireContext().getSharedPreferences("upload_preferences", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        // Inicialização dos componentes de interface do usuário
        et_description = view.findViewById(R.id.et_description);
        et_tag = view.findViewById(R.id.et_tag);
        btn_send = view.findViewById(R.id.btn_send);
        btn_choose_file = view.findViewById(R.id.btn_choose_file);
        imageView = (ImageView) view.findViewById(R.id.image_profile);

        // Criação da tabela MEME no banco de dados
        MainActivity.sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS MEME (Id INTEGER PRIMARY KEY AUTOINCREMENT, description VARCHAR ,tag VARCHAR,  image BLOB)");

        // Carrega os dados do meme das preferências
        loadMemeData();

        // Define o clique do botão para escolher um arquivo
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

        // Define o clique do botão para enviar o meme
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    String description = et_description.getText().toString().trim();
                    String tag = et_tag.getText().toString().trim();

                    Meme meme = new Meme();
                    meme.setDescription(description);
                    meme.setTag(tag);

                    saveMemeOnFirebase(meme);

                    // Insere o meme na tabela MEME do banco de dados
                    MainActivity.sqLiteHelper.insertMeme(
                            description,
                            tag,
                            imageViewToByte(imageView)
                    );

                    Toast.makeText(getActivity(), "Added successfully!", Toast.LENGTH_SHORT).show();

                    // Registra o upload do meme
                    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
                    Bundle bundle = new Bundle();
                    bundle.putString("description",description);
                    bundle.putString("tag", tag);
                    mFirebaseAnalytics.logEvent("Envio_de_meme", bundle);

                    // Limpa os campos após o envio do meme
                    clearForm();

                    // Navega para o fragmento de perfil
                    ((MainActivity) requireActivity()).replaceFragment(new ProfileFragment());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private void clearForm() {
        et_description.setText("");
        et_tag.setText("");
        imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.random_guest));
    }

    // Salva o meme no Firebase Storage
    public void saveMemeOnFirebase(Meme meme) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Meme Images")
                .child(selectedImage.getLastPathSegment());

        storageReference.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                uriTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri urlImage = task.getResult();
                            imageURL = urlImage.toString();
                            uploadData(meme);
                        } else {
                            // Tratar falha ao obter a URL da imagem
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Mostrar mensagem de erro
            }
        });
    }

    // Envia os dados do meme para o Firebase Realtime Database
    public void uploadData(Meme meme) {
        //description = et_description.getText().toString();
        //tag = et_tag.getText().toString();

        // Meme meme = new Meme(description, tag);

        meme.setMemeURL(imageURL);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        FirebaseDatabase.getInstance().getReference("Memes").child(currentDate)
                .setValue(meme);
    }

    // Salva os dados do formulário nas SharedPreferences
    private void saveMemeData() {
        // Obter os valores dos formulários
        String descriptionValue = et_description.getText().toString().trim();
        String tagValue = et_tag.getText().toString().trim();

        // Salvar a descrição e a tag nas SharedPreferences
        SharedPreferences.Editor editor = upload_preferences.edit();
        editor.putString("description", descriptionValue);
        editor.putString("tag", tagValue);
        editor.apply();
    }

    // Carrega os dados do meme das SharedPreferences
    private void loadMemeData() {
        // Carregar a descrição e a tag das SharedPreferences
        String savedDescription = upload_preferences.getString("description", "");
        String savedTag = upload_preferences.getString("tag", "");

        et_description.setText(savedDescription);
        et_tag.setText(savedTag);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Salvar os dados do formulário quando o fragmento estiver pausado
        saveMemeData();
    }

    // Converte uma ImageView em um array de bytes
    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return resizeImage(byteArray);
    }

    // Redimensiona uma imagem para evitar problemas de tamanho
    private byte[] resizeImage(byte[] image) {
        while (image.length > 500000) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.8), (int) (bitmap.getHeight() * 0.8), true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.PNG, 100, stream);
            image = stream.toByteArray();
        }
        return image;
    }

    // Solicita permissão de armazenamento
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
    }

    // Inicia a intenção de selecionar uma imagem
    private void dispatchTakePictureIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    /*
     * Manipula a resposta da solicitação de permissões do usuário.
     * Se a permissão READ_EXTERNAL_STORAGE for concedida, inicia a intenção de escolher uma imagem.
     * Caso contrário, exibe um Toast informando ao usuário que a permissão não foi concedida.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(getActivity(), "You don't have permission", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /*
     * Manipula o resultado da atividade iniciada pela intenção de captura de imagem.
     * Se o resultado for OK e os dados não forem nulos, obtém a imagem selecionada a partir dos dados retornados e exibe-a no ImageView.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK && data != null) {
            selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
