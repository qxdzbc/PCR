package com.qxdzbc.pcr.di

import com.qxdzbc.pcr.state.EntryContainer
import com.qxdzbc.pcr.state.EntryContainerImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [DefaultObjModule::class])
@InstallIn(SingletonComponent::class)
interface StateModule {
    @Binds
    @Singleton
    fun EntryContainer(i:EntryContainerImp):EntryContainer
}