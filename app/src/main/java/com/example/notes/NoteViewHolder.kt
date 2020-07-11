package com.example.notes

import android.text.format.DateFormat
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import java.util.*


class NoteViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
    var textTitle: TextView
    var textTime: TextView
    var noteCard: CardView
    fun setNoteTitle(title: String?) {
        textTitle.text = title
    }

    fun setNoteTime(time: String) {
        textTime.text = time
    }

    init {
        textTitle = mView.findViewById(R.id.note_title)
        textTime = mView.findViewById(R.id.note_time)
        noteCard = mView.findViewById(R.id.note_card)
    }



    fun getDate(timestamp: Long) :String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp * 1000L
        val date = DateFormat.format("dd-MM-yyyy",calendar).toString()
        return date
    }
}