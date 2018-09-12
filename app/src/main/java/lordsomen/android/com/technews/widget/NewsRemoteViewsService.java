package lordsomen.android.com.technews.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class NewsRemoteViewsService extends RemoteViewsService {

    @Override
    public NewsRemoteViewsFactory onGetViewFactory(Intent intent) {
        return new NewsRemoteViewsFactory(this.getApplicationContext(), intent);
    }

}
