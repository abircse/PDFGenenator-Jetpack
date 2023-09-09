package com.keepme.i7pdfmakerjetpackcompose

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.BarcodeQRCode
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.keepme.i7pdfmakerjetpackcompose.ui.theme.I7PDFMakerJetpackComposeTheme
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            I7PDFMakerJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GenerateInvoice()
                }
            }
        }
    }
}

@Composable
fun GenerateInvoice() {

    val context = LocalContext.current

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(onClick = {
            Log.d("Clicked", "Success")
            generateInvoice(context)
        }) {
            Text(text = "Generate PDF")
        }
    }
}

fun generateInvoice(context: Context) {
    try {


        /** Generate some dummy product **/
        val productList: ArrayList<ProductModel> = arrayListOf()
        productList.add(ProductModel("Macbook Air 2023", 2, 120000, 240000))
        productList.add(ProductModel("Macbook Air 2023", 2, 120000, 240000))
        productList.add(ProductModel("Macbook Air 2023", 2, 120000, 240000))
        productList.add(ProductModel("Macbook Air 2023", 2, 120000, 240000))
        productList.add(ProductModel("Macbook Air 2023", 2, 120000, 240000))



        /** Create Folder directory to store pdf **/
        val folderName = "/myInvoice"
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + folderName
        val dir = File(path)
        if (!dir.exists()) {
            dir.mkdirs()
            Toast.makeText(context, "Directory Created For Store Invoice", Toast.LENGTH_SHORT).show()
        }

        /** Create pdf file name **/
        val file = File(dir, "myinvoice.pdf")
        val fileOutputStream = FileOutputStream(file)

        /** Prepare document with size **/
        val document = Document()
        document.pageSize = PageSize.A5
        val pdfWriter =  PdfWriter.getInstance(document, fileOutputStream)
        document.open()


        /** Create Shop information **/
        val shopName = Paragraph(
            "MC Solutions BD \n",
            FontFactory.getFont("arial", 20f, Font.BOLD, BaseColor.BLACK)
        )
        shopName.alignment = Element.ALIGN_CENTER

        val address = Paragraph(
            "Address: Bali Mall, Chittagong Branch, Main Road\n",
            FontFactory.getFont("arial", 10f, Font.NORMAL, BaseColor.BLACK)
        )
        address.alignment = Element.ALIGN_CENTER

        val mobile = Paragraph(
            "Phone: +8801682148802\n",
            FontFactory.getFont("arial", 10f, Font.NORMAL, BaseColor.BLACK)
        )
        mobile.alignment = Element.ALIGN_CENTER

        val invoiceTitle = Paragraph(
            "Invoice\n\n",
            FontFactory.getFont("arial", 16f, Font.BOLD, BaseColor.BLACK),
        )
        invoiceTitle.alignment = Element.ALIGN_CENTER


        /** Create Table Column **/
        val table = PdfPTable(4)
        table.addCell("Product Name")
        table.addCell("Qtn")
        table.addCell("Price")
        table.addCell("Total")

        for (i in productList.indices){
            table.addCell(Paragraph(productList[i].name, FontFactory.getFont("arial", 12f, Font.NORMAL, BaseColor.BLACK)))
            table.addCell(Paragraph(productList[i].quantity.toString(), FontFactory.getFont("arial", 12f, Font.NORMAL, BaseColor.BLACK)))
            table.addCell(Paragraph(productList[i].singlePrice.toString(), FontFactory.getFont("arial", 12f, Font.NORMAL, BaseColor.BLACK)))
            table.addCell(Paragraph(productList[i].total.toString(), FontFactory.getFont("arial", 12f, Font.NORMAL, BaseColor.BLACK)))
        }


        val thanksMessage = Paragraph(
            "Thank you for Shopping!\n\n",
            FontFactory.getFont("arial", 10f, Font.NORMAL, BaseColor.BLACK),
        )
        thanksMessage.alignment = Element.ALIGN_CENTER

        /**
        val pdfContentByte = pdfWriter.directContent
        val barcodeEAN = BarcodeEAN()
        barcodeEAN.codeType = BarcodeEAN.EAN13
        barcodeEAN.code = "1234523453323"
        val codeEANImage: Image = barcodeEAN.createImageWithBarcode(pdfContentByte, BaseColor.BLACK, BaseColor.BLACK)
        codeEANImage.setAbsolutePosition(20F, 300F)
        codeEANImage.scalePercent(100F)
        codeEANImage.alignment = Element.ALIGN_CENTER
        **/

        var barcodeQrcode: BarcodeQRCode? =
            BarcodeQRCode("examples.javacodegeeks.com/author/chandan-singh", 1, 1, null)
        val qrcodeImage = barcodeQrcode!!.image
        qrcodeImage.setAbsolutePosition(20f, 500f)
        qrcodeImage.scalePercent(50f)

        document.add(shopName)
        document.add(address)
        document.add(mobile)
        document.add(invoiceTitle)
        document.add(table)
        document.add(thanksMessage)
       // document.add(codeEANImage)
        document.add(qrcodeImage)

        document.close()


    }
    catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: DocumentException) {
        e.printStackTrace()
    }
}
