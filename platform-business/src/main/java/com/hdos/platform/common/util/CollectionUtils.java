package com.hdos.platform.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 常用的集合工具类
 * 
 * @author matao
 * @date 2016年6月18日
 */
public class CollectionUtils {
	/**
	 * 转化一个字符串集合为数组
	 * 
	 * @param collection
	 *            <String>
	 * @return
	 */
	public static String[] toStringArray(Collection<String> collection) {
		if (collection == null) {
			return null;
		}
		return collection.toArray(new String[collection.size()]);
	}

	/**
	 * 判断一个集合体是否为空，包括NULL
	 * 
	 * @param test
	 *            待测试集合体
	 * @return
	 */
	public static boolean isEmpty(Map<?, ?> test) {
		return !isNotEmpty(test);
	}

	/**
	 * 判断一个集合体是否为空，包括NULL
	 * 
	 * @param test
	 *            待测试集合体
	 * @return
	 */
	public static boolean isNotEmpty(Map<?, ?> test) {
		return test != null && !test.isEmpty();
	}

	/**
	 * 判断一个集合体是否为空，包括NULL
	 * 
	 * @param test
	 *            待测试集合体
	 * @return
	 */
	public static boolean isEmpty(Collection<?> test) {
		return !isNotEmpty(test);
	}

	/**
	 * 判断一个集合体是否不为空，包括NULL
	 * 
	 * @param test
	 *            待测试集合体
	 * @return
	 */
	public static boolean isNotEmpty(Collection<?> test) {
		return test != null && !test.isEmpty();
	}

	/**
	 * 判断一个集合体是否为空，包括NULL
	 * 
	 * @param test
	 *            待测试集合体
	 * @return
	 */
	public static boolean isEmpty(Object[] test) {
		return !isNotEmpty(test);
	}

	/**
	 * 判断一个集合体是否不为空，包括NULL
	 * 
	 * @param test
	 *            待测试集合体
	 * @return
	 */
	public static boolean isNotEmpty(Object[] test) {
		return test != null && test.length > 0;
	}

	/**
	 * 测试两个集合体的长度是否一致
	 * 
	 * @param input1
	 *            待测试集合体1
	 * @param input2
	 *            待测试集合体2
	 * @return
	 */
	public static boolean testLength(Object[] input1, Object[] input2) {
		if (isEmpty(input1) || isEmpty(input2)) {
			return isEmpty(input1) && isEmpty(input2);
		}
		return input1.length == input2.length;
	}

	/**
	 * Merge the given Properties instance into the given Map, copying all
	 * properties (key-value pairs) over.
	 * <p>
	 * Uses {@code Properties.propertyNames()} to even catch default properties
	 * linked into the original Properties instance.
	 * 
	 * @param props
	 *            the Properties instance to merge (may be {@code null})
	 * @param map
	 *            the target Map to merge the properties into
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void mergePropertiesIntoMap(Properties props, Map map) {
		if (map == null) {
			throw new IllegalArgumentException("Map must not be null");
		}
		if (props != null) {
			for (Enumeration en = props.propertyNames(); en.hasMoreElements();) {
				String key = (String) en.nextElement();
				Object value = props.getProperty(key);
				if (value == null) {
					// Potentially a non-String value...
					value = props.get(key);
				}
				map.put(key, value);
			}
		}
	}

	/**
	 * Translate Enumeration 为 List
	 * 
	 * @param elements
	 * @return
	 */
	public static <V> List<V> asList(Enumeration<V> elements) {
		if (elements == null || !elements.hasMoreElements()) {
			return Collections.emptyList();
		}
		List<V> copy = new ArrayList<V>();
		for (; elements.hasMoreElements();) {
			copy.add(elements.nextElement());
		}
		return copy;
	}

	/**
	 * 将一个Collection<Collection<V>> 合并为 List<V>
	 * 
	 * @param values
	 *            待合并的Collection<Collection<V>>
	 * @return
	 */
	public static <V> List<V> uniteCollections(Collection<? extends Collection<V>> values) {
		if (isEmpty(values)) {
			return Collections.emptyList();
		}
		List<V> copy = new ArrayList<V>();
		for (Collection<V> collection : values) {
			copy.addAll(collection);
		}
		return copy;
	}

	/**
	 * 将一个Collection<V[]> 合并为 List<V>
	 * 
	 * @param values
	 *            待合并的Collection<V[]>
	 * @return
	 */
	public static <V> List<V> uniteCollectionArray(Collection<V[]> values) {
		if (isEmpty(values)) {
			return Collections.emptyList();
		}
		List<V> copy = new ArrayList<V>();
		for (V[] collections : values) {
			copy.addAll(Arrays.asList(collections));
		}
		return copy;
	}

	/**
	 * 转化E数组为Set集合
	 * 
	 * @param elements
	 * @return
	 */
	public static <E> Set<E> asSet(E... elements) {
		if (elements == null || elements.length == 0) {
			return Collections.emptySet();
		}
		LinkedHashSet<E> set = new LinkedHashSet<E>(elements.length * 4 / 3 + 1);
		Collections.addAll(set, elements);
		return set;
	}

	/**
	 * 转化E为List
	 * 
	 * @param elements
	 * @return
	 */
	public static <E> List<E> asList(E... elements) {
		if (elements == null || elements.length == 0) {
			return Collections.emptyList();
		}
		// Avoid integer overflow when a large array is passed in
		int capacity = (int) Math.min(5L + elements.length + (elements.length / 10), Integer.MAX_VALUE);
		ArrayList<E> list = new ArrayList<E>(capacity);
		Collections.addAll(list, elements);
		return list;
	}

}
