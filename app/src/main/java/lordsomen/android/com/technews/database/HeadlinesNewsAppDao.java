package lordsomen.android.com.technews.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

@Dao
public interface HeadlinesNewsAppDao {

    @Insert
    public void insert(HeadlinesNewsAppData headlinesNewsAppData);

    @Update
    public void update(HeadlinesNewsAppData headlinesNewsAppData);

    @Query("SELECT * FROM top_headlines_data")
    Cursor selectAllCursor();

    @Query("SELECT * FROM top_headlines_data")
    LiveData<List<HeadlinesNewsAppData>> selectAllList();

    @Query("SELECT * FROM top_headlines_data WHERE id =:id")
    Cursor selectById(int id);

    @Query("SELECT COUNT(*) FROM top_headlines_data")
    int count();

    @Query("DELETE FROM top_headlines_data WHERE id = :id")
    int deleteById(int id);

    @Query("DELETE FROM top_headlines_data ")
    int deleteAll();

    @Query("SELECT EXISTS(SELECT 1 from top_headlines_data where id =:id) limit 1")
    boolean isInTheTable(int id);

}
