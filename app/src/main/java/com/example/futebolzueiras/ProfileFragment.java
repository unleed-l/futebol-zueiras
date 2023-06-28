package com.example.futebolzueiras;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    GridView gridView;
    ArrayList<Meme> list;
    MemeListProfileAdapter adapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        // Encontre o GridView no layout pelo ID
        gridView = (GridView) view.findViewById(R.id.gridView);

        list = new ArrayList<>();

        // Crie uma instância do adaptador para o GridView e defina o adaptador para o GridView
        adapter = new MemeListProfileAdapter(requireContext(), R.layout.grid_item, list);
        gridView.setAdapter(adapter);

        Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM MEME");
        list.clear();
        while (cursor.moveToNext()) {
            // Pega os valores de cada coluna do cursor
            int id = cursor.getInt(0);
            String description = cursor.getString(1);
            String tag = cursor.getString(2);
            byte[] image = cursor.getBlob(3);

            list.add(new Meme(id ,description , tag , image));
        }

        // Fechar o cursor após o uso
        cursor.close();

        // Notifique o adaptador de que os dados foram alterados para atualizar a exibição
        adapter.notifyDataSetChanged();

        // Retorne a visualização do fragmento inflada com os dados do GridView
        return view;
    }
}