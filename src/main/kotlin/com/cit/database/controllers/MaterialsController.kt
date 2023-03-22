package com.cit.database.controllers

import com.cit.database.dao.DAOMaterials
import com.cit.database.tables.Material

class MaterialsController {
    private val daoMaterials = DAOMaterials()

    suspend fun getAllMaterials(): List<Material> = daoMaterials.selectAll()
}