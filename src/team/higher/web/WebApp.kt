package team.higher.web

import team.higher.web.cookie.CookieRequestFilter
import team.higher.web.cookie.CookieResponseFilter
import team.higher.web.resource.*
import team.higher.web.runtime.config.ServerConfiguration
import com.codahale.metrics.health.HealthCheckRegistry
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import io.dropwizard.jersey.setup.JerseyEnvironment
import io.dropwizard.jetty.setup.ServletEnvironment
import io.dropwizard.setup.Environment
import net.winterly.rxjersey.server.RxJerseyServerFeature
import org.eclipse.jetty.server.session.SessionHandler
import org.eclipse.jetty.servlets.CrossOriginFilter
import org.glassfish.jersey.logging.LoggingFeature
import org.slf4j.LoggerFactory
import java.net.URI
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import javax.inject.Inject
import javax.servlet.DispatcherType
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper

class WashExceptionMapper() : ExceptionMapper<Exception> {
  val logger = LoggerFactory.getLogger(WashExceptionMapper::class.java)

  init {
  }

  override fun toResponse(e: Exception): Response {
    e.printStackTrace()
    logger.error(e.message, e)
      val html = "<html><head><title>500 error</title></head><body><h3>500 error: ${e.message}</h3><img src=\"/images/car_wash.jpg\"/></body></html>"

    if (e is java.net.ConnectException) {
      return Response.status(500).entity(html).build();
    }

    return Response.status(500).entity(html).build();
  }
}

class WebApp @Inject constructor(
  private val userResource: UserResource,
  private val welcomeResource: WelcomeResource,
  private val dashboardResource: DashboardResource,
  private val userInModelFilter: UserInModelFilter
) {

  fun run(config: ServerConfiguration, environment: Environment) {
    configureCors(environment)
    configureMapper(environment.objectMapper)
    configureRoutes(environment.jersey())
    configureLogging(environment)
    configureHealthCheck(environment.healthChecks())
    configureRx(environment.jersey())
    configureSession(environment.servlets())
    configureFilter(environment.jersey())
  }

  private fun configureFilter(environment: JerseyEnvironment) {
    environment.register(CookieRequestFilter::class.java)
    environment.register(CookieResponseFilter::class.java)
  }

  private fun configureRx(environment: JerseyEnvironment) {
    environment.register(RxJerseyServerFeature())
  }

  private fun configureSession(servlet: ServletEnvironment) {
    servlet.setSessionHandler(SessionHandler())
  }

  private fun configureHealthCheck(registry: HealthCheckRegistry) {
    val healthCheck = HealthCheckResource()
    // Our standard health check
    registry.register("template", healthCheck)
  }

  private fun configureLogging(environment: Environment) {
    environment.jersey().register(
      LoggingFeature(
        Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME),
        Level.INFO,
        LoggingFeature.Verbosity.PAYLOAD_ANY,
        LoggingFeature.DEFAULT_MAX_ENTITY_SIZE
      ))
  }

  private fun configureRoutes(jersey: JerseyEnvironment) {
    jersey.register(WashExceptionMapper())
    jersey.register(userInModelFilter)
    jersey.register(userResource)
    jersey.register(dashboardResource)
    jersey.register(welcomeResource)
  }

  private fun configureMapper(mapper: ObjectMapper) {
    // To turn camePascal to camel_pascal in JSON payload
    mapper.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
    // Register custom modules for things like OffsetDateTime
    mapper.findAndRegisterModules()
    // We need this otherwise date time will be written as number
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
  }

  private fun configureCors(environment: Environment) {
    // Enable CORS headers
    val cors = environment.servlets().addFilter("CORS", CrossOriginFilter::class.java)
    // Configure CORS parameters
    cors.setInitParameter("allowedOrigins", "*")
    cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin,User-Id")
    cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD,PATCH,PUT")
    // Add URL mapping
    cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType::class.java), true, "/*")
  }
}
