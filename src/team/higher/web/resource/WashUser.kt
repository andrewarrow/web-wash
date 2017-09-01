package team.higher.web.resource

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.readValue
import java.util.Base64
import java.nio.charset.Charset

data class WashUser(
  @field:JsonProperty("user_id")
  val userId: String, 

  @field:JsonProperty("name")
  val name: String, 

  @field:JsonProperty("email")
  val email: String)
{

  companion object {

    fun isMobile(agent: String): Boolean {
      val s = agent.toLowerCase()
      if (s.contains("mobile")) {
        return true
      }
      if (s.contains("iphone")) {
        return true
      }
      return false
    }

    fun getUserObject(rawData: String): WashUser? {
      try {
        val base64 = Base64.getDecoder().decode(rawData)
        val json = base64.toString(Charset.defaultCharset())
        val mapper = com.fasterxml.jackson.module.kotlin.jacksonObjectMapper()
        val washUser: WashUser = mapper.readValue<WashUser>(json)
        return washUser
      } catch (e: Exception) {
        return null
      }
    }

    fun getUserId(rawData: String): String {
      val bu = getUserObject(rawData)
      if (bu == null) {
        return "bad-cookie"
      }
      return bu.userId
    }

    fun getName(rawData: String): String {
      val bu = getUserObject(rawData)
      if (bu == null) {
        return "bad-cookie"
      }

      if (bu.name == "") {
        return bu.email
      }

      return bu.name
    }

  }
}
