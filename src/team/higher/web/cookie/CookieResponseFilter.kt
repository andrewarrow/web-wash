package team.higher.web.cookie

import java.io.IOException
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerResponseContext
import javax.ws.rs.container.ContainerResponseFilter
import javax.ws.rs.container.ResourceInfo
import javax.ws.rs.core.Context

/**
 * TODO: Implement standard cookie format
 */
class CookieResponseFilter : ContainerResponseFilter {
  private val cookieName: String = CookieRequestFilter.COOKIE_NAME

  private val deleteCookie: String
  private val secure = false
  private val httpOnly = false
  private val sessionCookieFormat: String
  private val persistentCookieFormat: String
  @Context
  lateinit var api: ResourceInfo

  init {
    val cookieFormatBuilder = StringBuilder(cookieName).append(COOKIE_TEMPLATE)
    if (secure) {
      cookieFormatBuilder.append(SECURE_FLAG)
    }
    if (httpOnly) {
      cookieFormatBuilder.append(HTTP_ONLY_FLAG)
    }
    this.sessionCookieFormat = cookieFormatBuilder.toString()
    this.persistentCookieFormat = sessionCookieFormat + "; Max-Age=31536000;" // seconds in a year
    this.deleteCookie = cookieName + DELETE_COOKIE_TEMPLATE
  }

  @Throws(IOException::class)
  override fun filter(request: ContainerRequestContext, response: ContainerResponseContext) {
    if (api.resourceMethod.isAnnotationPresent(SetCookie::class.java)) {
      val principal = request.securityContext.userPrincipal
      if (principal != null) {
        if (request.securityContext is CookieSecurityContext) {
          val cookie = String.format(persistentCookieFormat, principal.name)
          response.headers.add("Set-Cookie", cookie)
        } else if (request.cookies.containsKey(cookieName)) {
          response.headers.add("Set-Cookie", deleteCookie)
        }
      } else {
        response.headers.add("Set-Cookie", deleteCookie)
      }
    }
  }

  companion object {

    private val COOKIE_TEMPLATE = "=%s; Path=/"
    private val SECURE_FLAG = "; Secure"
    private val HTTP_ONLY_FLAG = "; HttpOnly"
    private val DELETE_COOKIE_TEMPLATE = "=; Path=/; expires=Thu, 01-Jan-70 00:00:00 GMT"
  }
}
