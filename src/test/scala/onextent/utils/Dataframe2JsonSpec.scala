package onextent.utils
import au.com.bytecode.opencsv.CSVParser
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import org.scalatest._

trait TestData {
  val data1: List[String] = List(
    "this,is,valid,data",
    "this,is,in-valid,data"
  )
}

class Dataframe2JsonSpec extends FlatSpec with Matchers with BeforeAndAfter with TestData {

  def schema(types: Array[String], cols: Array[String]) = {
    val datatypes = types.map {
      case "String" => StringType
      case "Long" => LongType
      case "Double" => DoubleType
      // Add more types here based on your data.
      case _ => StringType
    }
    StructType(cols.indices.map(x => StructField(cols(x), datatypes(x))).toArray)
  }

  def df(data: List[String], types: Array[String], cols: Array[String], sc: SparkContext) = {
    val myRdd = sc.parallelize(data)
    //val split = myRdd.map(line => {
    val split = myRdd.map(line => {
      val parser = new CSVParser(',')
      parser.parseLine(line)
    }
    ) //failing due to serialize
    val rdd = split.map(arr => Row(arr(0), arr(1), arr(2), arr(3)))
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    sqlContext.createDataFrame(rdd, schema(types, cols))
  }

  private val master = "local[2]"
  private val appName = "Dataframe2Json-tests"
  private var sc: SparkContext = _

  before {
    val conf = new SparkConf()
      .setMaster(master)
      .setAppName(appName)

    sc = new SparkContext(conf)
  }

  after {
    if (sc != null) {
      sc.stop()
    }
  }

  "a string" should "have converted a length" in {
    val json = """ [] """
    json should have length 4
  }

  "a dataframe" should "have converted to a json string" in {
    val myDf = df(
      data1,
      Array("String", "String", "String", "String"),
      Array("One", "Two", "Three", "Four"),
      sc
    )
    //val json = Dataframe2Json(myDf, "/tmp/seeme.json")
    val json = Dataframe2Json(myDf)
    println(s"my json: $json.get")
    json.get should have length 162
  }

}
