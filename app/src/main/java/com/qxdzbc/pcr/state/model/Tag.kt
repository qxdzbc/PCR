package com.qxdzbc.pcr.state.model

import com.qxdzbc.pcr.database.model.DbTag
import com.qxdzbc.pcr.firestore.TagDoc

interface Tag {
    val tagId:String
    val name:String
    val isUploaded:Boolean
    val writeState:WriteState
    fun setWriteState(i:WriteState):Tag
    fun toDbTag():DbTag{
        if(this is DbTag){
            return this
        }else{
            return DbTag(
                id = tagId, name = name, isUploadedInternal = if(isUploaded) 1 else 0,
                state = this.writeState.name
            )
        }
    }

    fun toTagDoc():TagDoc{
        return TagDoc(
            id =tagId,
            name = name
        )
    }

    fun setName(newTagName: String): Tag
}

