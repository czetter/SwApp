package com.example.swapp.fragments

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.swapp.R
import com.example.swapp.data.Park
import com.example.swapp.database.DatabaseManager
import kotlinx.android.synthetic.main.fragment_addpark.view.*


class AddParkFragment : DialogFragment() {

    override fun onResume() {
        super.onResume()
        val params = dialog.window!!.attributes
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }

    lateinit var databaseManager: DatabaseManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_addpark, container, false)

        databaseManager = DatabaseManager(this!!.context!!)



        view.btnAdd.setOnClickListener {
            if (!view.etName.text.isEmpty() && !view.etLatitude.text.isEmpty() && !view.etLongitude.text.isEmpty()) {
                var name = view.etName.text.toString()
                var lat = view.etLatitude.text.toString().toDouble()
                var long = view.etLongitude.text.toString().toDouble()
                var newPark = Park(name, lat, long)
                databaseManager.addPark(newPark)
                Toast.makeText(context,getString(R.string.added_restart),Toast.LENGTH_LONG).show()
                dismiss()
            }
        }


        return view
    }


}