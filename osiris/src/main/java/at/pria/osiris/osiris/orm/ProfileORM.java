package at.pria.osiris.osiris.orm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * A class which will be mapped with the ormlite tool
 *
 * Created by helmuthbrunner on 23/02/15.
 */
@DatabaseTable(tableName= "Profil")
public class ProfileORM {

    // the fields in the database
    @DatabaseField(generatedId= true, columnName= "_id")
    public int _ID;
    @DatabaseField
    public String hostname= "";
    @DatabaseField
    public Integer port;

    public ProfileORM(Integer id, String hostname, Integer port) {
        this._ID= id;
        this.hostname= hostname;
        this.port= port;
    }

    @Override
    public  String toString() {
        return  "id: " + _ID +
                "\nhostname: " + hostname +
                "\nport: " + port;
    }

    public ProfileORM() {
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getId() {
        return _ID;
    }

    public void setId(Integer id) {
        this._ID = id;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
