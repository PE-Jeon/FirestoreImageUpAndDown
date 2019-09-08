package com.frentree.firestore

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_gallery.*
import java.io.IOException
import java.util.*
import java.util.UUID.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import java.net.HttpURLConnection
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import android.widget.ImageView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FileDownloadTask
import com.squareup.picasso.Picasso
import io.grpc.Context
import java.lang.NullPointerException


class GalleryActivity : AppCompatActivity() {




    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var imageView: ImageView? = null
    private var spaceRef : StorageReference? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        btn_choose_image.setOnClickListener { launchGallery() }
        btn_upload_image.setOnClickListener { uploadImage() }
        btn_download_image.setOnClickListener { getImageFromURL() }

    }


    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun uploadImage(){
        if(filePath != null){
            val ref = storageReference?.child("uploads/" + System.currentTimeMillis())
            val uploadTask = ref?.putFile(filePath!!)

            val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    addUploadRecordToDb(downloadUri.toString())
                } else {
                    // Handle failures
                }
            }?.addOnFailureListener{

            }
        }else{
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addUploadRecordToDb(uri: String){
        val db = FirebaseFirestore.getInstance()

        val data = HashMap<String, Any>()
        data["imageUrl"] = uri

        db.collection("posts")
            .add(data)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Saved to DB", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving to DB", Toast.LENGTH_LONG).show()
            }
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
               
                // 해결 안됨 
                // Type 변환 문제
                // uploadImage.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            filePath = data.data

            Picasso.get().load(filePath).into(image_preview)
        }
    }

    fun getImageFromURL() {





        var test1 = FirebaseStorage.getInstance().getReference()
        var test2 = FirebaseStorage.getInstance().getReference().child("uploads/1567938332065")
        var test3 = FirebaseStorage.getInstance().getReference().child("uploads/1567938332065").downloadUrl
        var test4 = FirebaseStorage.getInstance().getReference("uploads/1567938332065")
        var test5 = FirebaseStorage.getInstance().getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/fir-image-78ab8.appspot.com/o/uplods%1567938332065")
        var test6 = FirebaseStorage.getInstance().getReference().child("uploads/1567938332065")




        Log.d("test", "=========================================================================")
        Log.d("test", " result1 " + test1.toString())

        Log.d("test", "=========================================================================")
        Log.d("test", " result2 " + test2.toString())

        Log.d("test", "=========================================================================")
        Log.d("test", " result3 " + test3.toString())

        Log.d("test", "=========================================================================")
        Log.d("test", " result4 " + test4.toString())

        Log.d("test", "=========================================================================")
        Log.d("test", " result5 " + test5.toString())


        test2.downloadUrl.addOnSuccessListener {

            var download : Uri = it

            Log.d("test", "=========================================================================")
            Log.d("test", " result5 " + download.toString())

        }.addOnFailureListener {

        }

/*
        test6.downloadUrl.addOnSuccessListener() { object : OnSuccessListener<UploadTask.TaskSnapshot> {
            override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot?) {
                val url = taskSnapshot?.
                Log.d("FirebaseManager", "Upload Successful")
                downloadCallback.callback(url.toString())
            }
        }
        }*/


        //.load(spaceRef!!.downloadUrl.toString())
        //.load("https://firebasestorage.googleapis.com/v0/b/fir-image-78ab8.appspot.com/o/uploads%2F1567944666079?alt=media&token=35843999-af43-4666-87d2-721833163550")

        spaceRef = storageReference?.child("uploads/1567938332065")




        var storageRef2 = FirebaseStorage.getInstance().getReference()
        var dateRef2 = storageRef2.child("uploads/1567938332065")

        var httpsReference = firebaseStore?.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/fir-image-78ab8.appspot.com/o/uploads%2F1567944666079")
        var downloadUrl = httpsReference?.downloadUrl

        var downloadURI : Uri
        downloadURI = Uri.parse(httpsReference.toString())


        Log.d("test", "=========================================================================")
        Log.d("test", "=========================================================================")
        Log.d("test", "=========================================================================")
        Log.d("test", "=========================================================================")
        Log.d("test", "httpsReference result ======= " + downloadUrl.toString())
        Log.d("test", "httpsReference result ======= " + downloadURI)
        Log.d("test", "httpsReference result ======= " + httpsReference.toString())

        imageView = image_download
        Log.d("test", spaceRef!!.downloadUrl.toString())
        Toast.makeText(this, spaceRef!!.downloadUrl.toString(), Toast.LENGTH_LONG).show()

        Log.d("test", spaceRef!!.getDownloadUrl().toString())

        Log.d("test", storageRef2.toString())
        Log.d("test", dateRef2.toString())

        test2.downloadUrl.addOnSuccessListener {

            var download : Uri = it

            Picasso.get()
                .load(download)
                .into(imageView)

            Log.d("test", "=========================================================================")
            Log.d("test", " result5 " + download.toString())

        }.addOnFailureListener {

        }

        /*
        Picasso.get()
            .load("test1")
            .into(imageView)

        Picasso.get()
            //.load(spaceRef!!.downloadUrl.toString())
            //.load("https://firebasestorage.googleapis.com/v0/b/fir-image-78ab8.appspot.com/o/uploads%2F1567944666079?alt=media&token=35843999-af43-4666-87d2-721833163550")
            .load(downloadURI)
            .into(imageView)
*/
    }

    fun getBitmapFromURL(src: String): Bitmap? {
        try {
            val url = java.net.URL(src)
            val connection = url
                .openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input = connection.getInputStream()
            return BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
}