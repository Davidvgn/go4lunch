package com.davidvignon.go4lunch.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import java.util.Objects;

abstract class MainAction {
    static class Toast extends MainAction {
        @StringRes
        final int messageStringRes;

        Toast(@StringRes int messageStringRes) {
            this.messageStringRes = messageStringRes;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Toast toast = (Toast) o;
            return messageStringRes == toast.messageStringRes;
        }

        @Override
        public int hashCode() {
            return Objects.hash(messageStringRes);
        }

        @NonNull
        @Override
        public String toString() {
            return "Toast{" +
                "messageStringRes=" + messageStringRes +
                '}';
        }
    }

    static class DetailNavigation extends MainAction {
        final String placeId;

        DetailNavigation(String placeId) {
            this.placeId = placeId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DetailNavigation that = (DetailNavigation) o;
            return Objects.equals(placeId, that.placeId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(placeId);
        }

        @NonNull
        @Override
        public String toString() {
            return "DetailNavigation{" +
                "placeId='" + placeId + '\'' +
                '}';
        }
    }
}
