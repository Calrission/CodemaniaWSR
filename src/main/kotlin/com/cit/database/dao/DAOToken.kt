package com.cit.database.dao

import com.cit.database.DAOTable
import com.cit.database.DatabaseFactory
import com.cit.database.tables.Token
import com.cit.database.tables.TokenBody
import com.cit.database.tables.Tokens
import org.jetbrains.exposed.sql.*

class DAOToken: DAOTable<Token, Tokens, TokenBody, TokenBody>() {
    override fun resultRowToModel(row: ResultRow): Token {
        return Token(
            id = row[Tokens.id],
            token = row[Tokens.token],
            idUser = row[Tokens.idUser]
        )
    }

    override suspend fun selectAll(): List<Token> {
        return DatabaseFactory.pushQuery {
            Tokens.selectAll().map { resultRowToModel(it) }
        }
    }

    override suspend fun selectSingle(where: SqlExpressionBuilder.() -> Op<Boolean>): Token? {
        return DatabaseFactory.pushQuery {
            Tokens.select(where).map { resultRowToModel(it) }.singleOrNull()
        }
    }

    override suspend fun selectMany(where: SqlExpressionBuilder.() -> Op<Boolean>): List<Token> {
        return DatabaseFactory.pushQuery {
            Tokens.select(where)
                .map(::resultRowToModel)
        }
    }

    override suspend fun insert(model: TokenBody): Token? {
        return DatabaseFactory.pushQuery {
            Tokens.insert {
                it[token] = model.token
                it[idUser] = model.idUser
            }.resultedValues?.singleOrNull()?.let(::resultRowToModel)
        }
    }

    override suspend fun delete(where: Tokens.(ISqlExpressionBuilder) -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            Tokens.deleteWhere(op = where) > 0
        }
    }

    override suspend fun edit(model: TokenBody, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            Tokens.update(where) {
                it[token] = model.token
                it[idUser] = model.idUser
            } > 0
        }
    }
}