package com.cosmos.stealth.core.common.di

import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.DEFAULT_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.IO_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.MAIN_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.MAIN_IMMEDIATE_DISPATCHER_QUALIFIER
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
class DispatchersModule {

    object Qualifier {
        const val DEFAULT_DISPATCHER_QUALIFIER = "default_dispatcher"
        const val IO_DISPATCHER_QUALIFIER = "io_dispatcher"
        const val MAIN_DISPATCHER_QUALIFIER = "main_dispatcher"
        const val MAIN_IMMEDIATE_DISPATCHER_QUALIFIER = "main_immediate_dispatcher"
    }

    @Single
    @Named(DEFAULT_DISPATCHER_QUALIFIER)
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Single
    @Named(IO_DISPATCHER_QUALIFIER)
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Single
    @Named(MAIN_DISPATCHER_QUALIFIER)
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Single
    @Named(MAIN_IMMEDIATE_DISPATCHER_QUALIFIER)
    fun providesMainImmediateDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate
}
