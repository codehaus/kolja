/*
 * Copyright 2002-2004 The Apache Software Foundation Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.baulsupp.kolja.log.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.collections.primitives.IntCollection;
import org.apache.commons.collections.primitives.RandomAccessIntList;

/**
 * An {@link IntList}backed by an array of <code>int</code>s. This
 * implementation supports all optional methods.
 * 
 * @author Rodney Waldhoff
 * @version $Revision: 1.5 $ $Date: 2004/02/25 20:46:25 $
 * @since Commons Primitives 1.0
 */
public class FastIntList extends RandomAccessIntList {

  // constructors
  // -------------------------------------------------------------------------

  /**
   * Construct an empty list with the default initial capacity.
   */
  public FastIntList() {
    this(8);
  }

  /**
   * Construct an empty list with the given initial capacity.
   * 
   * @throws IllegalArgumentException when <i>initialCapacity </i> is negative
   */
  public FastIntList(int initialCapacity) {
    if (initialCapacity < 0) {
      throw new IllegalArgumentException("capacity " + initialCapacity);
    }
    // System.out.println(initialCapacity);

    // new Exception().printStackTrace();

    _data = new int[initialCapacity];
    listSize = 0;
  }

  /**
   * Constructs a list containing the elements of the given collection, in the
   * order they are returned by that collection's iterator.
   * 
   * @param that the non- <code>null</code> collection of <code>int</code> s
   *        to add
   * @throws NullPointerException if <i>that </i> is <code>null</code>
   * @see ArrayIntList#addAll(org.apache.commons.collections.primitives.IntCollection)
   */
  public FastIntList(IntCollection that) {
    this(that.size());
    // @PMD:REVIEWED:ConstructorCallsOverridableMethod: by yuri on 11/27/05 9:55
    // PM
    addAll(that);
  }

  // IntList methods
  // -------------------------------------------------------------------------

  public int get(int index) {
    checkRange(index);
    return _data[index];
  }

  public int size() {
    return listSize;
  }

  /**
   * Removes the element at the specified position in (optional operation). Any
   * subsequent elements are shifted to the left, subtracting one from their
   * indices. Returns the element that was removed.
   * 
   * @param index the index of the element to remove
   * @return the value of the element that was removed
   * @throws UnsupportedOperationException when this operation is not supported
   * @throws IndexOutOfBoundsException if the specified index is out of range
   */
  public int removeElementAt(int index) {
    checkRange(index);
    incrModCount();
    int oldval = _data[index];
    int numtomove = listSize - index - 1;
    if (numtomove > 0) {
      System.arraycopy(_data, index + 1, _data, index, numtomove);
    }
    listSize--;
    return oldval;
  }

  /**
   * Replaces the element at the specified position in me with the specified
   * element (optional operation).
   * 
   * @param index the index of the element to change
   * @param element the value to be stored at the specified position
   * @return the value previously stored at the specified position
   * @throws UnsupportedOperationException when this operation is not supported
   * @throws IndexOutOfBoundsException if the specified index is out of range
   */
  public int set(int index, int element) {
    checkRange(index);
    incrModCount();
    int oldval = _data[index];
    _data[index] = element;
    return oldval;
  }

  /**
   * Inserts the specified element at the specified position (optional
   * operation). Shifts the element currently at that position (if any) and any
   * subsequent elements to the right, increasing their indices.
   * 
   * @param index the index at which to insert the element
   * @param element the value to insert
   * @throws UnsupportedOperationException when this operation is not supported
   * @throws IllegalArgumentException if some aspect of the specified element
   *         prevents it from being added to me
   * @throws IndexOutOfBoundsException if the specified index is out of range
   */
  public void add(int index, int element) {
    checkRangeIncludingEndpoint(index);
    incrModCount();
    ensureCapacity(listSize + 1);
    int numtomove = listSize - index;
    System.arraycopy(_data, index, _data, index + 1, numtomove);
    _data[index] = element;
    listSize++;
  }

  public final boolean addAll(IntCollection arg0) {
    // TODO fix so it works always with other types
    FastIntList other = (FastIntList) arg0;

    int newSize = this.listSize + other.listSize;
    ensureCapacity(newSize);
    System.arraycopy(other._data, 0, _data, this.listSize, other.listSize);
    listSize = newSize;

    return other.listSize > 0;
  }

  public int[] toArray() {
    return _data;
  }

  public void clear() {
    incrModCount();
    listSize = 0;
  }

  // capacity methods
  // -------------------------------------------------------------------------

  /**
   * Increases my capacity, if necessary, to ensure that I can hold at least the
   * number of elements specified by the minimum capacity argument without
   * growing.
   */
  public void ensureCapacity(int mincap) {
    incrModCount();
    if (mincap > _data.length) {
      int newcap = (_data.length * 3) / 2 + 1;
      int[] olddata = _data;
      _data = new int[newcap < mincap ? mincap : newcap];
      System.arraycopy(olddata, 0, _data, 0, listSize);
    }
  }

  /**
   * Reduce my capacity, if necessary, to match my current {@link #size size}.
   */
  public void trimToSize() {
    incrModCount();
    if (listSize < _data.length) {
      int[] olddata = _data;
      _data = new int[listSize];
      System.arraycopy(olddata, 0, _data, 0, listSize);
    }
  }

  // private methods
  // -------------------------------------------------------------------------

  private void writeObject(ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
    out.writeInt(_data.length);
    for (int i = 0; i < listSize; i++) {
      out.writeInt(_data[i]);
    }
  }

  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
    _data = new int[in.readInt()];
    for (int i = 0; i < listSize; i++) {
      _data[i] = in.readInt();
    }
  }

  private final void checkRange(int index) {
    if (index < 0 || index >= listSize) {
      throw new IndexOutOfBoundsException("Should be at least 0 and less than " + listSize + ", found " + index);
    }
  }

  private final void checkRangeIncludingEndpoint(int index) {
    if (index < 0 || index > listSize) {
      throw new IndexOutOfBoundsException("Should be at least 0 and at most " + listSize + ", found " + index);
    }
  }

  // attributes
  // -------------------------------------------------------------------------

  private transient int[] _data = null;

  private int listSize = 0;
}