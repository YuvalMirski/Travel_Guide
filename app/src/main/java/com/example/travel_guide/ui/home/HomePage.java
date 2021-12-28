package com.example.travel_guide.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.travel_guide.R;


public class HomePage extends Fragment {


    public HomePage() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        Button attractionBtn = (Button)view.findViewById(R.id.attractions_home_btn);
        Button toursBtn = (Button)view.findViewById(R.id.tours_home_btn);


        attractionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(HomePageDirections.actionHomePageNavToPostListRvFragment());
            }
        });

        return view;
    }
}