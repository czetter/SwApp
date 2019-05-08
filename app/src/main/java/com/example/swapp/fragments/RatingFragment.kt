package com.example.swapp.fragments

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.example.swapp.R
import com.example.swapp.data.Park
import kotlinx.android.synthetic.main.fragment_rating.*
import kotlinx.android.synthetic.main.fragment_rating.view.*

class RatingFragment : DialogFragment() {


    var stars = arrayListOf<ImageView>()
    lateinit var communicator: Communicator
    var rating: Int = -1

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            communicator = if (targetFragment != null) {
                targetFragment as Communicator
            } else {
                activity as Communicator
            }
        } catch (e: ClassCastException) {
            throw RuntimeException(e)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_rating, container, false)

        stars.add(view.star1)
        stars.add(view.star2)
        stars.add(view.star3)
        stars.add(view.star4)
        stars.add(view.star5)


        for (i in 0 until 5) {
            stars[i].setOnClickListener {
                changeIcons(i)
                rating = i
            }
        }

        view.btnSubmit.setOnClickListener {
            if (rating != -1) {
                communicator.onRatingPicked(rating + 1,getCurrentParkName())
                dismiss()
            } else
                Toast.makeText(context, getString(R.string.chooseabove), Toast.LENGTH_LONG).show()
        }

        return view
    }


    fun changeIcons(num: Int) {
        for (i in 0 until 5)
            stars[i].setImageResource(R.drawable.ic_star_empty)
        for (i in 0..num)
            stars[i].setImageResource(R.drawable.ic_star_full)

    }

    fun getCurrentParkName(): String {
        var bundle = this.arguments
        if (bundle != null) return bundle.getString("name", "Nullocska")
        return "nullocska"
    }

    interface Communicator {
        fun onRatingPicked(num: Int, parkname:String)

    }
}

