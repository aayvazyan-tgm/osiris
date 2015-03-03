package at.pria.osiris.osiris.orm;

import android.content.Context;
import android.database.Cursor;
import at.pria.osiris.osiris.view.elements.Profile;
import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Draft from Ari Ayvazyan
 *
 * A class with static methods to read, write and update object in the orm tool.
 *
 * Created by helmuthbrunner on 23/02/15.
 */
public class DBQuery {

    /**
     * A method which returns only one Profile
     * @param context the context
     * @param searchForId the primary key
     * @return the profile
     * @throws SQLException
     */
    public static ProfileORM getStoredPackages(Context context, String searchForId) throws SQLException {
        //Obtain the handlers
        DBHelper dbHelper = OpenHelperManager.getHelper(context, DBHelper.class);
        RuntimeExceptionDao<ProfileORM, String> dao = dbHelper.getRuntimeSearchableItemDao();

        //Do the Search
        QueryBuilder<ProfileORM, String> qb=dao.queryBuilder();
        qb.where().like("_id", searchForId);
        qb.orderBy("_id", false);
        PreparedQuery<ProfileORM> pq = qb.prepare();

        List<ProfileORM> result = dao.query(pq);
        //Release the Helper
        OpenHelperManager.releaseHelper();
        result= new ArrayList<ProfileORM>(result);
        return result.get(0);
    }

    /**
     * A method to add new Profiles to DB
     * @param context the context
     * @param newItem the new Profile-Object
     */
    public static void insertProfileItem(Context context, ProfileORM newItem){
        //Obtain the handlers
        DBHelper dbHelper = OpenHelperManager.getHelper(context, DBHelper.class);
        RuntimeExceptionDao<ProfileORM, String> dao = dbHelper.getRuntimeSearchableItemDao();

        // insert the object to orm-db
        dao.createIfNotExists(newItem);

        //Release the Helper
        OpenHelperManager.releaseHelper();
    }

    /**
     * A method to update one specific Profile
     * @param context the context
     * @param updateItem the new Profile Object
     * @throws SQLException
     */
    public static void updateProfileItem(Context context, ProfileORM updateItem) throws SQLException {
        DBHelper dbHelper = OpenHelperManager.getHelper(context, DBHelper.class);
        RuntimeExceptionDao<ProfileORM, String> dao = dbHelper.getRuntimeSearchableItemDao();

        // update section
        UpdateBuilder<ProfileORM, String> ub= dao.updateBuilder();

        ub.where().eq("_id", String.valueOf(updateItem.getId()));
        ub.updateColumnValue("hostname", updateItem.getHostname());
        ub.updateColumnValue("port", String.valueOf(updateItem.getPort()));
        ub.updateColumnValue("controller_type", updateItem.getController_type());

        ub.update();

        //Release the Helper
        OpenHelperManager.releaseHelper();
    }

    /**
     * A method to delete one entry in the orm tool
     *
     * !!! not tested don't use this !!!
     *
     * @param context the context
     * @param oldItems a list of objects to be deleted
     */
    public static void deleteProfileItem(Context context, List<ProfileORM> oldItems){
        //Obtain the handlers
        DBHelper dbHelper = OpenHelperManager.getHelper(context, DBHelper.class);
        RuntimeExceptionDao<ProfileORM, String> dao = dbHelper.getRuntimeSearchableItemDao();

        dao.delete(oldItems);

        //Release the Helper
        OpenHelperManager.releaseHelper();
    }

    /**
     * A method to delete a object over the ID.
     * @param context the context
     * @param id the primary-key for the specific object
     * @throws SQLException
     */
    public static void deleteOverId(Context context, String id) throws SQLException {
        //Obtain the handlers
        DBHelper dbHelper = OpenHelperManager.getHelper(context, DBHelper.class);
        RuntimeExceptionDao<ProfileORM, String> dao = dbHelper.getRuntimeSearchableItemDao();

        // Delete one entry over the deleteBuilder
        DeleteBuilder<ProfileORM, String> db= dao.deleteBuilder();
        db.where().eq("_id", id);
        db.delete();

        //Release the Helper
        OpenHelperManager.releaseHelper();
    }

    /**
     * Returns all Profiles in the ORM tool
     * @param context the context
     * @return the objects as a List
     * @throws SQLException
     */
    public static List<ProfileORM> getAll(Context context) throws SQLException {
        //Obtain the handlers
        DBHelper dbHelper = OpenHelperManager.getHelper(context, DBHelper.class);
        RuntimeExceptionDao<ProfileORM, String> dao = dbHelper.getRuntimeSearchableItemDao();

        //Query for all items
        List<ProfileORM> result = dao.queryForAll();

        //Release the Helper
        OpenHelperManager.releaseHelper();
        return result;
    }

    /**
     * Returns the cursor, is used for the list-adapter
     * @param context the context
     * @return the cursor
     * @throws SQLException
     */
    public static Cursor getCursor(Context context) throws SQLException {
        DBHelper dbHelper = OpenHelperManager.getHelper(context, DBHelper.class);
        RuntimeExceptionDao<ProfileORM, String> dao = dbHelper.getRuntimeSearchableItemDao();
        QueryBuilder qb= dao.queryBuilder();

        CloseableIterator<ProfileORM> iterator = dao.iterator(qb.prepare());
        AndroidDatabaseResults results = (AndroidDatabaseResults) iterator.getRawResults();

        OpenHelperManager.releaseHelper();
        return results.getRawCursor();
    }
}
