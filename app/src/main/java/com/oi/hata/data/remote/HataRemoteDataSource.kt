package com.oi.hata.data.remote

import android.util.Log
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.http.HttpMethodName
import com.amazonaws.mobileconnectors.apigateway.ApiRequest
import com.amazonaws.util.IOUtils
import com.oi.hata.DevhataapiClient
import com.oi.hata.data.HataDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class HataRemoteDataSource constructor(val cognitoCachingCredentialsProvider: CognitoCachingCredentialsProvider,
                                       val devhatadevserviceClient: DevhataapiClient): HataDataSource {
    override suspend fun hello() {
        withContext(Dispatchers.IO){
            val apiRequest = ApiRequest(devhatadevserviceClient?.javaClass.simpleName)
                .withPath("/getuser")
                .withHttpMethod(HttpMethodName.GET)

            val response = devhatadevserviceClient?.execute(apiRequest)
            val responseContentStream = response?.getContent()

            if (responseContentStream != null) {

                val result = IOUtils.toString(responseContentStream)

                Log.d("REsult", result)

                //val responseObj = JSONObject(result)

                //val responsebodyObj = JSONObject(responseObj.getString("body"))

                //Log.d("response", res.toString())
            }
        }
    }
}