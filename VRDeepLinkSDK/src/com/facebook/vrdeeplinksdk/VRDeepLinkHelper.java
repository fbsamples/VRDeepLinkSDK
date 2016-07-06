/**
 * Copyright (c) 2016-present, Facebook, Inc. All rights reserved.

 * You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 * copy, modify, and distribute this software in source code or binary form for use
 * in connection with the web services and APIs provided by Facebook.

 * As with any software that integrates with the Facebook platform, your use of
 * this software is subject to the Facebook Developer Principles and Policies
 * [http://developers.facebook.com/policy/]. This copyright notice shall be
 * included in all copies or substantial portions of the software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.facebook.vrdeeplinksdk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.VisibleForTesting;

public class VRDeepLinkHelper {
  private static final String OCULUS_CINEMA_PACKAGE_NAME = "com.oculus.cinema";
  private static final String OCULUS_PHOTO_PACKAGE_NAME = "com.oculus.oculus360photos";

  private static final String URI_SCHEME_OCULUS = "oculus";
  private static final String URI_MEDIA_TYPE_VIDEO = "video";
  private static final String URI_MEDIA_TYPE_PHOTO = "photo";
  private static final String URI_MEDIA_SOURCE_FB = "fb";

  private static final String URI_SCHEME_HTTPS = "https";
  private static final String URI_FB_AUTHORITY = "m.facebook.com";

  public static enum MediaType {
    VIDEO,
    PHOTO
  }

  public static class VRDeepLinkParam {
    public final String mediaFbId;
    public final MediaType mediaType;

    public VRDeepLinkParam(
            String videoFbId,
            MediaType mediaType) {
      this.mediaFbId = videoFbId;
      this.mediaType = mediaType;
    }
  }

  /**
   * Helper function to create an intent to launch 360 photos/videos in Oculus Video/360Photo app.
   * If such app doesn't exist, we'll fallback to FB4A or m-site depending on application availability.
   *
   * @param context an android context
   * @param param A VRDeepLinkParam object that contains the fbid of the deeplink media and its type
   * @return an Intent object to launch the deeplinked content.
   */
  public static Intent createDeepLinkIntent(Context context, VRDeepLinkParam param) {
    if (param == null ||
            isStringNullOrEmpty(param.mediaFbId)) {
      return null;
    }

    if (IsVideoDeepLink(param)) {
      return createDeepLinkIntentForVideoContent(context, param);
    }
    return createDeepLinkIntentForPhotoContent(context, param);
  }

  private static Intent createDeepLinkIntentForVideoContent(Context context, VRDeepLinkParam param) {
    if (hasAppInstalled(context, OCULUS_CINEMA_PACKAGE_NAME)) {
      return createIntentForOculusApp(param);
    }
    return createIntentForFacebookApp(param);
  }

  private static Intent createDeepLinkIntentForPhotoContent(Context context, VRDeepLinkParam param) {
    if (hasAppInstalled(context, OCULUS_PHOTO_PACKAGE_NAME)) {
      return createIntentForOculusApp(param);
    }
    return createIntentForFacebookApp(param);
  }

  /**
   * Checks whether any of the support oculus app is installed.
   *
   * @param context an android context
   * @param packageName an android app package name
   * @return whether an app with the given package name is installed
   */
  @VisibleForTesting
  static boolean hasAppInstalled(Context context, String packageName) {
    try {
      context.getPackageManager().getPackageInfo(packageName, 0);
      return true;
    } catch (PackageManager.NameNotFoundException e) {
      return false;
    }
  }

  @VisibleForTesting
  static boolean IsVideoDeepLink(VRDeepLinkParam param) {
    return param != null && MediaType.VIDEO.equals(param.mediaType);
  }

  @VisibleForTesting
  static Intent createIntentForOculusApp(VRDeepLinkParam param) {
    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_VIEW);
    intent.addCategory(Intent.CATEGORY_DEFAULT);
    String packageName = OCULUS_CINEMA_PACKAGE_NAME;
    String mediaTypePath = URI_MEDIA_TYPE_VIDEO;
    if (MediaType.PHOTO.equals(param.mediaType)) {
      packageName = OCULUS_PHOTO_PACKAGE_NAME;
      mediaTypePath = URI_MEDIA_TYPE_PHOTO;
    }
    intent.setPackage(packageName);
    Uri.Builder builder = new Uri.Builder();
    builder.scheme(URI_SCHEME_OCULUS)
            .authority(mediaTypePath)
            .appendPath(URI_MEDIA_SOURCE_FB)
            .appendPath(param.mediaFbId);
    intent.setData(builder.build());
    return intent;
  }

  @VisibleForTesting
  static Intent createIntentForFacebookApp(VRDeepLinkParam param) {
    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_VIEW);
    intent.addCategory(Intent.CATEGORY_DEFAULT);
    intent.addCategory(Intent.CATEGORY_BROWSABLE);
    Uri.Builder builder = new Uri.Builder();
    builder.scheme(URI_SCHEME_HTTPS)
            .authority(URI_FB_AUTHORITY)
            .appendPath(param.mediaFbId);
    intent.setData(builder.build());
    return intent;
  }

  private static boolean isStringNullOrEmpty(String input) {
    return input == null || input == "";
  }
}
