package com.example.notes

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class NewNoteActivity : AppCompatActivity() {
    private var btnCreate: Button? = null
    private var etTitle: EditText? = null
    private var etContent: EditText? = null
    private var fAuth: FirebaseAuth? = null
    private var fNotesDatabase: DatabaseReference? = null
    private var noteID: String? = null
    private var isExist = false
    var switchR: Switch? = null
    override fun onBackPressed() {
        super.onBackPressed()
        Log.e("back >>", "test back action ")
        val title = etTitle!!.text.toString().trim { it <= ' ' }
        val content = etContent!!.text.toString().trim { it <= ' ' }
        Log.e("check TEXT ", "$title|$content")
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
            createNote(title, content)
        } else {
            Toast.makeText(applicationContext, "Fill empty fields NEW", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)
        // Log.e("slidr >>>>>>>>", String.valueOf(slidr.equals(Slidr.attach(this).toString())));
        try {
            noteID = intent.getStringExtra("noteId")

            //Toast.makeText(this, noteID, Toast.LENGTH_SHORT).show();
            isExist = this.noteID!!.trim { it <= ' ' } != ""
        } catch (e: Exception) {
            e.printStackTrace()
        }
        btnCreate = findViewById<View>(R.id.new_note_btn) as Button
        etTitle = findViewById<View>(R.id.new_note_title) as EditText
        etContent = findViewById<View>(R.id.new_note_content) as EditText


//        switchR = findViewById(R.id.switchbuttom);
//
//        switchR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    Log.e("Switch","on");
//                }
//                else {
//                    Log.e("Switch","off");
//                }
//            }
//        });
        fAuth = FirebaseAuth.getInstance()
        fNotesDatabase = FirebaseDatabase.getInstance().reference.child("Notes").child(fAuth!!.currentUser!!.uid)
        btnCreate!!.setOnClickListener(View.OnClickListener { view ->
            val title = etTitle!!.text.toString().trim { it <= ' ' }
            val content = etContent!!.text.toString().trim { it <= ' ' }
            Log.e("check TEXT ", "$title|$content")
            if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
                createNote(title, content)
            } else {
                Snackbar.make(view, "Fill empty fields NEW", Snackbar.LENGTH_SHORT).show()
            }
        })
        val fab = findViewById<FloatingActionButton>(R.id.fab_delete)
        fab.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                if (isExist) {
                    deleteNote()
                } else {
                    Toast.makeText(applicationContext, "Nothing to delete", Toast.LENGTH_SHORT).show()
                    val backhome = Intent(this@NewNoteActivity, Main::class.java)
                    startActivity(backhome)
                }
            }
        })
        putData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.new_note_menu, menu)
        return true
    }

    private fun putData() {
        if (isExist) {
            fNotesDatabase!!.child((noteID)!!).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.hasChild("title") && dataSnapshot.hasChild("content")) {
                        val title = dataSnapshot.child("title").value.toString()
                        val content = dataSnapshot.child("content").value.toString()
                        etTitle!!.setText(title)
                        etContent!!.setText(content)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }

    private fun createNote(title: String, content: String) = if (fAuth!!.currentUser != null) {
        if (isExist) {
            // UPDATE A NOTE
            val updateMap: MutableMap<String, String> = HashMap<String, String>()
            updateMap["title"] = etTitle!!.text.toString().trim { it <= ' ' }
            updateMap["content"] = etContent!!.text.toString().trim { it <= ' ' }
            updateMap["timestamp"] = System.currentTimeMillis().toString()

            fNotesDatabase!!.child((noteID)!!).updateChildren(updateMap as Map<String, Any>)
            Toast.makeText(this, "Note updated0 ${ServerValue.TIMESTAMP.toString()}", Toast.LENGTH_SHORT).show()
        } else {
            // CREATE A NEW NOTE
            val newNoteRef = fNotesDatabase!!.push()
            val noteMap: MutableMap<String, String> = HashMap<String, String>()
            noteMap["title"] = title
            noteMap["content"] = content
            noteMap["timestamp"] = System.currentTimeMillis().toString()
            val mainThread = Thread(object : Runnable {
                override fun run() {
                    newNoteRef.setValue(noteMap).addOnCompleteListener(object : OnCompleteListener<Void?> {
                        override fun onComplete(task: Task<Void?>) {
                            if (task.isSuccessful) {
                                Toast.makeText(this@NewNoteActivity, "Note added to database${ServerValue.TIMESTAMP.toString()}", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@NewNoteActivity, "ERROR: " + task.exception!!.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                }
            })
            mainThread.start()
        }
    } else {
        Toast.makeText(this, "USERS IS NOT SIGNED IN", Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.new_note_delete_btn -> if (isExist) {
                deleteNote()
            } else {
                Toast.makeText(this, "Nothing to delete", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    private fun deleteNote() {
        fNotesDatabase!!.child((noteID)!!).removeValue().addOnCompleteListener(object : OnCompleteListener<Void?> {
            override fun onComplete(task: Task<Void?>) {
                if (task.isSuccessful) {
                    Toast.makeText(this@NewNoteActivity, "Note Deleted", Toast.LENGTH_SHORT).show()
                    noteID = "no"
                    finish()
                } else {
                    Log.e("NewNoteActivity", task.exception.toString())
                    Toast.makeText(this@NewNoteActivity, "ERROR: " + task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}