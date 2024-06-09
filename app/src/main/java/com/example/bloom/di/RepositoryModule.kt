package com.example.bloom.di

import android.app.Application
import android.content.Context
import com.example.bloom.chatbot.data.repository.ChatRepositoryImpl
import com.example.bloom.chatbot.domain.ChatRepository
import com.example.bloom.chatroom.data.repository.MessageRepositoryImpl
import com.example.bloom.chatroom.data.repository.RoomRepositoryImpl
import com.example.bloom.chatroom.domain.MessageRepository
import com.example.bloom.chatroom.domain.RoomRepository
import com.example.bloom.journal.data.repository.JournalRepositoryImpl
import com.example.bloom.journal.domain.repository.JournalRepository
import com.example.bloom.profile.data.repository.ProfileRepositoryImpl
import com.example.bloom.profile.domain.ProfileRepository
import com.example.bloom.therapy.data.repository.DoctorsRepositoryImpl
import com.example.bloom.therapy.data.repository.GeneratePdfUseCaseImpl
import com.example.bloom.therapy.data.repository.PatientRepositoryImpl
import com.example.bloom.therapy.domain.DoctorsRepository
import com.example.bloom.therapy.domain.GeneratePdfUseCase
import com.example.bloom.therapy.domain.PatientRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ContextModule {
    @Singleton
    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindJournalRepository(
        journalRepositoryImpl: JournalRepositoryImpl
    ): JournalRepository

    @Binds
    @Singleton
    abstract fun bindRoomRepository(
        roomRepositoryImpl: RoomRepositoryImpl
    ):RoomRepository

    @Binds
    @Singleton
    abstract fun bindMessageRepository(
        messageRepositoryImpl: MessageRepositoryImpl
    ):MessageRepository

    @Binds
    @Singleton
    abstract fun bindChatBotRepository(
        chatRepositoryImpl: ChatRepositoryImpl
    ): ChatRepository

    @Binds
    @Singleton
    abstract fun bindDoctorRepository(
        doctorsRepositoryImpl: DoctorsRepositoryImpl
    ): DoctorsRepository

    @Binds
    @Singleton
    abstract fun bindPatientRepository(
        patientRepositoryImpl: PatientRepositoryImpl
    ): PatientRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        profileRepositoryImpl: ProfileRepositoryImpl
    ): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindPdfRepository(
        generatePdfUseCaseImpl: GeneratePdfUseCaseImpl
    ): GeneratePdfUseCase

}