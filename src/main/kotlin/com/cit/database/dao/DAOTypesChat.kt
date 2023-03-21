package com.cit.database.dao

import com.cit.database.DAOTable
import com.cit.database.DatabaseFactory
import com.cit.database.tables.TypeChat
import com.cit.database.tables.TypeChatBody
import com.cit.database.tables.TypesChat
import org.jetbrains.exposed.sql.*

class DAOTypesChat: DAOTable<TypeChat, TypesChat, TypeChatBody>() {
    override fun resultRowToModel(row: ResultRow): TypeChat {
        return TypeChat(
            id = row[TypesChat.id],
            name = row[TypesChat.name]
        )
    }

    override suspend fun selectAll(): List<TypeChat> {
        return DatabaseFactory.pushQuery {
            TypesChat.selectAll().map { resultRowToModel(it) }
        }
    }

    override suspend fun selectSingle(where: SqlExpressionBuilder.() -> Op<Boolean>): TypeChat? {
        return DatabaseFactory.pushQuery {
            TypesChat.select(where).map { resultRowToModel(it) }.singleOrNull()
        }
    }

    override suspend fun selectMany(where: SqlExpressionBuilder.() -> Op<Boolean>): List<TypeChat> {
        return DatabaseFactory.pushQuery {
            TypesChat.select(where)
                .map(::resultRowToModel)
        }
    }

    override suspend fun delete(where: TypesChat.(ISqlExpressionBuilder) -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            TypesChat.deleteWhere(op = where) > 0
        }
    }

    override suspend fun insert(model: TypeChatBody): TypeChat? {
        return DatabaseFactory.pushQuery {
            TypesChat.insert {
                it[name] = model.name
            }.resultedValues?.singleOrNull()?.let(::resultRowToModel)
        }
    }

    override suspend fun edit(model: TypeChatBody, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            TypesChat.update(where) {
                it[name] = model.name
            } > 0
        }
    }
}