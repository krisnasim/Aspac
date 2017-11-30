package id.co.ncl.aspac.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import id.co.ncl.aspac.entities.Sparepart;

/**
 * Created by Jonathan Simananda on 29/11/2017.
 */

@Dao
public interface SparepartDao {

    @Query("SELECT * FROM sparepart")
    List<Sparepart> getAll();

    @Query("SELECT * FROM sparepart WHERE number LIKE :number LIMIT 1")
    Sparepart findByNumber(String number);

    @Query("SELECT * FROM sparepart WHERE machine_id LIKE :id LIMIT 1")
    Sparepart findByMachineID(int id);

    @Insert
    void insertAll(List<Sparepart> spareparts);

    @Insert
    void insert(Sparepart sparepart);

    @Update
    void update(Sparepart sparepart);

    @Delete
    void delete(Sparepart sparepart);

}
