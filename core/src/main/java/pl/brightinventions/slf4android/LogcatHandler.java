package pl.brightinventions.slf4android;

/*
 * Copyright (C) 2008 The Android Open Source Project
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


import android.annotation.SuppressLint;
import android.util.Log;

import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class LogcatHandler extends Handler {

    private final LogRecordFormatter logRecordFormatter;

    public LogcatHandler(LogRecordFormatter logRecordFormatter) {
        this.logRecordFormatter = logRecordFormatter;
    }

    @Override
    public void close() {
    }

    @Override
    public void flush() {
    }

    @Override
    @SuppressLint("WrongConstant")
    public void publish(LogRecord record) {
        if (!isLoggable(record)) {
            return;
        }
        pl.brightinventions.slf4android.LogRecord slfRecord = pl.brightinventions.slf4android.LogRecord.fromRecord(record);
        int level = slfRecord.getLogLevel().getAndroidLevel();
        String tag = record.getLoggerName();

        try {
            String message = logRecordFormatter.format(slfRecord);
            /*
             * Apparently, lint doesn't understand that getAndroidLevel() returns a valid Android
             * log level value, hence the @SuppressLint("WrongConstant") above this method.
             */
            Log.println(level, tag, message);
        } catch (RuntimeException e) {
            Log.e("LogcatHandler", "Error logging message.", e);
        }
    }


}