package net.fionix.minima

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar

class DialogAdd(context: Context) : Dialog(context) {

    init {
        setCanceledOnTouchOutside(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        // super
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_add)

        // progress bar
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE;

        // edit texts
        val courseEditText: EditText = findViewById(R.id.editText1)
        val groupEditText: EditText = findViewById(R.id.editText2)

        // add button
        val addButton: Button = findViewById(R.id.button1)
        addButton.setOnClickListener {
            // todo: add course into database
        }
    }

}