package com.example.david.equationapp.DepencencyInjection;

import com.example.david.equationapp.IDatabase;
import com.example.david.equationapp.models.DatabaseController;

import dagger.Module;
import dagger.Provides;

/**
 * Created by David on 12/1/2017.
 */

/**
 * module class for the dagger 2 dependency injector
 * this class implements getting the IDatabase Dependency
 */
@Module
public class IDatabaseModule {

    @Provides
    public IDatabase getIDatabase(){
        return new DatabaseController();
    }
}
