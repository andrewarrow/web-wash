package team.higher.web.runtime.config

import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration
import io.dropwizard.client.JerseyClientConfiguration

class ServerConfiguration: Configuration() {

  @JsonProperty("environment")
  lateinit var environment: String

  @JsonProperty("api_url")
  lateinit var apiUrl: String

  // Jersey Rx client
  val client = JerseyClientConfiguration()
}
