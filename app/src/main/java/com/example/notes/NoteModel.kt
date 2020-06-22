package com.example.notes

class NoteModel {
    var noteTitle: String? = null
    var noteTime: String? = null

    constructor() {}
    constructor(noteTitle: String?, noteTime: String?) {
        this.noteTitle = noteTitle
        this.noteTime = noteTime
    }

}