package com.tokuraku;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

public class UriPathParser {

    private final String path;
    private final Uri uri;
    private final Context context;

    public UriPathParser(Uri uri,Context context) {
        this.uri = uri;
        this.context = context;
        this.path = getUriRealPathAboveKitkat(context,uri);
    }

    /*
This method will parse out the real local file path from the file content URI.
The method is only applied to the android SDK version number that is bigger than 19.
*/
    private String getUriRealPathAboveKitkat(Context ctx, Uri uri)
    {
        String ret = "";
        if(ctx != null && uri != null) {
            if(isContentUri(uri))
            {
                if(isGooglePhotoDoc(uri.getAuthority()))
                {
                    ret = uri.getLastPathSegment();
                }else if (isMediaDoc(uri.getAuthority())){
                    // Get uri related document id.
                    String documentId = DocumentsContract.getDocumentId(uri);
                    // Get uri authority.
                    String uriAuthority = uri.getAuthority();

                    if(isMediaDoc(uriAuthority)){
                        String[] idArr = documentId.split(":");
                        Log.d("getUriRealPath1",idArr[0]);
                        Log.d("getUriRealPath2",idArr[1]);
                        if(idArr.length == 2){
                            // First item is document type.
                            String docType = idArr[0];
                            // Second item is document real id.
                            String realDocId = idArr[1];
                            // Get content uri by document type.
                            Uri mediaContentUri;

                            if("video".equals(docType)) {
                                ret = Environment.getExternalStorageDirectory() + "/Movies/" + getImageRealPath(this.context.getContentResolver(), uri, null);

                            }else if("audio".equals(docType))
                            {
                                ret = Environment.getExternalStorageDirectory() + "/Music/" + getImageRealPath(this.context.getContentResolver(), uri, null);
                            }else if("image".equals(docType)){
                                ret = Environment.getExternalStorageDirectory() + "/Images/" + getImageRealPath(this.context.getContentResolver(), uri, null);
                            }else{
                                ret = Environment.getExternalStorageDirectory() + "/Documents/" + getImageRealPath(this.context.getContentResolver(), uri, null);
                            }
                        }
                    }
                }else if(isDownloadDoc(uri.getAuthority())) {
                    String documentId = DocumentsContract.getDocumentId(uri).split(":")[1];
                    // Build download URI.
                    Uri downloadUri = Uri.parse("content://downloads/public_downloads");
                    // Append download document id at URI end.
                    Uri downloadUriAppendId = ContentUris.withAppendedId(downloadUri, Long.parseLong(documentId));
                    Log.d("getUriRealPath3: ",documentId);
                    ret = Environment.getExternalStorageDirectory() + "/Download/" + getImageRealPath(this.context.getContentResolver(), uri, null);

                }
                else if(isExternalStoreDoc(uri.getAuthority()))
                {
                    String documentId = DocumentsContract.getDocumentId(uri);
                    String[] idArr = documentId.split(":");
                    if(idArr.length == 2)
                    {
                        String type = idArr[0];
                        String realDocId = idArr[1];
                        if("primary".equalsIgnoreCase(type))
                        {
                            ret = Environment.getExternalStorageDirectory() + "/" + realDocId;
                        }
                    }
                }
            }
            else if(isFileUri(uri)) {
                ret = uri.getPath();
            }else if(isDocumentUri(ctx, uri)){
                // Get uri related document id.
                String documentId = DocumentsContract.getDocumentId(uri);
                // Get uri authority.
                String uriAuthority = uri.getAuthority();
                if (isMediaDoc(uri.getAuthority())){
                    if(isMediaDoc(uriAuthority)){
                        String[] idArr = documentId.split(":");
                        Log.d("getUriRealPath1",idArr[0]);
                        Log.d("getUriRealPath2",idArr[1]);
                        if(idArr.length == 2){
                            // First item is document type.
                            String docType = idArr[0];
                            // Second item is document real id.
                            String realDocId = idArr[1];
                            // Get content uri by document type.
                            Uri mediaContentUri;

                            if("video".equals(docType)) {
                                ret = Environment.getExternalStorageDirectory() + "/Movies/" + getImageRealPath(this.context.getContentResolver(), uri, null);

                            }else if("audio".equals(docType))
                            {
                                ret = Environment.getExternalStorageDirectory() + "/Music/" + getImageRealPath(this.context.getContentResolver(), uri, null);
                            }else if("image".equals(docType)){
                                ret = Environment.getExternalStorageDirectory() + "/Images/" + getImageRealPath(this.context.getContentResolver(), uri, null);
                            }else{
                                ret = Environment.getExternalStorageDirectory() + "/Documents/" + getImageRealPath(this.context.getContentResolver(), uri, null);
                            }
                        }
                    }
                }
                else if(isDownloadDoc(uriAuthority)) {
                    documentId = DocumentsContract.getDocumentId(uri).split(":")[1];
                    // Build download URI.
                    Uri downloadUri = Uri.parse("content://downloads/public_downloads");
                    // Append download document id at URI end.
                    Uri downloadUriAppendId = ContentUris.withAppendedId(downloadUri, Long.parseLong(documentId));
                    Log.d("getUriRealPath3: ",documentId);
                    ret = Environment.getExternalStorageDirectory() + "/Download/" + getImageRealPath(this.context.getContentResolver(), uri, null);

                }else if(isExternalStoreDoc(uriAuthority))
                {
                    String[] idArr = documentId.split(":");
                    if(idArr.length == 2)
                    {
                        String type = idArr[0];
                        String realDocId = idArr[1];
                        if("primary".equalsIgnoreCase(type))
                        {
                            ret = Environment.getExternalStorageDirectory() + "/" + realDocId;
                        }
                    }
                }
            }
        }
        return ret;
    }

    /* Check whether this uri represent a document or not. */
    private boolean isDocumentUri(Context ctx, Uri uri)
    {
        boolean ret = false;
        if(ctx != null && uri != null) {
            ret = DocumentsContract.isDocumentUri(ctx, uri);
        }
        return ret;
    }
    /* Check whether this URI is a content URI or not.
     *  content uri like content://media/external/images/media/1302716
     *  */
    private boolean isContentUri(Uri uri)
    {
        boolean ret = false;
        if(uri != null) {
            String uriSchema = uri.getScheme();
            if("content".equalsIgnoreCase(uriSchema))
            {
                ret = true;
            }
        }
        return ret;
    }
    /* Check whether this URI is a file URI or not.
     *  file URI like file:///storage/41B7-12F1/DCIM/Camera/IMG_20180211_095139.jpg
     * */
    private boolean isFileUri(Uri uri)
    {
        boolean ret = false;
        if(uri != null) {
            String uriSchema = uri.getScheme();
            if("file".equalsIgnoreCase(uriSchema))
            {
                ret = true;
            }
        }
        return ret;
    }
    /* Check whether this document is provided by ExternalStorageProvider. Return true means the file is saved in external storage. */
    private boolean isExternalStoreDoc(String uriAuthority)
    {
        return "com.android.externalstorage.documents".equals(uriAuthority);
    }
    /* Check whether this document is provided by DownloadsProvider. return true means this file is a downloaded file. */
    private boolean isDownloadDoc(String uriAuthority)
    {
        return "com.android.providers.downloads.documents".equals(uriAuthority);
    }
    /*
    Check if MediaProvider provides this document, if true means this image is created in the android media app.
    */
    private boolean isMediaDoc(String uriAuthority)
    {
        return "com.android.providers.media.documents".equals(uriAuthority);
    }
    /*
    Check whether google photos provide this document, if true means this image is created in the google photos app.
    */
    private boolean isGooglePhotoDoc(String uriAuthority)
    {
        return "com.google.android.apps.photos.content".equals(uriAuthority);
    }
    /* Return uri represented document file real local path.*/
    private String getImageRealPath(ContentResolver contentResolver, Uri uri, String whereClause)
    {
        String ret = "";
        // Query the URI with the condition.
        @SuppressLint("Recycle")
        Cursor cursor = contentResolver.query(uri, null, whereClause, null, null);
        if(cursor!=null)
        {
            boolean moveToFirst = cursor.moveToFirst();
            if(moveToFirst)
            {
                // Get columns name by URI type.
                String columnName;
                if( uri==MediaStore.Images.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Images.Media.DATA;
                }else if( uri==MediaStore.Audio.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Audio.Media.DATA;
                }else if( uri==MediaStore.Video.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Video.Media.DATA;
                }else {
                    columnName = String.valueOf(MediaStore.Files.getContentUri("external"));
                }
                // Get column index.
                Log.d("Column name", columnName);
                //int imageColumnIndex = cursor.getColumnIndex(columnName);
                int imageColumnIndex = 2;
                Log.d("Column index", String.valueOf(imageColumnIndex));
                // Get column value which is the uri related file local path.
                ret = cursor.getString(imageColumnIndex);
            }
        }
        return ret;
    }

    public Uri getUri() {
        return uri;
    }

    public String getPath() {
        return path;
    }

}

