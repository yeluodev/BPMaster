package com.yeluodev.easydota.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBManager {

    public static Connection connectDB(){
        try {
            String driver ="org.mariadb.jdbc.Driver";
            //从配置参数中获取数据库url
            String url = "jdbc:mariadb://localhost:3306/easydota2";
            //从配置参数中获取用户名
            String user = "root";
            //从配置参数中获取密码
            String pass = "126973";

            //注册驱动
            Class.forName(driver);
            //获取数据库连接
            Connection conn = DriverManager.getConnection(url,user,pass);
            System.out.println("Success!");
            return conn;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
