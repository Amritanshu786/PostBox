package com.amritanshu.whatsappclone.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amritanshu.whatsappclone.AddStoryActivity;
import com.amritanshu.whatsappclone.Models.Story;
import com.amritanshu.whatsappclone.Models.Users;
import com.amritanshu.whatsappclone.R;
import com.amritanshu.whatsappclone.StoryActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    private Context mContext;
    private List<Story> mStory;

    public StoryAdapter(Context mContext, List<Story> mStory) {
        this.mContext = mContext;
        this.mStory = mStory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if(viewType==0)
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.add_story_item, viewGroup, false);
            return new StoryAdapter.ViewHolder(view);
        }
        else
        {
           View view = LayoutInflater.from(mContext).inflate(R.layout.story_item, viewGroup, false);
           return new StoryAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Story story = mStory.get(position);

        userInfo(viewHolder, story.getUserId(), position);

        if(viewHolder.getAdapterPosition()!=0)
        {
            //seenStory(viewHolder, story.getUserId());
        }
        if(viewHolder.getAdapterPosition() == 0)
        {
            myStory(viewHolder.addStoryText, viewHolder.storyPlus, false);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.getAdapterPosition()==0)
                {
                    myStory(viewHolder.addStoryText, viewHolder.storyPlus, true);
                }
                else
                {
                    Intent intent = new Intent(mContext, StoryActivity.class);
                    intent.putExtra("userId", story.getUserId());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public ImageView storyPhoto, storyPlus, storyPhotoSeen;
        public TextView storyUsername, addStoryText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            storyPhoto = itemView.findViewById(R.id.storyPhoto);
            storyPlus = itemView.findViewById(R.id.storyPlus);
            storyPhotoSeen = itemView.findViewById(R.id.storyPhotoSeen);
            storyUsername = itemView.findViewById(R.id.storyUsername);
            addStoryText = itemView.findViewById(R.id.addStoryText);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)
        {
            return 0;
        }
        return 1;
    }

    private void userInfo(ViewHolder viewHolder, String userId, int pos)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                Glide.with(mContext).load(user.getProfilePic()).placeholder(R.drawable.avatar).into(viewHolder.storyPhoto);
                if (pos != 0) {
                    Glide.with(mContext).load(user.getProfilePic()).placeholder(R.drawable.avatar).into(viewHolder.storyPhotoSeen);
                    viewHolder.storyUsername.setText(user.getUserName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void myStory(TextView textView, ImageView imageView, boolean click)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                long timeCurrent = System.currentTimeMillis();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Story story = snapshot.getValue(Story.class);
                    if(timeCurrent>story.getTimeStart() && timeCurrent<story.getTimeEnd())
                    {
                        count++;
                    }
                }
                if(click)
                {
                    if(count>0)
                    {
                        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "View Story", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(mContext, StoryActivity.class);
                                intent.putExtra("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                mContext.startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add a Story", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(mContext, AddStoryActivity.class);
                                mContext.startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                    else
                    {
                        Intent intent = new Intent(mContext, AddStoryActivity.class);
                        mContext.startActivity(intent);
                    }
                }
                else
                {
                    if(count>0)
                    {
                        textView.setText("My Story");
                        imageView.setVisibility(View.GONE);
                    }
                    else
                    {
                        textView.setText("Add Story");
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void seenStory(ViewHolder viewHolder, String userId)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    if(!snapshot.child("views")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .exists() && System.currentTimeMillis()< snapshot.getValue(Story.class).getTimeEnd())
                    {
                        i++;
                    }

                    if(i>0)
                    {
                        viewHolder.storyPhoto.setVisibility(View.VISIBLE);
                        viewHolder.storyPhotoSeen.setVisibility(View.GONE); //Gone
                    }
                    else
                    {
                        viewHolder.storyPhoto.setVisibility(View.GONE);  //Gone
                        viewHolder.storyPhotoSeen.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
