package com.liu.conf;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
@Scope("prototype")
public class DbInitUtils {
    @Autowired
    private DataSource dataSource;

    public DbInitUtils() {
    }

    public void checkOrCreateInitDB(String modeuleDesc, String tablePattern, String dbResPath) {
        boolean hasInited = false;
        Connection connection = null;
        ResultSet rs = null;

        //先判断表有没有在数据库里
        try {
            //跟数据库建立链接
            connection = dataSource.getConnection();

            rs = connection.getMetaData().getTables((String)null, (String)null, tablePattern, (String[])null);
            if (rs.next()) {
                // 如果rs匹配 表示已经初始化数据库表
                hasInited = true;
                System.out.println(modeuleDesc + " 表已经初始化过了，无需重新设置");
            }
        } catch (SQLException var20) {
            //数据报错
            hasInited = true;
            System.out.println(modeuleDesc + " 无法检测是否已经初始化过数据库，请手动设置");
        } finally {
            //关闭链接
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException var17) {
                    System.out.println(modeuleDesc + " 关闭数据库rs失败");
                }
            }

        }

        if (!hasInited) {
            ScriptRunner runner = new ScriptRunner(connection);
            //设置表编码
            Resources.setCharset(StandardCharsets.UTF_8);
            //打开发生错误终止
            runner.setStopOnError(true);

            try {
                //执行sql脚本
                runner.runScript(Resources.getResourceAsReader(dbResPath));
            } catch (IOException var19) {
                System.out.println(modeuleDesc + " 初始化数据库sql文件失败!");
            }
        }

        if (connection != null) {
            try {
                //关闭链接
                connection.close();
            } catch (SQLException var18) {
                System.out.println(modeuleDesc + " 链接关闭失败!");
            }
        }

    }
}
