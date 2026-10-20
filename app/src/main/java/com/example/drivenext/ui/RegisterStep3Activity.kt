package com.example.drivenext.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.drivenext.databinding.ActivityRegisterStep3Binding
import com.google.android.material.datepicker.MaterialDatePicker
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class RegisterStep3Activity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterStep3Binding

    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    private var avatarUri: Uri? = null
    private var licensePhotoUri: Uri? = null
    private var passportPhotoUri: Uri? = null

    private var currentTarget: PhotoTarget? = null
    private var tempCameraUri: Uri? = null

    private enum class PhotoTarget { AVATAR, LICENSE, PASSPORT }

    private val pickFromGallery = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            handlePhotoResult(uri)
        }
    }

    private val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && tempCameraUri != null) {
            handlePhotoResult(tempCameraUri!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterStep3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        setupDatePicker()
        setupValidationListeners()
        setupPhotoPickers()

        binding.btnNext.setOnClickListener {
            validateAndCompleteRegistration()
        }
    }

    private fun setupDatePicker() {
        val openPicker = {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Выберите дату выдачи")
                .build()

            datePicker.addOnPositiveButtonClickListener { selectedUtcMillis ->
                val dateString = dateFormatter.format(Date(selectedUtcMillis))
                binding.etIssueDate.setText(dateString)
                checkFormFilled()
            }
            datePicker.show(supportFragmentManager, "ISSUE_DATE_PICKER")
        }

        binding.etIssueDate.setOnClickListener { openPicker() }
        binding.tilIssueDate.setStartIconOnClickListener { openPicker() }
    }

    private fun setupPhotoPickers() {
        binding.flAvatarContainer.setOnClickListener {
            showImageSourceDialog(PhotoTarget.AVATAR)
        }

        binding.btnUploadLicense.setOnClickListener {
            showImageSourceDialog(PhotoTarget.LICENSE)
        }

        binding.btnUploadPassport.setOnClickListener {
            showImageSourceDialog(PhotoTarget.PASSPORT)
        }
    }

    private fun showImageSourceDialog(target: PhotoTarget) {
        currentTarget = target
        val options = arrayOf("Сделать снимок с камеры", "Выбрать из галереи")
        AlertDialog.Builder(this)
            .setTitle("Загрузка фото")
            .setItems(options) { _, which ->
                if (which == 0) {
                    launchCamera()
                } else {
                    pickFromGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            }
            .show()
    }

    private fun launchCamera() {
        val tempFile = File(cacheDir, "camera_${System.currentTimeMillis()}.jpg")
        val uri = FileProvider.getUriForFile(this, "${applicationContext.packageName}.fileprovider", tempFile)
        tempCameraUri = uri
        takePhoto.launch(uri)
    }

    private fun handlePhotoResult(uri: Uri) {
        when (currentTarget) {
            PhotoTarget.AVATAR -> {
                avatarUri = uri
                binding.ivAvatar.setImageURI(uri)
            }
            PhotoTarget.LICENSE -> {
                licensePhotoUri = uri
                binding.btnUploadLicense.text = "✓ Фото ВУ загружено"
            }
            PhotoTarget.PASSPORT -> {
                passportPhotoUri = uri
                binding.btnUploadPassport.text = "✓ Фото паспорта загружено"
            }
            null -> {}
        }
        checkFormFilled()
    }

    private fun setupValidationListeners() {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                checkFormFilled()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.etDriverLicense.addTextChangedListener(watcher)
        binding.etIssueDate.addTextChangedListener(watcher)
    }

    private fun checkFormFilled() {
        val licenseNumber = binding.etDriverLicense.text?.toString()?.trim().orEmpty()
        val issueDate = binding.etIssueDate.text?.toString()?.trim().orEmpty()

        val isFilled = licenseNumber.length == 10 &&
                issueDate.isNotEmpty() &&
                licensePhotoUri != null &&
                passportPhotoUri != null

        binding.btnNext.isEnabled = isFilled
    }

    private fun validateAndCompleteRegistration() {
        val licenseNumber = binding.etDriverLicense.text?.toString()?.trim().orEmpty()
        val issueDate = binding.etIssueDate.text?.toString()?.trim().orEmpty()

        if (licenseNumber.length != 10) {
            binding.tilDriverLicense.error = "Номер ВУ должен состоять ровно из 10 цифр"
            Toast.makeText(this, "Пожалуйста, заполните все обязательные поля.", Toast.LENGTH_SHORT).show()
            return
        }

        val dateRegex = Regex("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/[0-9]{4}$")
        if (!dateRegex.matches(issueDate)) {
            binding.tilIssueDate.error = "Введите корректную дату выдачи."
            Toast.makeText(this, "Введите корректную дату выдачи.", Toast.LENGTH_SHORT).show()
            return
        }

        if (licensePhotoUri == null || passportPhotoUri == null) {
            Toast.makeText(this, "Пожалуйста, загрузите все необходимые фото.", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, RegisterSuccessActivity::class.java)
        startActivity(intent)
        finish()
    }
}