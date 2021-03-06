package com.example.travel_guide.view.Account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.travel_guide.R;
import com.example.travel_guide.model.Model;
import com.example.travel_guide.model.User;
import com.squareup.picasso.Picasso;

public class Account extends Fragment {

    TextView userName, email, sex, country;
    ImageView userAvatar;
    Button userPostBtn, savedPostsBtn;
    ImageButton editBtn;
    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        userId = Model.instance.getCurrentUser().getId();
        Model.instance.getUserById(userId, user -> getUserAction(user));

        userName = view.findViewById(R.id.user_name_account_str_tv);
        email = view.findViewById(R.id.email_account_str_tv);
        sex = view.findViewById(R.id.sex_account_str_tv);
        country = view.findViewById(R.id.country_account_str_tv);
        userAvatar = view.findViewById(R.id.userAvatar_account_details_imv);

        userName.setEnabled(false);
        email.setEnabled(false);
        sex.setEnabled(false);
        country.setEnabled(false);

        editBtn = view.findViewById(R.id.edit_accoutn_btn);
        editBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(AccountDirections.actionAccountNavToEditUserFragment(userId)));

        userPostBtn = view.findViewById(R.id.myPosts_account_btn);
        userPostBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(AccountDirections.actionGlobalPostListRvFragment("userCreatePosts", userId, "")));

        savedPostsBtn = view.findViewById(R.id.savedPosts_account_btn);
        savedPostsBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(AccountDirections.actionGlobalPostListRvFragment("userSavedPost", userId, "")));

        return view;
    }

    private void getUserAction(User user) {
        userName.setText(user.getUserName());
        email.setText(user.getEmail());
        sex.setText(user.getSex());
        country.setText(user.getCountry());

        if (user.getAvatarUrl() != null) {
            Picasso.get()
                    .load(user.getAvatarUrl())
                    .into(userAvatar);
        }
    }
}