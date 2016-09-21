package org.herac.tuxguitar.android.activity;

import org.herac.tuxguitar.util.TGContext;
import org.herac.tuxguitar.util.singleton.TGSingletonFactory;
import org.herac.tuxguitar.util.singleton.TGSingletonUtil;

public class TGActivityController {

    private TGActivity activity;

    public TGActivityController() {
        super();
    }

    public TGActivity getActivity() {
        return activity;
    }

    public void setActivity(TGActivity activity) {
        this.activity = activity;
    }

    public static TGActivityController getInstance(TGContext context) {
        TGSingletonFactory<TGActivityController> tgSingletonFactory = new TGSingletonFactory<TGActivityController>() {
            public TGActivityController createInstance(TGContext context) {
                return new TGActivityController();
            }
        };
        return TGSingletonUtil.getInstance(context, TGActivityController.class.getName(), tgSingletonFactory);
    }
}
