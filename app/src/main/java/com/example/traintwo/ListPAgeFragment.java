package com.example.traintwo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ListPAgeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable Bundle savedInstanceState) {
        DataBaseHandler db = new DataBaseHandler(getContext());
        ArrayList<JournalEntry> listview_array = db.getAllJournals();

        ListView myList = (ListView) view.findViewById(R.id.listView);
        JournalListAdapter listAdapter = new JournalListAdapter(getActivity(), listview_array);
        myList.setAdapter(listAdapter);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //fragmetn 누르면 그 페이지로 넘어
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final JournalEntry item = (JournalEntry) listAdapter.getItem(position);
                Fragment fragment = new JournalSlidePageFragment();
                getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                //backstack
                Bundle bundle = new Bundle();
                //인텐트 데이터 넘기는거랑 비슷
                bundle.putInt("pos", position);
                fragment.setArguments(bundle);


            }
        });
    }
}
