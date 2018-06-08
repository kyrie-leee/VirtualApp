package com.lody.virtual.client.stub;


import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.lody.virtual.helper.compat.ContentProviderCompat;
import com.lody.virtual.helper.compat.UriCompat;

import java.io.FileNotFoundException;

public class ProxyContentProvider extends ContentProvider {
    private static final boolean DEBUG = false;

    @Override
    public boolean onCreate() {
        return true;
    }

    private Uri wrapperUri(String form, Uri uri) {
        if(DEBUG) {
            Log.i("UriCompat", "wrapperUri:" + form);
        }
        return UriCompat.wrapperUri(uri);
    }

    @Override
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        if(VASettings.PROVIDER_ONLY_FILE)return null;
        Uri a = wrapperUri("query", uri);
        try {
            return ContentProviderCompat.crazyAcquireContentProvider(getContext(), a)
                    .query(a, strArr, str, strArr2, str2);
        } catch (Exception e) {
            return new MatrixCursor(new String[]{});
        }
    }

    @Override
    public String getType(Uri uri) {
        if(VASettings.PROVIDER_ONLY_FILE)return null;
        Uri a = wrapperUri("getType", uri);
        try {
            return ContentProviderCompat.crazyAcquireContentProvider(getContext(), a)
                    .getType(a);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        if(VASettings.PROVIDER_ONLY_FILE)return uri;
        Uri a = wrapperUri("insert", uri);
        try {
            return ContentProviderCompat.crazyAcquireContentProvider(getContext(), a)
                    .insert(a, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        if(VASettings.PROVIDER_ONLY_FILE)return 0;
        Uri a = wrapperUri("insert", uri);
        try {
            return ContentProviderCompat.crazyAcquireContentProvider(getContext(), a)
                    .bulkInsert(a, values);
        }catch (Throwable e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int delete(Uri uri, String str, String[] strArr) {
        if(VASettings.PROVIDER_ONLY_FILE)return 0;
        Uri a = wrapperUri("delete", uri);
        try {
            return ContentProviderCompat.crazyAcquireContentProvider(getContext(), a)
                    .delete(a, str, strArr);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        if(VASettings.PROVIDER_ONLY_FILE)return 0;
        Uri a = wrapperUri("update", uri);
        try {
            return ContentProviderCompat.crazyAcquireContentProvider(getContext(), a)
                    .update(a, contentValues, str, strArr);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public AssetFileDescriptor openAssetFile(Uri uri, String str) throws FileNotFoundException {
        Uri a = wrapperUri("openAssetFile", uri);
        if ("file".equals(a.getScheme())) {
            ParcelFileDescriptor fd = openFile(uri, str);
            return fd != null ? new AssetFileDescriptor(fd, 0, -1) : null;
        }
        try {
            return ContentProviderCompat.crazyAcquireContentProvider(getContext(), a)
                    .openAssetFile(a, str);
        } catch (Throwable e) {
            if(DEBUG) {
                Log.w("UriCompat", "openAssetFile2", e);
            }
            return null;
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public AssetFileDescriptor openAssetFile(Uri uri, String str, CancellationSignal cancellationSignal) throws FileNotFoundException {
        Uri a = wrapperUri("openAssetFile2", uri);
        try {
            return ContentProviderCompat.crazyAcquireContentProvider(getContext(), a)
                    .openAssetFile(a, str, cancellationSignal);
        } catch (Throwable e) {
            if(DEBUG) {
                Log.w("UriCompat", "openAssetFile2", e);
            }
            return null;
        }
    }

    private static int modeToMode(String mode) {
        int modeBits;
        if ("r".equals(mode)) {
            modeBits = ParcelFileDescriptor.MODE_READ_ONLY;
        } else if ("w".equals(mode) || "wt".equals(mode)) {
            modeBits = ParcelFileDescriptor.MODE_WRITE_ONLY
                    | ParcelFileDescriptor.MODE_CREATE
                    | ParcelFileDescriptor.MODE_TRUNCATE;
        } else if ("wa".equals(mode)) {
            modeBits = ParcelFileDescriptor.MODE_WRITE_ONLY
                    | ParcelFileDescriptor.MODE_CREATE
                    | ParcelFileDescriptor.MODE_APPEND;
        } else if ("rw".equals(mode)) {
            modeBits = ParcelFileDescriptor.MODE_READ_WRITE
                    | ParcelFileDescriptor.MODE_CREATE;
        } else if ("rwt".equals(mode)) {
            modeBits = ParcelFileDescriptor.MODE_READ_WRITE
                    | ParcelFileDescriptor.MODE_CREATE
                    | ParcelFileDescriptor.MODE_TRUNCATE;
        } else {
            throw new IllegalArgumentException("Invalid mode: " + mode);
        }
        return modeBits;
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String str) throws FileNotFoundException {
        Uri a = wrapperUri("openFile", uri);
        if ("file".equals(a.getScheme())) {
            return ParcelFileDescriptor.open(new java.io.File(a.getPath()), modeToMode(str));
        }
        try {
            return ContentProviderCompat.crazyAcquireContentProvider(getContext(), a)
                    .openFile(a, str);
        } catch (Exception e) {
            if(DEBUG) {
                Log.w("UriCompat", "openFile", e);
            }
            return null;
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public ParcelFileDescriptor openFile(Uri uri, String str, CancellationSignal cancellationSignal) throws FileNotFoundException {
        Uri a = wrapperUri("openFile2", uri);
        try {
            return ContentProviderCompat.crazyAcquireContentProvider(getContext(), a)
                    .openFile(a, str, cancellationSignal);
        } catch (Exception e) {
            if(DEBUG) {
                Log.w("UriCompat", "openFile2", e);
            }
            return null;
        }
    }

    @Override
    public Bundle call(String str, String str2, Bundle bundle) {
        return super.call(str, str2, bundle);
    }

}