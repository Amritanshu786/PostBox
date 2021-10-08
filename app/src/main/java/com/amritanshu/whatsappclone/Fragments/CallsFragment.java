package com.amritanshu.whatsappclone.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.amritanshu.whatsappclone.JoinAudioCall;
import com.amritanshu.whatsappclone.JoinVideoCall;
import com.amritanshu.whatsappclone.R;

public class CallsFragment extends Fragment {

    public CallsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_calls, container, false);

        ImageView call_audio = (ImageView) rootView.findViewById(R.id.call_audio);
        ImageView call_video = (ImageView) rootView.findViewById(R.id.call_video);

        call_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), JoinAudioCall.class));
            }
        });

        call_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), JoinVideoCall.class));
            }
        });

        return rootView;
    }
}