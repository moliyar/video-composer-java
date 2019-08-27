package com.sombrainc.ffmpegtool.process.progress.listener;

import com.sombrainc.ffmpegtool.process.progress.model.ProgressItem;

public interface ProgressListener {

  void progress(ProgressItem progressItem);
}
