package com.example.freshkeeper.di

import android.app.Application
import androidx.room.Room
import com.example.freshkeeper.grocery.model.GroceryRepository
import com.example.freshkeeper.grocery.model.db.GroceryDao
import com.example.freshkeeper.grocery.model.db.GroceryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): GroceryDatabase {
        return Room.databaseBuilder(
                app,
                GroceryDatabase::class.java,
                "grocery_db"
            ).fallbackToDestructiveMigration()
            .build()

    }

    @Provides
    @Singleton
    fun provideGroceryDao(db: GroceryDatabase): GroceryDao {
        return db.groceryDao()
    }

    @Provides
    @Singleton
    fun provideRepository(dao: GroceryDao): GroceryRepository {
        return GroceryRepository(dao)
    }

}
