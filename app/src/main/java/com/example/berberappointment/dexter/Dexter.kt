import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.util.UUID

class Dexter(private val activity: AppCompatActivity, private val imageView: CircleImageView) {
    private val storage = FirebaseStorage.getInstance()
    private val storageReference: StorageReference = storage.reference.child("shopImage")

    init {
        requestPermissions()
    }

    private fun requestPermissions() {
        val permissionLauncher =
            activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    setClickListener()
                }
            }

        when (PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                setClickListener()
            }

            else -> {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun setClickListener() {
        imageView.setOnClickListener {
            pickImage.launch("image/*")
        }
    }

    val pickImage =
        activity.registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { selectedImage ->
                imageView.setImageURI(selectedImage)
                uploadImageToFirebaseStorage(selectedImage)
            }
        }

    fun uploadImageToFirebaseStorage(imageUri: Uri) {
        val imageName = UUID.randomUUID().toString()
        val setImage = storageReference.child("$imageName.jpg")

        setImage.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                setImage.downloadUrl.addOnSuccessListener { downloadUri ->
                    downloadUri.toString()

                }
            }
            .addOnFailureListener { exception ->
                exception.message
            }
    }
}
