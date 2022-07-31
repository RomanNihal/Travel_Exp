package com.example.travelexperience;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class dataSet extends AppCompatActivity {

    public static FirebaseDatabase firebaseDatabase;
    public static DatabaseReference databaseReference;

    private FloatingActionButton fButton;

    List<modelOfData> list;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    com.example.travelexperience.connector connector;

    public static String mail;
    public static int activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dataset);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        list = new ArrayList<>();

        fButton = findViewById(R.id.PFloatingActionButton);

        Bundle bundle = getIntent().getExtras();
        mail = bundle.getString("mail");
        activity = bundle.getInt("activity");

        if(activity == -1){
            fButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(dataSet.this, addNewEvent.class);
                    intent.putExtra("key",-1);
                    intent.putExtra("mail",mail);
                    startActivity(intent);
                }
            });
        }

        if(activity == 1){
            fButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    logIn.mAuth.signOut();
                    startActivity(new Intent(dataSet.this, logIn.class));
                    finish();
                }
            });
        }

        setData();
        setRecycle();
    }

    @Override
    public void onBackPressed() {
        if(activity == -1){
            AlertDialog.Builder dialogExit = new AlertDialog.Builder(dataSet.this);

            dialogExit.setTitle("EXIT!!");
            dialogExit.setMessage("Are you sure?");

            dialogExit.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);
                    finish();
                }
            });

            dialogExit.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            dialogExit.show();
        }

        if(activity == 1){
            Intent intent = new Intent(dataSet.this, dataSet.class);
            intent.putExtra("mail", mail);
            intent.putExtra("activity",-1);
            startActivity(intent);
            this.finish();
        }
    }

    private void setRecycle() {
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView = findViewById(R.id.PRecyclerView);
        recyclerView.setLayoutManager(layoutManager);
        connector = new connector(list, this);
        recyclerView.setAdapter(connector);
        connector.notifyDataSetChanged();
    }

    private void setData() {
        databaseReference.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if(activity == -1){
                    for(DataSnapshot snap : snapshot.getChildren()){
                        String str = snap.getValue(String.class);
                        if(str.equals(mail)) {
                            continue;
                        }
                        databaseReference.child(str).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                for(DataSnapshot snap1 : snapshot1.getChildren()){
                                    modelOfData data = snap1.getValue(modelOfData.class);
                                    list.add(data);
                                }
                                connector.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                if(activity == 1){
                    for(DataSnapshot snap : snapshot.getChildren()){
                        String str = snap.getValue(String.class);
                        if(!str.equals(mail)) {
                            continue;
                        }
                        databaseReference.child(str).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                for(DataSnapshot snap1 : snapshot1.getChildren()){
                                    modelOfData data = snap1.getValue(modelOfData.class);
                                    list.add(data);
                                }
                                connector.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void edit(Context context, int position){
        if(activity == -1){
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.model_design);
            dialog.setTitle("Details");

            ImageView image;
            TextView name, duration, cost, description;

            image = dialog.findViewById(R.id.Image);
            name = dialog.findViewById(R.id.Name);
            duration = dialog.findViewById(R.id.Duration);
            cost = dialog.findViewById(R.id.Cost);
            description = dialog.findViewById(R.id.Description);

            modelOfData data = com.example.travelexperience.connector.list.get(position);
            Glide.with(context).load(com.example.travelexperience.connector.list.get(position).getImage()).into(image);
            name.setText(data.getName());
            duration.setText(data.getDuration());
            cost.setText(data.getCost());
            description.setText(data.getDescription());

            int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.95);
            int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.6);
            dialog.getWindow().setLayout(width, height);
            dialog.show();
        }
        if(activity == 1){
            CharSequence[] items = {"Update", "Delete"};
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);

            dialog.setTitle("Choose an action");
            dialog.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(i == 0){
                        Intent intent = new Intent(context, addNewEvent.class);
                        intent.putExtra("key", position);
                        intent.putExtra("mail", mail);
                        context.startActivity(intent);
                    }
                    else{
                        String key = com.example.travelexperience.connector.list.get(position).getKey();
                        databaseReference.child(mail).child(key).removeValue();
                    }
                }
            });
            dialog.show();
        }
    }
}