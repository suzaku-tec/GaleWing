package gradle.task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * CustomRss DBファイル作成
 */
public class InitCustomRssSqliteDb {

    public static void main(String[] args) throws SQLException {
        String url = "jdbc:sqlite:customRss.db";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS test (id INTEGER PRIMARY KEY, name TEXT);");
        }
        System.out.println("SQLite DB created successfully.");
    }

}
