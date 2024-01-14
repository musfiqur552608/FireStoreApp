package org.freedu.firestoreapp

import com.google.firebase.Timestamp

data class Story(
    var id:String?=null,
    var title:String?=null,
    val description:String?=null,
    val timeStamp:Timestamp ?= null
)
