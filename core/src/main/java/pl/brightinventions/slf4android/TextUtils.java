package pl.brightinventions.slf4android;

abstract class TextUtils {
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }
}
