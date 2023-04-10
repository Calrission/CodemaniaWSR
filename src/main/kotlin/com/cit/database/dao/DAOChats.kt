package com.cit.database.dao

import com.cit.database.DAOTable
import com.cit.database.DatabaseFactory
import com.cit.database.tables.Chat
import com.cit.database.tables.Chats
import com.cit.database.tables.InsertChat
import org.jetbrains.exposed.sql.*

class DAOChats: DAOTable<Chat, Chats, InsertChat, Chat>() {
    override fun resultRowToModel(row: ResultRow): Chat {
        return Chat(
            id = row[Chats.id],
            firstIdUser = row[Chats.firstIdUser],
            secondIdUser = row[Chats.secondIdUser],
        )
    }

    override suspend fun selectAll(): List<Chat> {
        return DatabaseFactory.pushQuery {
            Chats.selectAll().map { resultRowToModel(it) }
        }
    }

    override suspend fun selectSingle(where: SqlExpressionBuilder.() -> Op<Boolean>): Chat? {
        return DatabaseFactory.pushQuery {
            Chats.select(where).map { resultRowToModel(it) }.singleOrNull()
        }
    }

    override suspend fun selectMany(where: SqlExpressionBuilder.() -> Op<Boolean>): List<Chat> {
        return DatabaseFactory.pushQuery {
            Chats.select(where)
                .map(::resultRowToModel)
        }
    }

    override suspend fun delete(where: Chats.(ISqlExpressionBuilder) -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            Chats.deleteWhere(op = where) > 0
        }
    }

    override suspend fun insert(model: InsertChat): Chat? {
        return DatabaseFactory.pushQuery {
            Chats.insert {
                it[firstIdUser] = model.firstIdUser
                it[secondIdUser] = model.secondIdUser
            }.resultedValues?.singleOrNull()?.let(::resultRowToModel)
        }
    }

    override suspend fun edit(model: Chat, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            Chats.update(where) {
                it[firstIdUser] = model.firstIdUser
                it[secondIdUser] = model.secondIdUser
            } > 0
        }
    }

    suspend fun getChat(idFirst: Int, idSecond: Int): Chat? = selectSingle {
        (Chats.firstIdUser eq idFirst).and(Chats.secondIdUser eq idSecond) or
                (Chats.secondIdUser eq idFirst).and(Chats.firstIdUser eq idSecond)
    }
    suspend fun checkExistChat(idFirst: Int, idSecond: Int): Boolean =
        getChat(idFirst, idSecond) != null
}