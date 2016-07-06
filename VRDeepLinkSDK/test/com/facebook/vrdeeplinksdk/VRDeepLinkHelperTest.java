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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static com.facebook.vrdeeplinksdk.VRDeepLinkHelper.VRDeepLinkParam;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for the VRDeepLinkHelper, because why not.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class VRDeepLinkHelperTest {

  private static final String RANDOM_FBID = "ImmaFBID";

  private Context mMockContext;
  private PackageManager mPackageManager;

  @Before
  public void setUp() {
    mMockContext = mock(Context.class);
    mPackageManager = mock(PackageManager.class);
    when(mMockContext.getPackageManager())
            .thenReturn(mPackageManager);
  }

  @Test
  public void testEmptyParam() {
    Assert.assertEquals(
            VRDeepLinkHelper.createDeepLinkIntent(mMockContext, null),
            null);
  }

  @Test
  public void testNullFBIDParam() {
    VRDeepLinkParam param = new VRDeepLinkParam(
            null,
            VRDeepLinkHelper.MediaType.VIDEO);
    Assert.assertEquals(
            VRDeepLinkHelper.createDeepLinkIntent(mMockContext, param),
            null);
  }

  @Test
  public void testEmptyFBIDParam() {
    VRDeepLinkParam param = new VRDeepLinkParam(
            "",
            VRDeepLinkHelper.MediaType.VIDEO);
    Assert.assertEquals(
            VRDeepLinkHelper.createDeepLinkIntent(mMockContext, param),
            null);
  }

  @Test
  public void testVideoIntentWithOculusVideoInstalled() {
    try {
      when(mPackageManager.getPackageInfo("com.oculus.cinema", 0))
              .thenReturn(null);
    } catch (PackageManager.NameNotFoundException e) {
    }
    VRDeepLinkParam param = new VRDeepLinkParam(
            RANDOM_FBID,
            VRDeepLinkHelper.MediaType.VIDEO);
    Intent intent = VRDeepLinkHelper.createDeepLinkIntent(mMockContext, param);
    Assert.assertNotNull(intent);
    Assert.assertEquals(
            intent.getData().toString(),
            "oculus://video/fb/ImmaFBID");
  }

  @Test
  public void testPhotoIntentWithOculus360PhotoInstalled() {
    try {
      when(mPackageManager.getPackageInfo("com.oculus.oculus360photos", 0))
              .thenReturn(null);
    } catch (PackageManager.NameNotFoundException e) {
    }
    VRDeepLinkParam param = new VRDeepLinkParam(
            RANDOM_FBID,
            VRDeepLinkHelper.MediaType.PHOTO);
    Intent intent = VRDeepLinkHelper.createDeepLinkIntent(mMockContext, param);
    Assert.assertNotNull(intent);
    Assert.assertEquals(
            intent.getData().toString(),
            "oculus://photo/fb/ImmaFBID");
  }

  @Test
  public void testVideoIntentWithNoSupportedAppInstalled() {
    try {
      when(mPackageManager.getPackageInfo("com.oculus.cinema", 0))
              .thenThrow(new PackageManager.NameNotFoundException());
    } catch (PackageManager.NameNotFoundException e) {
    }
    VRDeepLinkParam param = new VRDeepLinkParam(
            RANDOM_FBID,
            VRDeepLinkHelper.MediaType.VIDEO);
    Intent intent = VRDeepLinkHelper.createDeepLinkIntent(mMockContext, param);
    Assert.assertNotNull(intent);
    Assert.assertEquals(
            intent.getData().toString(),
            "https://m.facebook.com/ImmaFBID");
  }

  @Test
  public void testPhotoIntentWithNoSupportedAppInstalled() {
    try {
      when(mPackageManager.getPackageInfo("com.oculus.oculus360photos", 0))
              .thenThrow(new PackageManager.NameNotFoundException());
    } catch (PackageManager.NameNotFoundException e) {
    }
    VRDeepLinkParam param = new VRDeepLinkParam(
            RANDOM_FBID,
            VRDeepLinkHelper.MediaType.PHOTO);
    Intent intent = VRDeepLinkHelper.createDeepLinkIntent(mMockContext, param);
    Assert.assertNotNull(intent);
    Assert.assertEquals(
            intent.getData().toString(),
            "https://m.facebook.com/ImmaFBID");
  }

  @Test
  public void testHasAppInstalledFalse() {
    String mockPackageName = "haha.i.am.an.android.package";
    try {
      when(mPackageManager.getPackageInfo(mockPackageName, 0))
              .thenThrow(new PackageManager.NameNotFoundException());
    } catch (PackageManager.NameNotFoundException e) {
    }
    Assert.assertEquals(
            VRDeepLinkHelper.hasAppInstalled(mMockContext, mockPackageName),
            false);
  }

  @Test
  public void testHasAppInstalled() {
    String mockPackageName = "haha.i.am.an.android.package";
    try {
      when(mPackageManager.getPackageInfo(mockPackageName, 0))
              .thenReturn(null);
    } catch (PackageManager.NameNotFoundException e) {
    }
    Assert.assertEquals(
            VRDeepLinkHelper.hasAppInstalled(mMockContext, mockPackageName),
            true);
  }

  @Test
  public void testIsVideoDeepLink() {
    VRDeepLinkParam param = null;
    Assert.assertEquals(
            VRDeepLinkHelper.IsVideoDeepLink(param),
            false);

    param = new VRDeepLinkParam(
            RANDOM_FBID,
            VRDeepLinkHelper.MediaType.VIDEO);
    Assert.assertEquals(
            VRDeepLinkHelper.IsVideoDeepLink(param),
            true);

    param = new VRDeepLinkParam(
            RANDOM_FBID,
            VRDeepLinkHelper.MediaType.PHOTO);
    Assert.assertEquals(
            VRDeepLinkHelper.IsVideoDeepLink(param),
            false);
  }
}
