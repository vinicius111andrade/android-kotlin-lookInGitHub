package com.vdemelo.allstarktrepos.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

//@Dao
//interface GithubRepoDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAll(posts: List<RedditPost>)
//
//    @Query("SELECT * FROM posts WHERE subreddit = :subreddit ORDER BY indexInResponse ASC")
//    fun postsBySubreddit(subreddit: String): PagingSource<Int, RedditPost>
//
//    @Query("DELETE FROM posts WHERE subreddit = :subreddit")
//    suspend fun deleteBySubreddit(subreddit: String)
//
//    @Query("SELECT MAX(indexInResponse) + 1 FROM posts WHERE subreddit = :subreddit")
//    suspend fun getNextIndexInSubreddit(subreddit: String): Int
//}
