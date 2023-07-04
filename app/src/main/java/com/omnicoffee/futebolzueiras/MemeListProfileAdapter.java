package com.omnicoffee.futebolzueiras;

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

public class MemeListProfileAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Meme> memeList;

    public MemeListProfileAdapter(Context context, int layout, ArrayList<Meme> memeList) {
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

    // ViewHolder para armazenar as referências às views do layout
    private static class ViewHolder {
        ImageView imageView;
        TextView txtDescription;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            // Inflar o layout do item da lista
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layout, parent, false);

            // Inicializar o ViewHolder e obter as referências às views do layout
            holder = new ViewHolder();
            holder.txtDescription = convertView.findViewById(R.id.memeDesc);
            holder.imageView = convertView.findViewById(R.id.memeImage);

            // Armazenar o ViewHolder como uma tag na view
            convertView.setTag(holder);
        } else {
            // Se a view já está sendo reutilizada, obter o ViewHolder da tag
            holder = (ViewHolder) convertView.getTag();
        }

        // Obter o meme atual da lista
        Meme meme = memeList.get(position);

        // Configurar os dados do meme nas views correspondentes
        holder.txtDescription.setText(meme.getDescription());

        // Decodificar o byte array da imagem em um objeto Bitmap
        byte[] memeImage = meme.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(memeImage, 0, memeImage.length);

        // Configurar o Bitmap no ImageView
        holder.imageView.setImageBitmap(bitmap);

        return convertView;
    }
}
