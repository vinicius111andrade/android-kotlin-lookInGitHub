package com.vdemelo.allstarktrepos.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


//@Database(
//    entities = [RedditPost::class, SubredditRemoteKey::class],
//    version = 1,
//    exportSchema = false
//)
//abstract class GithubDb : RoomDatabase() {
//    companion object {
//        fun create(context: Context, useInMemory: Boolean): GithubDb {
//            val databaseBuilder = if (useInMemory) {
//                Room.inMemoryDatabaseBuilder(context, GithubDb::class.java)
//            } else {
//                Room.databaseBuilder(context, GithubDb::class.java, "reddit.db")
//            }
//            return databaseBuilder
//                .fallbackToDestructiveMigration()
//                .build()
//        }
//    }
//
//    abstract fun posts(): GithubRepoDao
//    abstract fun remoteKeys(): SubredditRemoteKeyDao
//}