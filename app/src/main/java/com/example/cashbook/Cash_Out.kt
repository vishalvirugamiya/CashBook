package com.example.cashbook

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cashbook.databinding.ActivityCashOutBinding
import java.text.SimpleDateFormat
import java.util.Calendar
class Cash_Out : AppCompatActivity() {

    lateinit var binding: ActivityCashOutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCashOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        date()
        time()
        // Retrieve the data passed from MyAdapter
        val model: UserData? = intent.getSerializableExtra("DataIn") as UserData?  // Use the same key "DataIn"

        if (model != null) {
            // Populate the fields with the data from the passed object
            binding.notesCashout.setText(model.notes)
            binding.cashoutAmount.setText(model.Amount.toString())
            binding.dateYearsCashout.setText(model.dateYear)
            binding.time.setText(model.Time)
        }

        binding.back.setOnClickListener {
            val intent: Intent = Intent(this@Cash_Out, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.swichCashin.setOnClickListener {
            val intent: Intent = Intent(this@Cash_Out, Cash_In::class.java)
            startActivity(intent)
            finish()
        }
        binding.timeSelact.setOnClickListener {

            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                binding.time.text = SimpleDateFormat("HH:mm aaa ").format(cal.time)
            }

            binding.time.setOnClickListener {
                TimePickerDialog(
                    this,
                    timeSetListener,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    true
                ).show()
            }

        }

        binding.calender.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val mYear: Int = c.get(Calendar.YEAR)
            val mMonth: Int = c.get(Calendar.MONTH)
            val mDay: Int = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    binding.dateYearsCashout.setText("$dayOfMonth/${monthOfYear + 1}/$year")
                },
                mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }

        binding.saveExite.setOnClickListener {
            val AmountC_out = binding.cashoutAmount.text.toString()
            val Notes = binding.notesCashout.text.toString()
            val dateYear = binding.dateYearsCashout.text.toString()
            val Time = binding.time.text.toString()

            if (AmountC_out.isNotEmpty() && dateYear.isNotEmpty() && Time.isNotEmpty()) {
                val db: Databasehelper = Databasehelper(this@Cash_Out)

                if (model != null) {
                    // Update the existing data
                    db.updateData(AmountC_out, Notes, dateYear, Time, "OUT", model.id)
                } else {
                    // Insert new data if model is null
                    db.insertData(AmountC_out, Notes, dateYear, Time, "OUT")
                }

                // Return to MainActivity after saving
                val intent: Intent = Intent(this@Cash_Out, MainActivity::class.java)
                startActivity(intent)
                finish()
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
        binding.dateYearsCashout.text = dateS
    }

    fun time() {

        var times: String
        var calendar: Calendar
        var simpleDateFormat: SimpleDateFormat

        calendar = Calendar.getInstance()
        simpleDateFormat = SimpleDateFormat("HH:mm aaa")
        times = simpleDateFormat.format(calendar.time).toString()

        binding.time.text = times
    }
}