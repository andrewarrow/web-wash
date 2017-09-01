package team.higher.web.resource

import team.higher.web.cookie.IgnoreAuthentication
import team.higher.web.view.WelcomeView
import javax.ws.rs.CookieParam
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/")
@Produces(MediaType.TEXT_HTML)
class WelcomeResource {

  @IgnoreAuthentication
  @GET
  fun index(
    @CookieParam("User-Id") cookie: String?
  ): WelcomeView {

    if (WashUser.getUserId(cookie ?: "") == "bad-cookie") {
      return WelcomeView(null)
    }

    return cookie?.let {
      WelcomeView(it)
    } ?: WelcomeView(null)
  }
}
