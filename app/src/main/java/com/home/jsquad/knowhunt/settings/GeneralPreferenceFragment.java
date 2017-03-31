package com.home.jsquad.knowhunt.settings;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.TextUtils;
import android.util.Log;

import com.home.jsquad.knowhunt.R;
import com.home.jsquad.knowhunt.instrumentation.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * This fragment shows general preferences only. It is used when the
 * activity is showing a two-pane settings UI.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class GeneralPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        setHasOptionsMenu(true);

        // Hack for saving map from settings
        Preference preference = findPreference(getString(R.string.settings_send_my_map));
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                File toFile;
                try
                {
                    toFile = new File(Environment.getExternalStorageDirectory(), "tmp.xml");
                    if (toFile.exists())
                        toFile.delete();

                    toFile.createNewFile();
                    FileOutputStream to = new FileOutputStream(toFile);

                    String excursion = "djurgarden";
                    String filename = TextUtils.join("/", new String[]{App.getContext().getFilesDir().getAbsolutePath(),
                            "excursions", excursion});

                    FileInputStream from = new FileInputStream(filename + "/" + excursion + ".xml");
                    copyFile(from, to);
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("vnd.android.cursor.dir/email");

                String to[] = {"dmytro.shervarly@gmail.com"};
                emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(toFile));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Try this map!");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));

                return false;
            }

            public void copyFile(FileInputStream src, FileOutputStream dst)
            {
                FileChannel inChannel = src.getChannel();
                FileChannel outChannel = dst.getChannel();
                try
                {
                    long a = inChannel.size();
                    long transfered = inChannel.transferTo(0, inChannel.size(), outChannel);
                    Log.d("auudiotravel", "copying");
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                finally
                {
                    try
                    {
                        if (inChannel != null)
                            inChannel.close();
                        if (outChannel != null)
                            outChannel.close();
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }


}