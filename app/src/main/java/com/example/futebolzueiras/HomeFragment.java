package com.example.futebolzueiras;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Meme> memeList;
    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;
    private MemeRecyclerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        // Configurando o layout do RecyclerView com um GridLayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Criação do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();

        // Inicialização das listas e do adapter
        memeList = new ArrayList<>();
        adapter = new MemeRecyclerAdapter(requireContext(), memeList);
        recyclerView.setAdapter(adapter);

        // Obtendo referência para o banco de dados Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Meme Teste");

        // Exibindo o diálogo de carregamento
        dialog.show();

        // Definindo o listener para obter os dados do Firebase
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                memeList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    // Obtendo o Meme do snapshot
                    Meme meme = itemSnapshot.getValue(Meme.class);
                    // Adicionando o Meme à lista
                    memeList.add(meme);
                }
                // Notificando o adapter sobre as mudanças nos dados
                adapter.notifyDataSetChanged();
                // Fechando o diálogo de carregamento
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Em caso de erro, fechar o diálogo de carregamento
                dialog.dismiss();
            }
        });

        return view;
    }
}
