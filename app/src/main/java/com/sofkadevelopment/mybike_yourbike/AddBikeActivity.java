package com.sofkadevelopment.mybike_yourbike;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AddBikeActivity extends BaseActivity implements
        View.OnClickListener{

    private static final String TAG = "AddBike";

    private EditText mBrandField;
    private EditText mPriceField;
    private EditText mCityField;
    private EditText mPhoneField;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbike);

        // Views
        mBrandField = findViewById(R.id.Brand);
        mPriceField = findViewById(R.id.Price);
        mCityField = findViewById(R.id.City);
        mPhoneField = findViewById(R.id.Phone);

        // Buttons
        findViewById(R.id.BacktoChoiceButton).setOnClickListener(this);
        findViewById(R.id.AddTheBikeButton).setOnClickListener(this);


        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // [END initialize_auth]
    }

    //    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }
    // [END on_start_check_user]


    private boolean validateForm() {
        boolean valid = true;

        String brand = mBrandField.getText().toString();
        if (TextUtils.isEmpty(brand)) {
            mBrandField.setError("Required.");
            valid = false;
        } else {
            mBrandField.setError(null);
        }

        String price = mPriceField.getText().toString();
        if (TextUtils.isEmpty(price)) {
            mPriceField.setError("Required.");
            valid = false;
        } else {
            mPriceField.setError(null);
        }

        String city = mCityField.getText().toString();
        if (TextUtils.isEmpty(city)) {
            mCityField.setError("Required.");
            valid = false;
        } else {
            mCityField.setError(null);
        }

        String phone = mPhoneField.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            mPhoneField.setError("Required.");
            valid = false;
        } else {
            mPhoneField.setError(null);
        }

        return valid;
    }


    private void AddTheBike(String brand, String price, String city, String phone) {
        Log.d(TAG, "Add bike");
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // Create a new user with a first and last name
        Map<String, Object> bike = new HashMap<>();
        bike.put("Brand", brand);
        bike.put("Price", price);
        bike.put("City", city);
        bike.put("Phone", phone);

        // Add a new document with a generated ID
        db.collection("bikes")
                .add(bike)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot successfully written: " +documentReference.getId());
                        Toast.makeText(getApplicationContext(),
                                "You added the bike",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        Toast.makeText(getApplicationContext(),
                                "Smth bad happened You did NOT add the bike",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        hideProgressDialog();

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.BacktoChoiceButton) {
            startActivity(new Intent(this, EmailPasswordActivity.class));
            finish();

        } else if (i == R.id.AddTheBikeButton) {
            AddTheBike(mBrandField.getText().toString(), mPriceField.getText().toString(), mCityField.getText().toString(), mPhoneField.getText().toString());
        }
    }
}

