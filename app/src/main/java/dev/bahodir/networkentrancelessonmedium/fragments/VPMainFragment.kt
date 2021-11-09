package dev.bahodir.networkentrancelessonmedium.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import dev.bahodir.networkentrancelessonmedium.R
import dev.bahodir.networkentrancelessonmedium.adapters.RV
import dev.bahodir.networkentrancelessonmedium.databinding.BottomSheetDialogBinding
import dev.bahodir.networkentrancelessonmedium.databinding.FragmentVPMainBinding
import dev.bahodir.networkentrancelessonmedium.network.NetworkHelper
import dev.bahodir.networkentrancelessonmedium.retrofit.ApiClient
import dev.bahodir.networkentrancelessonmedium.retrofit.ApiService
import dev.bahodir.networkentrancelessonmedium.room.DBHelper
import dev.bahodir.networkentrancelessonmedium.room.UserForRoom
import dev.bahodir.networkentrancelessonmedium.share.MyWorker
import dev.bahodir.networkentrancelessonmedium.user.User
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VPMainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VPMainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentVPMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var rv: RV
    private lateinit var requestQueue: RequestQueue
    private lateinit var networkHelper: NetworkHelper
    private lateinit var list: MutableList<User>
    private lateinit var dbHelper: DBHelper
    private lateinit var apiService: ApiService
    private lateinit var userForRoom: UserForRoom
    private val TAG = "VPMainFragment"
    private var isLike = false

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVPMainBinding.inflate(inflater, container, false)

        dbHelper = DBHelper.getInstance(requireContext())
        apiService = ApiClient.apiService

        requestQueue = Volley.newRequestQueue(requireContext())
        networkHelper = NetworkHelper(requireContext())

        rv = RV(object : RV.OnTouchListener {
            @SuppressLint("CheckResult")
            override fun itemClick(user: UserForRoom, position: Int, view: View) {
                val bind: BottomSheetDialogBinding =
                    BottomSheetDialogBinding.inflate(layoutInflater)
                /*val dialog = BottomSheetDialog(requireContext(), R.style.SheetDialog)
                dialog.setContentView(bind.root)*/

                val builder = AlertDialog.Builder(requireContext(), R.style.SheetDialog)
                builder.setView(bind.root)

                val dialog = builder.create()

                bind.countryOne.text = user.Ccy

                bind.fabCircle.setOnClickListener {
                    bind.courseTwo.text = ""
                    bind.courseOne.setText("")

                    val cOne = bind.countryOne.text.toString()
                    val cTwo = bind.countryTwo.text.toString()

                    var bufferTwo = cOne
                    var bufferOne = cTwo

                    bind.countryOne.text = bufferOne
                    bind.countryTwo.text = bufferTwo
                }

                onCreateObservable(bind)
                    .subscribe {
                        if (it == "") {

                        } else {
                            if (bind.countryOne.text.toString() == "UZS") {
                                bind.courseTwo.text = "${it.toDouble() / user.Rate.toDouble()}"
                            } else {
                                bind.courseTwo.text = "${user.Rate.toDouble() * it.toDouble()}"
                            }
                        }
                    }

                dialog.show()
            }

            override fun likeClick(user: UserForRoom, position: Int, view: View) {
                Toast.makeText(requireContext(), "Like clicked", Toast.LENGTH_SHORT).show()
            }

        })
        binding.rv.adapter = rv

        if (networkHelper.isNetworkConnected()) {

            apiService.getUsers().enqueue(object : Callback<List<User>> {
                @SuppressLint("CheckResult")
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    if (response.isSuccessful) {
                        val body = response.body()

                        body?.forEach {
                            it.image =
                                "https://flagcdn.com/w160/" + it.Ccy[0].lowercase() + it.Ccy[1].lowercase() + ".png"

                            userForRoom = UserForRoom(
                                it.Ccy,
                                it.CcyNm_UZ,
                                it.Date,
                                it.Diff,
                                it.Rate,
                                it.id,
                                it.image,
                                it.like
                            )

                            dbHelper.getDao().addUser(userForRoom)
                            dbHelper.getDao().getUser()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe {
                                    rv.submitList(it)
                                }
                        }
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    Toast.makeText(requireContext(), "No internet connected", Toast.LENGTH_SHORT).show()
                }

            })

            /*val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                "https://cbu.uz/uz/arkhiv-kursov-valyut/json/",
                null,
                { response ->
                    list = Gson().fromJson(
                        response.toString(),
                        object : TypeToken<MutableList<User>>() {}.type
                    )

                    for (i in list.indices) {
                        list[i].image =
                            "https://flagcdn.com/w160/" + list[i].Ccy[0].lowercase() + list[i].Ccy[1].lowercase() + ".png"
                    }
                    rv.submitList(list)
                }
            ) { error ->
                Log.d("TAG", "onCreateView: ${error?.message}")
            }
            requestQueue.add(jsonArrayRequest)*/

        } else {
            Toast.makeText(requireContext(), "Not Connected", Toast.LENGTH_SHORT).show()
        }

        dbHelper.getDao().getUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                rv.submitList(it)
            }

        val cons = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresCharging(true)
            .build()

        val work = PeriodicWorkRequest
            .Builder(MyWorker::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(cons).build()
        WorkManager.getInstance(requireContext())
            .enqueue(work)

        return binding.root
    }

    private fun onCreateObservable(bind: BottomSheetDialogBinding): Observable<String> {
        return Observable.create { emitter ->
            bind.courseOne.addTextChangedListener {
                emitter.onNext(it.toString())
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VPMainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VPMainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}