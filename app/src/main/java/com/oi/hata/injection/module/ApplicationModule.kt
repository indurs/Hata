package com.oi.hata.injection.module

import android.content.Context
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.oi.hata.DevhataapiClient
import com.oi.hata.common.reminder.data.HataReminderDatasource
import com.oi.hata.common.reminder.data.local.HataDatabase
import com.oi.hata.common.reminder.data.local.HataReminderRepository
import com.oi.hata.common.reminder.data.local.IHataReminderRepository
import com.oi.hata.common.reminder.data.local.dao.ReminderDao
import com.oi.hata.common.reminder.data.local.datasource.HataLocalReminderDatasource
import com.oi.hata.task.data.HataTaskDatasource
import com.oi.hata.task.data.HataTaskRepository
import com.oi.hata.task.data.IHataTaskRepository
import com.oi.hata.task.data.dao.GroupDao
import com.oi.hata.task.data.dao.TaskDao
import com.oi.hata.task.data.local.HataLocalTaskDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApplicationModule {


    @JvmStatic
    @Singleton
    @Provides
    fun provideGoogleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("")
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
        return CognitoCachingCredentialsProvider(context,"",
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


    @Singleton
    @Provides
    fun provideHataDatabase(@ApplicationContext context: Context): HataDatabase {
        return HataDatabase.getInstance(context)
    }


    @Provides
    fun provideHataLocalTaskDatasource(hataDatabase: HataDatabase,reminderDao: ReminderDao,taskDao: TaskDao,groupDao: GroupDao):  HataTaskDatasource{
        return HataLocalTaskDatasource(hataDatabase,taskDao, groupDao, reminderDao)
    }

    @Provides
    fun provideHataLocalReminderDatasource(hataDatabase: HataDatabase,reminderDao: ReminderDao,taskDao: TaskDao): HataReminderDatasource{
        return HataLocalReminderDatasource(hataDatabase,reminderDao,taskDao)
    }

    @Provides
    fun provideHataTaskRepository(hataLocalTaskDatasource: HataTaskDatasource,hataLocalReminderDatasource: HataReminderDatasource): IHataTaskRepository{
        return HataTaskRepository(hataLocalTaskDatasource,hataLocalReminderDatasource)
    }

    @Provides
    fun provideHataReminderRepository(hataLocalReminderDatasource: HataReminderDatasource): IHataReminderRepository{
        return HataReminderRepository(hataLocalReminderDatasource)
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