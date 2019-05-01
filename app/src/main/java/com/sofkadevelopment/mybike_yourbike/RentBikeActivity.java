package com.sofkadevelopment.mybike_yourbike;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import javax.annotation.Nullable;

public class RentBikeActivity extends BaseActivity implements
        View.OnClickListener {

    private static final String TAG = "RentBike";

    ListView mlistofBikes;

    TextView mPrintBikeView;
    private FirebaseAuth mAuth;
    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("bikes/Bikes") ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rentbike);

        mlistofBikes= findViewById(R.id.listViewofBikes);
        // Buttons
        findViewById(R.id.BacktoChoiceButton).setOnClickListener(this);
        findViewById(R.id.RentTheBikeButton).setOnClickListener(this);


        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();




        db.collection("bikes")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        List bikes = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            bikes.add(doc.getId()+doc.getData());
//                        bikes.add(doc.get("Brand"));
//                        bikes.add(doc.get("City"));
//                        bikes.add(doc.get("Phone"));
//                        bikes.add(doc.get("Price"));


                        }
                        ArrayAdapter<String>adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_checked,bikes);
                        adapter.notifyDataSetChanged();

                    mlistofBikes.setAdapter(adapter);
                        // mPrintBikeView.setText(bikes.toString());
                        Log.d(TAG, "Current bikes in DB: " + bikes);

                   }
                });
    }


    //    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();

    }
    // [END on_start_check_user]
    private void RentTheBike() {
        db.collection("bikes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> AllBikesData = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                AllBikesData.add(document.getId()+ document.getData());
                                mPrintBikeView.setText(AllBikesData.toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


}
//
//    private void AddTheBike(String brand, String price, String city, String phone) {
//        Log.d(TAG, "Add bike");
//        if (!validateForm()) {
//            return;
//        }
//
//        showProgressDialog();
//
//        // Create a new user with a first and last name
//        Map<String, Object> bike = new HashMap<>();
//        bike.put("Brand", brand);
//        bike.put("Price", price);
//        bike.put("City", city);
//        bike.put("Phone", phone);
//
//// Add a new document with a generated ID
//        db.collection("bikes").document( "Bikes").set(bike)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void avoid) {
//                        Log.d(TAG, "DocumentSnapshot successfully written: " );
//                        Toast.makeText(getApplicationContext(),
//                                "You added the bike",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error writing document", e);
//                        Toast.makeText(getApplicationContext(),
//                                "Smth bad happened You did NOT add the bike",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//        hideProgressDialog();
//
//    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.BacktoChoiceButton) {
            startActivity(new Intent(this, EmailPasswordActivity.class));
            finish();

        }
        else if (i == R.id.RentTheBikeButton) {
            RentTheBike();
        }
    }
}

