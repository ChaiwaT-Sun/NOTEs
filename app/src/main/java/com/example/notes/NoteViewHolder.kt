package com.example.notes

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView


class NoteViewHolder(var mView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(mView) {
    var textTitle: TextView
    var textTime: TextView
    var noteCard: CardView
    fun setNoteTitle(title: String?) {
        textTitle.text = title
    }

    fun setNoteTime(time: String?) {
        textTime.text = time
    }

    init {
        textTitle = mView.findViewById(R.id.note_title)
        textTime = mView.findViewById(R.id.note_time)
        noteCard = mView.findViewById(R.id.note_card)
    }
}