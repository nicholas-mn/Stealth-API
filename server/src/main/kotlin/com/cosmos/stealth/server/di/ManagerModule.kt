package com.cosmos.stealth.server.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("com.cosmos.stealth.server.data.manager")
class ManagerModule
