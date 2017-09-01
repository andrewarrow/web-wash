package team.higher.web.resource

import java.io.IOException
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.*
import team.higher.web.view.BaseView

class UserInModelFilter : ContainerResponseFilter {

  @Throws(IOException::class)
  override fun filter(request: ContainerRequestContext,
                      response: ContainerResponseContext) {
    

    if (response.getEntity() is BaseView) {
      val userData = request.getCookies().get("User-Id")!!.getValue()
      val agentData = request.getHeaderString("User-Agent")

      val v = response.getEntity() as BaseView
      v.who = WashUser.getName(userData)
      v.mobile = WashUser.isMobile(agentData)
      response.setEntity(v)
    }

  }
}
