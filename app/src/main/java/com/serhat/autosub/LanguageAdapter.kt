package com.serhat.autosub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class LanguageAdapter(
    private val currentLang: String?,
    private val onClick: (Language) -> Unit
) : ListAdapter<Language, LanguageAdapter.VH>(Diff()) {

    private var fullList: List<Language> = emptyList()

    fun submitFull(list: List<Language>) {
        fullList = list
        submitList(list)
    }

    fun filter(query: String?) {
        val q = query?.trim()?.lowercase().orEmpty()
        if (q.isEmpty()) {
            submitList(fullList)
        } else {
            submitList(fullList.filter {
                it.displayName.lowercase().contains(q) ||
                        it.nativeName.lowercase().contains(q) ||
                        it.code.lowercase().contains(q)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_language, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val card = itemView as MaterialCardView
        private val title = itemView.findViewById<TextView>(R.id.langTitle)
        private val subtitle = itemView.findViewById<TextView>(R.id.langSubtitle)

        fun bind(item: Language) {
            title.text = item.nativeName
            subtitle.text = "${item.displayName} • ${item.code}"

            val isSelected = currentLang?.equals(item.code, ignoreCase = true) == true

            val outlineColor = itemView.context.getColor(R.color.outline)
            val highlightColor = itemView.context.getColor(R.color.teal_700)

            card.strokeWidth = if (isSelected) 4 else 1
            card.strokeColor = if (isSelected) highlightColor else outlineColor

            card.setOnClickListener { onClick(item) }
        }
    }

    class Diff : DiffUtil.ItemCallback<Language>() {
        override fun areItemsTheSame(oldItem: Language, newItem: Language) = oldItem.code == newItem.code
        override fun areContentsTheSame(oldItem: Language, newItem: Language) = oldItem == newItem
    }
}
