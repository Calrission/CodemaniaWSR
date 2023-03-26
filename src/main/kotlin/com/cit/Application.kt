package com.cit

import com.cit.utils.LocalPropertiesUtils.Companion.parseArgsLocalProperties
import com.cit.database.DatabaseFactory.initDataBase
import com.cit.database.controllers.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.cit.plugins.*
import com.cit.utils.LocalPropertiesUtils.Companion.getLocalProperty
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.websocket.*
import kotlinx.serialization.json.Json
import java.time.Duration

val coursesController = CoursesController()
val catalogController = CatalogController()
val tagsController = TagsController()
val identityController = IdentityController()
val usersController = UsersController()
val materialsController = MaterialsController()
val workSpaceController = WorkSpaceController()
val lessonsController = LessonsController()

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
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    initDataBase()

    configureIdentityRouting()
    configureCatalog()
    configureFilesRouting()
    configureChat()
    configureWorkSpace()
}
