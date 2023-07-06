package com.cosmos.stealth.core.common.di

import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.DEFAULT_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.IO_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.MAIN_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.MAIN_IMMEDIATE_DISPATCHER_QUALIFIER
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

object DispatchersModule {

    object Qualifier {
        val DEFAULT_DISPATCHER_QUALIFIER = named("default_dispatcher")
        val IO_DISPATCHER_QUALIFIER = named("io_dispatcher")
        val MAIN_DISPATCHER_QUALIFIER = named("main_dispatcher")
        val MAIN_IMMEDIATE_DISPATCHER_QUALIFIER = named("main_immediate_dispatcher")
    }

    @Suppress("MemberNameEqualsClassName")
    val dispatchersModule = module {
        single(DEFAULT_DISPATCHER_QUALIFIER) { providesDefaultDispatcher() }
        single(IO_DISPATCHER_QUALIFIER) { providesIoDispatcher() }
        single(MAIN_DISPATCHER_QUALIFIER) { providesMainDispatcher() }
        single(MAIN_IMMEDIATE_DISPATCHER_QUALIFIER) { providesMainImmediateDispatcher() }
    }

    private fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    private fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    private fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    private fun providesMainImmediateDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate
}
