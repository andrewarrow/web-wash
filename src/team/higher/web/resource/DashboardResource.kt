package team.higher.web.resource

import team.higher.web.client.*
import team.higher.web.view.DashboardView
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.rxkotlin.Singles
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/dashboard")
@Produces(MediaType.TEXT_HTML)
class DashboardResource @Inject constructor(
  private val userClient: UserClient
) {

  @GET
  fun map(
    @CookieParam("User-Id") cookieData: String): DashboardView {
    val user_id = WashUser.getUserId(cookieData)
    return DashboardView(userClient.getSomething(user_id)) 
  }

}

