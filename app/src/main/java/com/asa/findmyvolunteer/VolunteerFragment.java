package com.asa.findmyvolunteer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class VolunteerFragment extends Fragment {
    BackendlessCollection<VictimData> victims;
    List<VictimData> totalVictims=new ArrayList<>();
    RecyclerView recyclerView;
    CardView cardView;
    RecyclerAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_volunteer, container, false);
        recyclerView=(RecyclerView) view.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter=new RecyclerAdapter(totalVictims);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        getData();


        return view;
    }
    private void getData(){
        Backendless.Persistence.of(VictimData.class).find(new AsyncCallback<BackendlessCollection<VictimData>>() {
            @Override
            public void handleResponse(BackendlessCollection<VictimData> victimDataBackendlessCollection) {
                Log.i("Data ","Received !");
                totalVictims.addAll(victimDataBackendlessCollection.getCurrentPage());
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.i("Data ","Failed !");

            }
        });
    }
}

