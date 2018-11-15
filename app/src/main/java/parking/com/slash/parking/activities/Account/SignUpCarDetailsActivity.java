package parking.com.slash.parking.activities.Account;

import android.Manifest;
import android.app.ActionBar;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gun0912.tedbottompicker.TedBottomPicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import parking.com.slash.parking.R;
import parking.com.slash.parking.activities.MainActivity;
import parking.com.slash.parking.interfaces.HandleRetrofitResp;
import parking.com.slash.parking.model.ModelCarBrand.Model;
import parking.com.slash.parking.model.ModelCarBrand.ModelCarBrand;
import parking.com.slash.parking.model.ModelGetColors.ModelGetColors;
import parking.com.slash.parking.model.ModelLoginRequest.ModelLoginRequest;
import parking.com.slash.parking.model.ModelLoginResponse.ModelLoginResponse;
import parking.com.slash.parking.retorfitconfig.HandleCalls;
import parking.com.slash.parking.utlities.DataEnum;
import parking.com.slash.parking.utlities.HelpMe;
import parking.com.slash.parking.utlities.SharedPrefHelper;
import retrofit2.Call;


public class SignUpCarDetailsActivity extends FragmentActivity implements HandleRetrofitResp, CompoundButton.OnCheckedChangeListener, Validator.ValidationListener {

    //region fields
    String brandId, modelId, colorId;
    Intent intent;
    List<String> arrName, arrIds;
    List<String> arrNameColor, arrIdsColor;
    List<String> arrNameModels, arrIdsModeles;
    String my_var;
    int selectedPosition;
    ModelCarBrand modelCarBrand, modelCarBrandModels;
    ModelGetColors modelGetColors;
    MultipartBody.Part CarImage;
    Validator validator;

    //endregion

    //region views
    @NotEmpty
    @BindView(R.id.edtSignUpCarColor)
    AutoCompleteTextView edtSignUpCarColor;

    @NotEmpty
    @BindView(R.id.edtSignUpCarPlate)
    EditText edtSignUpCarPlate;
    @BindView(R.id.imgSignUp)
    ImageView imgSignUp;
    @BindView(R.id.tvSignUpConnectPaypal)
    TextView tvSignUpConnectPaypal;
    @BindView(R.id.btnSignUp)
    Button btnSignUp;

    @NotEmpty
    @BindView(R.id.tvSignUpCarModel)
    AutoCompleteTextView tvSignUpCarModel;
    @BindView(R.id.chSignUpAcceptTerms)
    CheckBox chSignUpAcceptTerms;

    @NotEmpty
    @BindView(R.id.autovSignUpCarBrand)
    AutoCompleteTextView autovSignUpCarBrand;
    //endregion

    //region life cycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_car_details);
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        intent = getIntent();
        HandleCalls.getInstance(this).setonRespnseSucess(this);
        callGetBrandsList();
        chSignUpAcceptTerms.setOnCheckedChangeListener(this);

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    //endregion

    //region clicks

    @OnClick(R.id.tvSignUpCarModel)
    void onClicktvSignUpCarModel(View view) {
        if (brandId != null)
            callGetModelsList(brandId);
    }

    @OnClick(R.id.tvSignUpSelectImage)
    void onClicktvSignUpSelectImage(View view) {

        if (ActivityCompat
                .checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager
                .PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager
                .PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager
                .PERMISSION_GRANTED) {
            requestStorageAndCameraPermission();
            return;
        }
        selectImage();
    }

    @OnClick(R.id.tvSignUpConnectPaypal)
    void onClicktvSignUpConnectPaypal(View view) {

    }

    @OnClick(R.id.tvSignUpTerms)
    void onClicktvSignUpTerms(View view) {

    }

    @OnClick(R.id.btnSignUp)
    void onClickbtnSignUp(View view) {
        validator.validate();
    }


    //endregion

    //region calls

    private void callGetBrandsList() {
        Call call = HandleCalls.restParki.getClientService().callGetBrandsList();
        HandleCalls.getInstance(this).callRetrofit(call, DataEnum.callGetBrandsList.name(), true);
    }

    private void callGetColorsList() {
        Call call = HandleCalls.restParki.getClientService().callGetColorsList();
        HandleCalls.getInstance(this).callRetrofit(call, DataEnum.callGetColorsList.name(), true);
    }

    private void callGetModelsList(String brandId) {
        Call call = HandleCalls.restParki.getClientService().callGetModelsList(brandId);
        HandleCalls.getInstance(this).callRetrofit(call, DataEnum.callGetModelsList.name(), true);
    }

    private void callRegister() {
        Call call = HandleCalls.restParki.getClientService().callRegister(
                intent.getStringExtra(DataEnum.regMail.name()),
                intent.getStringExtra(DataEnum.regPassword.name()),
                intent.getStringExtra(DataEnum.regMobile.name()),
                intent.getStringExtra(DataEnum.regName.name()),
                edtSignUpCarPlate.getText().toString(),
                modelId,
                colorId,
                FirebaseInstanceId.getInstance().getToken(),
                false);
        HandleCalls.getInstance(this).callRetrofit(call, DataEnum.callRegister.name(), true);
    }

    private void callLogin() {

        ModelLoginRequest modelLoginRequest = new ModelLoginRequest();
        modelLoginRequest.setUsername(intent.getStringExtra(DataEnum.regMail.name()));
        modelLoginRequest.setPassword(intent.getStringExtra(DataEnum.regPassword.name()));
        modelLoginRequest.setDevicetoken(FirebaseInstanceId.getInstance().getToken());
        Call call = HandleCalls.restParki.getClientService().callLogin(modelLoginRequest);
        HandleCalls.getInstance(this).callRetrofit(call, DataEnum.callLogin.name(), true);
    }

    //endregion

    //region call response
    @Override
    public void onResponseSuccess(String flag, Object o) {


        if (flag.equals(DataEnum.callGetBrandsList.name())) {
            callGetColorsList();
            Gson gson = new Gson();
            JsonObject jsonObject = gson.toJsonTree(o).getAsJsonObject();
            modelCarBrand = gson.fromJson(jsonObject, ModelCarBrand.class);


            arrName = new ArrayList<>();
            arrIds = new ArrayList<>();
            for (int i = 0; i < modelCarBrand.getModel().size(); i++) {

                arrName.add(modelCarBrand.getModel().get(i).getName());
                arrIds.add(modelCarBrand.getModel().get(i).getId());
            }
            final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, arrName);

            autovSignUpCarBrand.setThreshold(2);
            autovSignUpCarBrand.setAdapter(adapter);


            autovSignUpCarBrand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    my_var = adapter.getItem(position).toString();
                    selectedPosition = position;
                    brandId = getID(my_var, modelCarBrand);
                    callGetModelsList(getID(my_var, modelCarBrand));
                }
            });

            autovSignUpCarBrand.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    my_var = null;
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });


//            showListDialog(brandNames, brandIds, DataEnum.brand.name());

        } else if (flag.equals(DataEnum.callGetModelsList.name())) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.toJsonTree(o).getAsJsonObject();
            modelCarBrandModels = gson.fromJson(jsonObject, ModelCarBrand.class);

            arrNameModels = new ArrayList<>();
            arrIdsModeles = new ArrayList<>();
            for (int i = 0; i < modelCarBrandModels.getModel().size(); i++) {

                arrNameModels.add(modelCarBrandModels.getModel().get(i).getName());
                arrIdsModeles.add(modelCarBrandModels.getModel().get(i).getId());
            }
            final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, arrNameModels);

            tvSignUpCarModel.setThreshold(2);
            tvSignUpCarModel.setAdapter(adapter);


            tvSignUpCarModel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    my_var = adapter.getItem(position).toString();
                    selectedPosition = position;
                    modelId = getID(my_var, modelCarBrandModels);
//                    callGetModelsList(getID(my_var,modelCarBrand));
                }
            });

            tvSignUpCarModel.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    my_var = null;
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });


        } else if (flag.equals(DataEnum.callGetColorsList.name())) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.toJsonTree(o).getAsJsonObject();
            modelGetColors = gson.fromJson(jsonObject, ModelGetColors.class);

            arrNameColor = new ArrayList<>();
            arrIdsColor = new ArrayList<>();
            for (int i = 0; i < modelGetColors.getModel().size(); i++) {

                arrNameColor.add(modelGetColors.getModel().get(i).getColorname());
                arrIdsColor.add(modelGetColors.getModel().get(i).getCarcolorid());
            }
            final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, arrNameColor);

            edtSignUpCarColor.setThreshold(2);
            edtSignUpCarColor.setAdapter(adapter);


            edtSignUpCarColor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    my_var = adapter.getItem(position).toString();
                    selectedPosition = position;
                    colorId = getID(my_var, modelGetColors);
//                    callGetModelsList(getID(my_var,modelCarBrand));
                }
            });

            edtSignUpCarColor.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    my_var = null;
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });


        } else if (flag.equals(DataEnum.callRegister.name())) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.toJsonTree(o).getAsJsonObject();
            ModelLoginResponse modelGetNearByResponse = gson.fromJson(jsonObject, ModelLoginResponse.class);

            SharedPrefHelper.getInstance(this).setUser(modelGetNearByResponse.getModel());
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } /*else if (flag.equals(DataEnum.callLogin.name())) {
            if (flag.equals(DataEnum.callLogin.name())) {
                Gson gson = new Gson();
                JsonObject jsonObject = gson.toJsonTree(o).getAsJsonObject();
                ModelLoginResponse modelGetNearByResponse = gson.fromJson(jsonObject, ModelLoginResponse.class);

                SharedPrefHelper.getInstance(this).setUser(modelGetNearByResponse.getModel());
                startActivity(new Intent(this, MainActivity.class));
                finish();

            }
        }*/

    }

    @Override
    public void onNoContent(String flag, int code) {

    }

    @Override
    public void onResponseSuccess(String flag, Object o, int position) {

    }

    //endregion

    //region functions

    /*  private void showListDialog(final String[] arrName, final String[] arrId, final String flag) {
          List<String> sss = new ArrayList<>();
          sss.add("ads");
          sss.add("ads");
          new MaterialDialog.Builder(SignUpCarDetailsActivity.this)
                  .title(flag)
                  .items(sss)

                  .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                      @Override
                      public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                          if (flag.equals(DataEnum.brand.name())) {
                              brandId = arrId[which];
                              tvSignUpCarBrand.setText(arrName[which]);
                          } else if (flag.equals(DataEnum.carModel.name())) {
                              modelId = arrId[which];
                              tvSignUpCarModel.setText(arrName[which]);
                          }

                          dialog.dismiss();
                          return true;
                      }
                  })
                  .negativeText(this.getString(R.string.cancel))
                  .show();


          tvSignUpCarBrand.setText("dcghjkls");
      }
  */
    private String getID(String name, ModelCarBrand modelCarBrand) {

        for (Model modelCarBrand1 : modelCarBrand.getModel()) {
            if (name.equals(modelCarBrand1.getName()))
                return modelCarBrand1.getId();
        }

        return null;
    }

    private String getID(String name, ModelGetColors modelGetColors) {

        for (parking.com.slash.parking.model.ModelGetColors.Model model : modelGetColors.getModel()) {
            if (name.equals(model.getColorname()))
                return model.getCarcolorid();
        }

        return null;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if (b) {
            btnSignUp.setAlpha(1f);
            btnSignUp.setEnabled(true);
        } else {
            btnSignUp.setAlpha(0.2f);
            btnSignUp.setEnabled(false);
        }
    }
    //endregion

    //region images

    private void requestStorageAndCameraPermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            selectImage();
                        } else if (report.isAnyPermissionPermanentlyDenied()) {
//                            showMessage(R.string.please_grant_permissions);
                            Log.d("permissions", "permissions grant");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                        showPermissionRationaleMessage(token);

                    }

                }).check();
    }

    private void showPermissionRationaleMessage(final PermissionToken token) {

        HelpMe.showMessage(this, getResources().getString(R.string.permissions_needed), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                token.continuePermissionRequest();
                dialog.dismiss();
            }
        }, new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                token.cancelPermissionRequest();
                dialog.dismiss();
            }
        });
    }

    private void selectImage() {

        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(this)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        CarImage = prepareFilePart("CarImage", uri);
                        Picasso.with(SignUpCarDetailsActivity.this)
                                .load(uri)
                                .into(imgSignUp);
                    }
                })
                .create();
      /*  Context mContext;
        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
*/
        tedBottomPicker.show(getSupportFragmentManager());

    }

    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {

        File file = new File(getRealPathFromUri(fileUri));
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private String getRealPathFromUri(final Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(this, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(this, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(this, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(this, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    //endregion

    //region validation
    @Override
    public void onValidationSucceeded() {
        callRegister();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
//                showMessage(message);
            }
        }
    }

    //endregion

}
