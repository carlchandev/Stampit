package carl.rs.logo

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_edit_image.*


class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_image)

        val bundle = intent.extras
        val imageUri = bundle.getString("imageUri")

        photoView.setImageURI(Uri.parse(imageUri))
    }
}