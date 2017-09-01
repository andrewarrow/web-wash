package team.higher.web.cookie


import java.security.Principal
import javax.ws.rs.container.ContainerRequestContext

class CookiePrincipal(private val name: String) : Principal {

  override fun getName(): String {
    return name
  }

  fun signIn(context: ContainerRequestContext) {
    context.securityContext = CookieSecurityContext(this, context.securityContext.isSecure)
  }

  companion object {

    fun signOut(context: ContainerRequestContext) {
      context.securityContext = CookieSecurityContext(null, context.securityContext.isSecure)
    }
  }
}
