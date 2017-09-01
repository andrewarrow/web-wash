package team.higher.web.client

import team.higher.web.model.User
import io.reactivex.Maybe
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import com.fasterxml.jackson.annotation.JsonProperty

interface WashClient {

  @GET("wash/user")
  fun getCurrentUser(
    @Header("User-Id") id: String
  ): Maybe<User>

}

