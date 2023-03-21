package com.cit.database.dao

import com.cit.database.DAOTable
import com.cit.database.DatabaseFactory
import com.cit.database.tables.TemplatePlan
import com.cit.database.tables.TemplatesPlans
import org.jetbrains.exposed.sql.*

class DAOTemplatesPlans: DAOTable<TemplatePlan, TemplatesPlans, TemplatePlan>() {
    override fun resultRowToModel(row: ResultRow): TemplatePlan {
        return TemplatePlan(
            idCourse = row[TemplatesPlans.idCourse],
            plan = row[TemplatesPlans.plan]
        )
    }

    override suspend fun selectAll(): List<TemplatePlan> {
        return DatabaseFactory.pushQuery {
            TemplatesPlans.selectAll().map { resultRowToModel(it) }
        }
    }

    override suspend fun selectSingle(where: SqlExpressionBuilder.() -> Op<Boolean>): TemplatePlan? {
        return DatabaseFactory.pushQuery {
            TemplatesPlans.select(where).map { resultRowToModel(it) }.singleOrNull()
        }
    }

    override suspend fun selectMany(where: SqlExpressionBuilder.() -> Op<Boolean>): List<TemplatePlan> {
        return DatabaseFactory.pushQuery {
            TemplatesPlans.select(where)
                .map(::resultRowToModel)
        }
    }

    override suspend fun insert(model: TemplatePlan): TemplatePlan? {
        return DatabaseFactory.pushQuery {
            TemplatesPlans.insert {
                it[idCourse] = model.idCourse
                it[plan] = model.plan
            }.resultedValues?.singleOrNull()?.let(::resultRowToModel)
        }
    }

    override suspend fun delete(where: TemplatesPlans.(ISqlExpressionBuilder) -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            TemplatesPlans.deleteWhere(op = where) > 0
        }
    }

    override suspend fun edit(model: TemplatePlan, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return DatabaseFactory.pushQuery {
            TemplatesPlans.update(where) {
                it[idCourse] = model.idCourse
                it[plan] = model.plan
            } > 0
        }
    }
}