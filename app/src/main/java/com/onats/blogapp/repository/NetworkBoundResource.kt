package com.onats.blogapp.repository

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import com.onats.blogapp.ui.DataState
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

abstract class NetworkBoundResource<ResponseObject, ViewStateType>(isNetworkAvailable: Boolean) {

    protected val result = MediatorLiveData<DataState<ViewStateType>>()
    protected lateinit var job: CompletableJob
    protected lateinit var coroutineScope: CoroutineScope


    @UseExperimental(InternalCoroutinesApi::class)
    private fun initNewJob(): Job {
        job = Job()
        job.invokeOnCompletion(onCancelling = true, invokeImmediately = true) {

            if (job.isCancelled) {
                Log.e("", "Job has been cancelled.")
                it?.let {

                }
            } else if (job.isCompleted){
                Log.e("", "Job has been cancelled.")

            }

        }

        coroutineScope = CoroutineScope(IO + job)
        return job
    }

}