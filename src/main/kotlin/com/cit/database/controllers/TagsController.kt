package com.cit.database.controllers

import com.cit.database.dao.DAOTags
import com.cit.database.dao.DAOTagsCourse
import com.cit.database.tables.Tag
import com.cit.database.tables.Tags
import com.cit.database.tables.TagsCourses

class TagsController {
    private val daoTags = DAOTags()
    private val daoTagsCourse = DAOTagsCourse()

    suspend fun getALlTags(): List<Tag> = daoTags.selectAll()

    suspend fun getTagsCourse(idCourse: Int) = daoTagsCourse.selectMany {
        TagsCourses.idCourse eq idCourse }.mapNotNull {daoTags.selectSingle { Tags.id eq it.idTag}
    }


}