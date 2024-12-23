package com.example.hirupsehat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PencapaianFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pencapaian, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_pencapaian)
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        val items = listOf(
            PencapaianItem(R.drawable.ic_pencapaian, "Streak Hari Ke-10"),
            PencapaianItem(R.drawable.ic_pencapaian, "Streak Hari Ke-30")
        )

        recyclerView.adapter = PencapaianAdapter(items)

        return view
    }
}
