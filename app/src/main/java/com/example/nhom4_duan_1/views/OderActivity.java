package com.example.nhom4_duan_1.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.nhom4_duan_1.R;
import com.example.nhom4_duan_1.adapters.OderAdapter;
import com.example.nhom4_duan_1.adapters.ProductAdapter;
import com.example.nhom4_duan_1.models.Bills;
import com.example.nhom4_duan_1.models.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class OderActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerOder;
    String IdUser;
    ArrayList<Bills> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oder);

        Intent intent = getIntent();
        IdUser = intent.getStringExtra("Id");


        list = new ArrayList<>();
        ImageView ivBackOder = findViewById(R.id.ivBackOder);
        ivBackOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getData();
    }

    public void getData(){
        list.clear();
        db.collection("Bills")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                i+=1;
                                Map<String, Object> item = document.getData();
                                if (item.get("Id_User").equals(IdUser)){
                                    Bills bills = new Bills();
                                    bills.setId(document.getId());
                                    bills.setUser(item.get("Id_User").toString());
                                    bills.setTime(item.get("Time").toString());
                                    bills.setPrice(item.get("Total").toString());
                                    bills.setAmount(item.get("Amount").toString());
                                    list.add(bills);
                                    System.out.println(i + " ---" + list.get(list.size()-1));
                                }
                            }
                            Sort();
                        } else {
                            Log.w(">>>TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void Sort(){
        if (list.size() > 0) {
            Collections.sort(list, new Comparator<Bills>() {
                @Override
                public int compare(Bills o1, Bills o2) {
                    return o2.getTime().compareTo(o1.getTime());
                }
            });
            FillData(list);
        }
    }

    public void FillData(ArrayList<Bills> list){
        recyclerOder = (RecyclerView) findViewById(R.id.recyclerOder);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OderActivity.this);
        recyclerOder.setLayoutManager(linearLayoutManager);
        OderAdapter adapter = new OderAdapter(OderActivity.this, list);
        recyclerOder.setAdapter(adapter);
    }
}