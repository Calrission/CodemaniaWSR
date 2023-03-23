package com.cit.models

import com.cit.database.tables.User
import io.ktor.server.websocket.*

data class Connection(
    val session: WebSocketServerSession,
    val user: User
)