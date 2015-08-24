/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifis.toxifinder.core;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.h2gis.h2spatial.CreateSpatialExtension;

/**
 *
 * @author Christoph
 */
public class DatabaseTest {
    
    // public static String h2url =" jdbc:h2:mem:syntax";
    //public static String h2url ="jdbc:h2:~/test";
    public static String h2url ="jdbc:h2:tcp://localhost/~/test";
    
    public static void main (String[] args) {
        try {
            Class.forName("org.h2.Driver");
            // Open memory H2 table
            try(Connection connection = DriverManager.getConnection(h2url,"sa", "");
                Statement st = connection.createStatement()) {
                // Import spatial functions, domains and drivers
                // If you are using a file database, you have to do only that once.
                CreateSpatialExtension.initSpatialExtension(connection);
                // Create a table
                st.execute("CREATE TABLE ROADS (the_geom MULTILINESTRING, speed_limit INT)");
                // Add some roads
                st.execute("INSERT INTO ROADS VALUES ('MULTILINESTRING((15 5, 20 6, 25 7))', 80)");
                st.execute("INSERT INTO ROADS VALUES ('MULTILINESTRING((20 6, 21 15, 21 25))', 50)");
                // Compute the sum of roads length
                try(ResultSet rs = st.executeQuery("SELECT SUM(ST_LENGTH(the_geom)) total_length from ROADS")) {
                    if(rs.next()) {
                        System.out.println("Total length of roads: "+rs.getDouble("total_length")+" m");
                    }
                }
               //  st.execute("DROP TABLE ROADS");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
