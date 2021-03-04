package com.example.academyproject.views

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.util.*

class FragmentDatePicker :
    DialogFragment(),
    DatePickerDialog.OnDateSetListener{

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()

        return DatePickerDialog(
            requireContext(),
            this,
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        )
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val bundle = Bundle()
        bundle.putInt(BUNDLE_KEY_YEAR, year)
        bundle.putInt(BUNDLE_KEY_MONTH, month)
        bundle.putInt(BUNDLE_KEY_DAY_OF_MONTH, dayOfMonth)

        setFragmentResult(KEY_RESULT, bundle)
    }

    companion object {
        const val KEY_RESULT = "DATE_FROM_DATE_PICKER"
        const val BUNDLE_KEY_YEAR = "YEAR"
        const val BUNDLE_KEY_MONTH = "MONTH"
        const val BUNDLE_KEY_DAY_OF_MONTH = "DAY_OF_MONTH"
    }
}