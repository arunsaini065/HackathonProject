package com.serhat.autosub

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import androidx.core.widget.addTextChangedListener

class LanguagePickerActivity : AppCompatActivity() {

    private lateinit var adapter: LanguageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_picker)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        val recycler = findViewById<RecyclerView>(R.id.recycler)
        val searchInput = findViewById<TextInputEditText>(R.id.searchInput)

        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        adapter = LanguageAdapter(
            currentLang = LocaleHelper.currentLangTag(this),
            onClick = { lang -> onLanguageSelected(lang) }
        )
        recycler.layoutManager = GridLayoutManager(this, 2)
        recycler.adapter = adapter


        adapter.submitFull(Languages.all)

        searchInput.addTextChangedListener { text ->
            adapter.filter(text?.toString())
        }
    }

    private fun onLanguageSelected(lang: Language) {
        LocaleHelper.applyLocale(this, lang.code)
        setResult(RESULT_OK, Intent().putExtra("selected_lang_code", lang.code))
        finish()
    }
}
