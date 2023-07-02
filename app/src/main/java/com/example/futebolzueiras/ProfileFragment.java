package com.example.futebolzueiras;

import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private GridView gridView;
    private ArrayList<Meme> memeList;
    private MemeListProfileAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar o layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Encontrar o GridView no layout pelo ID
        gridView = view.findViewById(R.id.gridView);

        memeList = new ArrayList<>();

        // Criar uma instância do adaptador para o GridView
        adapter = new MemeListProfileAdapter(requireContext(), R.layout.grid_item, memeList);
        gridView.setAdapter(adapter);

        // Recuperar dados dos memes do banco de dados
        Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM MEME");
        memeList.clear();
        while (cursor.moveToNext()) {
            // Recuperar valores de cada coluna do cursor
            int id = cursor.getInt(0);
            String description = cursor.getString(1);
            String tag = cursor.getString(2);
            byte[] image = cursor.getBlob(3);

            // Criar um objeto Meme e adicioná-lo à lista
            Meme meme = new Meme(id, description, tag, image);
            memeList.add(0, meme);
        }

        // Fechar o cursor após o uso
        cursor.close();

        // Notificar o adaptador que os dados foram alterados para atualizar a exibição
        adapter.notifyDataSetChanged();

        // Retornar a visualização do fragmento inflado com os dados do GridView
        return view;
    }
}
