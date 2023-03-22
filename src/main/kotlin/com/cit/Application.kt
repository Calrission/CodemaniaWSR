package com.cit

import com.cit.utils.LocalPropertiesUtils.Companion.parseArgsLocalProperties
import com.cit.database.DatabaseFactory.initDataBase
import com.cit.database.controllers.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.cit.plugins.*
import com.cit.utils.LocalPropertiesUtils.Companion.getLocalProperty
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.doublereceive.*

val coursesController = CoursesController()
val catalogController = CatalogController()
val tagsController = TagsController()
val identityController = IdentityController()
val usersController = UsersController()

fun main(args: Array<String>) {

    parseArgsLocalProperties(args)

    embeddedServer(
        Netty,
        port = getLocalProperty("port").toInt(),
        host = getLocalProperty("host"),
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    install(DoubleReceive)

    initDataBase()

    configureSecurity()

    configureIdentityRouting()
    configureCatalog()
    configureFilesRouting()
}
