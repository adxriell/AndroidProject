package com.example.imageproject

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.imageproject.databinding.ActivityUpdateBookBinding
import java.io.ByteArrayOutputStream
import java.util.Calendar

class updateBook : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBookBinding
    private lateinit var db: BookDatabaseHelper
    private var bookId: Int = -1
    private var photo: Bitmap? = null
    private val GALLERY_REQUEST_CODE = 200
    private val CAMERA_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = BookDatabaseHelper(this)

        bookId = intent.getIntExtra("book_id", -1)
        if (bookId == -1) {
            finish()
            return
        }

        // Load existing book data
        val book = db.getBookByID(bookId)
        binding.updatenama.setText(book.nama)
        binding.updatenamapang.setText(book.namapang)
        binding.updateemail.setText(book.email)
        binding.updatealamat.setText(book.alamat)
        binding.updatetglLahir.setText(book.tglLahir)
        binding.updatetelp.setText(book.telp)

        // Set photo or default image
        val bitmap = if (book.photo != null) {
            BitmapFactory.decodeByteArray(book.photo, 0, book.photo.size)
        } else {
            BitmapFactory.decodeResource(resources, R.drawable.baseline_image_search_24)
        }
        binding.editphoto.setImageBitmap(bitmap)

        // Set photo selection listener
        binding.editphoto.setOnClickListener {
            selectPhoto()
        }

        // Date picker
        binding.updatetglLahir.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    binding.updatetglLahir.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        binding.updatesavebutton.setOnClickListener {
            saveUpdatedData()
        }
    }

    private fun selectPhoto() {
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Photo")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePicture, CAMERA_REQUEST_CODE)
    }

    private fun openGallery() {
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhoto, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val selectedPhoto = data?.extras?.get("data") as Bitmap
                    binding.editphoto.setImageBitmap(selectedPhoto)
                    photo = selectedPhoto
                }
                GALLERY_REQUEST_CODE -> {
                    val selectedImageUri: Uri? = data?.data
                    selectedImageUri?.let {
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                        binding.editphoto.setImageBitmap(bitmap)
                        photo = bitmap
                    }
                }
            }
        }
    }

    private fun saveUpdatedData() {
        val newnama = binding.updatenama.text.toString()
        val newnamapang = binding.updatenamapang.text.toString()
        val newemail = binding.updateemail.text.toString()
        val newalamat = binding.updatealamat.text.toString()
        val newtglLahir = binding.updatetglLahir.text.toString()
        val newtelp = binding.updatetelp.text.toString()

        val newphoto = photo?.let { bitmapToByteArray(it) }

        val updateBook = Book(bookId, newnama, newnamapang, newemail, newalamat, newtglLahir, newtelp, newphoto)
        db.updateBook(updateBook)
        finish()
        Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        return stream.toByteArray()
    }
}
