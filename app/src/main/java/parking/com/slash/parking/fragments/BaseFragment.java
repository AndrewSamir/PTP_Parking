package parking.com.slash.parking.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.Unbinder;
import parking.com.slash.parking.R;
import parking.com.slash.parking.activities.BaseActivity;
import parking.com.slash.parking.activities.SplashScreenActivity;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * This is base code that will help other fragments. <br\></br\>
 * Remember This will require you to implement few functions like showing header. <br\></br\>
 * You may need to add Margin to the top of your fragment, if you are going to show the header
 */
public abstract class BaseFragment extends RxFragment
{
    //region fields
    public final String TAG = this.getClass().getSimpleName();
    //    protected AppHeader appHeader;
    protected BottomNavigationViewEx bottomNavigationView;
    //    protected SpaceNavigationView bottomNavigationView;
    protected Unbinder unbinder;
    //endregion

    //region life cycles


    @Nullable
    @CallSuper
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = super.onCreateView(inflater, container, savedInstanceState);
//        appHeader = getBaseActivity().getAppHeader();
        bottomNavigationView = getBaseActivity().getBottomNavigationView();


        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

       /* if (getTitle() != null)
            setTitle(getTitle());
        else
            setTitle("");
*/

       /* if (canShowAppHeader())
            appHeader.setVisibility(View.VISIBLE);
        else
        {
            appHeader.setVisibility(View.GONE);
        }
*/

        if (canShowBottomBar() && getBaseActivity().getBottomNavigationView() != null)
        {
            getBaseActivity().getBottomNavigationView().setVisibility(View.VISIBLE);
        } else if (!canShowBottomBar() && getBaseActivity().getBottomNavigationView() != null)
        {
            getBaseActivity().getBottomNavigationView().setVisibility(View.GONE);
        }

//        appHeader.setBackArrow(canShowBackArrow());
        setBackAction();
    }


    @Override
    public void onStop()
    {
        super.onStop();
    }


    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onDestroy()
    {
        if (unbinder != null)
            unbinder.unbind();
    /*    qBadgeView.bindTarget(bottomNavigationView.getBottomNavigationItemView(0))
                .hide(true);*/
        super.onDestroy();
    }

//endregion

    //region abstract methods

    protected abstract boolean canShowAppHeader();

    protected abstract boolean canShowBottomBar();

    protected abstract boolean canShowBackArrow();

    protected abstract String getTitle();

    public abstract int getSelectedMenuId();

//endregion

    //region showing message helpers
    protected void showMessage(@StringRes int stringID)
    {
        if (getBaseActivity() != null)
        {
            getBaseActivity().showMessage(stringID);
        }
    }

    protected void showMessage(String text)
    {
        if (getBaseActivity() != null)
        {
            getBaseActivity().showMessage(text);
        }
    }

    public void showMessage(@StringRes int stringResId, @NonNull MaterialDialog.SingleButtonCallback positiveClick)
    {
        if (getBaseActivity() != null)
        {
            getBaseActivity().showMessage(getString(stringResId), positiveClick, null);
        }
    }

    public void showMessage(@StringRes int stringResId, @NonNull MaterialDialog.SingleButtonCallback positiveClick, @NonNull MaterialDialog.SingleButtonCallback negativeClick)
    {
        if (getBaseActivity() != null)
        {
            getBaseActivity().showMessage(getString(stringResId), positiveClick, negativeClick);
        }
    }

//endregion

    /*
    //region header helpers
    public void setTitle(@StringRes int textRes)
    {
        appHeader.setTitle(textRes);
    }

    public void setTitle(String text)
    {
        appHeader.setTitle(text);
    }


    protected void setHeaderLeft(@DrawableRes int imageRes)
    {
        appHeader.setLeft(imageRes);
    }


    protected void setRightImgClickListener(View.OnClickListener onClickListener)
    {
        appHeader.setRightImgClickListener(onClickListener);
    }

    protected void setSecondRightImgClickListener(View.OnClickListener onClickListener)
    {
        appHeader.setSecondRightImgClickListener(onClickListener);
    }

    protected void setRightTxtClickListener(View.OnClickListener onClickListener)
    {
        appHeader.setRightTxtClickListener(onClickListener);
    }

    protected void setHeaderRight(@StringRes int textRes, @DrawableRes int imageRes, @DrawableRes int imageRes_2)
    {
        appHeader.setRight(textRes, imageRes, imageRes_2);
    }


//endregion
*/
    //region bottom navigation type
    //Those are used to select the correct bottom button

//endregion

    //region helper functions


    public void addFragment(Fragment fragment, boolean addToBackStack)
    {
        if (getActivity() != null)
        {
            //            showLoading(false);
            ((BaseActivity) getActivity()).addContentFragment(fragment, addToBackStack);
        }
    }

    public void setBottomNavigationView(int actionId)
    {
        bottomNavigationView.getMenu().findItem(actionId).setChecked(true);

    }

    private void setBackAction()
    {
        /*if (appHeader != null)
        {
            appHeader.setLeftClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (getActivity() != null)
                    {
                        hideSoftKeyboard();
                        getActivity().onBackPressed();
                    }
                }
            });
        }
*/

    }

    public void hideSoftKeyboard()
    {
        if (getBaseActivity().getCurrentFocus() != null)
        {
            InputMethodManager inputMethodManager = (InputMethodManager) getBaseActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getBaseActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    protected String getShowDate(String receivedDate)
    {

        String[] dateItems = receivedDate.split("-");
        String day = dateItems[2];
        String year = dateItems[0];
        String month;
        switch (dateItems[1])
        {

            case "01":
                month = "Jan";
                break;
            case "02":
                month = "Feb";
                break;
            case "03":
                month = "Mar";
                break;
            case "04":
                month = "Apr";
                break;
            case "05":
                month = "May";
                break;
            case "06":
                month = "Jun";
                break;
            case "07":
                month = "Jul";
                break;
            case "08":
                month = "Aug";
                break;
            case "09":
                month = "Sep";
                break;
            case "10":
                month = "Oct";
                break;
            case "11":
                month = "Nov";
                break;
            default:
                month = "Dec";
                break;
        }

        return day + "/" + month + "/" + year;
    }

    protected void signOut()
    {
//        SharedPreferenceController.Companion.signOut();
        Intent intent = new Intent(getActivity(), SplashScreenActivity.class);
        startActivity(intent);
    }

    protected void share(String text)
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }

    /**
     * show\hide loading which is in title bar
     */
    public void showLoading(boolean loading)
    {
        if (getActivity() == null)
            return;

        ((BaseActivity) getActivity()).showLoading(loading);
    }

    /**
     * this method will return baseActivity only if it is instanceOf {@link BaseActivity}
     *
     * @return {@link BaseActivity}
     * throws NullPointerException
     */
    protected BaseActivity getBaseActivity() throws NullPointerException
    {
        if (getActivity() instanceof BaseActivity)
            return (BaseActivity) getActivity();
        else
            return null;
    }

    //endregion
}