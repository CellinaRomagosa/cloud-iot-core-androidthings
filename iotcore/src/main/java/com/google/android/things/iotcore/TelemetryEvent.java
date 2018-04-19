// Copyright 2018 Google LLC.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.android.things.iotcore;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Message containing telemetry event data to publish to Cloud IoT Core. */
public class TelemetryEvent {

    private final String mTopicSubpath;
    private final byte[] mData;
    private final @Qos int mQos;

    /** Quality of service opitons. */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({QOS_AT_MOST_ONCE, QOS_AT_LEAST_ONCE})
    public @interface Qos {}

    /** At most once delivery. */
    public static final int QOS_AT_MOST_ONCE = 0;

    /** At least once delivery. */
    public static final int QOS_AT_LEAST_ONCE = 1;

    /**
     * Construct a new TelemetryEvent with the data to publish and an
     * optional topic subpath destination.
     *
     * @param data the telemetry event data to send to Cloud IoT Core
     * @param topicSubpath the subpath under "../device/../events/"
     * @param qos the quality of service to use when sending the message
     */
    public TelemetryEvent(@NonNull byte[] data, @Nullable String topicSubpath, @Qos int qos) {
        if (qos != QOS_AT_MOST_ONCE && qos != QOS_AT_LEAST_ONCE) {
            throw new IllegalArgumentException("Invalid quality of service provided.");
        }


        if (TextUtils.isEmpty(topicSubpath)) {
            topicSubpath = "";
        } else if (topicSubpath.charAt(0) != '/') {
            topicSubpath = "/" + topicSubpath;
        }

        mTopicSubpath = topicSubpath;
        mData = data;
        mQos = qos;
    }

    /** Return this event's data. */
    public byte[] getData() {
        return mData;
    }

    /**
     * Return this event's topic.
     *
     *<p>Non-empty strings returned by this method always begin with a slash (e.g. /foo).
     */
    public String getTopicSubpath() {
        return mTopicSubpath;
    }

    /** Return this event's quality of service settings. */
    public @Qos int getQos() {
        return mQos;
    }
}
