package com.liu.init.db;

import com.liu.conf.DbInitUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDbInit {

    private static final String MODULE_DESC = "module-user";
    //表达表名右侧模糊查询
    private static final String DBTABLE_PATTERN = "check%";
    // TODO: 2020/5/26 待修改
    private static final String DBRESOURCE_PATH = "init/db-user.sql";

    @Autowired
    private DbInitUtils dbInitUtils;

    /**
     * String tableSchema = rs.getString(2);
     * String tableName = rs.getString(3);
     */
    public void checkOrCreateInitDB() {
        dbInitUtils.checkOrCreateInitDB(MODULE_DESC,DBTABLE_PATTERN,DBRESOURCE_PATH);
    }


}
