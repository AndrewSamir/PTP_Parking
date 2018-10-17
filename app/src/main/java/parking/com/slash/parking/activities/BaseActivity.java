package parking.com.slash.parking.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import parking.com.slash.parking.R;
import parking.com.slash.parking.fragments.BaseFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseActivity extends RxAppCompatActivity
{
    //region variables
    public final String content_Fragment = "Content_Fragment";
    protected String TAG;
    protected View mainProgressBar;
//    protected AppHeader appHeader;
    protected BottomNavigationViewEx bottomNavigationView;
//    private static FirebaseDatabase database;
//    protected static DatabaseReference myRef;
    //endregion

    //region life cycle

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getBaseLayoutID());
        mainProgressBar = findViewById(R.id.progress);
//        appHeader = findViewById(R.id.appHeader);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
/*

        bottomNavigationView.enableAnimation(false);
        bottomNavigationView.enableShiftingMode(false);
        bottomNavigationView.enableItemShiftingMode(false);
*/

        /*database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.keepSynced(true);
*/
        TAG = getClass().getSimpleName();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    //endregion

    //region handling back pressed

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        //        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    //endregion

    //region keyboard handlers

    public void hideVirtualKeyBoard()
    {
        try
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //			if(getCurrentFocus() != null)
            //			{
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            //			}
        } catch (NullPointerException mException)
        {
//            LoggerHelper.i(this.getClass().getSimpleName(), "null pointer to hide keyboard");
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void showVirtualKeyBoard()
    {
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    //endregion

    //region helper methods

    /**
     * method to insert Fragments in ContentFragment
     *
     * @param fragment       Fragment to be addded
     * @param addToBackStack record the transaction or not. True to record false other wise.
     */
    public void addContentFragment(Fragment fragment, boolean addToBackStack)
    {
        hideVirtualKeyBoard();
        showLoading(false);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(getFragmentContainerID(), fragment);

        if (addToBackStack)
        {
            fragmentTransaction.addToBackStack(content_Fragment);
        }
        try
        {
            fragmentTransaction.commit();
        } catch (Exception ex)
        {
//           LoggerHelper.e(ex);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState)
    {
//        super.onSaveInstanceState(outState, outPersistentState);
    }

    /**
     * method to insert Fragments in ContentFragment
     *
     * @param fragment       Fragment to be addded
     * @param addToBackStack record the transaction or not. True to record false other wise.
     */
    @SuppressLint("ResourceType")
    public void addContentFragment(Fragment fragment, boolean addToBackStack, @AnimRes int enterAnim, @AnimRes int exitAnim)
    {
        hideVirtualKeyBoard();
        showLoading(false);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (enterAnim > 0 && exitAnim > 0)
            fragmentTransaction.setCustomAnimations(enterAnim, exitAnim);

        fragmentTransaction.replace(getFragmentContainerID(), fragment);

        if (addToBackStack)
        {
            fragmentTransaction.addToBackStack(content_Fragment);
        }
        try
        {
            fragmentTransaction.commit();
        } catch (Exception ex)
        {
//           LoggerHelper.e(ex);
        }
    }

    /**
     * This is used to add Fragments
     */
    public abstract int getFragmentContainerID();

    /**
     * This is the main layout for activity
     */
    protected abstract int getBaseLayoutID();

    /**
     * clear backStack for the fragments and go back to the first fragment in the application
     */
    public void clearBackStack()
    {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0)
        {
            //			FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(content_Fragment, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }


  /*  public void openUrlInBrowser(String url)
    {
        //to show user a dialog with all browser list, so he can choose preferred
        String HTTPS = "https://";
        String HTTP = "http://";
        if (!url.startsWith(HTTP) && !url.startsWith(HTTPS))
        {
            url = HTTP + url;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        String tempText = getString(R.string.chooseBrowser);
        startActivity(Intent.createChooser(intent, tempText));
    }*/

    public void callPhone(final String phone)
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
        {
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(callIntent);
        } else
        {
            Dexter.withActivity(this).withPermission(Manifest.permission.CALL_PHONE).withListener(new PermissionListener()
            {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse response)
                {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(callIntent);
                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse response)
                {
                    showMessage(getString(R.string.permissions_needed));
                }

                @Override
                public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token)
                {
                    showPermissionRationaleMessage(token);

                }

            });
        }
    }

    /**
     * This will show the activity ProgressBar and will stop any interaction ont he screen. <br></br>
     * Make sure you call it again to close the loading
     *
     * @param loading true to show loading, false otherwise
     */
    public void showLoading(boolean loading)
    {
        if (mainProgressBar == null)
        {
            return;
        }
        if (loading)
        {
            mainProgressBar.setVisibility(View.VISIBLE);
            mainProgressBar.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                }
            });
//            showProgressDialog();
        } else
        {
            mainProgressBar.setVisibility(View.GONE);
            mainProgressBar.setOnClickListener(null);
        }
    }


    private void showProgressDialog()
    {
        ProgressDialog progressDialog = new ProgressDialog(this, R.style.CustomDialog);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIcon(R.drawable.common_full_open_on_phone);
        progressDialog.show();
    }


    //endregion

    //region getters
//    public AppHeader getAppHeader()
//    {
//        return appHeader;
//    }

    public BottomNavigationViewEx getBottomNavigationView()
    {
        return bottomNavigationView;
    }

//    public DatabaseReference getMyRef()
//    {
//        return myRef;
//    }
    //endregion

    //region showing message in dialog
    public MaterialDialog.Builder getMaterialDialogBuilder()
    {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
//        builder.typeface("TheSansArabic-Bold.otf", "TheSansArabic-Plain.otf");

        return builder;
    }

    public void showMessage(@StringRes int stringResourceId)
    {
        showMessage(null, getString(stringResourceId));
    }

    public void showMessage(String message)
    {
        showMessage(null, message);
    }

    public void showMessage(@Nullable String title, @NonNull String message)
    {
        if (!isFinishing())
        {
            MaterialDialog.Builder builder = getMaterialDialogBuilder();
            builder.content(message);
            if (title != null)
            {
                builder.title(title);
            }

            builder.content(message).positiveText(R.string.agree).onPositive(new MaterialDialog.SingleButtonCallback()
            {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
                {
                    dialog.dismiss();
                }
            }).autoDismiss(true).titleGravity(GravityEnum.CENTER).contentGravity(GravityEnum.CENTER).show();
        }
    }

    /**
     * show a message while providing the clicks on positive and negative button.
     * <br></br> This dialog will not auto dismissed. you need to dismiss it yourself in any click
     */
    public void showMessage(String message, @NonNull MaterialDialog.SingleButtonCallback positiveClick, @Nullable
            MaterialDialog.SingleButtonCallback negativeClick)
    {
        if (!isFinishing())
        {
            MaterialDialog.Builder builder = getMaterialDialogBuilder().content(message).positiveText(R.string.agree)
                    .onPositive(positiveClick).positiveColor(getResources()
                            .getColor(R.color.colorPrimary)).titleGravity(GravityEnum.CENTER).contentGravity(GravityEnum.CENTER);
            if (negativeClick != null)
            {
                builder.negativeText(R.string.cancel);
                builder.onNegative(negativeClick);
                builder.typeface("TheSansArabic-Plain.otf", "TheSansArabic-Plain.otf");
                builder.negativeColor(getResources().getColor(R.color.colorPrimary));
            }

            builder.show();
        }
    }

    /**
     * show a message while providing the clicks on positive and negative button.
     * <br></br> This dialog will not auto dismissed. you need to dismiss it yourself in any click
     */
    public void showMessage(String message, String positiveText, @NonNull MaterialDialog.SingleButtonCallback positiveClick,
                            String negativeText, @Nullable MaterialDialog.SingleButtonCallback negativeClick)
    {
        if (!isFinishing())
        {
            MaterialDialog.Builder builder = getMaterialDialogBuilder().content(message).positiveText(positiveText)
                    .onPositive(positiveClick).positiveColor(getResources()
                            .getColor(R.color.colorPrimary)).titleGravity(GravityEnum.CENTER).contentGravity(GravityEnum.CENTER);
            if (negativeClick != null)
            {
                builder.negativeText(negativeText);
                builder.onNegative(negativeClick);
                builder.negativeColor(getResources().getColor(R.color.colorPrimary));
            }

            builder.show();
        }
    }

    public void showPermissionRationaleMessage(final PermissionToken token)
    {
        showMessage(this, getString(R.string.permissions_needed), new MaterialDialog.SingleButtonCallback()
        {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
            {
                token.continuePermissionRequest();
                dialog.dismiss();
            }
        }, new MaterialDialog.SingleButtonCallback()
        {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
            {
                token.cancelPermissionRequest();
                dialog.dismiss();
            }
        });
    }

    public void showMessage(Activity activity, @NonNull String message, @NonNull MaterialDialog.SingleButtonCallback
            positiveClick, @Nullable MaterialDialog.SingleButtonCallback negativeClick)
    {
        if (activity != null && !activity.isFinishing())
        {
            MaterialDialog.Builder builder = getMaterialDialogBuilder().content(message).positiveText(R.string.agree)
                    .onPositive(positiveClick)
                    .positiveColor(activity.getResources()
                            .getColor(R.color.colorPrimary))
                    .titleGravity(GravityEnum.CENTER)
                    .contentGravity(GravityEnum.CENTER);
            if (negativeClick != null)
            {
                builder.negativeText(R.string.no);
                builder.onNegative(negativeClick);
            }

            builder.show();
        }
    }
    //endregion

    //region photos
    PublishSubject<Uri> publishSubject_images;

    /* public io.reactivex.Observable<Uri> takeImage() {
         publishSubject_images = PublishSubject.create();
         getMaterialDialogBuilder().title(getString(R.string.choose_image))
                 .titleColor(getResources().getColor(R.color.colorPrimary)).titleGravity(GravityEnum.CENTER)
                 .items(R.array.chooseImageDialogList).itemsGravity(GravityEnum.CENTER)
                 .itemsCallback(new MaterialDialog.ListCallback() {
                     @Override
                     public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence
                             text) {
                         if (position == 0)//camera
                         {
                             cameraClicked();
                         } else //gallery
                         {
                             galleryClicked();
                         }
                     }
                 }).show();

         return publishSubject_images;
     }
 */
    private void galleryClicked()
    {
        Dexter.withActivity(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener()
        {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response)
            {
//                chooseFromGallery();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response)
            {
                showMessage(getString(R.string.permissions_needed));
            }

            @Override
            public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token)
            {
                showPermissionRationaleMessage(token);

            }

        }).check();
    }

   /* private void chooseFromGallery()
    {
        RxImagePicker.with(this).requestImage(Sources.GALLERY).subscribe(new Observer<Uri>()
        {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d)
            {

            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull Uri uri)
            {
                publishSubject_images.onNext(uri);
                publishSubject_images.onComplete();
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e)
            {
//               LoggerHelper.e(e);
            }

            @Override
            public void onComplete()
            {
//               LoggerHelper.i(TAG, "images from gallery onComplete");
            }
        });
    }*/

    /**
     * This will check permission and upon success, it will start taking picture
     */
    private void cameraClicked()
    {
        Dexter.withActivity(this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener()
                {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report)
                    {
                        if (report.areAllPermissionsGranted())
                        {
//                            takePhotoInternally();
                        } else if (report.isAnyPermissionPermanentlyDenied())
                        {
                            showMessage(getString(R.string.permissions_needed));
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token)
                    {

                        showPermissionRationaleMessage(token);
                    }


                }).check();
    }
/*
    private void takePhotoInternally()
    {
        RxImagePicker.with(this).requestImage(Sources.CAMERA).subscribe(new Consumer<Uri>()
        {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Uri uri) throws Exception
            {
                *//*List<Uri list = new ArrayList<>(0);
                list.add(uri);
                *//*
                publishSubject_images.onNext(uri);
                publishSubject_images.onComplete();

            }
        });
    }*/
    //endregion

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(getFragmentContainerID());
        if (fragment != null)
        {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
