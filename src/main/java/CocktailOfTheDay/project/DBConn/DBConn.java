package CocktailOfTheDay.project.DBConn;


import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;

@Configuration
public class DBConn {

    String ip = "andLeeme.iptime.org";
    String port = "3306";
    String DB_name = "cotd";
    String id = "hyunha";
    String pw = "1234!@#$";
    String url = "jdbc:mysql://" + ip + ":" + port + "/" + DB_name + "?characterEncoding=UTF-8&serverTimezone=UTC";
    String sql = null;
    Connection conn = null;

    //db 커넥트
    public Connection connect(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, id, pw);
            System.out.println("db접속성공");
        }catch (Exception e){
            System.out.println("db접속실패");
            e.printStackTrace();
        }
        return conn;
    }

}