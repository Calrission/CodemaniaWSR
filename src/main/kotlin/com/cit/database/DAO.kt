package com.cit.database

import org.jetbrains.exposed.sql.*

/*
R: Row table
T: Table
M: Model for insert request
P: Model for path request
 */
interface DAO <R, T: Table, M, P> {
    fun resultRowToModel(row: ResultRow): R
    suspend fun selectAll(): List<R>
    suspend fun selectSingle(where: SqlExpressionBuilder.() -> Op<Boolean>): R?
    suspend fun selectMany(where: SqlExpressionBuilder.() -> Op<Boolean>): List<R>
    suspend fun edit(model: P, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean
    suspend fun insert(model: M): R?
    suspend fun delete(where: T.(ISqlExpressionBuilder) -> Op<Boolean>): Boolean
    suspend fun editAndSelect(model: P, where: SqlExpressionBuilder.() -> Op<Boolean>): R?
}

abstract class DAOTable <R, T: Table, M, P>: DAO<R, T, M, P> {
    override suspend fun editAndSelect(model: P, where: SqlExpressionBuilder.() -> Op<Boolean>): R? {
        edit(model, where)
        return selectSingle(where)
    }

}