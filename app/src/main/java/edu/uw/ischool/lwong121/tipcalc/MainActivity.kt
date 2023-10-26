package edu.uw.ischool.lwong121.tipcalc

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private var tipPct: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnTip = findViewById<Button>(R.id.btnTip)
        val editText = findViewById<EditText>(R.id.editText)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val radioFifteen = findViewById<RadioButton>(R.id.radioFifteen)

        btnTip.isEnabled = false
        // select 15% as the default
        radioFifteen.isChecked = true
        tipPct = 0.15

        editText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                btnTip.isEnabled = true

                // need to remove otherwise infinite loop
                editText.removeTextChangedListener(this)

                val amountStr = s.toString().replace("$", "")
                if (amountStr.isEmpty()) {
                    // don't just show $ with no numbers, show the "Amount" label instead
                    editText.text.clear()
                    btnTip.isEnabled = false
                } else {
                    // add the $ to the string
                    val formattedAmount = "$$amountStr"
                    editText.setText(formattedAmount)
                    editText.setSelection(formattedAmount.length)
                }

                // add the listener back for future changes
                editText.addTextChangedListener(this)
            }
        })

        // Extra Credit: Add a control to choose the tip amount
        radioGroup.setOnCheckedChangeListener { _, radioId ->
            // update the selected tipPct state
            tipPct = when (radioId) {
                R.id.radioTen -> 0.10
                R.id.radioEighteen -> 0.18
                R.id.radioTwenty -> 0.20
                R.id.radioTwentyFive -> 0.25
                else -> 0.15
            }
        }

        btnTip.setOnClickListener {
            // get the amount input in currency form
            val amountStr = editText.text.toString().replace("$", "")
            var amount = amountStr.toDouble()
            amount = (amount * 100.0).roundToInt() / 100.0

            // update the value seen in the EditText box to only have 2 decimal places
            editText.setText(String.format("$%.2f", amount))

            // calculate tip + format in currency form
            val tip = tipPct * amount
            val tipStr = String.format("$%.2f", tip)

            // create and show the toast with tip
            Toast.makeText(this, tipStr, Toast.LENGTH_LONG).show()
        }
    }
}