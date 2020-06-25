package com.example.notes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Main : AppCompatActivity() {
    private var fAuth: FirebaseAuth? = null
    private var mNotesList: RecyclerView? = null
    private var fNotesDatabase: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mNotesList = findViewById<View>(R.id.notes_list) as RecyclerView
        val fab = findViewById<FloatingActionButton>(R.id.fab_add)
        fab.setOnClickListener {
            val newIntent = Intent(this@Main, NewNoteActivity::class.java)
            startActivity(newIntent)
        }
        // gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, true);
        mNotesList!!.setHasFixedSize(true)
        mNotesList!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        //gridLayoutManager.setReverseLayout(true);
        //gridLayoutManager.setStackFromEnd(true);
        //mNotesList.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(0), true));
        fAuth = FirebaseAuth.getInstance()
        fNotesDatabase = FirebaseDatabase.getInstance().getReference(".info/connected")
        fNotesDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val connected = dataSnapshot.getValue(Boolean::class.java)!!
                if (connected) {
                    Log.e("test connect", "connect")
                } else {
                    Log.e("test connect", "not connected")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("test connect", "Listener was cancelled")
            }
        })
        if (fAuth!!.currentUser != null) {
            fNotesDatabase = FirebaseDatabase.getInstance().reference.child("Notes").child(fAuth!!.currentUser!!.uid)
        }
        updateUI()
        loadData()
    }

    public override fun onStart() {
        super.onStart()
    }

    private fun loadData() {
//        val query = fNotesDatabase!!.orderByValue()
//        val firebaseRecyclerAdapter: FirebaseRecyclerAdapter<NoteModel, NoteViewHolder> = object : FirebaseRecyclerAdapter<NoteModel?, NoteViewHolder>(
//                NoteModel::class.java,
//                R.layout.single_note_layout,
//                NoteViewHolder::class.java,
//                query
//        ) {
//            override fun populateViewHolder(viewHolder: NoteViewHolder, model: NoteModel?, position: Int) {
//                val noteId = getRef(position).key
//                fNotesDatabase!!.child(noteId!!).addValueEventListener(object : ValueEventListener {
//                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//                        if (dataSnapshot.hasChild("title") && dataSnapshot.hasChild("timestamp")) {
//                            val title = dataSnapshot.child("title").value.toString()
//                            val timestamp = dataSnapshot.child("timestamp").value.toString()
//                            viewHolder.setNoteTitle(title)
//                            //viewHolder.setNoteTime(timestamp);
//                            val getTimeAgo = GetTimeAgo()
//                            viewHolder.setNoteTime(getTimeAgo.getTimeAgo(timestamp.toLong(), applicationContext))
//                            viewHolder.noteCard.setOnClickListener {
//                                val intent = Intent(this@Main, NewNoteActivity::class.java)
//                                intent.putExtra("noteId", noteId)
//                                startActivity(intent)
//                            }
//                        }
//                    }
//
//                    override fun onCancelled(databaseError: DatabaseError) {}
//                })
//            }
//        }
//        mNotesList!!.adapter = firebaseRecyclerAdapter
    }

    private fun updateUI() {
        if (fAuth!!.currentUser != null) {
            Log.i("MainActivity", "fAuth != null")
        } else {
            val startIntent = Intent(this@Main, StartActivity::class.java)
            startActivity(startIntent)
            finish()
            Log.i("MainActivity", "fAuth == null")
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
    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics))
    }
}