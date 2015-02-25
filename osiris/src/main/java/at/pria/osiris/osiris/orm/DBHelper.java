package at.pria.osiris.osiris.orm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import at.pria.osiris.osiris.R;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Draft from Ari Ayvazyan
 *
 * A class which is needed for the DBQuery
 *
 * Created by helmuthbrunner on 23/02/15.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "profiles.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<ProfileORM, String> searchableItemDao = null;
    private RuntimeExceptionDao<ProfileORM, String> runtimeSearchableItemDao = null;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, ProfileORM.class);
        } catch (SQLException e) {
            Log.d("Osiris", "Exception", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, ProfileORM.class, true);
        } catch (SQLException e) {
            Log.d("Osiris", "Exception", e);
        }
        onCreate(database, connectionSource);
    }

    public Dao<ProfileORM, String> getProfileItemDao() throws SQLException {
        if (searchableItemDao == null) {
            searchableItemDao = getDao(ProfileORM.class);
        }
        return searchableItemDao;
    }

    public RuntimeExceptionDao<ProfileORM, String> getRuntimeSearchableItemDao() {
        if (runtimeSearchableItemDao == null) {
            runtimeSearchableItemDao = getRuntimeExceptionDao(ProfileORM.class);
        }
        return runtimeSearchableItemDao;
    }
}
