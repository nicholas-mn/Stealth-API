package com.cosmos.stealth.server.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [ManagerModule::class])
@ComponentScan("com.cosmos.stealth.server.data.service")
class ServiceModule
