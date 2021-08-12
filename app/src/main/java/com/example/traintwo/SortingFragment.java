package com.example.traintwo;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SortingFragment extends Fragment {

    ArrayList<JournalEntry> listview_array;
    int min_id;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sorting_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        DataBaseHandler db = new DataBaseHandler(getContext());
        TextView text_date = (TextView) view.findViewById(R.id.date);
        RecyclerView myList = (RecyclerView) view.findViewById(R.id.sortingList);

        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        myList.setLayoutManager(manager);

        //주위 회색되면서 작은 창 뜨는거!
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_calendar);
        text_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                MaterialCalendarView calendar = dialog.findViewById(R.id.calendar);
                calendar.setSelectedDate(CalendarDay.today());//기본적으로 오늘로 선택
                ArrayList<CalendarDay> dates = db.getAllJournalDates();
                calendar.addDecorator(new EventDecorator(Color.BLUE, dates, getActivity()));

                Button cancel = dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                Button ok = dialog.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
                        String date = sdf.format(calendar.getSelectedDate().getDate());
                        listview_array = db.getAllJournals(date);

                        if(listview_array.size()>0){
                            min_id = listview_array.get(0).getId();
                        }
                        JournalRecyclerAdapter listAdapter = new JournalRecyclerAdapter(getActivity(), listview_array);
                        myList.setAdapter(listAdapter);
                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(listAdapter));
                        itemTouchHelper.attachToRecyclerView(myList);

                        sdf = new SimpleDateFormat("yyyy.MM.dd");
                        text_date.setText(sdf.format(calendar.getSelectedDate().getDate()));
                        dialog.dismiss();
                    }
                });
            }
        });

        Button save = (Button) view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (listview_array.size() > 0) {

                    int count = min_id;
                    for(JournalEntry journal : listview_array){
                        journal.setId(count);
                        db.updateJournalId(journal);
                        count++;
                    }
                }
            }
        });
    }
}






