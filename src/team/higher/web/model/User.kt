package team.higher.web.model

import com.google.gson.annotations.SerializedName

data class User(
  @SerializedName("id")
  val id: String = "1",

  @SerializedName("name")
  val name: String? = null,

  @SerializedName("email")
  val email: String? = null
)
