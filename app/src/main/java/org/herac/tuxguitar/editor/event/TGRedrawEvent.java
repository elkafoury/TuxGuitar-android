package org.herac.tuxguitar.editor.event;

import org.herac.tuxguitar.event.TGEvent;
import org.herac.tuxguitar.util.TGAbstractContext;

public class TGRedrawEvent extends TGEvent {
	
	public static final String EVENT_TYPE = "ui-redraw";
	public static final String PROPERTY_REDRAW_MODE = "redrawMode";
	
	public static final int NORMAL = 1;
	public static final int PLAYING_THREAD = 2;
	public static final int PLAYING_NEW_BEAT = 3;

	public static final String PLUGIN_EVENT_TYPE = "plugin-ui-redraw";
	public static final String PLUGIN_PROPERTY_REDRAW_MODE = "plugin-redrawMode";

	public static final int PLUGIN_NORMAL = 4;
	public static final int PLUGIN_PLAYING_THREAD = 5;
	public static final int PLUGIN_PLAYING_NEW_BEAT = 6;
	
	public TGRedrawEvent(int redrawMode, TGAbstractContext sourceContext) {

		super(EVENT_TYPE, sourceContext);

		this.setAttribute(PROPERTY_REDRAW_MODE, Integer.valueOf(redrawMode));
	}
	public TGRedrawEvent(int redrawMode , TGAbstractContext sourceContext, Boolean isPlugin) {

		super(PLUGIN_EVENT_TYPE, sourceContext);

		this.setAttribute(PLUGIN_PROPERTY_REDRAW_MODE, Integer.valueOf(redrawMode));
	}

}
