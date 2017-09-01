package team.higher.web.utility

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class StringTest {

  @Test
  fun orElse() {
    val s: String? = null
    assertThat(s.orElse("test")).isEqualTo("test")
  }
}
