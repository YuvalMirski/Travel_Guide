package com.example.travel_guide.view.HomePage;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.travel_guide.R;

public class HomePage extends Fragment {

    HomePageViewModel homeViewModel;
    String userId;
    View view;

    public HomePage() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeViewModel = new ViewModelProvider(this).get(HomePageViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_page, container, false);

        Button attractionBtn = (Button) view.findViewById(R.id.attractions_home_btn);
        Button toursBtn = (Button) view.findViewById(R.id.tours_home_btn);
        Button restaurantsBtn = (Button) view.findViewById(R.id.restaurants_home_btn);
        Button museumsBtn = (Button) view.findViewById(R.id.museums_home_btn);
        Button allCategoriesBtn = (Button) view.findViewById(R.id.allCategories_home_btn);
        Spinner citySpinner = (Spinner) view.findViewById(R.id.homePage_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.CityList, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);

        userId = homeViewModel.getUserId();

        attractionBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(HomePageDirections.actionGlobalPostListRvFragment("attractions", userId, citySpinner.getSelectedItem().toString())));
        toursBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(HomePageDirections.actionGlobalPostListRvFragment("tours", userId, citySpinner.getSelectedItem().toString())));
        restaurantsBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(HomePageDirections.actionGlobalPostListRvFragment("restaurants", userId, citySpinner.getSelectedItem().toString())));
        museumsBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(HomePageDirections.actionGlobalPostListRvFragment("museums", userId, citySpinner.getSelectedItem().toString())));
        allCategoriesBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(HomePageDirections.actionGlobalPostListRvFragment("allCategories", userId, citySpinner.getSelectedItem().toString())));

        return view;
    }
}