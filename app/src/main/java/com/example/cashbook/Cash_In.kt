package com.example.cashbook

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cashbook.databinding.ActivityCashInBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class Cash_In : AppCompatActivity() {

    lateinit var binding: ActivityCashInBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //enableEdgeToEdge()
        binding=ActivityCashInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        time()
        date()
        binding.swichCashout.setOnClickListener {


            var intent:Intent=Intent(this@Cash_In,Cash_Out::class.java)
            startActivity(intent)
            finish()
        }

        binding.calender.setOnClickListener {

            val c: Calendar = Calendar.getInstance()
            val mYear: Int = c.get(Calendar.YEAR) // current year
            val mMonth: Int = c.get(Calendar.MONTH) // current month
            val mDay: Int = c.get(Calendar.DAY_OF_MONTH) // current day

            // date picker dialog
            var datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth -> // set day of month , month and year value in the edit text
                    binding.dateYears.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }

        binding.saveExite.setOnClickListener {

            var Amount = binding.cashinAmount.text.toString()
            var Notes = binding.notes.text.toString()

            var dateYear = binding.dateYears.text.toString()
            var Time = binding.timeText.text.toString()


            if(Amount!="" && Notes!="" && dateYear!=""&& Time!="")
            {
                var db:Databasehelper = Databasehelper(this@Cash_In)
                db.insertData(Amount,Notes,dateYear,Time)


                var inten :Intent=Intent(this@Cash_In,MainActivity::class.java)
                startActivity(inten)
                finish()

            }


            Log.d("===*==", " Anout=$Amount")
            Log.d("===*==", " NOtes=$Notes")
            Log.d("===*==", " date =$dateYear")
            Log.d("===*==", " TIme =$Time")
        }


        binding.timeSelact.setOnClickListener {

            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                binding.timeText.text = SimpleDateFormat("HH:mm aaa ").format(cal.time)
            }

            binding.timeText.setOnClickListener {
                TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
            }

        }
    }

    fun date() {

        var dateS: String
        var calendar: Calendar
        var simpleDateFormat: SimpleDateFormat

        calendar = Calendar.getInstance()
        simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        dateS = simpleDateFormat.format(calendar.time).toString()
        binding.dateYears.text = dateS
    }

    fun time() {

        var times: String
        var calendar: Calendar
        var simpleDateFormat: SimpleDateFormat

        calendar = Calendar.getInstance()
        simpleDateFormat = SimpleDateFormat("HH:mm aaa")
        times = simpleDateFormat.format(calendar.time).toString()

        binding.timeText.text = times
    }

}
