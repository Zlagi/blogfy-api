@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package dev.zlagi.application

import com.auth0.jwt.interfaces.JWTVerifier
import dev.zlagi.application.auth.firebase.FirebaseAdmin
import dev.zlagi.application.plugins.*
import dev.zlagi.data.dao.UserDao
import dev.zlagi.data.database.DatabaseProviderContract
import io.ktor.application.*
import org.koin.core.annotation.KoinReflectAPI
import org.koin.ktor.ext.inject

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@OptIn(KoinReflectAPI::class)
@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    val databaseProvider by inject<DatabaseProviderContract>()
    val userDao by inject<UserDao>()
    val jwtVerifier by inject<JWTVerifier>()

    configureKoin()
    configureMonitoring()
    configureSerialization()
    configureSecurity(userDao, jwtVerifier)
    configureRouting()

    // initialize database
    databaseProvider.init()

    // initialize Firebase Admin SDK
    FirebaseAdmin.init()
}
