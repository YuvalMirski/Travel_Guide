package com.example.travel_guide.ui.account;

import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.travel_guide.R;
import com.example.travel_guide.model.Model;
import com.example.travel_guide.model.User;
import com.squareup.picasso.Picasso;

//User Details Page
public class Account extends Fragment {

    TextView userName, email, sex, country, password;
    ImageView userAvatar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);


        //TODO :: if no user id will be pass it'll be null here - add condition that check it
        // now it goes to user id 1000 !!

         String userId = AccountArgs.fromBundle(getArguments()).getUserId();
        // String userId = AccountArgs.fromBundle(getArguments().getBundle());
        //  String userId = String.valueOf(1000);

        Model.instance.getUserById(userId, new Model.GetUserById() {
            @Override
            public void onComplete(User user) {
                userName.setText(user.getUserName());
                email.setText(user.getEmail());
                sex.setText(user.getSex());
                country.setText(user.getCountry());
                password.setText(user.getPassword());

                if(user.getAvatarUrl()!=null) {
                    Picasso.get()
                            .load(user.getAvatarUrl())
                            .into(userAvatar);
                }
            }
        });

        userName = view.findViewById(R.id.user_name_account_str_tv);
        email = view.findViewById(R.id.email_account_str_tv);
        sex = view.findViewById(R.id.sex_account_str_tv);
        country = view.findViewById(R.id.country_account_str_tv);
        password = view.findViewById(R.id.password_account_str_tv);
        userAvatar = view.findViewById(R.id.userAvatar_account_details_imv);

        Button editBtn = view.findViewById(R.id.edit_accoutn_btn);
        Button userPostBtn = view.findViewById(R.id.post_account_btn); //list of user posts

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(AccountDirections.actionGlobalEditUserFragment(userId));
            }
        });

        return view;
    }
}