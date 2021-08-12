package com.example.traintwo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class JournalRecyclerAdapter extends RecyclerView.Adapter<JournalRecyclerAdapter.ItemViewHolder> implements ItemTouchHelperListener {

    ArrayList<JournalEntry> journals;
    Context c;

    public JournalRecyclerAdapter(Context c, ArrayList<JournalEntry> journals){
        this.journals = journals;
        this.c = c;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.journal_list_view, parent, false );
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalRecyclerAdapter.ItemViewHolder holder, int position) {
        holder.onBind(journals.get(position));
    }

    @Override
    public int getItemCount() {
        return journals.size();
    }

    @Override
    public boolean onItemMove(int pos1, int pos2) {
        Collections.swap(journals, pos1, pos2);
        notifyItemMoved(pos1, pos2);
        return true;
    }

    @Override
    public void onItemSwipe(int pos) {
        journals.remove(pos);
        notifyItemRemoved(pos);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textView, dateText;
        ImageView imageView;

        public ItemViewHolder(View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.listtext);
            dateText = itemView.findViewById(R.id.listdate);
            imageView = itemView.findViewById(R.id.listimageview);
        }


        public void onBind(JournalEntry journal) {
            try{
                File imgFile = new File(journal.getImagePath());
                if(imgFile.exists());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 32;
                options.inJustDecodeBounds = false;
                String path = imgFile.getAbsolutePath();
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                Log.e("IO", "IO" + e);
            }
            //새로운 거 넣고 이미지뷰 업데이
            imageView.postInvalidate();

            textView.setText(journal.getText());
            dateText.setText(journal.getDate());

        }
    }
}

