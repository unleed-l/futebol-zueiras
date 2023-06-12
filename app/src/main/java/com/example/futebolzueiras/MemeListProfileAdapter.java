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

    private class ViewHolder{
        ImageView imageView;
        TextView txtDescription, txtTag;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtDescription = (TextView) row.findViewById(R.id.txtDescription);
            holder.txtTag = (TextView) row.findViewById(R.id.txtTag);
            holder.imageView = (ImageView) row.findViewById(R.id.imgMeme);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        Meme meme = memeList.get(position);

        holder.txtDescription.setText(meme.getDescription());
        holder.txtTag.setText(meme.getTag());

        byte[] foodImage = meme.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(foodImage, 0, foodImage.length);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }
}
