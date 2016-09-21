package org.herac.tuxguitar.android.drawer.main;

import org.herac.tuxguitar.android.action.TGActionProcessorListener;
import org.herac.tuxguitar.android.action.impl.browser.TGBrowserPrepareForReadAction;
import org.herac.tuxguitar.android.action.impl.browser.TGBrowserPrepareForWriteAction;
import org.herac.tuxguitar.android.action.impl.browser.TGBrowserSaveCurrentElementAction;
import org.herac.tuxguitar.android.action.impl.gui.TGOpenDialogAction;
import org.herac.tuxguitar.android.action.impl.gui.TGOpenFragmentAction;
import org.herac.tuxguitar.android.action.impl.track.TGGoToTrackAction;
import org.herac.tuxguitar.android.browser.TGBrowserManager;
import org.herac.tuxguitar.android.browser.model.TGBrowserSession;
import org.herac.tuxguitar.android.fragment.TGFragmentController;
import org.herac.tuxguitar.android.fragment.impl.TGChannelListFragmentController;
import org.herac.tuxguitar.android.view.dialog.TGDialogController;
import org.herac.tuxguitar.android.view.dialog.info.TGSongInfoDialogController;
import org.herac.tuxguitar.document.TGDocumentContextAttributes;
import org.herac.tuxguitar.editor.action.file.TGLoadTemplateAction;
import org.herac.tuxguitar.song.models.TGTrack;

public class TGMainDrawerActionHandler {
	
	private TGMainDrawer mainDrawer;
	
	public TGMainDrawerActionHandler(TGMainDrawer mainDrawer) {
		this.mainDrawer = mainDrawer;
	}
	
	public TGActionProcessorListener createAction(String actionId) {
		return new TGActionProcessorListener(this.mainDrawer.findContext(), actionId);
	}
	
	public TGActionProcessorListener createGoToTrackAction(TGTrack track) {
		TGActionProcessorListener tgActionProcessor = this.createAction(TGGoToTrackAction.NAME);
		tgActionProcessor.setAttribute(TGDocumentContextAttributes.ATTRIBUTE_TRACK, track);
		return tgActionProcessor;
	}
	
	public TGActionProcessorListener createBrowserAction(String actionId) {
		TGActionProcessorListener tgActionProcessor = this.createAction(actionId);
		tgActionProcessor.setAttribute(TGBrowserSession.class.getName(), this.findBrowserSession());
		return tgActionProcessor;
	}
	
	public TGActionProcessorListener createNewFileAction() {
		return this.createAction(TGLoadTemplateAction.NAME);
	}
	
	public TGActionProcessorListener createOpenFileAction() {
		return this.createBrowserAction(TGBrowserPrepareForReadAction.NAME);
	}
	
	public TGActionProcessorListener createSaveFileAsAction() {
		return this.createBrowserAction(TGBrowserPrepareForWriteAction.NAME);
	}
	
	public TGActionProcessorListener createSaveFileAction() {
		return this.createBrowserAction(TGBrowserSaveCurrentElementAction.NAME);
	}
	
	public TGActionProcessorListener createFragmentAction(TGFragmentController<?> controller) {
		TGActionProcessorListener tgActionProcessor = this.createAction(TGOpenFragmentAction.NAME);
		tgActionProcessor.setAttribute(TGOpenFragmentAction.ATTRIBUTE_CONTROLLER, controller);
		tgActionProcessor.setAttribute(TGOpenFragmentAction.ATTRIBUTE_ACTIVITY, this.mainDrawer.findActivity());
		return tgActionProcessor;
	}
	
	public TGActionProcessorListener createDialogAction(TGDialogController controller) {
		TGActionProcessorListener tgActionProcessor = this.createAction(TGOpenDialogAction.NAME);
		tgActionProcessor.setAttribute(TGOpenDialogAction.ATTRIBUTE_DIALOG_CONTROLLER, controller);
		tgActionProcessor.setAttribute(TGOpenDialogAction.ATTRIBUTE_DIALOG_ACTIVITY, this.mainDrawer.findActivity());
		return tgActionProcessor;
	}
	
	public TGActionProcessorListener createOpenInstrumentsAction() {
		return this.createFragmentAction(TGChannelListFragmentController.getInstance(this.mainDrawer.findContext()));
	}
	
	public TGActionProcessorListener createOpenInfoAction() {
		return this.createDialogAction(new TGSongInfoDialogController());
	}
	
	public TGBrowserSession findBrowserSession() {
		return TGBrowserManager.getInstance(this.mainDrawer.findContext()).getSession();
	}
}
