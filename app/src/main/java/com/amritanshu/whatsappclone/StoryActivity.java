package com.amritanshu.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amritanshu.whatsappclone.Fragments.StatusFragment;
import com.amritanshu.whatsappclone.Models.Story;
import com.amritanshu.whatsappclone.Models.Users;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import jp.shts.android.storiesprogressview.StoriesProgressView;

public class StoryActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {

    int counter =0;
    long pressTime = 0L;
    long limit = 500L;

    StoriesProgressView storiesProgressView;
    ImageView image, storyPhoto;
    TextView storyUsername;

    LinearLayout rSeen;
    TextView seenNumber;
    ImageView storyDelete;


    List<String> images;
    List<String> storyIds;
    String userId;

    View.OnTouchListener onTouchListener = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            switch(motionEvent.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    return limit< now - pressTime;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        getSupportActionBar().hide();

        rSeen = findViewById(R.id.rSeen);
        seenNumber = findViewById(R.id.seenNumber);
        storyDelete = findViewById(R.id.storyDelete);

        storiesProgressView = findViewById(R.id.stories);
        image = findViewById(R.id.image);
        storyPhoto = findViewById(R.id.storyPhoto);
        storyUsername = findViewById(R.id.storyUsername);

        rSeen.setVisibility(View.GONE);
        storyDelete.setVisibility(View.GONE);

        userId = getIntent().getStringExtra("userId");

        if(userId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
        {
            rSeen.setVisibility(View.VISIBLE);
            storyDelete.setVisibility(View.VISIBLE);
        }

        userId = getIntent().getStringExtra("userId");

        getStories(userId);
        userInfo(userId);

        View reverse = findViewById(R.id.reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });
        reverse.setOnTouchListener(onTouchListener);


        View skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {        //reverse may be in place of skip
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });
        skip.setOnTouchListener(onTouchListener);

        rSeen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoryActivity.this, StoryViewsActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("storyId", storyIds.get(counter));
                intent.putExtra("title", "views");
                startActivity(intent);
            }
        });

        storyDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story").child(userId).child(storyIds.get(counter));
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(StoryActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onNext() {

        Glide.with(getApplicationContext()).load(images.get(++counter)).into(image);

        addView(storyIds.get(counter));
        seenNumber(storyIds.get(counter));
    }

    @Override
    public void onPrev() {
        if((counter-1)<0)   return;
        Glide.with(getApplicationContext()).load(images.get(--counter)).into(image);

        seenNumber(storyIds.get(counter));
    }

    @Override
    public void onComplete() {
        finish();
    }

    @Override
    protected void onDestroy() {
        storiesProgressView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        storiesProgressView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        storiesProgressView.resume();
        super.onResume();
    }

    private void getStories(String userId)
    {
        images = new ArrayList<>();
        storyIds = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story").child(userId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                images.clear();
                storyIds.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Story story = snapshot.getValue(Story.class);
                    long timeCurrent = System.currentTimeMillis();
                    if(timeCurrent > story.getTimeStart() && timeCurrent < story.getTimeEnd())
                    {
                        images.add(story.getImageUrl());
                        storyIds.add(story.getStoryId());
                    }
                }

                storiesProgressView.setStoriesCount(images.size());
                storiesProgressView.setStoryDuration(5000L);
                storiesProgressView.setStoriesListener(StoryActivity.this);
                storiesProgressView.startStories(counter);

                Glide.with(getApplicationContext()).load(images.get(counter)).into(image);

                addView(storyIds.get(counter));
                seenNumber(storyIds.get(counter));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void userInfo(String userId)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                Glide.with(getApplicationContext()).load(user.getProfilePic()).into(storyPhoto);
                storyUsername.setText(user.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addView(String storyId)
    {
        if(!userId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
        {
            FirebaseDatabase.getInstance().getReference("Story").child(userId).child(storyId).child("views").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
        }
    }

    private void seenNumber(String storyId)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story").child(userId).child(storyId).child("views");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                seenNumber.setText(""+dataSnapshot.getChildrenCount());     // Or dataSnapshot.getChildrenCount().toString()
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}