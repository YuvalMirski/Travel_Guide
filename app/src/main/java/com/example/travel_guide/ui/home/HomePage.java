package com.example.travel_guide.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.travel_guide.R;


public class HomePage extends Fragment {

    HomePageViewModel homeViewModel;
    String userId;
    public HomePage() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeViewModel = new ViewModelProvider(this).get(HomePageViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        Button attractionBtn = (Button)view.findViewById(R.id.attractions_home_btn);
        Button toursBtn = (Button)view.findViewById(R.id.tours_home_btn);
        Button restaurantsBtn = (Button)view.findViewById(R.id.restaurants_home_btn);
        Button museumsBtn = (Button)view.findViewById(R.id.museums_home_btn);
        Button allCategoriesBtn = (Button)view.findViewById(R.id.allCategories_home_btn);


        //String userId = HomePageArgs.fromBundle(getArguments()).getUserId();
        userId = homeViewModel.getUserId();
        System.out.println("user id is: "+userId);

        //this is temporary Button will be replace with the bottom nav bar
        Button addBtn = (Button)view.findViewById(R.id.add_new_post_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(HomePageDirections.actionHomePageNavToNewPostPage());
            }
        });

        attractionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(HomePageDirections.actionHomePageNavToPostListRvFragment("attractions",userId));
            }
        });
        toursBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(HomePageDirections.actionHomePageNavToPostListRvFragment("tours",userId)));
        restaurantsBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(HomePageDirections.actionHomePageNavToPostListRvFragment("restaurants",userId)));
        museumsBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(HomePageDirections.actionHomePageNavToPostListRvFragment("museums",userId)));
        allCategoriesBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(HomePageDirections.actionHomePageNavToPostListRvFragment("allCategories",userId)));


        return view;
    }
}