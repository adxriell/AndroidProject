package com.example.imageproject

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class BookAdapter(private var books: List<Book>, private val context: Context) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private val db: BookDatabaseHelper = BookDatabaseHelper(context)

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namatext: TextView = itemView.findViewById(R.id.nama)
        val namapangtext: TextView = itemView.findViewById(R.id.namapang)
        val emailtext: TextView = itemView.findViewById(R.id.email)
        val alamattext: TextView = itemView.findViewById(R.id.alamat)
        val tglLahirtext: TextView = itemView.findViewById(R.id.tglLahir)
        val telptext: TextView = itemView.findViewById(R.id.telp)
        val photo: ImageView = itemView.findViewById(R.id.imageView3)
        val editButton: ImageView = itemView.findViewById(R.id.editbutton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deletebutton)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_book_item, parent, false)
        return BookViewHolder(view)
    }

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]

        holder.namatext.text = book.nama
        holder.namapangtext.text = book.namapang
        holder.emailtext.text = book.email
        holder.alamattext.text = book.alamat
        holder.tglLahirtext.text = book.tglLahir
        holder.telptext.text = book.telp

        book.photo?.let { photoByteArray ->
            if (photoByteArray.isNotEmpty()) {
                // Define BitmapFactory options to compress the image
                val options = BitmapFactory.Options().apply {
                    inSampleSize = 2 // Adjust this value for more compression (e.g., 2 means 1/2 of the original size)
                }
                val bitmap = BitmapFactory.decodeByteArray(photoByteArray, 0, photoByteArray.size, options)
                holder.photo.setImageBitmap(bitmap)
            } else {
                holder.photo.setImageResource(R.drawable.baseline_orang)
            }
        } ?: run {
            holder.photo.setImageResource(R.drawable.baseline_orang)
        }

        val colors = listOf(
            ContextCompat.getColor(context, R.color.ungu),
            ContextCompat.getColor(context, R.color.ungumuda),
            ContextCompat.getColor(context, R.color.pink),
            ContextCompat.getColor(context, R.color.kuning)
        )

        val randomColor = colors[Random.nextInt(colors.size)]
        holder.cardView.setCardBackgroundColor(randomColor)

        holder.editButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, updateBook::class.java).apply {
                putExtra("book_id", book.id)
            }
            Log.d("BookAdapter", "Starting updateBook Activity with book_id: ${book.id}")
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener { view ->
            val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        db.deleteBook(book.id)
                        refreshData(db.getAllNotes())
                        Toast.makeText(
                            holder.itemView.context,
                            "Book Deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    DialogInterface.BUTTON_NEGATIVE -> dialog.dismiss()
                }
            }
            val builder: AlertDialog.Builder = AlertDialog.Builder(holder.itemView.context)
            builder.setMessage("Apakah anda yakin akan menghapusnya?")
                .setPositiveButton("Ya", dialogClickListener)
                .setNegativeButton("Tidak", dialogClickListener)
                .show()
        }
    }

    fun refreshData(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }
}
