package team.higher.web.model

import com.google.gson.annotations.SerializedName

data class Wash(
  @SerializedName("id")
  val id: String = "1",

  @SerializedName("name")
  val name: String? = null
)
