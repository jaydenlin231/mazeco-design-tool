package com.mazeco.database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 * Provides a SQL database connection for <code>JDBCMazeBrowserDataSource</code> 
 * using properties in /config/db.props.
 * 
 * @see JDBCMazeBrowserDataSource
 */   
public class DBConnection {

   /**
    * The singleton instance of the database connection.
    */
   private static Connection instance = null;

   /**
    * Constructs a DBConnection Object.
    */
   private DBConnection() {
      Properties props = new Properties();
      InputStream in = null;
      try {
         // get database properties config file
         in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/db.props");
         props.load(in);
         in.close();
         // specify the data source, username and password
         String url = props.getProperty("jdbc.url");
         String username = props.getProperty("jdbc.username");
         String password = props.getProperty("jdbc.password");
         String schema = props.getProperty("jdbc.schema");
         // get a connection
         instance = DriverManager.getConnection(url + "/" + schema, username, password);
      } catch (SQLException exception) {
         exception.printStackTrace();
      } catch (NullPointerException exception) {
         JOptionPane.showMessageDialog(null, "Database configuration file \"db.props\" not found in \"resources/config\" directory", "Error", JOptionPane.ERROR_MESSAGE);
      } catch (FileNotFoundException exception) {
         exception.printStackTrace();
      } catch (IOException exception) {
         exception.printStackTrace();
      }
   }

   /**
    * Provides global access to the singleton instance of the UrlSet.
    * 
    * @return a handle to the singleton instance of the UrlSet.
    */
   public static Connection getInstance() {
      if (instance == null) {
         new DBConnection();
      }
      return instance;
   }
}