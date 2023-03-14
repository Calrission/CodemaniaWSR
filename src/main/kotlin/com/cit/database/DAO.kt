package com.cit.database

import org.jetbrains.exposed.sql.*

/*
R: Row table
T: Table
M: Model for insert request
 */
interface DAO <R, T: Table, M> {
    fun resultRowToModel(row: ResultRow): R
    suspend fun selectAll(): List<R>
    suspend fun selectSingle(where: SqlExpressionBuilder.() -> Op<Boolean>): R?
    suspend fun selectMany(where: SqlExpressionBuilder.() -> Op<Boolean>): List<R>
    suspend fun edit(model: M, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean
    suspend fun insert(model: M): R?
    suspend fun delete(where: T.(ISqlExpressionBuilder) -> Op<Boolean>): Boolean
    suspend fun editAndSelect(model: M, where: SqlExpressionBuilder.() -> Op<Boolean>): R?
}

abstract class DAOTable <R, T: Table, M>: DAO<R, T, M> {
    override suspend fun editAndSelect(model: M, where: SqlExpressionBuilder.() -> Op<Boolean>): R? {
        edit(model, where)
        return selectSingle(where)
    }

}