package com.rewedigital.composer.html;

import java.util.Objects;

public class ContentRange {

    private final int start;
    private final int end;

    public static ContentRange allUpToo(final int end) {
        return new ContentRange(0, end);
    }

    public static ContentRange empty() {
        return new ContentRange(0, 0);
    }

    public ContentRange(final int start, final int end) {
        this.start = start;
        this.end = end;
    }

    public int start() {
        return start;
    }

    public int end() {
        return end;
    }

    public boolean isInRange(final int value) {
        return start <= value && value <= end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(end, start);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ContentRange other = (ContentRange) obj;
        return this.end == other.end && this.start == other.start;
    }

    @Override
    public String toString() {
        return "ContentRange [start=" + start + ", end=" + end + "]";
    }
}
