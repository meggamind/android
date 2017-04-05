package meggamind.sudoku;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * Created by aniket on 3/28/17.
 */

public class Prefs extends PreferenceActivity{
    private static final boolean OPT_HINTS_DEF = true;
    private static final String OPT_HINTS = "hints";
    private static final String OPT_MUSIC= "music";
    private static final boolean OPT_MUSIC_DEF = true;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment()).commit();
    }

    public static class PrefsFragment extends PreferenceFragment{
        @Override
        public void onCreate(Bundle svaedInstance){
            super.onCreate(svaedInstance);
            PreferenceManager.setDefaultValues(getActivity(), R.xml.settings, false);
            addPreferencesFromResource(R.xml.settings);
        }
    }

    // Get the current value of the music option
    public static boolean getMusic(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_MUSIC, OPT_MUSIC_DEF);
    }

    // Get the current value of the hints option
    public static boolean getHints(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_HINTS, OPT_HINTS_DEF);
    }
}
