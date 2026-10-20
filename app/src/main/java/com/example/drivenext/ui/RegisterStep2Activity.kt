package com.example.drivenext.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.drivenext.databinding.ActivityRegisterStep2Binding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class RegisterStep2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterStep2Binding

    private val dateFormatter = SimpleDateFormat("MM/dd/yyyy", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterStep2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        setupDatePicker()
        setupValidationListeners()

        binding.btnNext.setOnClickListener {
            proceedToStep3()
        }
    }

    private fun setupDatePicker() {
        val openPicker = {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Выберите дату рождения")
                .build()

            datePicker.addOnPositiveButtonClickListener { selectedUtcMillis ->
                val dateString = dateFormatter.format(Date(selectedUtcMillis))
                binding.etBirthDate.setText(dateString)
                checkFormFilled()
            }
            datePicker.show(supportFragmentManager, "BIRTH_DATE_PICKER")
        }

        binding.etBirthDate.setOnClickListener { openPicker() }
        binding.tilBirthDate.setStartIconOnClickListener { openPicker() }
    }

    private fun setupValidationListeners() {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                checkFormFilled()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.etLastName.addTextChangedListener(watcher)
        binding.etFirstName.addTextChangedListener(watcher)
        binding.etBirthDate.addTextChangedListener(watcher)

        binding.rgGender.setOnCheckedChangeListener { _, _ ->
            checkFormFilled()
        }
    }

    private fun checkFormFilled() {
        val lastName = binding.etLastName.text?.toString()?.trim().orEmpty()
        val firstName = binding.etFirstName.text?.toString()?.trim().orEmpty()
        val birthDate = binding.etBirthDate.text?.toString()?.trim().orEmpty()
        val isGenderSelected = binding.rgGender.checkedRadioButtonId != -1

        binding.btnNext.isEnabled = lastName.isNotEmpty() &&
                firstName.isNotEmpty() &&
                birthDate.isNotEmpty() &&
                isGenderSelected
    }

    private fun proceedToStep3() {
        val birthDate = binding.etBirthDate.text?.toString()?.trim().orEmpty()

        val dateRegex = Regex("^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/[0-9]{4}$")
        if (!dateRegex.matches(birthDate)) {
            binding.tilBirthDate.error = "Введите корректную дату рождения."
            Toast.makeText(this, "Введите корректную дату рождения.", Toast.LENGTH_SHORT).show()
            return
        }

        val gender = if (binding.rbMale.isChecked) "Мужской" else "Женский"

        val intent = Intent(this, RegisterStep3Activity::class.java).apply {
            putExtras(this@RegisterStep2Activity.intent)
            putExtra("EXTRA_LAST_NAME", binding.etLastName.text?.toString()?.trim())
            putExtra("EXTRA_FIRST_NAME", binding.etFirstName.text?.toString()?.trim())
            putExtra("EXTRA_MIDDLE_NAME", binding.etMiddleName.text?.toString()?.trim())
            putExtra("EXTRA_BIRTH_DATE", birthDate)
            putExtra("EXTRA_GENDER", gender)
        }
        startActivity(intent)
    }
}