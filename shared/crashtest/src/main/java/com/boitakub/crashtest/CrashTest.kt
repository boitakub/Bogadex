/*
 * Copyright (c) 2022, Boitakub
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of mosquitto nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.boitakub.crashtest

import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.annotation.StringRes
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

/**
 * Credits : https://www.rockandnull.com/crashlytics-non-fatal/
 */
class CrashTest private constructor(
    isDebug: Boolean,
    private val isReportingEnabled: Boolean = true,
    private val isAnonymous: Boolean,
    private val firebaseCrashlytics: FirebaseCrashlytics?,
) {

    init {
        if (isDebug) {
            Timber.plant(Timber.DebugTree())
            val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
            Thread.setDefaultUncaughtExceptionHandler(defaultHandler)
        } else {
            Timber.plant(CrashReportingTree())
        }
        setCrashlyticsCollectionEnabled(!isDebug && isReportingEnabled)
    }

    class CrashTestExceptionHandler : Thread.UncaughtExceptionHandler {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

        override fun uncaughtException(t: Thread, e: Throwable) {
            TODO("Not yet implemented")
        }
    }

    class CrashTestCoroutineExceptionHandler(override val key: CoroutineContext.Key<*>) : CoroutineExceptionHandler {
        override fun handleException(context: CoroutineContext, exception: Throwable) {
            TODO("Not yet implemented")
        }
    }

//    val handler = CoroutineExceptionHandler { _, e ->
//        when (e) {
//            is ConnectionError -> showMessage("No internet")
//            is ExpectedBackendError -> showMessage("Wrong input")
//            else -> showMessage("Something went wrong")
//        }
//        flowErisation().catch { e ->
//            e.message?.let { onError(it) }
//        }
//    }

    fun <T> crashTest(action: suspend kotlinx.coroutines.flow.FlowCollector<T>.(Throwable) -> Unit): Flow<T> {
        return flow {
        }
    }

//    fun flowErisation(): Flow<Message> {
//        return flow {
//            Message("", "")
//        }
//    }

    private fun setCrashlyticsCollectionEnabled(setCrashlyticsCollectionEnabled: Boolean) {
        firebaseCrashlytics?.setCrashlyticsCollectionEnabled(setCrashlyticsCollectionEnabled)
    }

    fun setUserId(identifier: String) {
        if (!isAnonymous) {
            Timber.i("setUserId:", identifier)
            firebaseCrashlytics?.let {
                // TODO: Handle empty identifier
                firebaseCrashlytics.setUserId(identifier)
            }
        } else {
            Timber.i("setUserId: User is Anonymous")
        }
    }

    fun setKeyValue(key: String, value: String) {
        Timber.i("setKeyValue:", key, value)
        firebaseCrashlytics?.let {
            firebaseCrashlytics.setCustomKey(key, value)
        }
    }

    fun recordJournal(message: Message) {
        Timber.d("log:", message)
        firebaseCrashlytics?.let {
            // TODO: Handle empty message
            firebaseCrashlytics.log(message.technicalMessage)
        }
    }

    fun recordNonFatalException(throwable: NonFatalException) {
        Timber.d("recordException:", throwable)
        firebaseCrashlytics?.let {
            firebaseCrashlytics.recordException(throwable)
        }
    }

    @Throws(RuntimeException::class)
    fun throwTest() {
        throw RuntimeException("Crash Test")
    }

    data class Builder(
        var isDebug: Boolean,
        private val isReportingEnabled: Boolean = true,
        var firebaseCrashlytics: FirebaseCrashlytics? = null,
        var isAnonymous: Boolean? = null,
    ) {
        fun isDebug(isDebug: Boolean) = apply { this.isDebug = isDebug }
        fun isAnonymous(isAnonymous: Boolean?) = apply { this.isAnonymous = isAnonymous }
        fun firebaseCrashlytics(firebaseCrashlytics: FirebaseCrashlytics) =
            apply { this.firebaseCrashlytics = firebaseCrashlytics }

        fun build() = CrashTest(isDebug, isReportingEnabled, isAnonymous == true, firebaseCrashlytics)
    }
}

class CrashReportingTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.DEBUG || priority == Log.ERROR) {
            Log.e("ho", "Log was secured")
        }
    }
}

interface CustomerMessageHandler {
    fun onMessage(message: String)
}

class Message(val technicalMessage: String, customerMessage: ResString? = null) {
    constructor(context: Context, @StringRes developerMessageRes: Int, @StringRes customerMessageRes: Int) : this(
        context.getString(developerMessageRes), context.getString(customerMessageRes).toResString()
    )

    constructor(resources: Resources, @StringRes developerMessageRes: Int, @StringRes customerMessageRes: Int) : this(
        resources.getString(developerMessageRes), resources.getString(customerMessageRes).toResString()
    )
}

class FatalException(throwable: Throwable, message: Message? = null) : Throwable(throwable) {
    constructor(throwable: Throwable, message: String) : this(throwable, Message(message))
}

open class NonFatalException(throwable: Throwable?, message: Message? = null) : Throwable(throwable) {
    constructor(throwable: Throwable?, message: String) : this(throwable, Message(message))
    constructor(throwable: Throwable?, technicalMessage: String, customerMessage: String) : this(
        throwable,
        Message(technicalMessage, customerMessage.toResString())
    )
}

fun <T> Flow<T>.catchLog(default: T? = null): Flow<T> = this.catch { e ->
    Timber.e(
        e,
        "Error from flow (${coroutineContext[CoroutineName]?.name}): " +
            "${e.stackTraceToString()}"
    )
    default?.let { emit(it) }
}

// User not filled
// User unknow
// We need some times to get your data ready
// Network - Something went wrong, please try again later
// Local - Something went wrong we notice technical service of this
