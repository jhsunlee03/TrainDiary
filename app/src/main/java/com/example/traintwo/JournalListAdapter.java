package com.example.traintwo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class JournalListAdapter extends BaseAdapter {
    ArrayList<JournalEntry> journalArrayList;
    Context c;

    public JournalListAdapter(Context c, ArrayList<JournalEntry> list){
        journalArrayList = list;
        this.c =c;
    }





    @Override
    public int getCount() {
        return journalArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return journalArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = null;
        LayoutInflater inflater = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            row = inflater.inflate(R.layout.journal_list_view, parent, false);
        }else{
            row=convertView;
        }
        JournalEntry journalData = journalArrayList.get(position);
        ImageView imageview = (ImageView) row.findViewById(R.id.listimageview);
        File imgFile = new File(journalData.imagePath);
        try {
            if (imgFile.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 32;
                options.inJustDecodeBounds = false;
                String path = imgFile.getAbsolutePath();
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                imageview.setImageBitmap(bitmap);
            }
        }
        catch(Exception e){
            Log.e("IO", "IO"+e);
            return null;
        }

        imageview.postInvalidate();
        imageview.invalidate();

        TextView textView = (TextView) row.findViewById(R.id.listtext);
        textView.setText(journalData.text);
        TextView dataText = (TextView) row.findViewById(R.id.listdate);
        dataText.setText(journalData.date);

        return row;
    }
}
