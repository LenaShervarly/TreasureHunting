package com.home.croaton.followme.domain;

import android.content.Context;

import com.home.croaton.followme.R;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;

public class ExcursionRepository {
    private final Context context;
    private ExcursionGallery gallery;

    public ExcursionRepository(Context context) {
        this.context = context;
    }

    public ExcursionGallery getGallery() {
        if (gallery == null) {
            gallery = loadGallery();
        }
        return gallery;
    }

    private ExcursionGallery loadGallery() {
        Serializer serializer = new Persister();
        try {
            InputStream stream = context.getResources().openRawResource(R.raw.excursion_gallery);
            return serializer.read(ExcursionGallery.class, stream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ExcursionGallery();
    }
}
