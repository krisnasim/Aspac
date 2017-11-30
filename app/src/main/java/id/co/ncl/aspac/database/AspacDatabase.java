package id.co.ncl.aspac.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import id.co.ncl.aspac.dao.MachineDao;
import id.co.ncl.aspac.dao.ServiceDao;
import id.co.ncl.aspac.dao.SparepartDao;
import id.co.ncl.aspac.entities.Machine;
import id.co.ncl.aspac.entities.Service;
import id.co.ncl.aspac.entities.Sparepart;

/**
 * Created by Jonathan Simananda on 29/11/2017.
 */

@Database(entities = {Service.class, Machine.class, Sparepart.class}, version = 1)
public abstract class AspacDatabase extends RoomDatabase {
    //put your DAOs here
    public abstract ServiceDao serviceDao();
    public abstract MachineDao machineDao();
    public abstract SparepartDao sparepartDao();
}
