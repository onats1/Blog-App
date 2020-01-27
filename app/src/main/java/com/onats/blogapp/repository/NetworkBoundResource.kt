package com.onats.blogapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.onats.blogapp.ui.DataState
import com.onats.blogapp.ui.Response
import com.onats.blogapp.ui.ResponseType
import com.onats.blogapp.util.*
import com.onats.blogapp.util.Constants.Companion.NETWORK_TIMEOUT
import com.onats.blogapp.util.Constants.Companion.TESTING_NETWORK_DELAY
import com.onats.blogapp.util.ErrorHandling.Companion.ERROR_CHECK_NETWORK_CONNECTION
import com.onats.blogapp.util.ErrorHandling.Companion.ERROR_UNKNOWN
import com.onats.blogapp.util.ErrorHandling.Companion.UNABLE_TODO_OPERATION_WO_INTERNET
import com.onats.blogapp.util.ErrorHandling.Companion.UNABLE_TO_RESOLVE_HOST
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

abstract class NetworkBoundResource<ResponseObject, ViewStateType>(isNetworkAvailable: Boolean) {

    protected val result = MediatorLiveData<DataState<ViewStateType>>()
    protected lateinit var job: CompletableJob
    protected lateinit var coroutineScope: CoroutineScope

    init {
        setJob(initNewJob())

        setValue(DataState.loading(loading = true, cachedData = null))

        if (isNetworkAvailable){

            coroutineScope.launch {

                delay(TESTING_NETWORK_DELAY)

                //Switching to main scope because the maediator live data object has to be observed from the main thread.

                withContext(Main) {

                    val apiResponse = createCall()

                    result.addSource(apiResponse) { response ->
                        result.removeSource(apiResponse)

                        coroutineScope.launch {
                            handleApiResponse(response)
                        }

                    }
                }
            }

            GlobalScope.launch(IO) {
                delay(NETWORK_TIMEOUT)

                if (!job.isCompleted) {

                    job.cancel(CancellationException(UNABLE_TO_RESOLVE_HOST))
                }
            }

        } else{

            onErrorReturn(UNABLE_TODO_OPERATION_WO_INTERNET, shouldUsedialog = true, shouldUseToast = false)


        }
    }

    private suspend fun handleApiResponse(response: GenericApiResponse<ResponseObject>?) {
        when (response){
            is ApiSuccessResponse -> {
                handleApiSuccesResponse(response)
            }
            is ApiErrorResponse -> {
                onErrorReturn(response.errorMessage, true, false)
            }
            is ApiEmptyResponse -> {

            }
        }
    }


    fun onErrorReturn(errorMessage: String?, shouldUsedialog: Boolean, shouldUseToast: Boolean){
        var errorMessage = errorMessage
        var useDialog = shouldUsedialog
        var responseType: ResponseType = ResponseType.None()

        if(errorMessage == null){
            errorMessage == ERROR_UNKNOWN
        } else if (ErrorHandling.isNetworkError(errorMessage)) {
            errorMessage = ERROR_CHECK_NETWORK_CONNECTION
            useDialog = false
        }

        if(shouldUseToast){
            responseType = ResponseType.Toast()
        }

        if(useDialog){
            responseType = ResponseType.Dialog()
        }

        onCompleteJob(DataState.error(
            response = Response(
                message = errorMessage,
                responseType = responseType
            )
        ))
    }

    fun onCompleteJob(dataState: DataState<ViewStateType>){
        GlobalScope.launch(Main) {
            job.complete()
            setValue(dataState)
        }
    }

    private fun setValue(dataState: DataState<ViewStateType>) {
        result.value = dataState
    }


    @UseExperimental(InternalCoroutinesApi::class)
    private fun initNewJob(): Job {
        job = Job()
        job.invokeOnCompletion(onCancelling = true, invokeImmediately = true) {

            if (job.isCancelled) {
                Log.e("", "Job has been cancelled.")
                it?.let {
                    onErrorReturn(it.message, false, true)
                }?: onErrorReturn(ERROR_UNKNOWN, false, true)
            } else if (job.isCompleted){
                Log.e("", "Job has been cancelled.")
            }

        }

        coroutineScope = CoroutineScope(IO + job)
        return job
    }

    fun asLiveData() = result as LiveData<DataState<ViewStateType>>

    abstract suspend fun handleApiSuccesResponse(response: ApiSuccessResponse<ResponseObject>)

    abstract fun createCall(): LiveData<GenericApiResponse<ResponseObject>>

    abstract fun setJob(job: Job)


}