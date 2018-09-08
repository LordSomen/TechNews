package lordsomen.android.com.technews.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

@Dao
public interface NewsAppDao {

    @Insert
    public void insert(NewsAppData newsData);

    @Update
    public void update(NewsAppData newsData);

    @Query("SELECT * FROM news_fav_data")
    Cursor selectAllCursor();

    @Query("SELECT * FROM news_fav_data")
    LiveData<List<NewsAppData>> selectAllList();

    @Query("SELECT * FROM news_fav_data WHERE id =:id")
    Cursor selectById(int id);

    @Query("SELECT COUNT(*) FROM news_fav_data")
    int count();

    @Query("DELETE FROM news_fav_data WHERE id = :id")
    int deleteById(int id);

    @Query("SELECT EXISTS(SELECT 1 from news_fav_data where id =:id) limit 1")
    boolean isInTheTable(int id);

}
