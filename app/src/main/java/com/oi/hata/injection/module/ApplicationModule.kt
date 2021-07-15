package com.oi.hata.injection.module

import android.content.Context
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.oi.hata.DevhataapiClient
import com.oi.hata.common.reminder.data.local.HataDatabase
import com.oi.hata.common.reminder.data.local.dao.ReminderDao
import com.oi.hata.data.HataDataSource
import com.oi.hata.data.remote.HataRemoteDataSource
import com.oi.hata.task.data.dao.GroupDao
import com.oi.hata.task.data.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApplicationModule {


    @JvmStatic
    @Singleton
    @Provides
    fun provideGoogleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("606294249890-ieahov3odc6tn9gibprocn43psicgjp1.apps.googleusercontent.com")
                .requestEmail()
                .build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideGoogleSignInClient(googleSignInOptions: GoogleSignInOptions,@ApplicationContext context: Context): GoogleSignInClient {
        return GoogleSignIn.getClient(context,googleSignInOptions)
    }


    @JvmStatic
    @Singleton
    @Provides
    fun provideAwsCredentialProvider(@ApplicationContext context: Context) : CognitoCachingCredentialsProvider {
        return CognitoCachingCredentialsProvider(context,"us-east-1:ee3cb5cc-127d-486b-94c8-f2e234082045",
            Regions.US_EAST_1)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun providesDevhatadevserviceClient(
        cognitoCachingCredentialsProvider: CognitoCachingCredentialsProvider
    ): DevhataapiClient {
        var apiClient = ApiClientFactory().credentialsProvider(cognitoCachingCredentialsProvider)
            .build(DevhataapiClient::class.java)

        return apiClient
    }

    @JvmStatic
    @Singleton
    @Provides
    fun providesHataRemoteDataSource(
        cognitoCachingCredentialsProvider: CognitoCachingCredentialsProvider,
        hataApiClient: DevhataapiClient
    ): HataRemoteDataSource {
        return com.oi.hata.data.remote.HataRemoteDataSource(cognitoCachingCredentialsProvider,hataApiClient)
    }

    @Singleton
    @Provides
    fun provideHataDatabase(@ApplicationContext context: Context): HataDatabase {
        return HataDatabase.getInstance(context)
    }

    @Provides
    fun provideReminderDao(hataDatabase: HataDatabase): ReminderDao {
        return hataDatabase.reminderDao()
    }

    @Provides
    fun provideTaskDao(hataDatabase: HataDatabase): TaskDao {
        return hataDatabase.taskDao()
    }

    @Provides
    fun provideGroupDao(hataDatabase: HataDatabase): GroupDao {
        return hataDatabase.groupDao()
    }

}