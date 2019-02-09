package com.example.developer.calvin;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.developer.calvin.Fragments.ProfitFragment;

import java.io.FileOutputStream;
import java.io.IOException;

public class CaculatorActivity extends AppCompatActivity {
    EditText edCommodityName, edBuyingPrice,edProfit,edSellingPrice;
    Button bn;
    ProgressDialog pd;
    Snackbar snackbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caculator);

        edCommodityName = findViewById(R.id.edCommodityName);
        edBuyingPrice = findViewById(R.id.edBuyingPrice);
        edProfit = findViewById(R.id.edProfit);
        edSellingPrice = findViewById(R.id.edSellingPrice);
        bn = findViewById(R.id.bn);
        pd = new ProgressDialog(getApplicationContext());

        bn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                printPDF(view);
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        };
        edBuyingPrice.addTextChangedListener(textWatcher);
        edProfit.addTextChangedListener(textWatcher);
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void printPDF(View view) {
        PrintManager printManager = (PrintManager) getSystemService(PRINT_SERVICE);
        printManager.print("Report", new ViewPrintAdapter(getApplicationContext(), findViewById(R.id.rv)), null);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public class ViewPrintAdapter extends PrintDocumentAdapter {

        private PrintedPdfDocument mDocument;
        private Context mContext;
        private View mView;

        public ViewPrintAdapter(Context context, View view) {
            mContext = context;
            mView = view;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes,
                             CancellationSignal cancellationSignal,
                             LayoutResultCallback callback, Bundle extras) {

            mDocument = new PrintedPdfDocument(mContext, newAttributes);

            if (cancellationSignal.isCanceled()) {
                callback.onLayoutCancelled();
                return;
            }

            PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                    .Builder("print_output.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(1);

            PrintDocumentInfo info = builder.build();
            callback.onLayoutFinished(info, true);
        }

        @Override
        public void onWrite(PageRange[] pages, ParcelFileDescriptor destination,
                            CancellationSignal cancellationSignal,
                            WriteResultCallback callback) {


            PdfDocument.Page page = mDocument.startPage(0);

            Bitmap bitmap = Bitmap.createBitmap(mView.getWidth(), mView.getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            mView.draw(canvas);

            Rect src = new Rect(0, 0, mView.getWidth(), mView.getHeight());
            // get the page canvas and measure it.
            Canvas pageCanvas = page.getCanvas();
            float pageWidth = pageCanvas.getWidth();
            float pageHeight = pageCanvas.getHeight();

            float scale = Math.min(pageWidth/src.width(), pageHeight/src.height());
            float left = pageWidth / 2 - src.width() * scale / 2;
            float top = pageHeight / 2 - src.height() * scale / 2;
            float right = pageWidth / 2 + src.width() * scale / 2;
            float bottom = pageHeight / 2 + src.height() * scale / 2;
            RectF dst = new RectF(left, top, right, bottom);

            pageCanvas.drawBitmap(bitmap, src, dst, null);
            mDocument.finishPage(page);

            try {
                mDocument.writeTo(new FileOutputStream(
                        destination.getFileDescriptor()));
            } catch (IOException e) {
                callback.onWriteFailed(e.toString());
                return;
            } finally {
                mDocument.close();
                mDocument = null;
            }
            callback.onWriteFinished(new PageRange[]{new PageRange(0, 0)});
        }
    }

    private void calculate() {
        double buyingprice = 0;
        double profit = 0.0;
        double sellingprice;

        if (edBuyingPrice != null)
            buyingprice = Double.parseDouble(!edBuyingPrice.getText().toString().equals("")? edBuyingPrice.getText().toString():"0");
        if (edProfit != null)
            profit = Double.parseDouble(!edProfit.getText().toString().equals("")? edProfit.getText().toString():"0");
        sellingprice = ((100 + profit) * buyingprice)/100;
        String textResult = String.valueOf(sellingprice);
        edSellingPrice.setText(textResult+"Ksh");
    }

    public void showSnackbar(String stringSnackbar){
        snackbar.make(CaculatorActivity.this.findViewById(android.R.id.content), stringSnackbar.toString(), Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                .show();
    }

}
