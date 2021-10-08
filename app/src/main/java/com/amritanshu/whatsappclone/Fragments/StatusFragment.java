package com.amritanshu.whatsappclone.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amritanshu.whatsappclone.Adapter.StoryAdapter;
import com.amritanshu.whatsappclone.Models.Story;
import com.amritanshu.whatsappclone.Models.Users;
import com.amritanshu.whatsappclone.databinding.FragmentStatusBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class StatusFragment extends Fragment {

    private RecyclerView recyclerViewStory;
    FragmentStatusBinding binding;
    private StoryAdapter storyAdapter;
    private ArrayList<Story> storyList;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;

    private List<String> followingList;

    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentStatusBinding.inflate(inflater, container, false);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        storyList = new ArrayList<>();
        followingList = new ArrayList<>();

        storyAdapter = new StoryAdapter(getContext(), storyList);
        binding.recyclerViewStory.setAdapter(storyAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerViewStory.setLayoutManager(layoutManager);

        checkFollowing();

        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.fragment_status, container, false);
        //recyclerViewStory = view.findViewById(R.id.recycler_view_story);
        //recyclerViewStory.setHasFixedSize(true);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        //recyclerViewStory.setAdapter(storyAdapter);
        /*database.getReference().child("Story").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    followingList.add(snapshot.getKey());
                }
                long timeCurrent = System.currentTimeMillis();
                storyList.clear();
                storyList.add(new Story("", 0, 0, "", FirebaseAuth.getInstance().getCurrentUser().getUid()));
                for(String id: followingList)
                {
                    int countStory=0;
                    Story story = null;
                    for(DataSnapshot snapshot : dataSnapshot.child(id).getChildren())
                    {
                        story = snapshot.getValue(Story.class);
                        if(timeCurrent > story.getTimeStart() && timeCurrent< story.getTimeEnd())
                        {
                            countStory++;
                        }
                    }
                    if(countStory>0)
                    {
                        storyList.add(story);
                    }
                }
                storyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/

        return binding.getRoot();
    }

    private void checkFollowing()
    {
        followingList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    followingList.add(snapshot.getKey());
                }
                readStory();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readStory()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long timeCurrent = System.currentTimeMillis();
                storyList.clear();
                storyList.add(new Story("", 0, 0, "", FirebaseAuth.getInstance().getCurrentUser().getUid()));
                for(String id: followingList)
                {
                    int countStory=0;
                    Story story = null;
                    for(DataSnapshot snapshot : dataSnapshot.child(id).getChildren())
                    {
                        story = snapshot.getValue(Story.class);
                        if(timeCurrent > story.getTimeStart() && timeCurrent< story.getTimeEnd() && !(story.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())))
                        {
                            countStory++;
                        }
                    }
                    if(countStory>0)
                    {
                        storyList.add(story);
                    }
                }
                storyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}