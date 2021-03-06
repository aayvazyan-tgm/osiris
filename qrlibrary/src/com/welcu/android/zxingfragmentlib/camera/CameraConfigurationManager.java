/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.welcu.android.zxingfragmentlib.camera;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.welcu.android.zxingfragmentlib.PreferencesActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * A class which deals with reading, parsing, and setting the camera parameters which are used to
 * configure the camera hardware.
 */
final class CameraConfigurationManager {

  private static final String TAG = "CameraConfiguration";

  // This is bigger than the size of a small screen, which is still supported. The routine
  // below will still select the default (presumably 320x240) size for these. This prevents
  // accidental selection of very low resolution on some devices.
  private static final int MIN_PREVIEW_PIXELS = 470 * 320; // normal screen
  private static final int MAX_PREVIEW_PIXELS = 1280 * 800;

  private final Context context;
  private final View view;
  private Point screenResolution;
  private Point cameraResolution;

  CameraConfigurationManager(Context context, View view) {
    this.context = context;
    this.view = view;
  }

  /**
   * Reads, one time, values from the camera that are needed by the app.
   */
  void initFromCameraParameters(Camera camera) {
    Camera.Parameters parameters = camera.getParameters();
    WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display display = manager.getDefaultDisplay();
    int width = display.getWidth();
    int height = display.getHeight();
    // We're landscape-only, and have apparently seen issues with display thinking it's portrait 
    // when waking from sleep. If it's not landscape, assume it's mistaken and reverse them:
    if (width < height) {
      Log.i(TAG, "Display reports portrait orientation; assuming this is incorrect");
      int temp = width;
      width = height;
      height = temp;
    }
    screenResolution = new Point(width, height);
    Log.i(TAG, "Screen resolution: " + screenResolution);
    cameraResolution = findBestPreviewSizeValue(parameters, screenResolution);
    Log.i(TAG, "Camera resolution: " + cameraResolution);
  }


@SuppressLint("NewApi")
void setDesiredCameraParameters(Camera camera, boolean safeMode) {
    Camera.Parameters parameters = camera.getParameters();

    if (parameters == null) {
      Log.w(TAG, "Device error: no camera parameters are available. Proceeding without configuration.");
      return;
    }

    Log.i(TAG, "Initial camera parameters: " + parameters.flatten());

    if (safeMode) {
      Log.w(TAG, "In camera config safe mode -- most settings will not be honored");
    }

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

    initializeTorch(parameters, prefs, safeMode);

    String focusMode = null;
    focusMode = findSettableValue(parameters.getSupportedFocusModes(),
			Camera.Parameters.FOCUS_MODE_AUTO);
	// Maybe selected auto-focus but not available, so fall through here:
	if (!safeMode && focusMode == null) {
		focusMode = findSettableValue(parameters.getSupportedFocusModes(),
				Camera.Parameters.FOCUS_MODE_MACRO, "edof"); // Camera.Parameters.FOCUS_MODE_EDOF
																// in 2.2+
	}
	if (focusMode != null) {
		parameters.setFocusMode(focusMode);
	}

    /** invert scan colors logic **/
//    if (prefs.getBoolean(PreferencesActivity.KEY_INVERT_SCAN, false)) {
//      String colorMode = findSettableValue(parameters.getSupportedColorEffects(),
//                                           Camera.Parameters.EFFECT_NEGATIVE);
//      if (colorMode != null) {
//        parameters.setColorEffect(colorMode);
//      }
//    }

    /** set zoom to optimize to small codes **/
	if (parameters.isZoomSupported()) {
		int targetZoomRatio = 100;
		// 3.5x, ideal to scan seals
		int targetZoom = -1;
		List<Integer> zoomRatios = parameters.getZoomRatios();
		for (Iterator<Integer> iterator = zoomRatios.iterator(); iterator
				.hasNext();) {
			Integer zoomRatio = (Integer) iterator.next();
			if (zoomRatio >= targetZoomRatio) {
				targetZoom = zoomRatios.lastIndexOf(zoomRatio);
				break;
			}
		}
		if (targetZoom == -1) {
			// if not found, use higher zoom available
			targetZoom = parameters.getMaxZoom();
		}
		parameters.setZoom(targetZoom);
	}

	/** Optimizing focus and metering on center screen - API 14 needed **/
	if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
		api14optimizations(parameters);
	}

    parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);

    setOrientation(camera, parameters);   
    
    camera.setParameters(parameters);
  }
  
  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void api14optimizations(Camera.Parameters parameters) {
		Rect newRect = new Rect(-25, -25, 25, 25);
		Camera.Area areaOfInterest = new Camera.Area(newRect, 1000);
		if (parameters.getMaxNumFocusAreas() > 0) {
			List<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
			focusAreas.add(areaOfInterest);
			parameters.setFocusAreas(focusAreas);
		}
		if (parameters.getMaxNumMeteringAreas() > 0) {
			List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
			meteringAreas.add(areaOfInterest);
			parameters.setMeteringAreas(meteringAreas);
		}
	}

  private void setOrientation(Camera camera, Camera.Parameters parameters) {
      if (view.getWidth() < view.getHeight()){
          if (Build.VERSION.SDK_INT<=7) {
              setOrientationBefore8(parameters);
          } else {
              setOrientation8(camera);
          }
      }
  }

  private void setOrientationBefore8(Camera.Parameters parameters) {
      parameters.set("orientation", "portrait");
      parameters.setRotation(90);
  }

  @TargetApi(Build.VERSION_CODES.FROYO)
  private void setOrientation8(Camera camera) {
      camera.setDisplayOrientation(90);
  }

  Point getCameraResolution() {
	return cameraResolution;
  }

  Point getViewResolution() {
    return new Point(view.getWidth(), view.getHeight());
  }

  Point getScreenResolution() {
    return screenResolution;
  }

  boolean getTorchState(Camera camera) {
    if (camera != null) {
      Camera.Parameters parameters = camera.getParameters();
      if (parameters != null) {
        String flashMode = camera.getParameters().getFlashMode();
        return flashMode != null &&
            (Camera.Parameters.FLASH_MODE_ON.equals(flashMode) ||
             Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode));
      }
    }
    return false;
  }

  void setTorch(Camera camera, boolean newSetting) {
    Camera.Parameters parameters = camera.getParameters();
    doSetTorch(parameters, newSetting, false);
    camera.setParameters(parameters);
  }

  private void initializeTorch(Camera.Parameters parameters, SharedPreferences prefs, boolean safeMode) {
    boolean currentSetting = FrontLightMode.readPref(prefs) == FrontLightMode.ON;
    doSetTorch(parameters, currentSetting, safeMode);
  }

  private void doSetTorch(Camera.Parameters parameters, boolean newSetting, boolean safeMode) {
    String flashMode;
    if (newSetting) {
      flashMode = findSettableValue(parameters.getSupportedFlashModes(),
                                    Camera.Parameters.FLASH_MODE_TORCH,
                                    Camera.Parameters.FLASH_MODE_ON);
    } else {
      flashMode = findSettableValue(parameters.getSupportedFlashModes(),
                                    Camera.Parameters.FLASH_MODE_OFF);
    }
    if (flashMode != null) {
      parameters.setFlashMode(flashMode);
    }

    /*
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    if (!prefs.getBoolean(PreferencesActivity.KEY_DISABLE_EXPOSURE, false)) {
      if (!safeMode) {
        ExposureInterface exposure = new ExposureManager().build();
        exposure.setExposure(parameters, newSetting);
      }
    }
     */
  }

  private Point findBestPreviewSizeValue(Camera.Parameters parameters, Point screenResolution) {

    List<Camera.Size> rawSupportedSizes = parameters.getSupportedPreviewSizes();
    if (rawSupportedSizes == null) {
      Log.w(TAG, "Device returned no supported preview sizes; using default");
      Camera.Size defaultSize = parameters.getPreviewSize();
      return new Point(defaultSize.width, defaultSize.height);
    }

    // Sort by size, descending
    List<Camera.Size> supportedPreviewSizes = new ArrayList<Camera.Size>(rawSupportedSizes);
    Collections.sort(supportedPreviewSizes, new Comparator<Camera.Size>() {
      @Override
      public int compare(Camera.Size a, Camera.Size b) {
        int aPixels = a.height * a.width;
        int bPixels = b.height * b.width;
        if (bPixels < aPixels) {
          return -1;
        }
        if (bPixels > aPixels) {
          return 1;
        }
        return 0;
      }
    });

    if (Log.isLoggable(TAG, Log.INFO)) {
      StringBuilder previewSizesString = new StringBuilder();
      for (Camera.Size supportedPreviewSize : supportedPreviewSizes) {
        previewSizesString.append(supportedPreviewSize.width).append('x')
            .append(supportedPreviewSize.height).append(' ');
      }
      Log.i(TAG, "Supported preview sizes: " + previewSizesString);
    }

    Point bestSize = null;
    float screenAspectRatio = (float) screenResolution.x / (float) screenResolution.y;
    // 1.779166667

    float diff = Float.POSITIVE_INFINITY;
    for (Camera.Size supportedPreviewSize : supportedPreviewSizes) {
      int realWidth = supportedPreviewSize.width;
      int realHeight = supportedPreviewSize.height;
      int pixels = realWidth * realHeight;
      // 409920
      // MIN = 150400
      // MAX = 1024000
      if (pixels < MIN_PREVIEW_PIXELS || pixels > MAX_PREVIEW_PIXELS) {
        continue;
      }
      boolean isCandidatePortrait = realWidth < realHeight;
      int maybeFlippedWidth = isCandidatePortrait ? realHeight : realWidth;
      int maybeFlippedHeight = isCandidatePortrait ? realWidth : realHeight;
      if (maybeFlippedWidth == screenResolution.x && maybeFlippedHeight == screenResolution.y) {
        Point exactPoint = new Point(realWidth, realHeight);
        Log.i(TAG, "Found preview size exactly matching screen size: " + exactPoint);
        return exactPoint;
      }
      float aspectRatio = (float) maybeFlippedWidth / (float) maybeFlippedHeight;
      float newDiff = Math.abs(aspectRatio - screenAspectRatio);
      if (newDiff < diff) {
        bestSize = new Point(realWidth, realHeight);
        diff = newDiff;
      }
    }

    if (bestSize == null) {
      Camera.Size defaultSize = parameters.getPreviewSize();
      bestSize = new Point(defaultSize.width, defaultSize.height);
      Log.i(TAG, "No suitable preview sizes, using default: " + bestSize);
    }

    Log.i(TAG, "Found best approximate preview size: " + bestSize);
    return bestSize;
  }

  private static String findSettableValue(Collection<String> supportedValues,
                                          String... desiredValues) {
    Log.i(TAG, "Supported values: " + supportedValues);
    String result = null;
    if (supportedValues != null) {
      for (String desiredValue : desiredValues) {
        if (supportedValues.contains(desiredValue)) {
          result = desiredValue;
          break;
        }
      }
    }
    Log.i(TAG, "Settable value: " + result);
    return result;
  }

}
