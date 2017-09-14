package org.remdev.android.common.utils;

import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;

import java.io.Serializable;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;

public class BundleUtils {

    /**
     * Builds the bundle for the given key-value pairs
     * <br>
     *     The are several restrictions
     *     <ul>
     *         <li>the value in {@link Pair#second} should not be null</li>
     *         <li>the value in {@link Pair#second} should one of the type supported by bundle</li>
     *         <ul>
     *             <li>{@link Boolean} or boolean[]</li>
     *             <li>{@link Byte} or byte[]</li>
     *             <li>{@link Character} or char[]</li>
     *             <li>{@link Short} or short[]</li>
     *             <li>{@link Integer} or int[] or ArrayList of Integer</li>
     *             <li>{@link Long} or long[]</li>
     *             <li>{@link Float} or float[]</li>
     *             <li>{@link Double} or double[]</li>
     *             <li>{@link String} or String[] or ArrayList of String</li>
     *             <li>{@link CharSequence} or CharSequence[] or ArrayList of CharSequence</li>
     *             <li>{@link Size} or Size[]</li>
     *             <li>{@link SizeF} or SizeF[]</li>
     *             <li>{@link Parcelable} or Parcelable[] or ArrayList of Parcelable or SparseArray of Parcelable</li>
     *             <li>{@link IBinder}</li>
     *             <li>{@link Serializable}</li>
     *         </ul>
     *     </ul>
     * @param first the first pair to be added. Can not be null
     * @param others the number of other pairs to add
     * @return created bundle
     */
    @SafeVarargs
    public static Bundle compose(@NonNull Pair<String, Object> first, Pair<String, Object> ... others) {
        Bundle bundle = new Bundle();
        put(bundle, first.first, first.second);
        if (others == null) {
            return bundle;
        }
        for (Pair<String, Object> other : others) {
            put(bundle, other.first, other.second);
        }
        return bundle;
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    private static void put(Bundle bundle, String key, Object val) {
        if (val == null) {
            throw new IllegalArgumentException("The value is null for key " + key);
        }
        Class<?> valType = val.getClass();
        if (valType.isAssignableFrom(Boolean.class)) {
            bundle.putBoolean(key, (Boolean) val);
        } else if (valType.isAssignableFrom(boolean[].class)) {
            bundle.putBooleanArray(key, (boolean[]) val);
        } else if (valType.isAssignableFrom(Byte.class)) {
            bundle.putByte(key, (Byte) val);
        } else if (valType.isAssignableFrom(byte[].class)) {
            bundle.putByteArray(key, (byte[]) val);
        } else if (valType.isAssignableFrom(Short.class)) {
            bundle.putShort(key, (Short) val);
        } else if (valType.isAssignableFrom(short[].class)) {
            bundle.putShortArray(key, (short[]) val);
        } else if (valType.isAssignableFrom(Character.class)) {
            bundle.putChar(key, (Character) val);
        } else if (valType.isAssignableFrom(char[].class)) {
            bundle.putCharArray(key, (char[]) val);
        } else if (valType.isAssignableFrom(Integer.class)) {
            bundle.putInt(key, (Integer) val);
        } else if (valType.isAssignableFrom(int[].class)) {
            bundle.putIntArray(key, (int[]) val);
        } else if (valType.isAssignableFrom(Long.class)) {
            bundle.putLong(key, (Long) val);
        } else if (valType.isAssignableFrom(long[].class)) {
            bundle.putLongArray(key, (long[]) val);
        } else if (valType.isAssignableFrom(Float.class)) {
            bundle.putFloat(key, (Float) val);
        } else if (valType.isAssignableFrom(float[].class)) {
            bundle.putFloatArray(key, (float[]) val);
        } else if (valType.isAssignableFrom(Double.class)) {
            bundle.putDouble(key, (Double) val);
        } else if (valType.isAssignableFrom(double[].class)) {
            bundle.putDoubleArray(key, (double[]) val);
        } else if (valType.isAssignableFrom(String.class)) {
            bundle.putString(key, (String) val);
        } else if (valType.isAssignableFrom(String[].class)) {
            bundle.putStringArray(key, (String[]) val);
        } else if (valType.isAssignableFrom(CharSequence.class)) {
            bundle.putCharSequence(key, (CharSequence) val);
        } else if (valType.isAssignableFrom(CharSequence[].class)) {
            bundle.putCharSequenceArray(key, (CharSequence[]) val);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && valType.isAssignableFrom(Size.class)) {
            bundle.putSize(key, (Size) val);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && valType.isAssignableFrom(SizeF.class)) {
            bundle.putSizeF(key, (SizeF) val);
        } else if (valType.isAssignableFrom(Parcelable.class)) {
            bundle.putParcelable(key, (Parcelable) val);
        } else if (valType.isAssignableFrom(Parcelable[].class)) {
            bundle.putParcelableArray(key, (Parcelable[]) val);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && valType.isAssignableFrom(IBinder.class)) {
            bundle.putBinder(key, (IBinder) val);
        } else if (valType.isAssignableFrom(Serializable.class)) {
            bundle.putSerializable(key, (Serializable) val);
        } else if (valType.isAssignableFrom(ArrayList.class)) {
            putArrayList(bundle, key, (ArrayList) val, valType);
        } else if (valType.isAssignableFrom(SparseArray.class)) {
            putSparseArray(bundle, key, (SparseArray) val, valType);
        } else {
            throw new IllegalArgumentException("Unsupported type of the data to add to bundle: " + valType.getName());
        }
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    private static void putArrayList(Bundle bundle, String key, ArrayList val, Class<?> valType) {
        TypeVariable<? extends Class<?>>[] parameters = valType.getTypeParameters();
        if (parameters == null || parameters.length == 0) {
            throw new IllegalArgumentException("Unknown type of the data in ArrayList to add to bundle");
        }
        TypeVariable<? extends Class<?>> typeParam = parameters[0];
        Class paramClass = typeParam.getGenericDeclaration();
        if (paramClass == Integer.class){
            bundle.putIntegerArrayList(key, (ArrayList<Integer>) val);
        } else if (paramClass == CharSequence.class) {
            bundle.putCharSequenceArrayList(key, (ArrayList<CharSequence>) val);
        } else if (paramClass == String.class) {
            bundle.putStringArrayList(key, (ArrayList<String>) val);
        } else if (paramClass == Parcelable.class) {
            bundle.putParcelableArrayList(key, (ArrayList<Parcelable>) val);
        } else {
            throw new IllegalArgumentException("Unsupported type of the data in ArrayList to add to bundle: " + paramClass.getName());
        }
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    private static void putSparseArray(Bundle bundle, String key, SparseArray val, Class<?> valType) {
        TypeVariable<? extends Class<?>>[] parameters = valType.getTypeParameters();
        if (parameters == null || parameters.length == 0) {
            throw new IllegalArgumentException("Unknown type of the data in SparseArray to add to bundle");
        }
        TypeVariable<? extends Class<?>> typeParam = parameters[0];
        Class paramClass = typeParam.getGenericDeclaration();
        if (paramClass == Parcelable.class){
            bundle.putSparseParcelableArray(key, (SparseArray<Parcelable>) val);
        } else {
            throw new IllegalArgumentException("Unsupported type of the data in SparseArray to add to bundle: " + paramClass.getName());
        }
    }

    public static Pair<String, Object> of(String key, Object val) {
        return new Pair<>(key, val);
    }
}
