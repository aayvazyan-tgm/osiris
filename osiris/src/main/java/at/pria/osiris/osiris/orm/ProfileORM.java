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
    @DatabaseField
    public String controller_type;

    public ProfileORM(Integer id, String hostname, Integer port, String controller_type) {
        this._ID= id;
        this.hostname= hostname;
        this.port= port;
        this.controller_type= controller_type;
    }

    @Override
    public  String toString() {
        return  "id: " + _ID +
                "\nhostname: " + hostname +
                "\nport: " + port +
                "\ncontroller_type: " + controller_type;
    }

    public ProfileORM() {
    }

    public String getController_type() {
        return controller_type;
    }

    public void setController_type(String controller_type) {
        this.controller_type = controller_type;
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
