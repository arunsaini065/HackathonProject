package com.serhat.autosub

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.VideoOnly
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaItem.SubtitleConfiguration
import androidx.media3.common.MimeTypes
import androidx.media3.exoplayer.ExoPlayer
import com.serhat.autosub.databinding.ActivityTranslatorBinding
import java.io.File
import java.util.Objects

class TranslatorActivity : AppCompatActivity() {

    private  var binding: ActivityTranslatorBinding? = null
    private var currentVideoUri: Uri? = null
    private var currentSubtitleUri: Uri? = null
    private var languageCode: String? = null
    private var player: ExoPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTranslatorBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setListeners()
    }


    private val openFileLauncher = registerForActivityResult<Intent?, ActivityResult?>(
        StartActivityForResult() as ActivityResultContract<Intent?, ActivityResult?>
    ) { result: ActivityResult? ->
        if (result!!.resultCode == RESULT_OK) {
            val filename =
                Objects.requireNonNull<Intent?>(result.data).getStringExtra("srt_file")

            binding!!.toolbar.setSubtitle(filename)

            Log.d("auto_sub_tag", ": " + filename)
            val subtitleUri = Uri.fromFile(File(filename))

            // 🔹 Subtitle configuration
            val subtitle = SubtitleConfiguration.Builder(subtitleUri)
                .setMimeType(MimeTypes.APPLICATION_SUBRIP) // For .srt
                .setLanguage("hi") // optional
                .setSelectionFlags(C.SELECTION_FLAG_DEFAULT)
                .build()

            // 🔹 Combine video + subtitle
            val mediaItem = MediaItem.Builder()
                .setUri(currentVideoUri)
                .setSubtitleConfigurations(mutableListOf<SubtitleConfiguration?>(subtitle) as List<SubtitleConfiguration>)
                .build()

            /* ArrayList<MediaItem.SubtitleConfiguration> arrayList = new ArrayList<>();
                arrayList.add(subtitle);*/
            player = ExoPlayer.Builder(this).build()
            val playerView = binding!!.playerView2
            playerView.setPlayer(player)
            binding!!.playerView2.setVisibility(View.VISIBLE)
            binding!!.selectLanguage.setVisibility(View.GONE)
            binding!!.editSubtitleBT.setVisibility(View.GONE)
            player?.setMediaItem(mediaItem)
            player?.prepare()
            player?.play()
        } else {
            Log.d("@Arun", "User canceled or closed viewer")
        }
    }

    var resultLauncher: ResultLauncher = object : ResultLauncher(null, this) {
        public override fun onLauncherResult(result: Intent) {
            try {
                languageCode = result.getStringExtra("selected_lang_code")
            } catch (ignored: Exception) {
            }
        }
    }

    var pickMedia: ActivityResultLauncher<PickVisualMediaRequest?> =
        registerForActivityResult<PickVisualMediaRequest?, Uri?>(
            PickVisualMedia() as ActivityResultContract<PickVisualMediaRequest?, Uri?>,
            ActivityResultCallback { uri: Uri? ->
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: " + uri)
                    binding!!.selectVideoBT.setVisibility(View.GONE)
                    currentVideoUri = uri
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            })

    private fun selectVideo() {
        pickMedia.launch(
            PickVisualMediaRequest.Builder()
                .setMediaType(VideoOnly)
                .build()
        )
    }

    var pickSubtitle: ActivityResultLauncher<Array<String?>?> =
        registerForActivityResult<Array<String?>?, Uri?>(
            OpenDocument() as ActivityResultContract<Array<String?>?, Uri?>,
            ActivityResultCallback { uri: Uri? ->
                if (uri != null) {
                    Log.d("SubtitlePicker", "Selected SRT URI: " + uri)
                    getContentResolver().takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    currentSubtitleUri = uri
                } else {
                    Log.d("SubtitlePicker", "No subtitle selected")
                }
            })


    private fun setListeners() {
        binding?.translateSubtitleBT?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if (currentVideoUri == null) {
                    Toast.makeText(this@TranslatorActivity, "select video first", Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                val autoSubtitleGenerator: String? = binding?.editSubtitleBT?.getText().toString()

                if (TextUtils.isEmpty(languageCode)) {
                    Toast.makeText(this@TranslatorActivity, "No Language selected", Toast.LENGTH_SHORT)
                        .show()
                    return
                }

                if (TextUtils.isEmpty(autoSubtitleGenerator)) {
                    Toast.makeText(this@TranslatorActivity, "No enter title", Toast.LENGTH_SHORT).show()

                    return
                }

                if (currentSubtitleUri != null) {
                    val intent = Intent("TranslationBridgeActivity")
                    intent.setData(currentSubtitleUri)
                    intent.putExtra("title", autoSubtitleGenerator)
                    intent.putExtra("lan_code", languageCode)
                    openFileLauncher.launch(intent)
                } else {
                    Toast.makeText(this@TranslatorActivity, "No subtitle selected", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

        binding?.selectLanguage?.setOnClickListener {
            val intent = Intent(this@TranslatorActivity, LanguagePickerActivity::class.java)
            intent.putExtra("from_auto", true)
            resultLauncher.launchByLauncher(intent)
        }

        binding?.selectVideoBT?.setOnClickListener {
            selectVideo()
        }

        binding?.selectSubtitleBT?.setOnClickListener {
            pickSubtitle.launch(arrayOf<String>("application/x-subrip", "text/plain") as Array<String?>?)
        }

    }


}
