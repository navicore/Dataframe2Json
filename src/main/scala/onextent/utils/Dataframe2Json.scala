package onextent.utils

import org.apache.spark.sql.DataFrame
import spray.json._
import DefaultJsonProtocol._

object Dataframe2Json {

  def printToFile(content: String, location: String) =
    Some(new java.io.PrintWriter(location)).foreach{f => try{f.write(content)}finally{f.close()}}

  def apply(df: DataFrame): Option[String] = {
    val collectedData  = df.toJSON.coalesce(1).collect().mkString("\n")
    val json = "[" + ("}\n".r replaceAllIn (collectedData, "},\n")) + "]"
    val pretty = json.parseJson.prettyPrint
    Some(s"$pretty\n")
  }

  def apply(df: DataFrame, location: String): Option[String] = {
    val content = apply(df)
    printToFile(content.get, location)
    content
  }

}

