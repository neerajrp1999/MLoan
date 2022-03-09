package com.example.myapplicationc;

import android.os.Bundle;

import android.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.myapplicationc.MainActivity;

import java.security.PublicKey;

public class ActiveUserFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SearchView userSearchBox;
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView1;
    String SearchQ;
    private RecyclerView.LayoutManager layoutManager;
    private ShowUserAdapter showUserAdapter;

    public ActiveUserFragment() {    }

    public static ActiveUserFragment newInstance(String param1, String param2) {
        ActiveUserFragment fragment = new ActiveUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot= inflater.inflate(R.layout.fragment_active_user, container, false);

        recyclerView1=viewRoot.findViewById(R.id.recycleviewActiveUser);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView1.setLayoutManager(layoutManager);

        SearchQ=null;

        userSearchBox=viewRoot.findViewById(R.id.activeUserSearchBox);
        userSearchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchQ=query.trim();
                showUserAdapter=new ShowUserAdapter(new db(getContext()).feachActiveUser(SearchQ));
                recyclerView1.setAdapter(showUserAdapter);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                SearchQ=newText.trim();
                showUserAdapter=new ShowUserAdapter(new db(getContext()).feachActiveUser(SearchQ));
                recyclerView1.setAdapter(showUserAdapter);
                return false;
            }
        });
        userSearchBox.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                SearchQ=null;
                showUserAdapter=new ShowUserAdapter(new db(getContext()).feachActiveUser(SearchQ));
                recyclerView1.setAdapter(showUserAdapter);
                return false;
            }
        });
        showUserAdapter=new ShowUserAdapter(new db(getContext()).feachActiveUser(SearchQ));
        recyclerView1.setAdapter(showUserAdapter);
        return viewRoot;
    }
}