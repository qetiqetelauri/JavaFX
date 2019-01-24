package connectivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {
    public Connection getConnection()  {


        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/moiare","root","");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
