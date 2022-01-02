package com.example.travel_guide;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.travel_guide.model.Model;
import com.example.travel_guide.model.User;

//User Edit Page
public class EditUserFragment extends Fragment {

    EditText userName, email, sex, country, password;
    String new_userName, new_email, new_sex, new_country, new_password, new_id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);

        String userId = EditUserFragmentArgs.fromBundle(getArguments()).getUserId();
        new_id = userId;
        Model.instance.getUserById(userId, new Model.GetUserById() {
            @Override
            public void onComplete(User user) {
                userName.setText(user.getUserName());
                email.setText(user.getEmail());
                sex.setText(user.getSex());
                country.setText(user.getCountry());
                password.setText(user.getPassword());
            }
        });
        userName = view.findViewById(R.id.user_name_account_edit_et);
        email = view.findViewById(R.id.email_account_edit_et);
        sex = view.findViewById(R.id.sex_account_edit_et);
        country = view.findViewById(R.id.sex_account_edit_et);
        password = view.findViewById(R.id.password_account_edit_et);


        Button deleteBtn = view.findViewById(R.id.delete_account_edit_btn);
        Button saveBtn = view.findViewById(R.id.save_account_edit_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.instance.deleteUserById(userId,()-> Navigation.findNavController(userName).navigateUp());
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_userName = userName.getText().toString();
                new_email = email.getText().toString();
                new_sex = sex.getText().toString();
                new_country = country.getText().toString();
                new_password = password.getText().toString();

                User u = new User(new_userName,new_email,new_sex,new_country,new_password,new_id);
                Model.instance.addUser(u,()->Navigation.findNavController(userName).navigateUp());
            }
        });

        return view;
    }
}