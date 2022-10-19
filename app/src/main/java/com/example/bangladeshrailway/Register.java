package com.example.bangladeshrailway;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Register extends Fragment {
    EditText et_email,et_password,et_name,et_phone,et_nid;
    Button register;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public Register() {

    }

    public static Register newInstance(String param1, String param2) {
        Register fragment = new Register();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        register=view.findViewById(R.id.sign_up);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


           et_email= view.findViewById(R.id.email);
        et_password=view.findViewById(R.id.password_res);
        et_name=view.findViewById(R.id.name);
        et_phone=view.findViewById(R.id.phone_number);
        et_nid=view.findViewById(R.id.nid);

//
        String phone_number = et_phone.getText().toString();

        String email = et_email.getText().toString();
        String name = et_name.getText().toString();
        String nid = et_nid.getText().toString();
        String pass = et_password.getText().toString();

        Intent intent = new Intent(getActivity(),OTPVerrifcation.class);
        intent.putExtra("email",email);
        intent.putExtra("pass",pass);
        intent.putExtra("nid",nid);
        intent.putExtra("phone_number",phone_number);
        intent.putExtra("name",name);

        Toast.makeText(getActivity(), "Verification process going on", Toast.LENGTH_SHORT).show();
        startActivity(intent);
                }
            });
//
        return view;
    }
}