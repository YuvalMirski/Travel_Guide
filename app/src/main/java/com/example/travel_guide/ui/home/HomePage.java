package com.example.travel_guide.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.travel_guide.R;
import com.example.travel_guide.model.Model;
import com.example.travel_guide.model.User;
import com.google.android.material.navigation.NavigationView;


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
        Spinner citySpinner = (Spinner)view.findViewById(R.id.homePage_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.CityList, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);

        //String userId = HomePageArgs.fromBundle(getArguments()).getUserId();
        userId = homeViewModel.getUserId();
        System.out.println("user id is: "+userId);

        //String city = citySpinner.getSelectedItem().toString(); TODO:: to enter in the buttoms below
        attractionBtn.setOnClickListener(v->Navigation.findNavController(v).navigate(HomePageDirections.actionGlobalPostListRvFragment("attractions",userId,citySpinner.getSelectedItem().toString())));
        toursBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(HomePageDirections.actionGlobalPostListRvFragment("tours",userId,citySpinner.getSelectedItem().toString())));
        restaurantsBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(HomePageDirections.actionGlobalPostListRvFragment("restaurants",userId,citySpinner.getSelectedItem().toString())));
        museumsBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(HomePageDirections.actionGlobalPostListRvFragment("museums",userId,citySpinner.getSelectedItem().toString())));
        allCategoriesBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(HomePageDirections.actionGlobalPostListRvFragment("allCategories",userId,citySpinner.getSelectedItem().toString())));

//        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view); //gets  null!!! TODO:: to check WHY??
//        TextView userName = navigationView.getHeaderView(0).findViewById(R.id.userName_tv);
//        TextView userEmail = navigationView.getHeaderView(0).findViewById(R.id.userEmail_tv);
//        System.out.println("!!!!!!!!!!!");
//        System.out.println("!!!!!!!!!!!!!!!");
//        Model.instance.getUserById(userId, new Model.GetUserById() {
//            @Override
//            public void onComplete(User user) {
//                userName.setText(user.getUserName());
//                userEmail.setText(user.getEmail());
//            }
//        });
//        Menu menu = navigationView.getMenu();
//        MenuItem nav_Login = menu.findItem(R.id.logIn_nav);
//        MenuItem nav_signUp = menu.findItem(R.id.signUp_nav);
//        MenuItem nav_Logout = menu.findItem(R.id.logOut_nav);
//        nav_Login.setVisible(false);
//        nav_signUp.setVisible(false);
//        nav_Logout.setVisible(true);

        return view;
    }
}