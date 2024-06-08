package com.a.b.c.d.pangolin.util;


import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

public class CollectionUtils {
    private CollectionUtils() {
    }


    public static final Collection EMPTY_COLLECTION = Collections.emptyList();

    /**
     * Returns the immutable EMPTY_COLLECTION with generic type safety.
     *
     * @param <T> the element type
     * @return immutable empty collection
     * @see #EMPTY_COLLECTION
     * @since 4.0
     */
    @SuppressWarnings("unchecked") // OK, empty collection is compatible with any type
    public static <T> Collection<T> emptyCollection() {
        return EMPTY_COLLECTION;
    }

    /**
     * Returns an immutable empty collection if the argument is <code>null</code>,
     * or the argument itself otherwise.
     *
     * @param <T>        the element type
     * @param collection the collection, possibly <code>null</code>
     * @return an empty collection if the argument is <code>null</code>
     */
    public static <T> Collection<T> emptyIfNull(final Collection<T> collection) {
        return collection == null ? CollectionUtils.<T>emptyCollection() : collection;
    }


    /**
     * Returns <code>true</code> iff all elements of {@code coll2} are also contained
     * in {@code coll1}. The cardinality of values in {@code coll2} is not taken into account,
     * which is the same behavior as {@link Collection#containsAll(Collection)}.
     * <p>
     * In other words, this method returns <code>true</code> iff the
     * {@link #intersection} of <i>coll1</i> and <i>coll2</i> has the same cardinality as
     * the set of unique values from {@code coll2}. In case {@code coll2} is empty, {@code true}
     * will be returned.
     * <p>
     * This method is intended as a replacement for {@link Collection#containsAll(Collection)}
     * with a guaranteed runtime complexity of {@code O(n + m)}. Depending on the type of
     * {@link Collection} provided, this method will be much faster than calling
     * {@link Collection#containsAll(Collection)} instead, though this will come at the
     * cost of an additional space complexity O(n).
     *
     * @param coll1 the first collection, must not be null
     * @param coll2 the second collection, must not be null
     * @return <code>true</code> iff the intersection of the collections has the same cardinality
     * as the set of unique elements from the second collection
     * @since 4.0
     */
    public static boolean containsAll(final Collection<?> coll1, final Collection<?> coll2) {
        if (coll2.isEmpty()) {
            return true;
        }
        final Iterator<?> it = coll1.iterator();
        final Set<Object> elementsAlreadySeen = new HashSet<>();
        for (final Object nextElement : coll2) {
            if (elementsAlreadySeen.contains(nextElement)) {
                continue;
            }

            boolean foundCurrentElement = false;
            while (it.hasNext()) {
                final Object p = it.next();
                elementsAlreadySeen.add(p);
                if (nextElement == null ? p == null : nextElement.equals(p)) {
                    foundCurrentElement = true;
                    break;
                }
            }

            if (!foundCurrentElement) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns <code>true</code> iff at least one element is in both collections.
     * <p>
     * In other words, this method returns <code>true</code> iff the
     * {@link #intersection} of <i>coll1</i> and <i>coll2</i> is not empty.
     *
     * @param <T>   the type of object to lookup in <code>coll1</code>.
     * @param coll1 the first collection, must not be null
     * @param coll2 the second collection, must not be null
     * @return <code>true</code> iff the intersection of the collections is non-empty
     * @see #intersection
     * @since 4.2
     */
    public static <T> boolean containsAny(final Collection<?> coll1, @SuppressWarnings("unchecked") final T... coll2) {
        if (coll1.size() < coll2.length) {
            for (final Object aColl1 : coll1) {
                if (ArrayUtils.contains(coll2, aColl1)) {
                    return true;
                }
            }
        } else {
            for (final Object aColl2 : coll2) {
                if (coll1.contains(aColl2)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns <code>true</code> iff at least one element is in both collections.
     * <p>
     * In other words, this method returns <code>true</code> iff the
     * {@link #intersection} of <i>coll1</i> and <i>coll2</i> is not empty.
     *
     * @param coll1 the first collection, must not be null
     * @param coll2 the second collection, must not be null
     * @return <code>true</code> iff the intersection of the collections is non-empty
     * @see #intersection
     * @since 2.1
     */
    public static boolean containsAny(final Collection<?> coll1, final Collection<?> coll2) {
        if (coll1.size() < coll2.size()) {
            for (final Object aColl1 : coll1) {
                if (coll2.contains(aColl1)) {
                    return true;
                }
            }
        } else {
            for (final Object aColl2 : coll2) {
                if (coll1.contains(aColl2)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns a {@link Map} mapping each unique element in the given
     * {@link Collection} to an {@link Integer} representing the number
     * of occurrences of that element in the {@link Collection}.
     * <p>
     * Only those elements present in the collection will appear as
     * keys in the map.
     *
     * @param <O>  the type of object in the returned {@link Map}. This is a super type of &lt;I&gt;.
     * @param coll the collection to get the cardinality map for, must not be null
     * @return the populated cardinality map
     */
    public static <O> Map<O, Integer> getCardinalityMap(final Iterable<? extends O> coll) {
        final Map<O, Integer> count = new HashMap<>();
        for (final O obj : coll) {
            final Integer c = count.get(obj);
            if (c == null) {
                count.put(obj, Integer.valueOf(1));
            } else {
                count.put(obj, Integer.valueOf(c.intValue() + 1));
            }
        }
        return count;
    }

    //-----------------------------------------------------------------------

    /**
     * Null-safe check if the specified collection is empty.
     * <p>
     * Null returns true.
     *
     * @param coll the collection to check, may be null
     * @return true if empty or null
     * @since 3.2
     */
    public static boolean isEmpty(final Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    /**
     * Null-safe check if the specified collection is not empty.
     * <p>
     * Null returns false.
     *
     * @param coll the collection to check, may be null
     * @return true if non-null and non-empty
     * @since 3.2
     */
    public static boolean isNotEmpty(final Collection<?> coll) {
        return !isEmpty(coll);
    }

    //-----------------------------------------------------------------------

    /**
     * Reverses the order of the given array.
     *
     * @param array the array to reverse
     */
    public static void reverseArray(final Object[] array) {
        int i = 0;
        int j = array.length - 1;
        Object tmp;

        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

}
