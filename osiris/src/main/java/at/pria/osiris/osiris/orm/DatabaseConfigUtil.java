package at.pria.osiris.osiris.orm;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by helmuthbrunner on 23/02/15.
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil {

    public  static final Class<?>[] classes = new Class[]{ProfileORM.class};

    public static void main(String[] args) throws IOException, SQLException {
        writeConfigFile("ormlite_config.txt",classes);
        System.out.println("done");
    }
}
