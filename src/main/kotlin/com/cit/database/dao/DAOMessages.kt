package com.cit.database.dao

import com.cit.database.DAOTable
import com.cit.database.DatabaseFactory
import com.cit.database.tables.Message
import com.cit.database.tables.InsertMessage
import com.cit.database.tables.Messages
import org.jetbrains.exposed.sql.*

class DAOMessages: DAOTable<Message, Messages, InsertMessage, InsertMessage>() {
    override fun resultRowToModel(row: ResultRow): Message {
        return Message(
            id = row[Messages.id],
            text = row[Messages.text],
            idChat = row[Messages.idChat],
            datetime = row[Messages.datetime],
            isAudio = row[Messages.isAudio],
            idUser = row[Messages.idUser]
        )
    }

    override suspend fun selectAll(): List<Message> {
        return DatabaseFactory.pushQuery {
            Messages.selectAll().map { resultRowToModel(it) }
        }
    }

    override suspend fun selectSingle(where: SqlExpressionBuilder.() -> Op<Boolean>): Message? {
        return DatabaseFactory.pushQuery {
            Messages.select(where).map { resultRowToModel(it) }.singleOrNull()
        }
    }

    override suspend fun selectMany(where: SqlExpressionBuilder.() -> Op<Boolean>): List<Message> {
        return DatabaseFactory.pushQuery {
            Messages.select(where)
                .map(::resultRowToModel)
        }
    }

    override suspend fun delete(where: Messages.(ISqlExpressionBuilder) -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            Messages.deleteWhere(op = where) > 0
        }
    }

    override suspend fun insert(model: InsertMessage): Message? {
        return DatabaseFactory.pushQuery {
            Messages.insert {
                it[idUser] = model.idUser
                it[idChat] = model.idChat
                it[isAudio] = model.isAudio
                it[datetime] = model.datetime
                it[text] = model.text
            }.resultedValues?.singleOrNull()?.let(::resultRowToModel)
        }
    }

    override suspend fun edit(model: InsertMessage, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            Messages.update(where) {
                it[idUser] = model.idUser
                it[idChat] = model.idChat
                it[isAudio] = model.isAudio
                it[datetime] = model.datetime
                it[text] = model.text
            } > 0
        }
    }
}