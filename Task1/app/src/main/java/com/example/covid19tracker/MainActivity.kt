package com.example.covid19tracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.covid19tracker.databinding.ActivityMainBinding
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    companion object {
        var modelList: MutableList<Model> = ArrayList()
    }
    var adapter: Adapter? = null
    var _binding: ActivityMainBinding?=null
    val binding get() = _binding!!
    val cities:List<String> = listOf("Agar Malwa","Alirajpur","Anuppur","Ashoknagar","Balaghat","Barwani","Betul","Bhind","Bhopal","Burhanpur","Chhatarpur","Chhindwara","Damoh","Datia","Dewas","Dhar","Dindori","Guna","Gwalior","Harda","Hoshangabad","Indore","Jabalpur","Jhabua","Katni","Khandwa","Khargone","Mandla","Mandsaur","Morena","Narsinghpur","Neemuch","Niwari","Other Region","Panna","Raisen","Rajgarh","Ratlam","Rewa","Sagar","Satna","Sehore","Seoni","Shahdol","Shajapur","Sheopur","Shivpuri","Sidhi","Singrauli","Tikamgarh","Ujjain","Umaria","Vidisha",)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fetchData()
    }

    private fun fetchData() {
        val url = "https://data.covid19india.org/state_district_wise.json"
        val request = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    binding.prgbar.visibility = View.GONE
                    val `object` = JSONObject(response)
                    val object1 = `object`.getJSONObject("Madhya Pradesh")
                    val object2 = object1.getJSONObject("districtData")
                    for(i in cities.indices) {
                        val object3 = object2.getJSONObject(cities[i])
                        val object4 = object3.getJSONObject("delta")
                        val active = object3.getString("active")
                        val confirmed = object3.getString("confirmed")
                        val deceased = object3.getString("deceased")
                        val recovered = object3.getString("recovered")
                        val confInc = object4.getString("confirmed")
                        val confDec = object4.getString("deceased")
                        val confRec = object4.getString("recovered")
                        val model = Model(
                            cities[i], confirmed, deceased, recovered,
                            active,
                            confInc, confDec, confRec
                        )
                        modelList.add(model)
                    }

                    adapter = Adapter(this@MainActivity, modelList)
                    binding.listView.adapter = adapter

                    // In case of error it will run
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error ->
            when (error) {
                is NetworkError -> {
                    binding.prgbar.visibility= View.GONE
                    Toast.makeText(this,error.message, Toast.LENGTH_SHORT).show()
                }
                is ServerError -> {
                    binding.prgbar.visibility= View.GONE
                    Toast.makeText(this,error.message, Toast.LENGTH_SHORT).show()
                }
                is AuthFailureError -> {
                    binding.prgbar.visibility= View.GONE
                    Toast.makeText(this,error.message, Toast.LENGTH_SHORT).show()
                }
                is ParseError -> {
                    binding.prgbar.visibility= View.GONE
                    Toast.makeText(this,error.message, Toast.LENGTH_SHORT).show()
                }
                is NoConnectionError -> {
                    binding.prgbar.visibility= View.GONE
                    Toast.makeText(this,error.message, Toast.LENGTH_SHORT).show()
                }
                is TimeoutError -> {
                    binding.prgbar.visibility= View.GONE
                    Toast.makeText(this,error.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    binding.prgbar.visibility= View.GONE
                    Toast.makeText(this,"Something Went Wrong...", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)
    }


}