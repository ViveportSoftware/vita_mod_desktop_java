package com.htc.vita.mod.desktop.preference;

import com.htc.vita.core.preference.PreferenceFactory;
import com.htc.vita.core.preference.Preferences;

public class JavaPreferenceFactory extends PreferenceFactory {
    @Override
    protected Preferences onLoadPreferences(
            String category,
            String label) {
        return new JavaPreferences(
                category,
                label
        ).initialize();
    }
}
