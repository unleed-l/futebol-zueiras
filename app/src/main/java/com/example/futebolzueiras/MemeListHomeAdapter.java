package com.example.futebolzueiras;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class MemeListHomeAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Meme> memeList;

    public MemeListHomeAdapter(Context context, int layout, ArrayList<Meme> memeList) {
        this.context = context;
        this.layout = layout;
        this.memeList = memeList;
    }

    @Override
    public int getCount() {
        return memeList.size();
    }

    @Override
    public Object getItem(int position) {
        return memeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txtDescription;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            // Inflar o layout do item da lista
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            // Obter as referências às views do layout e armazená-las no ViewHolder
            holder.txtDescription = (TextView) row.findViewById(R.id.meme_name);
            holder.imageView = (ImageView) row.findViewById(R.id.grid_image);

            // Armazenar o ViewHolder como uma tag na view
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        // Obter o meme atual da lista
        Meme meme = memeList.get(position);

        // Configurar a descrição do meme na view correspondente
        holder.txtDescription.setText(meme.getDescription());

        // Decodificar o byte array da imagem em um objeto Bitmap
        byte[] memeImage = meme.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(memeImage, 0, memeImage.length);

        // Configurar o Bitmap no ImageView
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }
}
