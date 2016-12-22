package location.share.com.aleksandr.aleksandrov.sharelocation.services;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by Aleksandr on 12/21/2016.
 */

public class Service {

    private Context context;

    public Service(Context context) {
        this.context = context;
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
