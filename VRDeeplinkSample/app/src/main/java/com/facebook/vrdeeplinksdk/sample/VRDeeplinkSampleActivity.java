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

package com.facebook.vrdeeplinksdk.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.facebook.vrdeeplinksdk.VRDeepLinkHelper;

public class VRDeeplinkSampleActivity extends AppCompatActivity {

  private static final String VIDEO_FBID = "1329049410442559";
  private static final String PHOTO_FBID = "1084268434960161";
  private final View.OnClickListener mClickListener =
          new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      String fbid = VIDEO_FBID;
      VRDeepLinkHelper.MediaType mediaType = VRDeepLinkHelper.MediaType.VIDEO;
      if (v == mPhotoButton) {
        fbid = PHOTO_FBID;
        mediaType = VRDeepLinkHelper.MediaType.PHOTO;
      }
      Intent intent = VRDeepLinkHelper.createDeepLinkIntent(
              VRDeeplinkSampleActivity.this,
              new VRDeepLinkHelper.VRDeepLinkParam(
                      fbid,
                      mediaType));
      startActivity(intent);
    }
  };

  private Button mVideoButton;
  private Button mPhotoButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_vrdeeplink_sample);
    mVideoButton = (Button) findViewById(R.id.video_button);
    mPhotoButton = (Button) findViewById(R.id.photo_button);
    mVideoButton.setOnClickListener(mClickListener);
    mPhotoButton.setOnClickListener(mClickListener);
  }
}
