package net.fionix.minima;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by nazebzurati on 22/09/2017.
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new WidgetFactory(this.getApplicationContext(), intent));
    }
}
