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

        gridView = (GridView) view.findViewById(R.id.gridView);
        list = new ArrayList<>();
        // get context or get activity
        adapter = new MemeListProfileAdapter(requireContext(), R.layout.meme_profile, list);
        gridView.setAdapter(adapter);

        // get all data from sqlite


        Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM MEME");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String description = cursor.getString(1);
            String tag = cursor.getString(2);
            byte[] image = cursor.getBlob(3);

            list.add(new Meme(id ,description , tag , image));
        }
        cursor.close(); // Fechar o cursor após o uso
        adapter.notifyDataSetChanged();

        return view;
    }
}