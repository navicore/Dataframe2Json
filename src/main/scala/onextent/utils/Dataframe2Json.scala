package onextent.utils

import org.apache.spark.sql.DataFrame
import play.api.libs.json._

object Dataframe2Json {

  def printToFile(content: String, location: String) =
    Some(new java.io.PrintWriter(location)).foreach{f => try{f.write(content)}finally{f.close()}}

  def apply(df: DataFrame): Option[String] = {
    val str:String = df.toJSON.coalesce(1).collect().mkString("\n")
    val json:String = "[" + ("}\n".r replaceAllIn (str, "},\n")) + "]"
    val readableString: String = Json.prettyPrint(Json.parse(json))
    Some(s"$readableString\n")
  }

  def apply(df: DataFrame, location: String): Option[String] = {
    val content = apply(df)
    printToFile(content.get, location)
    content
  }

}

