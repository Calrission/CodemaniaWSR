package com.cit.database.dao

import com.cit.database.DAOTable
import com.cit.database.DatabaseFactory
import com.cit.database.tables.Chat
import com.cit.database.tables.Chats
import org.jetbrains.exposed.sql.*

class DAOChats: DAOTable<Chat, Chats, Chat, Chat>() {
    override fun resultRowToModel(row: ResultRow): Chat {
        return Chat(
            id = row[Chats.id],
            firstIdUser = row[Chats.firstIdUser],
            secondIdUser = row[Chats.firstIdUser],
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

    override suspend fun insert(model: Chat): Chat? {
        return DatabaseFactory.pushQuery {
            Chats.insert {
                it[firstIdUser] = firstIdUser
                it[secondIdUser] = secondIdUser
            }.resultedValues?.singleOrNull()?.let(::resultRowToModel)
        }
    }

    override suspend fun edit(model: Chat, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            Chats.update(where) {
                it[firstIdUser] = firstIdUser
                it[secondIdUser] = secondIdUser
            } > 0
        }
    }
}