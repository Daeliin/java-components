package com.daeliin.components.core.image;

import com.google.common.base.MoreObjects;

import java.util.Objects;

public final class SimpleImageFormat implements ImageFormat {

    private final int width;
    private final int height;
    private final String extension;
    private final String prefix;
    private final String suffix;
    private final float compression;

    private SimpleImageFormat(int width, int height, String extension, String prefix, String suffix, float compression) {
        this.width = width;
        this.height = height;
        this.extension = Objects.requireNonNull(extension);
        this.prefix = prefix;
        this.suffix = suffix;
        this.compression = compression;
    }

    public static class Builder {

        private int width;
        private int height;
        private String extension;
        private String prefix;
        private String suffix;
        private float compression;

        public Builder() {
        }

        public Builder withWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder withHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder withExtension(String extension) {
            this.extension = Objects.requireNonNull(extension);
            return this;
        }

        public Builder withPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder withSuffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        public Builder withCompression(float compression) {
            this.compression = compression;
            return this;
        }

        public ImageFormat build() {
            return new SimpleImageFormat(this.width, this.height, this.extension, this.prefix, this.suffix, this.compression);
        }
    }

    @Override
    public int width() {
        return this.width;
    }

    @Override
    public int height() {
        return this.height;
    }

    @Override
    public String extension() {
        return this.extension;
    }

    @Override
    public String prefix() {
        return this.prefix;
    }

    @Override
    public String suffix() {
        return this.suffix;
    }

    @Override
    public float compression() {
        return this.compression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleImageFormat that = (SimpleImageFormat) o;

        if (width != that.width) return false;
        if (height != that.height) return false;
        if (Float.compare(that.compression, compression) != 0) return false;
        if (extension != null ? !extension.equals(that.extension) : that.extension != null) return false;
        if (prefix != null ? !prefix.equals(that.prefix) : that.prefix != null) return false;
        return suffix != null ? suffix.equals(that.suffix) : that.suffix == null;
    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + height;
        result = 31 * result + (extension != null ? extension.hashCode() : 0);
        result = 31 * result + (prefix != null ? prefix.hashCode() : 0);
        result = 31 * result + (suffix != null ? suffix.hashCode() : 0);
        result = 31 * result + (compression != +0.0f ? Float.floatToIntBits(compression) : 0);
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("width", width)
            .add("height", height)
            .add("extension", extension)
            .add("prefix", prefix)
            .add("suffix", suffix)
            .add("compression", compression)
            .toString();
    }
}
