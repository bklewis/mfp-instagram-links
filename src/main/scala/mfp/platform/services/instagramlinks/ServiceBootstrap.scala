package mfp.platform.services.instagramlinks

object ScalaJdbcConnectSelect extends App{

  override def main(args: Array[String]): Unit = {
    import java.sql.{DriverManager, ResultSet}

    // Change to Your Database Config
    val conn_str = "jdbc:mysql://localhost:8889/ig_links?user=blewis&password=testdb"

    // Load the driver
    classOf[com.mysql.jdbc.Driver]

    // Setup the connection
    val conn = DriverManager.getConnection(conn_str)
    try {
      // Configure to be Read Only
      val statement = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)

      val prep = conn.prepareStatement("INSERT INTO ig_links" +
        "(url, hashtag_id, ig_username, ig_postdate, status, admin_username, created_at, updated_at) VALUES " +
        "('https://instagram.com/p/4P239PExhr/', " +
        "1, " +
        "'_itzjustash', " +
        "'2015-06-22 22:15:19', " +
        "'approved', " +
        "'admin1', " +
        "'2015-06-22 22:21:57', " +
        "'2015-06-22 22:21:57');").execute()



      // Execute Query
      //val rs = statement.executeQuery("SELECT quote FROM quotes LIMIT 5")

      // Iterate Over ResultSet
      /*while (rs.next) {
        println(rs.getString("quote"))
      }*/
    } catch {
      case e : Throwable => e.printStackTrace()
    }
    finally {
      conn.close
    }


  }
    // connect to the database named "mysql" on the localhost
    /*val driver = "com.mysql.jdbc.Driver"
    val url = "jdbc:mysql://localhost:8889/ig_links"
    val username = "blewis"
    val password = "testdb"

    var connection:Connection = null

    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      // create the statement, and run the select query
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("SELECT host, user FROM user")
      while ( resultSet.next() ) {
        val host = resultSet.getString("host")
        val user = resultSet.getString("user")
        println("host, user = " + host + ", " + user)
      }

    connection.close()
  }*/

}


object ServiceBootstrap  {

}


