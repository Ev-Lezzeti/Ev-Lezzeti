package com.example.evlezzeti.di

import com.example.evlezzeti.data.datasource.FirestoreDataSource
import com.example.evlezzeti.data.repo.Repository
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    @Named("Mutfaklar")
    fun provideMutfakCollectionReference(): CollectionReference {
        return Firebase.firestore.collection("Mutfaklar")
    }

    @Provides
    @Singleton
    @Named("Kategoriler")
    fun provideKategoriCollectionReference(): CollectionReference {
        return Firebase.firestore.collection("Kategoriler")
    }

    @Provides
    @Singleton
    @Named("Oneriler")
    fun provideOnerilerCollectionReference(): CollectionReference {
        return Firebase.firestore.collection("Oneriler")
    }

    @Provides
    @Singleton
    @Named("users")
    fun provideUsersCollectionReference(): CollectionReference {
        return Firebase.firestore.collection("users")
    }

    @Provides
    @Singleton
    fun provideFireStoreDataSource(
        @Named("Mutfaklar") mutfakCollection: CollectionReference,
        @Named("Kategoriler") kategoriCollection: CollectionReference,
        @Named("Oneriler") onerilerCollection: CollectionReference,
        @Named("users") usersCollection: CollectionReference
    ): FirestoreDataSource {
        return FirestoreDataSource(mutfakCollection, kategoriCollection, onerilerCollection , usersCollection  )
    }

    @Provides
    @Singleton
    fun provideRepository(fds: FirestoreDataSource): Repository {
        return Repository(fds)
    }
}