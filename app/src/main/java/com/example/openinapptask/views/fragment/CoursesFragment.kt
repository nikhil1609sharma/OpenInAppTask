package com.example.openinapptask.views.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.openinapptask.R
import com.example.openinapptask.databinding.FragmentCoursesBinding


class CoursesFragment : Fragment() {

    lateinit var coursesBinding: FragmentCoursesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        coursesBinding = FragmentCoursesBinding.inflate(layoutInflater)
        val view = coursesBinding.root
        return view
    }

}