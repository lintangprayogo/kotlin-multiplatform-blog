package com.lintang.androidapp.model

import com.lintang.shared.Category
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey


open class Post : RealmObject {
    @PrimaryKey
    var _id: String = ""
    var date: Long = 0
    var title: String = ""
    var subtitle: String = ""
    var thumbnail: String = ""
    var content: String = ""
    var category:String =""
}


