package onextent.utils

import org.apache.spark.sql.{DataFrame, SQLContext}
import scala.util.parsing.json.{JSON, JSONArray, JSONFormat, JSONObject}

object Dataframe2Json {

  def format(t: Any, i: Int = 0): String = t match {
    case o: JSONObject =>
      o.obj.map{ case (k, v) =>
        "  "*(i+1) + JSONFormat.defaultFormatter(k) + ": " + format(v, i+1)
      }.mkString("{\n", ",\n", "\n" + "  "*i + "}")
    case a: JSONArray =>
      a.list.map{
        e => "  "*(i+1) + format(e, i+1)
      }.mkString("[\n", ",\n", "\n" + "  "*i + "]")
    case _ => JSONFormat defaultFormatter t
  }

  def apply(df: DataFrame): String = {
    val str:String = df.toJSON.coalesce(1).collect().mkString("\n")
    format(JSON.parseRaw("[" + ("}\n".r replaceAllIn (str, "},\n")) + "]"))
  }

}

