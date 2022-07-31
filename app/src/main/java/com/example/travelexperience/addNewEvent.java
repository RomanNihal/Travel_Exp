package com.example.travelexperience;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class addNewEvent extends AppCompatActivity {

    private TextView update;
    private EditText addPlace, addDuration, addCost, addDescription;
    private ImageView addImage;
    private Button imageButton, postButton, myListButton;

    FirebaseStorage firebaseStorage;

    private ActivityResultLauncher<String> launcher;
    private String mail, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);

        update = findViewById(R.id.PD);
        addPlace = findViewById(R.id.Name);
        addDuration = findViewById(R.id.Duration);
        addCost = findViewById(R.id.Cost);
        addImage = findViewById(R.id.Image);
        addDescription = findViewById(R.id.Description);
        imageButton = findViewById(R.id.PDChooseButton);
        postButton = findViewById(R.id.PDPost);
        myListButton = findViewById(R.id.PDMyList);

        firebaseStorage = FirebaseStorage.getInstance();

        Bundle bundle = getIntent().getExtras();
        int position = bundle.getInt("key");
        mail = bundle.getString("mail");

        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                addImage.setImageURI(result);

                final StorageReference reference = firebaseStorage.getReference().child("image");
                reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                image = uri.toString();
                            }
                        });
                    }
                });
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcher.launch("image/*");
            }
        });

        if(position == -1){
            myListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(addNewEvent.this, dataSet.class);
                    intent.putExtra("mail",mail);
                    intent.putExtra("activity",1);
                    startActivity(intent);
                }
            });

            postButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = addPlace.getText().toString().trim();
                    String duration = addDuration.getText().toString().trim();
                    String cost = addCost.getText().toString().trim();
                    String description = addDescription.getText().toString().trim();

                    String key = dataSet.databaseReference.push().getKey();
                    int flag = -1;

                    if(name.isEmpty()){
                        addPlace.setError("Can't be empty");
                        addPlace.requestFocus();
                        flag = 0;
                    }
                    if(duration.isEmpty()){
                        addDuration.setError("Can't be empty");
                        addDuration.requestFocus();
                        flag = 0;
                    }
                    if(cost.isEmpty()){
                        addCost.setError("Can't be empty");
                        addCost.requestFocus();
                        flag = 0;
                    }
                    if(image.isEmpty()){
                        imageButton.setError("");
                        flag = 0;
                    }
                    if(description.isEmpty()){
                        addDescription.setError("Can't be empty");
                        addDescription.requestFocus();
                        flag = 0;
                    }

                    if(flag == -1){
                        modelOfData data = new modelOfData(key, name, duration, cost, image, description);

                        dataSet.databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                dataSet.databaseReference.child(mail).child(key).setValue(data);
                                Intent intent = new Intent(addNewEvent.this, dataSet.class);
                                intent.putExtra("mail",mail);
                                intent.putExtra("activity",-1);
                                startActivity(intent);
                                finish();
                                Toast.makeText(addNewEvent.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(addNewEvent.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

        }

        if(position != -1){
            update.setText("UPDATE");
            postButton.setVisibility(View.INVISIBLE);
            myListButton.setText("Update");
            addPlace.setText(connector.list.get(position).getName());
            addDuration.setText(connector.list.get(position).getDuration());
            addCost.setText(connector.list.get(position).getCost());
            Glide.with(addNewEvent.this).load(connector.list.get(position).getImage()).into(addImage);
            addDescription.setText(connector.list.get(position).getDescription());
            String key = connector.list.get(position).getKey();

            myListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = addPlace.getText().toString().trim();
                    String duration = addDuration.getText().toString().trim();
                    String cost = addCost.getText().toString().trim();
                    String description = addDescription.getText().toString().trim();
                    int flag = -1;

                    if(name.isEmpty()){
                        addPlace.setError("Can't be empty");
                        addPlace.requestFocus();
                        flag = 0;
                    }
                    if(duration.isEmpty()){
                        addDuration.setError("Can't be empty");
                        addDuration.requestFocus();
                        flag = 0;
                    }
                    if(cost.isEmpty()){
                        addCost.setError("Can't be empty");
                        addCost.requestFocus();
                        flag = 0;
                    }
                    if(image.isEmpty()){
                        imageButton.setError("");
                        flag = 0;
                    }
                    if(description.isEmpty()){
                        addDescription.setError("Can't be empty");
                        addDescription.requestFocus();
                        flag = 0;
                    }

                    if(flag == -1){
                        modelOfData data = new modelOfData(key, name, duration, cost, image, description);

                        dataSet.databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                dataSet.databaseReference.child(mail).child(key).setValue(data);
                                Intent intent = new Intent(addNewEvent.this, dataSet.class);
                                intent.putExtra("mail",mail);
                                intent.putExtra("activity",1);
                                startActivity(intent);
                                finish();
                                Toast.makeText(addNewEvent.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(addNewEvent.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

    }
}