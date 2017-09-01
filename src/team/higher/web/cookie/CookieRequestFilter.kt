package team.higher.web.cookie

import java.io.IOException
import javax.ws.rs.WebApplicationException
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerRequestFilter
import javax.ws.rs.container.ResourceInfo
import javax.ws.rs.core.Context

/**
 * TODO: Parse cookie format and evaluate expiration, max-age etc.
 */
class CookieRequestFilter : ContainerRequestFilter {

  @Context
  lateinit var api: ResourceInfo

  @Throws(IOException::class)
  override fun filter(context: ContainerRequestContext) {
    if (api.resourceMethod.isAnnotationPresent(IgnoreAuthentication::class.java)) {
      return
    }
    val cookie = context.cookies[COOKIE_NAME] ?: throw WebApplicationException("In a wrong place buddy :)")
    println("cookie=" + cookie)
  }

  companion object {

    val COOKIE_NAME = "User-Id"
  }
}
