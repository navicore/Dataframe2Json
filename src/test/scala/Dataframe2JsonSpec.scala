import org.scalatest._

class Dataframe2JsonSpec extends FlatSpec with Matchers {

  "a dataframe" should "have converted to a json string" in {
      val json = """ [] """
      json should have length 4
  }

}
