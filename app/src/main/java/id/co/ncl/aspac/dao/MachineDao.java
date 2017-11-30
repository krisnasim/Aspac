package id.co.ncl.aspac.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import id.co.ncl.aspac.entities.Machine;

/**
 * Created by Jonathan Simananda on 29/11/2017.
 */

@Dao
public interface MachineDao {

    @Query("SELECT * FROM machine")
    List<Machine> getAll();

    @Query("SELECT * FROM machine WHERE serial_number LIKE :serialNumber LIMIT 1")
    Machine findBySerialNumber(String serialNumber);

    @Query("SELECT * FROM machine WHERE service_id LIKE :serviceID LIMIT 1")
    Machine findByServiceID(String serviceID);

    @Insert
    void insertAll(List<Machine> machines);

    @Insert
    long insert(Machine machine);

    @Update
    void update(Machine machine);

    @Delete
    void delete(Machine machine);

}
