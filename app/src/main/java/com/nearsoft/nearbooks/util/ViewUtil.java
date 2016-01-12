package com.nearsoft.nearbooks.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.support.v4.util.Pair;
import android.support.v7.graphics.Palette;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.ActionMenuView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.nearsoft.nearbooks.R;

import java.util.ArrayList;

/**
 * Utilities related with views.
 * Created by epool on 1/8/16.
 */
public class ViewUtil {

    public static Pair<Integer, Palette.Swatch> getVibrantPriorityColorSwatchPair(
            Palette palette, int defaultColor) {
        if (palette == null) return null;

        if (palette.getVibrantSwatch() != null) {
            return new Pair<>(palette.getVibrantColor(defaultColor),
                    palette.getVibrantSwatch());
        } else if (palette.getLightVibrantSwatch() != null) {
            return new Pair<>(palette.getLightVibrantColor(defaultColor),
                    palette.getLightVibrantSwatch());
        } else if (palette.getDarkVibrantSwatch() != null) {
            return new Pair<>(palette.getDarkVibrantColor(defaultColor),
                    palette.getDarkVibrantSwatch());
        } else if (palette.getMutedSwatch() != null) {
            return new Pair<>(palette.getMutedColor(defaultColor),
                    palette.getMutedSwatch());
        } else if (palette.getLightMutedSwatch() != null) {
            return new Pair<>(palette.getLightMutedColor(defaultColor),
                    palette.getLightMutedSwatch());
        } else if (palette.getDarkMutedSwatch() != null) {
            return new Pair<>(palette.getDarkMutedColor(defaultColor),
                    palette.getDarkMutedSwatch());
        }
        return null;
    }

    public static class Toolbar {
        /**
         * Use this method to colorize toolbar icons to the desired target color
         *
         * @param toolbarView       toolbar view being colored
         * @param toolbarIconsColor the target color of toolbar icons
         * @param activity          reference to activity needed to register observers
         */
        public static void colorizeToolbar(android.support.v7.widget.Toolbar toolbarView,
                                           int toolbarIconsColor, Activity activity) {
            final PorterDuffColorFilter colorFilter =
                    new PorterDuffColorFilter(toolbarIconsColor, PorterDuff.Mode.MULTIPLY);

            for (int i = 0; i < toolbarView.getChildCount(); i++) {
                final View v = toolbarView.getChildAt(i);

                //Step 1 : Changing the color of back button (or open drawer button).
                if (v instanceof ImageButton) {
                    //Action Bar back button
                    ((ImageButton) v).getDrawable().setColorFilter(colorFilter);
                }

                if (v instanceof ActionMenuView) {
                    for (int j = 0; j < ((ActionMenuView) v).getChildCount(); j++) {

                        //Step 2: Changing the color of any ActionMenuViews - icons that
                        //are not back button, nor text, nor overflow menu icon.
                        final View innerView = ((ActionMenuView) v).getChildAt(j);

                        if (innerView instanceof ActionMenuItemView) {
                            int drawablesCount = ((ActionMenuItemView) innerView)
                                    .getCompoundDrawables().length;
                            for (int k = 0; k < drawablesCount; k++) {
                                if (((ActionMenuItemView) innerView)
                                        .getCompoundDrawables()[k] != null) {
                                    final int finalK = k;

                                    //Important to set the color filter in seperate thread,
                                    //by adding it to the message queue
                                    //Won't work otherwise.
                                    innerView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            ((ActionMenuItemView) innerView)
                                                    .getCompoundDrawables()[finalK]
                                                    .setColorFilter(colorFilter);
                                        }
                                    });
                                }
                            }
                        }
                    }
                }

                //Step 3: Changing the color of title and subtitle.
                toolbarView.setTitleTextColor(toolbarIconsColor);
                toolbarView.setSubtitleTextColor(toolbarIconsColor);

                //Step 4: Changing the color of the Overflow Menu icon.
                setOverflowButtonColor(activity, colorFilter);
            }
        }

        /**
         * It's important to set overflowDescription attribute in styles, so we can grab the
         * reference to the overflow icon. Check: res/values/styles.xml
         *
         * @param activity    Activity where is called.
         * @param colorFilter Color to be set.
         */
        private static void setOverflowButtonColor(final Activity activity,
                                                   final PorterDuffColorFilter colorFilter) {
            @SuppressLint("PrivateResource")
            final String overflowDescription = activity
                    .getString(R.string.abc_action_menu_overflow_description);
            final ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            final ViewTreeObserver viewTreeObserver = decorView.getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    final ArrayList<View> outViews = new ArrayList<>();
                    decorView.findViewsWithText(outViews, overflowDescription,
                            View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    if (outViews.isEmpty()) {
                        return;
                    }
                    ImageView overflow = (ImageView) outViews.get(0);
                    overflow.setColorFilter(colorFilter);
                    removeOnGlobalLayoutListener(decorView, this);
                }
            });
        }

        private static void removeOnGlobalLayoutListener(View v,
                                                         ViewTreeObserver
                                                                 .OnGlobalLayoutListener listener) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
            } else {
                v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
            }
        }
    }

}
