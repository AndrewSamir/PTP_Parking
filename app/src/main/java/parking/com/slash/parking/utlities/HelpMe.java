package parking.com.slash.parking.utlities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.ConnectException;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;


import parking.com.slash.parking.R;

import static parking.com.slash.parking.utlities.Constant.baseUrl;


/**
 * Created by lenovo on 1/26/2016.
 */
public class HelpMe
{
    // static Uri.Builder builder;

    private static Context context;
    private static HelpMe instance = null;
    // public final String Socket_SERVER_URL = "http://192.168.1.110:3000/";

    //   public String Socket_SERVER_URL = baseSocketUrl;
    private Gson gson;
//  public   Socket mSocket;

    public static HelpMe getInstance(Context context)
    {


        HelpMe.context = context;

        if (instance == null)
        {
            instance = new HelpMe();
        }
        return instance;
    }

    public boolean isTablet()
    {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public Object parseJsonToObject(String response, Class<?> modelClass)
    {
        if (gson == null)
        {
            gson = new GsonBuilder().serializeNulls().create();
        }

        try
        {
            return gson.fromJson(response, modelClass);
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Serializes the specified object into its equivalent Json representation.
     *
     * @param object The object which Json will represent.
     * @return Json representation of src.
     */
    public String parseObjectToJson(Object object)
    {
        if (gson == null)
        {
            gson = new GsonBuilder().serializeNulls().create();
        }
        return gson.toJson(object);
    }


    public boolean isAppInstalled(String packageName)
    {
        try
        {
            //boolean whatsappFound = AndroidHelper.isAppInstalled(context, "com.whatsapp");
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }

    public Bitmap createDrawableFromView(View view)
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels,
                displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }


    public void initLang(String lang)
    {

        //  String lang = SharedPrefUtil.getInstance(getApplicationContext()).read("settingLangName", "en");
        //  String lang = "ar";
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }


    public void hideKeyBoard(Activity act)
    {
        act.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void spliteForLog(String veryLongString)
    {
        int maxLogStringSize = 1000;
        for (int i = 0; i <= veryLongString.length() / maxLogStringSize; i++)
        {
            int start = i * maxLogStringSize;
            int end = (i + 1) * maxLogStringSize;
            end = end > veryLongString.length() ? veryLongString.length() : end;
            Log.v("spletres", veryLongString.substring(start, end));
        }
    }

    //======================================================//
    public String md5(String s)
    {
        try
        {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return "";
    }

    public void hidekeyboard(Activity act)
    {
        act.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
    }


    public void importDB()
    {

        try
        {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite())
            {
                String currentDBPath = "/data/" + "ksi.com.ysf.ysf" + "/databases/" + "ysf.db";
                String backupDBPath = Environment.getExternalStorageDirectory() + "/ysf.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists())
                {
                    FileChannel src = new FileInputStream(backupDB).getChannel();
                    FileChannel dst = new FileOutputStream(currentDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Toast.makeText(context, "Database Restored successfully", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }


    }


    public String getVestionCode(Context c)
    {
        /*
        p40sdmkkmgjb1ggyadqz
        e4bbe5b7a4c1eb55652965aee885dd59bd2ee7f4
         */
        String v = "";
        try
        {

            v += c.getPackageManager()
                    .getPackageInfo(c.getPackageName(), 0).versionName;

        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        // Log.e("log",v);
        return v;

    }

    public Bitmap getCroppedBitmap(Bitmap bmp, int radius)
    {
        Bitmap sbmp;

        if (bmp.getWidth() != radius || bmp.getHeight() != radius)
        {
            float smallest = Math.min(bmp.getWidth(), bmp.getHeight());
            float factor = smallest / radius;
            sbmp = Bitmap.createScaledBitmap(bmp,
                    (int) (bmp.getWidth() / factor),
                    (int) (bmp.getHeight() / factor), false);
        } else
        {
            sbmp = bmp;
        }

        Bitmap output = Bitmap.createBitmap(radius, radius, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final String color = "#BAB399";
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, radius, radius);

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor(color));
        canvas.drawCircle(radius / 2 + 0.7f, radius / 2 + 0.7f,
                radius / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }


    public Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth)
    {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }


    public Bitmap getCroppedBitmap(Bitmap srcBitmap)
    {


        int squareBitmapWidth = Math.min(srcBitmap.getWidth(), srcBitmap.getHeight());

        // Initialize a new instance of Bitmap
        Bitmap dstBitmap = Bitmap.createBitmap(
                squareBitmapWidth, // Width
                squareBitmapWidth, // Height
                Bitmap.Config.ARGB_8888 // Config
        );

        /*
            Canvas
                The Canvas class holds the "draw" calls. To draw something, you need 4 basic
                components: A Bitmap to hold the pixels, a Canvas to host the draw calls (writing
                into the bitmap), a drawing primitive (e.g. Rect, Path, text, Bitmap), and a paint
                (to describe the colors and styles for the drawing).
        */
        // Initialize a new Canvas to draw circular bitmap
        Canvas canvas = new Canvas(dstBitmap);

        // Initialize a new Paint instance
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        /*
            Rect
                Rect holds four integer coordinates for a rectangle. The rectangle is represented by
                the coordinates of its 4 edges (left, top, right bottom). These fields can be accessed
                directly. Use width() and height() to retrieve the rectangle's width and height.
                Note: most methods do not check to see that the coordinates are sorted correctly
                (i.e. left <= right and top <= bottom).
        */
        /*
            Rect(int left, int top, int right, int bottom)
                Create a new rectangle with the specified coordinates.
        */
        // Initialize a new Rect instance
        Rect rect = new Rect(0, 0, squareBitmapWidth, squareBitmapWidth);

        /*
            RectF
                RectF holds four float coordinates for a rectangle. The rectangle is represented by
                the coordinates of its 4 edges (left, top, right bottom). These fields can be
                accessed directly. Use width() and height() to retrieve the rectangle's width and
                height. Note: most methods do not check to see that the coordinates are sorted
                correctly (i.e. left <= right and top <= bottom).
        */
        // Initialize a new RectF instance
        RectF rectF = new RectF(rect);

        /*
            public void drawOval (RectF oval, Paint paint)
                Draw the specified oval using the specified paint. The oval will be filled or
                framed based on the Style in the paint.

            Parameters
                oval : The rectangle bounds of the oval to be drawn

        */
        // Draw an oval shape on Canvas
        canvas.drawOval(rectF, paint);

        /*
            public Xfermode setXfermode (Xfermode xfermode)
                Set or clear the xfermode object.
                Pass null to clear any previous xfermode. As a convenience, the parameter passed
                is also returned.

            Parameters
                xfermode : May be null. The xfermode to be installed in the paint
            Returns
                xfermode
        */
        /*
            public PorterDuffXfermode (PorterDuff.Mode mode)
                Create an xfermode that uses the specified porter-duff mode.

            Parameters
                mode : The porter-duff mode that is applied

        */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // Calculate the left and top of copied bitmap
        float left = (squareBitmapWidth - srcBitmap.getWidth()) / 2;
        float top = (squareBitmapWidth - srcBitmap.getHeight()) / 2;

        /*
            public void drawBitmap (Bitmap bitmap, float left, float top, Paint paint)
                Draw the specified bitmap, with its top/left corner at (x,y), using the specified
                paint, transformed by the current matrix.

                Note: if the paint contains a maskfilter that generates a mask which extends beyond
                the bitmap's original width/height (e.g. BlurMaskFilter), then the bitmap will be
                drawn as if it were in a Shader with CLAMP mode. Thus the color outside of the

                original width/height will be the edge color replicated.

                If the bitmap and canvas have different densities, this function will take care of
                automatically scaling the bitmap to draw at the same density as the canvas.

            Parameters
                bitmap : The bitmap to be drawn
                left : The position of the left side of the bitmap being drawn
                top : The position of the top side of the bitmap being drawn
                paint : The paint used to draw the bitmap (may be null)
        */
        // Make a rounded image by copying at the exact center position of source image
        canvas.drawBitmap(srcBitmap, 0, 0, paint);

        // Free the native object associated with this bitmap.
        srcBitmap.recycle();

        // Return the circular bitmap
        dstBitmap = Bitmap.createScaledBitmap(dstBitmap, 150, 150, false);

        return dstBitmap;

    }

    public Bitmap overlay(Bitmap bmp1, Bitmap bmp2)
    {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, new Matrix(), null);
        return bmOverlay;
    }


    public void exportDB(Context cnt)
    {
        try
        {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite())
            {


                String currentDBPath = "/data/" + "ksi.com.ysf.ysf" + "/databases/" + "ysf.db";
                String backupDBPath = Environment.getExternalStorageDirectory() + "/database_copy.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(cnt, "Backup Successful!",
                        Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(cnt, "Backup Failed!", Toast.LENGTH_SHORT)
                    .show();

        }
    }


    public void retrofironFailure(Throwable t)
    {

        if (t instanceof ConnectException)
        {
            //TODO call
//            TastyToast.makeText(context, context.getString(R.string.ntproblem), TastyToast.LENGTH_LONG, TastyToast.ERROR);
        } else
        {
            //TODO call

//            TastyToast.makeText(context, t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }
    }

    public void retrofirOnNotTwoHundred(int x)
    {
        if (x == 204)
        {
            //TODO call

//            TastyToast.makeText(context, context.getString(R.string.no_content), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
        } else if (x == 400)
        {
            //TODO call

//            TastyToast.makeText(context, context.getString(R.string.no_data), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
        }


    }

    public void initPicasso(String path, ImageView v)
    {
        String url = baseUrl + path;
        // String url= "http://192.168.1.126/vkader_main/vkader_frontend/public/uploads/users/09808924-6c27-4abd-b63e-980a69e764f4/orig/8b2dd443-80f9-47f6-9577-fe716fc5fffb-20170618105013.jpeg";
        Log.e("urlimage", url);
        Picasso.with(context)
                .load(url)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(v);
    }


    public void initPicassoForDialog(String path, ImageView v)
    {

        String url = path;

        if (url.length() == 0)
        {
            url = "empty";
        }
        // String url= "http://192.168.1.126/vkader_main/vkader_frontend/public/uploads/users/09808924-6c27-4abd-b63e-980a69e764f4/orig/8b2dd443-80f9-47f6-9577-fe716fc5fffb-20170618105013.jpeg";
        Log.e("urlimage", url);
        Picasso.with(context)
                .load(url)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(v);
    }

}
