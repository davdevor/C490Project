package com.example.david.equationapp.DepencencyInjection;

/**
 * Created by David on 12/1/2017.
 */

import com.example.david.equationapp.IDatabase;

import dagger.Component;

/**
 * dependency injector interface defines methods of getting decencies
 * methods implemented in Modules and linked in the @Component tag
 */
@Component(modules = IDatabaseModule.class)
public interface EquationAppComponent {
    IDatabase getIDatabsae();
}
