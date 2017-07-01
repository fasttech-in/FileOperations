package com.file.dropbox.operations.auth;

import java.util.Locale;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;

public class DropboxAuthenticator {

	 static String AUTH_KEY =
	 "NL2VWiWyhvMAAAAAAAAAN6kuX4-Yb4sntImvzz1rLcQJouMxdCJlACSBj8N67Vqo";

	public static DbxClient authenticate(String key, String userName)
			throws DbxException {
	
		DbxClient client = connect(AUTH_KEY);

		return authenticateUser(client, userName) ? connect(key) : null;
	}

	protected static DbxClient connect(String accessToken) throws DbxException {
		DbxRequestConfig config = new DbxRequestConfig("Precise/DMS", Locale
				.getDefault().toString());

		DbxClient client = new DbxClient(config, accessToken);
		System.out.println("Linked account: "+ client.getAccountInfo().displayName);
		return client;
	}

	private static boolean authenticateUser(DbxClient client, String userName) throws DbxException {

		DbxEntry.WithChildren listing = client.getMetadataWithChildren("/DMS/AuthenticatedUsers");
		if (listing != null) {
			for (DbxEntry child : listing.children) {
				if (child.isFolder() && userName.equalsIgnoreCase(child.name)) {
					return true;
				}
			}
		}
		System.out.println("Unauthenticated access by user: " + userName);
		return false;
	}

}
