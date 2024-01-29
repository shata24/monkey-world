package com.example.monkey_classification.presentation.screens

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.monkey_classification.R
import com.example.monkey_classification.ml.Model
import com.example.monkey_classification.ui.theme.primaryColor
import com.google.firebase.auth.FirebaseAuth
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

@Composable
fun ImageClassificationScreen(
    navController: NavController,
    auth: FirebaseAuth
) {
    val context = LocalContext.current
    val classes = listOf(
        "Bald Ukari",
        "Emperor Tamarin",
        "Golden Monkey",
        "Gray Langur",
        "Hamadryas Baboon",
        "Mandril",
        "Proboscis Monkey",
        "Red Howler",
        "Vervet Monkey",
        "White Faced Saki"
    )
    val result = remember { mutableStateOf("") }
    val resultIndex = remember { mutableStateOf<Int?>(null) }
    val imageBitmap = remember { mutableStateOf<Bitmap?>(null) }
    val takePhotoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            imageBitmap.value = bitmap
            result.value = ""
        }
    }

    val pickFromGalleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val contentResolver: ContentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(it)
            val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
            imageBitmap.value = bitmap
            result.value = ""
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                title = "Image"
            )
        },
        topBar = {
            AppBar(navController = navController, title = "Monkey Image Classification")
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {

                Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = primaryColor,
                    contentColor = Color.White
                ),
                    onClick = { takePhotoLauncher.launch(null) }) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Camera")
                }

                Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = primaryColor,
                    contentColor = Color.White
                ),
                    onClick = { pickFromGalleryLauncher.launch("image/*") }) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Pick from Gallery")
                }

                if (imageBitmap.value != null) {
                    Image(
                        bitmap = imageBitmap.value!!.asImageBitmap(),
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .height(500.dp)
                            .fillMaxWidth()
                            .padding(16.dp),
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.placeholder),
                        contentDescription = "Placeholder Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    )
                }

                Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = primaryColor,
                    contentColor = Color.White
                ),
                    onClick = {
                        if (imageBitmap.value != null) {
                            val resizedImage = Bitmap.createScaledBitmap(imageBitmap.value!!, 224, 224, false);

                            val model = Model.newInstance(context)
                            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
                            val tensorImage = TensorImage(DataType.FLOAT32)
                            tensorImage.load(resizedImage)
                            val byteBuffer = tensorImage.buffer

                            inputFeature0.loadBuffer(byteBuffer)
                            val outputs = model.process(inputFeature0)
                            val outputFeature0: TensorBuffer = outputs.outputFeature0AsTensorBuffer
                            val ansArr = outputFeature0.floatArray
                            val maxIndex = findMaxIndex(ansArr)
                            var r = ""
                            ansArr.forEachIndexed { index, ans ->
                                r += "${classes[index]}: $ans\n"
                            }

                            result.value = r
                            resultIndex.value = maxIndex
                            model.close()
                        } else {
                            Toast.makeText(
                                context,
                                "Please select a picture or take a picture",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }) {
                    Text(text = "Predict Monkey")
                }
                if (result.value != "" && resultIndex.value != null) {
                    Text(
                        text = result.value,
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier.padding(top = 20.dp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Predicted Monkey: ${classes[resultIndex.value!!]}",
                        style = TextStyle(fontSize = 20.sp),
                        modifier = Modifier.padding(top = 24.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}

fun findMaxIndex(array: FloatArray): Int {

    var maxIndex = 0
    var maxValue = array[0]

    for (i in 1 until array.size) {
        if (array[i] > maxValue) {
            maxValue = array[i]
            maxIndex = i
        }
    }

    return maxIndex
}

