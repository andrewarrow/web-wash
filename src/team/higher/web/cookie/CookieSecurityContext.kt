package team.higher.web.cookie

import java.security.Principal
import javax.ws.rs.core.SecurityContext

/**
 * Security context set after a cookie authentication
 */
internal class CookieSecurityContext(
  private val cookie: CookiePrincipal?,
  private val secure: Boolean
) : SecurityContext {

  override fun getUserPrincipal(): Principal? {
    return cookie
  }

  override fun isUserInRole(role: String): Boolean {
    return false
  }

  override fun isSecure(): Boolean {
    return secure
  }

  override fun getAuthenticationScheme(): String {
    return "WASH_COOKIE"
  }
}
