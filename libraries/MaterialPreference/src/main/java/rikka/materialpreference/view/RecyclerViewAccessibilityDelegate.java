/*
 * Copyright (C) 2014 The Android Open Source Project
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

package rikka.materialpreference.view;

import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * The AccessibilityDelegate used by RecyclerView.
 * <p>
 * This class handles basic accessibility actions and delegates them to LayoutManager.
 */
public class RecyclerViewAccessibilityDelegate extends View.AccessibilityDelegate {

	final RecyclerView mRecyclerView;

	public RecyclerViewAccessibilityDelegate(RecyclerView recyclerView) {
		mRecyclerView = recyclerView;
	}

	@Override
	public boolean performAccessibilityAction(View host, int action, Bundle args) {
		if (super.performAccessibilityAction(host, action, args)) {
			return true;
		}
		if (mRecyclerView.getLayoutManager() != null) {
			return mRecyclerView.getLayoutManager().performAccessibilityAction(action, args);
		}

		return false;
	}

	@Override
	public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfo info) {
		super.onInitializeAccessibilityNodeInfo(host, info);
		info.setClassName(RecyclerView.class.getName());
		if (mRecyclerView.getLayoutManager() != null) {
			mRecyclerView.getLayoutManager().onInitializeAccessibilityNodeInfo(info);
		}
	}

	@Override
	public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
		super.onInitializeAccessibilityEvent(host, event);
		event.setClassName(RecyclerView.class.getName());
		if (host instanceof RecyclerView) {
			RecyclerView rv = (RecyclerView) host;
			if (rv.getLayoutManager() != null) {
				rv.getLayoutManager().onInitializeAccessibilityEvent(event);
			}
		}
	}

	View.AccessibilityDelegate getItemDelegate() {
		return mItemDelegate;
	}

	final View.AccessibilityDelegate mItemDelegate = new View.AccessibilityDelegate() {
		@Override
		public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfo info) {
			super.onInitializeAccessibilityNodeInfo(host, info);
			if (mRecyclerView.getLayoutManager() != null) {
				mRecyclerView.getLayoutManager().
						onInitializeAccessibilityNodeInfoForItem(host, info);
			}
		}

		@Override
		public boolean performAccessibilityAction(View host, int action, Bundle args) {
			if (super.performAccessibilityAction(host, action, args)) {
				return true;
			}
			if (mRecyclerView.getLayoutManager() != null) {
				return mRecyclerView.getLayoutManager().
						performAccessibilityActionForItem(host, action, args);
			}
			return false;
		}
	};
}