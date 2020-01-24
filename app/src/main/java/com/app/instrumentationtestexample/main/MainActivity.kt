package com.app.instrumentationtestexample.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.instrumentationtestexample.R
import com.app.instrumentationtestexample.model.Team
import com.app.instrumentationtestexample.service.NbaService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.adapter_item_view.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NbaService.getTeams {
            it?.also {
                recycler_view?.visibility = View.VISIBLE
                recycler_view?.layoutManager = LinearLayoutManager(this)

                val adapter = Adapter(it)
                adapter.listener = { openDetailActivity(it) }

                recycler_view.adapter = adapter
            }
        }
    }

    private fun openDetailActivity(team: Team) {
        val intent = Intent(this, TeamDetailActivity::class.java)
        intent.putExtra(TeamDetailActivity.INTENT_TEAM_DETAIL_KEY, team)
        startActivity(intent)
    }

    inner class Adapter(private val teams: List<Team>) :
        RecyclerView.Adapter<Adapter.ViewHolder>() {

        var listener: ((team: Team) -> Unit)? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_item_view, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return teams.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val team = teams[position]
            holder.bindView(team)
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            init {
                itemView.setOnClickListener { listener?.invoke(teams[adapterPosition]) }
            }

            fun bindView(team: Team) {
                itemView.text_view.text = team.teamName
                itemView.image_view.let {
                    Picasso.get()
                        .load(team.image)
                        .into(it)
                }
            }
        }
    }
}
