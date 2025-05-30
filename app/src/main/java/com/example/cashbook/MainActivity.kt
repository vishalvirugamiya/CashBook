package com.example.cashbook
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView  // ✅ CORRECT ONE
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cashbook.databinding.ActivityMainBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.io.FileOutputStream
import java.util.stream.Collectors
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var databaseHelper: Databasehelper

    lateinit var List: ArrayList<UserData>
    lateinit var display :ArrayList<UserData>

    private lateinit var toolbar: MaterialToolbar

    private lateinit var dateRangeText: TextView

    @SuppressLint("SetTextI18n")
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

        CashinFunction()
        Adappter()

        val chipGroup = findViewById<ChipGroup>(R.id.chipGroup)

        dateRangeText = findViewById(R.id.dateRangeText)
        // Loop through all chips and set individual click listeners

        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            chip.setOnClickListener {

            //    Toast.makeText(this, "Clicked: ${chip.text}", Toast.LENGTH_SHORT).show()

                if(chip.id==R.id.all)
                {
                    dateRangeText.text = chip.text
                    CashinFunction()
                    Adappter()

                }else if(chip.id==R.id.Today)
                {
                    var dateS: String
                    var calendar: Calendar
                    var simpleDateFormat: SimpleDateFormat

                    calendar = Calendar.getInstance()
                    simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                    dateS = simpleDateFormat.format(calendar.time).toString()

                    dateRangeText.text = chip.text

                    todayTransaction(dateS)


                }else if(chip.id==R.id.weekly)
                {
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
                    val startDate = calendar.time

                    calendar.add(Calendar.DAY_OF_WEEK, 6)
                    val endDate = calendar.time

                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                     dateRangeText.text = "${dateFormat.format(startDate)} -> ${dateFormat.format(endDate)}"

                        // Iterator calendar for looping through the week range
                        val loopCalendar = Calendar.getInstance()
                        loopCalendar.time = startDate

                        while (!loopCalendar.time.after(endDate)) {
                            val currentDate = loopCalendar.time
                            val currentFormatted = dateFormat.format(currentDate)

                            loopCalendar.add(Calendar.DAY_OF_MONTH, 1)
                            Log.d("WeekDate", currentFormatted)
                            weeklytransaction(currentFormatted)

                        }

                }else if(chip.id==R.id.monthly)
                {
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.DAY_OF_MONTH, 1)
                    val startDate = calendar.time

                    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                    val endDate = calendar.time

                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                    dateRangeText.text = "${dateFormat.format(startDate)} -> ${dateFormat.format(endDate)}"

                    val loopCalendar = Calendar.getInstance()
                    loopCalendar.time = startDate

                    while (!loopCalendar.time.after(endDate)) {
                        val currentDate = loopCalendar.time
                        val currentFormatted = dateFormat.format(currentDate)

                        loopCalendar.add(Calendar.DAY_OF_MONTH, 1)

                        monthlyTransaction(currentFormatted) // Daily filtering
                    }

                }else if(chip.id==R.id.yearly)
                {
                    val calendar = Calendar.getInstance()

                      // Set to the beginning of the year (January 1st)
                    calendar.set(Calendar.MONTH, Calendar.JANUARY)
                    calendar.set(Calendar.DAY_OF_MONTH, 1)
                    calendar.set(Calendar.YEAR, 2025) // Explicitly set year if needed
                    val startDate = calendar.time

                       // Set to the end of the year (December 31st)
                    calendar.set(Calendar.MONTH, Calendar.DECEMBER)
                    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                    val endDate = calendar.time

                        // Format the date range in "dd-MMM-yyyy" (e.g., 01-Jan-2025)
                    val displayFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                    dateRangeText.text = "${displayFormat.format(startDate)} -> ${displayFormat.format(endDate)}"

                     // Loop through all dates in the year
                    val loopCalendar = Calendar.getInstance()
                    loopCalendar.time = startDate

                    while (!loopCalendar.time.after(endDate)) {
                        val currentDate = loopCalendar.time
                        val currentFormatted = displayFormat.format(currentDate)

                        yearly(currentFormatted)

                        loopCalendar.add(Calendar.DAY_OF_MONTH, 1)
                    }

                 //   dateRangeText.text = chip.text
                }
            }
        }

        toolbar = binding.toAppbar
        setSupportActionBar(toolbar)

        val sharedPref = getSharedPreferences("CashBook", Context.MODE_PRIVATE)

        val amountin = sharedPref.getInt("AmountIN", 0)

        val amountout = sharedPref.getInt("AmountOUT", 0)


        Log.d("===*====","cashIN = $amountin")
        Log.d("===*====","cashout = $amountout")

        binding.totalCashIn?.text  =amountin.toString()
        binding.totalCashOut?.text  =amountout.toString()

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

        binding.toAppbar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item ->

            when(item.itemId)
            {
                R.id.pdf ->{
                    val dataList = databaseHelper.fetchAllData()
                    generateCashbookPDF(this@MainActivity, dataList)
                }

                R.id.cashinMenu ->{

                    cashInOut("IN")
                }
                R.id.cashOutmenu ->{

                    cashInOut("OUT")
                }
                R.id.nameAddress ->{

                    var bottonSTdialog = BottomSheetDialog(this@MainActivity)

                    bottonSTdialog.setContentView(R.layout.notesearch)
                }
                R.id.Print ->{

                    val dataList = databaseHelper.fetchAllData()
                    generateCashbookPDF(this@MainActivity, dataList)
                }
            }

            return@OnMenuItemClickListener false
        })

    }

    private fun yearly(yearly: String) {

        val filteredList = List.filter { it.dateYear == yearly }

        if (filteredList.isNotEmpty()) {
            Log.d("==***===", "Match found: $yearly")

            display.clear()
            display.addAll(filteredList)

            Adappter()  // Ensure Adapter() updates the RecyclerView or UI
        }
    }

    private fun monthlyTransaction(monthly: String) {

        val monthData = mutableListOf<UserData>()

        monthData.addAll(List.filter { it.dateYear == monthly })

        if (monthData.isNotEmpty()) {
            Log.d("==***===", "Match found: $monthly")

            display.clear()
            display.addAll(monthData)
            Adappter()
        }

    }

    private fun weeklytransaction(weeklyday: String) {

        val filteredList = List.filter { it.dateYear == weeklyday }

        if (filteredList.isNotEmpty()) {
            Log.d("==***===", "Match found: $weeklyday")

            display.clear()
            display.addAll(filteredList)
            Adappter()

        }
    }

    private fun todayTransaction(dateS: String) {

        var filteredList : List<UserData> = List.stream()
            .filter { data : UserData -> dateS == data.dateYear
            }.collect(Collectors.toList<Any>()) as List<UserData>

        display.clear()
        display.addAll(filteredList)
        Adappter()
    }

    private fun cashInOut(type: String) {

       var filteredList : List<UserData> = List.stream()
           .filter { data : UserData -> type ==data.type
       }.collect(Collectors.toList<Any>()) as List<UserData>

        display.clear()
        display.addAll(filteredList)

        Adappter()

    }

    private fun filterData(Searchtext: String) {

        display.clear()

        for (model in List){

            if(model.notes.lowercase().contains(Searchtext.lowercase()))
            {
                display.add(model)
            }else if(model.dateYear.plus(model.Amount).contains(Searchtext))
            {
                display.add(model)
            }

        }
        Adappter()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val searchItem = menu?.findItem(R.id.Search)
        val searchView = searchItem?.actionView as SearchView

        searchView.queryHint = "Search..."

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MainActivity, "You searched: $query", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle search text change
                if (newText != null) {
                    filterData(newText)
                }
                return true
            }
        })

        // Optional: Handle open/close animations or events
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                toolbar.title = ""
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {

                toolbar.title = "vishal"
                return true
            }


        })

        return true
    }


    private fun generateCashbookPDF(mainActivity: MainActivity, dataList: ArrayList<UserData>) {
        val pdfDocument = PdfDocument()
        val paint = Paint()
        val titlePaint = Paint()

        // Setup for the PDF page
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        var y = 50
        titlePaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        titlePaint.textSize = 20f
        titlePaint.color = Color.BLACK
        canvas.drawText("Cashbook Report", 220f, y.toFloat(), titlePaint)
        y += 40

        paint.textSize = 12f

        // Headers
        canvas.drawText("ID", 10f, y.toFloat(), paint)
        canvas.drawText("Amount", 40f, y.toFloat(), paint)
        canvas.drawText("Notes", 100f, y.toFloat(), paint)
        canvas.drawText("Date", 250f, y.toFloat(), paint)
        canvas.drawText("Time", 350f, y.toFloat(), paint)
        canvas.drawText("Type", 450f, y.toFloat(), paint)

        y += 20

        for (record in dataList) {
            if (y > 800) break // Avoid going off the page; implement multipage if needed

            canvas.drawText(record.id.toString(), 10f, y.toFloat(), paint)
            canvas.drawText(record.Amount.toString(), 40f, y.toFloat(), paint)
            canvas.drawText(record.notes ?: "", 100f, y.toFloat(), paint)
            canvas.drawText(record.dateYear ?: "", 250f, y.toFloat(), paint)
            canvas.drawText(record.Time ?: "", 350f, y.toFloat(), paint)
            canvas.drawText(record.type ?: "", 450f, y.toFloat(), paint)
            y += 20
        }

        pdfDocument.finishPage(page)

        val file = File(mainActivity.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Cashbook_Report.pdf")
        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()
//
//        Toast.makeText(context, "PDF saved to: ${file.absolutePath}", Toast.LENGTH_LONG)
//            .show() TODO ("Not yet implemented")


        val uri = FileProvider.getUriForFile(this, "$packageName.provider", file)

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No PDF viewer found.", Toast.LENGTH_SHORT).show()
        }
    }


    @SuppressLint("NotifyDataSetChanged")

    private fun Adappter() {

       if (!::display.isInitialized) return  // ✅ Prevent crash if display is not ready

        val layoutManager = LinearLayoutManager(this).apply {
            reverseLayout = true       // Show items in reverse order
            stackFromEnd = true
        }
        binding.RecView.layoutManager = layoutManager


        val adapter = MyAdapter(this@MainActivity, display)
        binding.RecView.adapter = adapter

        adapter.notifyDataSetChanged()


    }

    private fun CashinFunction() {

        databaseHelper=Databasehelper(this@MainActivity)
        List=databaseHelper.fetchAllData()

        display = ArrayList()
        display.addAll(List)

        var totalIn = 0
        var totalOut = 0

        for (item in List) {
            if (item.type == "IN") totalIn += item.Amount
            else if (item.type == "OUT") totalOut += item.Amount
        }

        val sharedPref = getSharedPreferences("CashBook", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putInt("AmountIN", totalIn)
        editor.putInt("AmountOUT", totalOut)
        editor.apply()

        binding.totalCashIn?.text = totalIn.toString()
        binding.totalCashOut?.text = totalOut.toString()

        if(totalIn>totalOut)
        {
            binding.balane?.setTextColor(Color.parseColor("#1A7506"))
        }else{

            binding.balane?.setTextColor(Color.parseColor("#FF1503"))
        }

        binding.balane?.text  = (totalIn-totalOut).toString()
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

    override fun onResume() {
        super.onResume()
        CashinFunction()
        binding.RecView.adapter = MyAdapter(this, List)

    }

}
