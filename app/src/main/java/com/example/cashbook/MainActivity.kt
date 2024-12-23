package com.example.cashbook

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cashbook.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var List: ArrayList<UserData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // enableEdgeToEdge()

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.toAppbar.setNavigationOnClickListener {

                binding.drawerLayout.openDrawer(Gravity.START)
        }

        binding.cashin.setOnClickListener {

            var inten : Intent = Intent(this@MainActivity,Cash_In::class.java)
            startActivity(inten)
        }

        binding.cashout.setOnClickListener {

            var inten : Intent = Intent(this@MainActivity,Cash_Out::class.java)
            startActivity(inten)
        }


        CashinFunction()

        binding.RecView.layoutManager= LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        var adapter:MyAdapter=MyAdapter(this@MainActivity,List)
        binding.RecView.adapter=adapter


      //  Adappter()
    }
//
//    private fun Adappter() {
//
//
//    }

    private fun CashinFunction() {

        var db:Databasehelper=Databasehelper(this@MainActivity)
        List=db.cashInData()
    }



    override fun onBackPressed():Unit {
        super.onBackPressed()
        //Log.d("CDA", "onBackPressed Called")
        val setIntent = Intent(Intent.ACTION_MAIN)
        setIntent.addCategory(Intent.CATEGORY_HOME)
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(setIntent)

        return
    }

}