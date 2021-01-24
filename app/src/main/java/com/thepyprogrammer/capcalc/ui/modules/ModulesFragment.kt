package com.thepyprogrammer.capcalc.ui.modules

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.thepyprogrammer.capcalc.MainActivity
import com.thepyprogrammer.capcalc.R
import com.thepyprogrammer.capcalc.model.Database
import com.thepyprogrammer.capcalc.model.Module
import java.io.InputStream


class ModulesFragment : Fragment() {

    private lateinit var moduleViewModel: ModuleViewModel

    companion object {
        val modules = HashMap<Module, Double>()
    }

    val padding = resources.getDimensionPixelOffset(R.dimen.table_text_padding)
    val caps = arrayListOf(5.0, 4.5, 4.0, 3.5, 3.0, 2.5, 2.0, 1.5, 1.0, 0.0)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        moduleViewModel = ViewModelProvider(this).get(ModuleViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_modules, container, false)
        
        val capselector: Spinner = root.findViewById(R.id.capselector)
        val autocomplete: AutoCompleteTextView = root.findViewById(R.id.autocomplete)
        val add: Button = root.findViewById(R.id.add)
        val table: TableLayout = root.findViewById(R.id.table)
        
        val spinnerAdapter = this.context?.let {
            ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_item,
                    caps
            )
        }
        spinnerAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        capselector.adapter = spinnerAdapter



        var database = Database.currentOccurence
        if(database == null) {
            val inputStream: InputStream = resources.openRawResource(R.raw.data)
            database = Database(inputStream)
        }

        val autocompleteAdapter = this.context?.let {
            ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_item,
                    database.getFullNames()
            )
        }

        autocompleteAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        autocomplete.setAdapter(autocompleteAdapter)

        add.setOnClickListener{
            val module = database.get(autocomplete.listSelection)
            val cap: Double = capselector.selectedItem as Double
            if(module != null) {
                modules.put(module, cap)

                val row = TableRow(activity)
                row.layoutParams = TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT
                )



                row.addView(formatTextView(TextView(activity), module.code));
                row.addView(formatTextView(TextView(activity), "$cap"));


                table.addView(row)
            }


        }




        return root
    }

    fun formatTextView(view: TextView, text: String): TextView {
        view.layoutParams = TableRow.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                R.dimen.rowHeight
        ).apply {
            gravity = Gravity.CENTER
        }
        view.setPadding(padding, padding, padding, padding)
        view.text = text
        view.textAlignment = View.TEXT_ALIGNMENT_CENTER
        view.textSize = resources.getDimension(R.dimen.table_text_size)
        return view
    }
}