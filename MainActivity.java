package com.example.wagbaapplicationforrestaurants;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    EditText orderNb;
    Button updateStatus;
    RadioGroup statusGroup;
    TextView allOrderNbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // db
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Order");

        orderNb = findViewById(R.id.editTextOrderNb);
        statusGroup = findViewById(R.id.statusRadioGroup);
        updateStatus = findViewById(R.id.updateStatus);
        allOrderNbs = findViewById(R.id.textViewOrderNumbers);


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String id = ds.getKey();
                        Log.d("status", "onDataChange: " + id);
                        myRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        String orderNbById = ds.getKey();
                                        Log.d("status", "onDataChange: " + orderNbById);
                                        String displayedOrderNbs = allOrderNbs.getText().toString();
                                        allOrderNbs.setText(displayedOrderNbs + " \n" + "#" +
                                                orderNbById //+ " \t \t \t" + ds.getValue(String.class)
                                                );
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Order number does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        updateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String orderNumber = orderNb.getText().toString();
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // allOrderNbs.setText("");
                            for(DataSnapshot ds : snapshot.getChildren()) {
                                String id = ds.getKey();
                                Log.d("status", "onDataChange: " + id);
                                myRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            for (DataSnapshot ds : snapshot.getChildren()) {
                                                String orderNbById = ds.getKey();
                                                Log.d("status", "onDataChange: " + orderNbById);
                                                if (orderNbById.contentEquals(orderNumber)){
                                                    int radioButtonId = statusGroup.getCheckedRadioButtonId();
                                                    Log.d("ordernumber", "onDataChange: " + radioButtonId);
                                                    if (radioButtonId == -1 )
                                                    {
                                                        Toast.makeText(MainActivity.this, "Please choose the corresponding status", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }
                                                    RadioButton statusBtn = findViewById(radioButtonId);
                                                    String status = statusBtn.getText().toString();
                                                    Log.d("status", "onDataChange: " + status);
                                                    myRef.child(id).child(orderNumber).setValue(status);

                                                }
                                                /* String displayedOrderNbs = allOrderNbs.getText().toString();
                                                allOrderNbs.setText(displayedOrderNbs + " \n" + "#" +
                                                        orderNbById //+ " \t \t \t" + ds.getValue(String.class)
                                                ); */
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Order number does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });
    }
}