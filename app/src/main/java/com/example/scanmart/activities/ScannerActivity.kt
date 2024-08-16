package com.example.scanmart.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.scanmart.R
import com.example.scanmart.domains.ItemDomain
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScannerActivity : AppCompatActivity() {

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var barcodeScanner: BarcodeScanner
    private lateinit var previewView: PreviewView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        previewView = findViewById(R.id.previewView)
        cameraExecutor = Executors.newSingleThreadExecutor()
        barcodeScanner = BarcodeScanning.getClient()
        if (allPermissionsGranted()) {
            startClient()
        } else {
            requestPermissions()
        }

    }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_PERMISSIONS
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startClient()
            } else {
                // Permission denied, show a message to the user
            }
        }
    }

    private fun startClient() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = androidx.camera.core.Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            val imageCapture = ImageCapture.Builder().build()

            val imageAnalyzer = ImageAnalysis.Builder().build().also {
                it.setAnalyzer(cameraExecutor) { imageProxy ->
                    processImageProxy(imageProxy)
                }
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(this))

    }

    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            barcodeScanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        handleBarcode(barcode)
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }

    private fun handleBarcode(barcode: Barcode) {
        if (isProcessingBarcode) return

        isProcessingBarcode = true

        // Open details page after a QR code is scanned
        openDetailsActivity()

        // Reset flag to allow future scans
        isProcessingBarcode = false
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun openDetailsActivity() {

        Log.d("ScannerActivity", "Opening DetailsActivity")
        // Hardcoded item data
        val item = ItemDomain(
            Id = 1,
            Title = "Lay's",
            Description = "India's Magic Masala",
            ImagePath = "https://firebasestorage.googleapis.com/v0/b/scan-mart.appspot.com/o/lays_no_bg.png?alt=media&token=531428b3-c47e-4358-8679-05b66cb4cbd7", // Replace with captured image's link
            CategoryId = 1,
            Price = 20.0,
            Stars = 3.5
        )

        // Create an Intent to start DetailsActivity
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("item", item)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private var isProcessingBarcode = false
    }
}