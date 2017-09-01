package team.higher.web.resource

import team.higher.web.cookie.CookiePrincipal
import team.higher.web.cookie.SetCookie
import team.higher.web.cookie.IgnoreAuthentication
import team.higher.web.client.*
import io.reactivex.Maybe
import io.reactivex.Single
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.validation.constraints.NotNull
import javax.ws.rs.*
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import java.net.URI
import java.util.Base64

@Path("/user")
@Produces(MediaType.TEXT_HTML)
class UserResource @Inject constructor(
  private val userClient: UserClient,
  private val washClient: WashClient
) {

  val logger = LoggerFactory.getLogger(UserResource::class.java)

  @IgnoreAuthentication
  @SetCookie
  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  fun login(
    @Context context: ContainerRequestContext,
    @FormParam("username") userId: String,
    @FormParam("password") password: String
  ): Maybe<Response> {
    return userClient
      .getCurrentUser(userId)
      .map { user ->

        val userName = user.name ?: ""
        val userEmail = user.email ?: ""
        val json = "{\"user_id\": \"$userId\", \"name\": \"$userName\", \"email\": \"$userEmail\"}"
        val base64 = Base64.getEncoder().encodeToString(json.toByteArray())

        val principal = CookiePrincipal(base64)
        principal.signIn(context)
      }
      .map { _ ->
        Response
          .seeOther(URI.create("/wash/dashboard"))
          .build()
      }
  }

  @SetCookie
  @POST
  @Path("logout")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  fun logout(
    @Context context: ContainerRequestContext
  ): Response {
    CookiePrincipal.signOut(context)
    return Response.seeOther(URI.create("/")).build()
  }
}
