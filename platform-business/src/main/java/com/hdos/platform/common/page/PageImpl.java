package com.hdos.platform.common.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * Reference: Spring Data Domain.
 * </p>
 * 
 * Basic {@code Page} implementation.
 * 
 * @param <T>
 *            the type of which the page consists.
 * @author Oliver Gierke
 */
public class PageImpl<T> implements Page<T> {

	private final List<T> content = new ArrayList<T>();
	private final int pageNumber;
	private final int pageSize;

	private final long total;

	/**
	 * Constructor of {@code PageImpl}.
	 * 
	 * @param content
	 *            the content of this page, must not be {@literal null}.
	 * @param pageNumber
	 *            the paging information, count from 1.
	 * @param pageSize
	 *            the paging information.
	 * @param total
	 *            the total amount of items available
	 */
	public PageImpl(List<T> content, int pageNumber, int pageSize, long total) {
		if (null == content) {
			throw new IllegalArgumentException("Content must not be null!");
		}
		this.content.addAll(content);
		this.total = total;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
	}

	/**
	 * Creates a new {@link PageImpl} with the given content. This will result
	 * in the created {@link Page} being identical to the entire {@link List}.
	 * 
	 * @param content
	 *            must not be {@literal null}.
	 */
	public PageImpl(List<T> content) {
		this(content, 1, null == content ? 0 : content.size(), null == content ? 0 : content.size());
	}

	@Override
	public int getNumber() {
		return pageNumber;
	}

	@Override
	public int getSize() {
		return pageSize;
	}

	@Override
	public int getTotalPages() {
		return getSize() == 0 ? 1 : (int) Math.ceil((double) total / (double) getSize());
	}

	@Override
	public int getNumberOfElements() {
		return content.size();
	}

	@Override
	public long getTotalElements() {
		return total;
	}

	@Override
	public boolean hasPreviousPage() {
		return getNumber() > 1;
	}

	@Override
	public boolean isFirstPage() {
		return !hasPreviousPage();
	}

	@Override
	public boolean hasNextPage() {
		return getNumber() < getTotalPages();
	}

	@Override
	public boolean isLastPage() {
		return !hasNextPage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.domain.Page#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return content.iterator();
	}

	@Override
	public List<T> getContent() {
		return Collections.unmodifiableList(content);
	}

	@Override
	public boolean hasContent() {
		return !content.isEmpty();
	}

	@Override
	public String toString() {
		String contentType = "UNKNOWN";
		if (content.size() > 0) {
			contentType = content.get(0).getClass().getName();
		}
		return String.format("Page %s of %d containing %s instances", getNumber(), getTotalPages(), contentType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PageImpl<?>)) {
			return false;
		}
		PageImpl<?> that = (PageImpl<?>) obj;
		boolean totalEqual = this.total == that.total;
		boolean contentEqual = this.content.equals(that.content);
		boolean pageableEqual = this.pageNumber == that.pageNumber && this.pageSize == that.pageSize;

		return totalEqual && contentEqual && pageableEqual;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + (int) (total ^ total >>> 32);
		result = 31 * result + (pageNumber ^ pageNumber >>> 32);
		result = 31 * result + (pageSize ^ pageSize >>> 32);
		result = 31 * result + content.hashCode();
		return result;
	}
}