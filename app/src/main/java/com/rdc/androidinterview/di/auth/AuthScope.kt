package com.rdc.androidinterview.di.auth

import javax.inject.Scope

/**
 * AuthScope is strictly for login (scalable for including registration, password recovery and so...)
 */
@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class AuthScope