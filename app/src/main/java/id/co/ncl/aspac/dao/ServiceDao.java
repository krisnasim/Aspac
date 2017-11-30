package id.co.ncl.aspac.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.arch.lifecycle.LiveData;

import java.util.List;

import id.co.ncl.aspac.entities.Service;

/**
 * Created by Jonathan Simananda on 29/11/2017.
 */

@Dao
public interface ServiceDao {

    @Query("SELECT * FROM service")
    List<Service> getAll();

    @Query("SELECT * FROM service")
    LiveData<List<Service>> getAllSync();

    @Query("SELECT * FROM service WHERE id LIKE :id LIMIT 1")
    Service findByID(int id);

    @Insert
    void insertAll(List<Service> services);

    @Insert
    long insert(Service service);

    @Update
    void update(Service service);

    @Delete
    void delete(Service service);

    @Query("DELETE FROM service")
    void deleteAll();

}
