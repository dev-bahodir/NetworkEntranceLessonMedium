package dev.bahodir.networkentrancelessonmedium.share

import android.content.Context
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import dev.bahodir.networkentrancelessonmedium.network.NetworkHelper
import dev.bahodir.networkentrancelessonmedium.retrofit.ApiClient
import dev.bahodir.networkentrancelessonmedium.retrofit.ApiService
import dev.bahodir.networkentrancelessonmedium.room.DBHelper
import dev.bahodir.networkentrancelessonmedium.room.UserForRoom
import dev.bahodir.networkentrancelessonmedium.user.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyWorker(var context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    lateinit var appDatabase: DBHelper
    lateinit var apiService: ApiService
    lateinit var networkHelper: NetworkHelper
    override fun doWork(): Result {
        apiService = ApiClient.apiService
        appDatabase = DBHelper.getInstance(context)
        networkHelper = NetworkHelper(context)
        if (networkHelper.isNetworkConnected()) {
            apiService.getUsers().enqueue(object : Callback<List<User>> {
                override fun onResponse(
                    call: Call<List<User>>,
                    response: Response<List<User>>,
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        body?.forEach {
                            val userEntity =
                                UserForRoom(it.Ccy,
                                    it.CcyNm_UZ,
                                    it.Date,
                                    it.Diff,
                                    it.Rate,
                                    it.id,
                                    it.image,
                                    it.like
                                )
                            appDatabase.getDao().addUser(userEntity)
                            appDatabase.getDao().getUser()
                        }
                    }
                }
                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    Toast.makeText(context, "No internet connected", Toast.LENGTH_SHORT).show()
                }
            })
        }
        return Result.success()
    }
}