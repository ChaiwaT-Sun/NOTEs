package com.example.notes;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class Main extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private RecyclerView mNotesList;


    private DatabaseReference fNotesDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNotesList = (RecyclerView) findViewById(R.id.notes_list);
        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(Main.this, NewNoteActivity.class);
                startActivity(newIntent);

            }
        });

       // gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, true);

        mNotesList.setHasFixedSize(true);
        mNotesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        //gridLayoutManager.setReverseLayout(true);
        //gridLayoutManager.setStackFromEnd(true);
        //mNotesList.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(0), true));

        fAuth = FirebaseAuth.getInstance();


        fNotesDatabase = FirebaseDatabase.getInstance().getReference(".info/connected");
        fNotesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean connected = dataSnapshot.getValue(Boolean.class);
                if (connected) {

                    Log.e("test connect","connect");
                } else {
                    Log.e("test connect","not connected");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("test connect","Listener was cancelled");
            }
        });


        if (fAuth.getCurrentUser() != null) {
            fNotesDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(fAuth.getCurrentUser().getUid());
        }

        updateUI();
        loadData();
    }


    @Override
    public void onStart() {
        super.onStart();


    }


    private void loadData() {
        Query query = fNotesDatabase.orderByValue();
        FirebaseRecyclerAdapter<NoteModel, NoteViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<NoteModel, NoteViewHolder>(

                NoteModel.class,
                R.layout.single_note_layout,
                NoteViewHolder.class,
                query

        ) {
            @Override
            protected void populateViewHolder(final NoteViewHolder viewHolder, NoteModel model, int position) {
                final String noteId = getRef(position).getKey();

                fNotesDatabase.child(noteId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("title") && dataSnapshot.hasChild("timestamp")) {
                            String title = dataSnapshot.child("title").getValue().toString();
                            String timestamp = dataSnapshot.child("timestamp").getValue().toString();

                            viewHolder.setNoteTitle(title);
                            //viewHolder.setNoteTime(timestamp);

                            GetTimeAgo getTimeAgo = new GetTimeAgo();
                            viewHolder.setNoteTime(getTimeAgo.getTimeAgo(Long.parseLong(timestamp), getApplicationContext()));

                            viewHolder.noteCard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Main.this, NewNoteActivity.class);
                                    intent.putExtra("noteId", noteId);
                                    startActivity(intent);
                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        mNotesList.setAdapter(firebaseRecyclerAdapter);
    }

    private void updateUI(){

        if (fAuth.getCurrentUser() != null){
            Log.i("MainActivity", "fAuth != null");
        } else {
            Intent startIntent = new Intent(Main.this, StartActivity.class);
            startActivity(startIntent);
            finish();
            Log.i("MainActivity", "fAuth == null");
        }

    }

 //   @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        Log.e("menu", "click");
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        super.onOptionsItemSelected(item);
//
//        switch (item.getItemId()) {
//            case R.id.main_new_note_btn:
//                Intent newIntent = new Intent(Main.this, NewNoteActivity.class);
//                startActivity(newIntent);
//                break;
//        }
//
//        return true;
//    }



    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }



}
