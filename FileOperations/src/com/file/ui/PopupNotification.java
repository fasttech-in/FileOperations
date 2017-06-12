package com.file.ui;

import javafx.event.EventHandler;
import eu.hansolo.enzo.notification.NotificationEvent;
import eu.hansolo.enzo.notification.Notification.Notifier;

public class PopupNotification {

	public static void showSuccess(String title, String message) {
		Notifier notification = Notifier.INSTANCE;
		notification.notifySuccess(title, message);
		setOnHideNotification(notification);
	}
	
	public static void showError(String title, String message) {
		Notifier notification = Notifier.INSTANCE;
		notification.notifyError(title, message);
		setOnHideNotification(notification);
	}
	
	public static void showWarning(String title, String message) {
		Notifier notification = Notifier.INSTANCE;
		notification.notifyWarning(title, message);
		setOnHideNotification(notification);
	}
	
	public static void showInfo(String title, String message) {
		Notifier notification = Notifier.INSTANCE;
		notification.notifyInfo(title, message);
		setOnHideNotification(notification);
	}

	private static void setOnHideNotification(Notifier notification) {
		notification.setOnHideNotification(new EventHandler<NotificationEvent>() {
			@Override
			public void handle(NotificationEvent event) {
				notification.stop();
			}
		});
	}

}
