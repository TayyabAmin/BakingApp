package com.example.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Tayyab on 8/18/2017.
 */

public class ListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new WidgetRemoteViewsFactory(this.getApplicationContext(), intent));
    }
}

