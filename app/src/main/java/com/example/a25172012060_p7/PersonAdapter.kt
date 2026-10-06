package com.example.a25172012060_p7

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PersonAdapter(
    private val context: Context,
    private val personList: ArrayList<Person>,
    private val onDeleteClick: (Person, Int) -> Unit
) : RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvPhone: TextView = itemView.findViewById(R.id.tvPhone)
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        val btnDelete: FloatingActionButton = itemView.findViewById(R.id.btnDelete)
        val cardPerson: View = itemView.findViewById(R.id.cardPerson)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.single_item_view, parent, false)
        return PersonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = personList[position]

        holder.tvName.text = person.name
        holder.tvPhone.text = person.phoneNo
        holder.tvEmail.text = person.emailId
        holder.tvAddress.text = person.address

        holder.btnDelete.setOnClickListener {
            onDeleteClick(person, holder.adapterPosition)
        }

        holder.cardPerson.setOnClickListener {
            val intent = Intent(context, MapActivity::class.java).apply {
                putExtra("person", person)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = personList.size
}
