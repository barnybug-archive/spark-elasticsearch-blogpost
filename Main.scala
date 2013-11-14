import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.hadoop.io.{MapWritable, Text, NullWritable}
import org.elasticsearch.hadoop.mr.ESOutputFormat

object Main extends App {
  // create the spark context
  val sc = new SparkContext("local", "Syslog example", jars=List())

  // define regex for parsing syslog entries
  val re = """(\w{3}\s+\d{2} \d{2}:\d{2}:\d{2}) (\w+) (\S+)\[(\d+)\]: (.+)""".r
  // open /var/log/syslog
  val syslog = sc.textFile("/var/log/syslog")
  // parse each line
  val entries = syslog.collect { case re(timestamp, hostname, process, pid, message) =>
      Map("timestamp" -> timestamp, "hostname" -> hostname, "process" -> process, "pid" -> pid, "message" -> message)
  }
  // convert to Writables
  val writables = entries.map(toWritable)
  // message the types so the collection is typed to an OutputFormat[Object, Object]
  val output = writables.map { v => (NullWritable.get.asInstanceOf[Object], v.asInstanceOf[Object]) }
  // index the data to elasticsearch
  sc.hadoopConfiguration.set("es.resource", "syslog/entry")
  output.saveAsHadoopFile[ESOutputFormat]("-")

  // helper function to convert Map to a Writable
  def toWritable(map: Map[String, String]) = {
      val m = new MapWritable
      for ((k, v) <- map)
          m.put(new Text(k), new Text(v))
      m
  }
}