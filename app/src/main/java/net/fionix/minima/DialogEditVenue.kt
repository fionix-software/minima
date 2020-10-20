package net.fionix.minima

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.fionix.minima.database.DatabaseMain
import net.fionix.minima.model.EntityTimetable

class DialogEditVenue(context: Context, val data: EntityTimetable) : Dialog(context) {

    init {
        setCanceledOnTouchOutside(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        // super
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_edit_venue)

        // edit text
        val timetableVenueEditText: EditText = findViewById(R.id.editText1)
        if (data.timetableVenue.isNotEmpty()) {
            timetableVenueEditText.setText(data.timetableVenue)
        }

        // save button
        val saveButton: Button = findViewById(R.id.saveButton)
        saveButton.setOnClickListener {

            // check if string is empty
            val venue = timetableVenueEditText.text.toString().trim()
            if (venue.isEmpty() || venue == data.timetableVenue) {
                Toast.makeText(context, "Invalid venue", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // update venue name
            GlobalScope.launch(Dispatchers.IO) {

                // update venue name
                DatabaseMain.getDatabase(context).timetableDao().updateVenue(venue, data.timetableId)

                // close dialog
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }
    }
}