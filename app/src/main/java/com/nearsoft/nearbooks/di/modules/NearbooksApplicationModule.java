package com.nearsoft.nearbooks.di.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.nearsoft.nearbooks.NearbooksApplication;
import com.nearsoft.nearbooks.models.sqlite.User;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger 2 Nearbooks module.
 * Created by epool on 11/17/15.
 */
@Module
public class NearbooksApplicationModule {

    private final NearbooksApplication mNearbooksApplication;

    public NearbooksApplicationModule(NearbooksApplication nearbooksApplication) {
        mNearbooksApplication = nearbooksApplication;

        FlowManager.init(this.mNearbooksApplication.getApplicationContext());
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return mNearbooksApplication.getApplicationContext();
    }

    @Provides
    @Singleton
    public User provideUser() {
        return SQLite
                .select()
                .from(User.class)
                .querySingle();
    }

    @Provides
    @Singleton
    public SharedPreferences provideDefaultSharedPreferences() {
        return PreferenceManager
                .getDefaultSharedPreferences(mNearbooksApplication);
    }

}
