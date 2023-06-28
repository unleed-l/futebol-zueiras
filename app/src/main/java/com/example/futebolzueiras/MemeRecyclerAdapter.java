package com.example.futebolzueiras;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MemeRecyclerAdapter extends RecyclerView.Adapter<MemeRecyclerAdapter.MyViewHolder> {

    private Context context;
    private List<Meme> dataList;

    public MemeRecyclerAdapter(Context context, List<Meme> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar o layout do item da RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Configurar os dados do meme nas views correspondentes
        Glide.with(context).load(dataList.get(position).getMemeURL()).into(holder.memeImage);
        holder.memeDesc.setText(dataList.get(position).getDescription());

        // Configurar o listener de clique no item da RecyclerView
        holder.memeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abrir a atividade MemeInteractions ao clicar no item
                Intent intent = new Intent(context, MemeInteractions.class);
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getMemeURL());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<Meme> searchList) {
        dataList = searchList;
        notifyDataSetChanged();
    }

    // ViewHolder para otimizar o desempenho da RecyclerView
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView memeCard;
        ImageView memeImage;
        TextView memeDesc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Obter as referências às views do layout do item
            memeCard = itemView.findViewById(R.id.memeCard);
            memeImage = itemView.findViewById(R.id.memeImage);
            memeDesc = itemView.findViewById(R.id.memeDesc);
        }
    }
}
