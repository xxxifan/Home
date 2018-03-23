package com.xxxifan.home

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.PartialContent
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.locations.Locations
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngineEnvironment
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.io.File

object Server {

  @JvmStatic fun main(args: Array<String>) {
    if (File("server.conf").exists()) {
      val customConfig = "-config=server.conf"
      embeddedServer(Netty, commandLineEnvironment(arrayOf(*args, customConfig))).start()
    } else {
//      logd("server.conf cannot be found, use default config instead.")
      embeddedServer(Netty, 3008) { module() }.start()
    }
  }
//
//  class ExcludePropertyStateStrategy : ExclusionStrategy {
//    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
//      return clazz == PropertyState::class.java
//          || clazz?.getAnnotation(GsonIgnore::class.java) != null
//    }
//
//    override fun shouldSkipField(f: FieldAttributes?): Boolean {
//      return f?.declaredClass == PropertyState::class.java
//          || f?.getAnnotation(GsonIgnore::class.java) != null
//    }
//  }
}


fun Application.module() {
  val startTime = System.currentTimeMillis()

  install(ContentNegotiation) {
    gson {
//      setExclusionStrategies(com.xxxifan.home.Server.ExcludePropertyStateStrategy())
    }
  }
  install(Locations)
  install(PartialContent)
//  install(AutoHeader)
//  install(HttpLogging) { debug = true }
  install(StatusPages) {
    exception<Throwable> { cause ->
//      if (cause is ServerException) {
//        call.response.status(HttpStatusCode.InternalServerError)
//        call.respond(BaseResponse(null, cause.code, cause.message))
//      } else {
//        call.response.status(HttpStatusCode.InternalServerError)
//        call.respond(BaseResponse(null, ErrorCode.COMMON_ERROR))
//      }
//      loge("com.xxxifan.home.Server Uncatched Error", cause)
    }
  }
  routing {
//    resources("web")

    get("/") {
      call.respondText { "com.xxxifan.home.Server is running: ${(System.currentTimeMillis() - startTime) / 1000}s" }
    }
  }

  val engineEnvironment = environment as? ApplicationEngineEnvironment
  val config = engineEnvironment?.connectors?.firstOrNull()
  config?.run {
    println("com.xxxifan.home.Server has been started on: http://localhost:$port")
  }
}