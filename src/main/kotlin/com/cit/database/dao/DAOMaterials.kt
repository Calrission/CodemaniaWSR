package com.cit.database.dao

import com.cit.database.DAOTable
import com.cit.database.DatabaseFactory.pushQuery
import com.cit.database.tables.*
import org.jetbrains.exposed.sql.*

class DAOMaterials: DAOTable<Material, Materials, MaterialBody>() {
    override fun resultRowToModel(row: ResultRow): Material {
        return Material(
            id = row[Materials.id],
            title = row[Materials.title],
            cover = row[Materials.cover],
            idCourse = row[Materials.idCourse],
            url = row[Materials.url]
        )
    }

    override suspend fun selectAll(): List<Material> {
        return pushQuery {
            Materials.selectAll().map { resultRowToModel(it) }
        }
    }

    override suspend fun selectSingle(where: SqlExpressionBuilder.() -> Op<Boolean>): Material? {
        return pushQuery {
            Materials.select(where).map { resultRowToModel(it) }.singleOrNull()
        }
    }

    override suspend fun selectMany(where: SqlExpressionBuilder.() -> Op<Boolean>): List<Material> {
        return pushQuery {
            Materials.select(where)
                .map(::resultRowToModel)
        }
    }

    override suspend fun delete(where: Materials.(ISqlExpressionBuilder) -> Op<Boolean>): Boolean {
        return pushQuery {
            Materials.deleteWhere(op = where) > 0
        }
    }

    override suspend fun insert(model: MaterialBody): Material? {
        return pushQuery {
            Materials.insert {
                it[cover] = model.cover
                it[url] = model.url
                it[title] = model.title
                it[idCourse] = model.idCourse
            }.resultedValues?.singleOrNull()?.let(::resultRowToModel)
        }
    }

    override suspend fun edit(model: MaterialBody, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return pushQuery {
            Materials.update(where) {
                it[cover] = model.cover
                it[url] = model.url
                it[title] = model.title
                it[idCourse] = model.idCourse
            } > 0
        }
    }

}