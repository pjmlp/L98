package org.progtools.l98.util;
import java.util.*;


/**
 * List nodes for a double linked list.
 */
class ListNode {
  ListNode prev, next;
  Object   value;

  public ListNode (Object elem, ListNode prevNode, ListNode nextNode) {
    value = elem;
    prev = prevNode;
    next = nextNode;
  }
}

/**
 * Class for list manipulation.
 * At the time this code was writen, Java was at version 1.1 so the collections
 * package wasn't available and this class was created.
 * Nowadays the correct way would be to make use of java.util.List, but I wanted
 * to avoid rewriting too much code, given some of the API differences between
 * both classes.
 */
public class List implements Cloneable {
  private ListNode head;
  private ListNode tail;
  private int count;

  public List () {
    count = 0;
  }
  
  /**
   * Adds an element to the head.
   */
  public void pushFront (Object elem) {
    ListNode node = new ListNode (elem, null, head);
    
    if (head != null)
      head.prev = node;
    else
      tail = node;

    head = node;
    count++;
  }

  /**
   * Adds an element to the tail.
   */
  public void pushBack (Object elem) {
    ListNode node = new ListNode (elem, tail, null);

    if (tail != null)
      tail.next = node;
    else
      head = node;

    tail = node;
    count++;
  }

  /**
   * Removes the element at the head.
   * @return the removed element.
   */
  public Object popFront () {
    if (head == null)
      return null;

    ListNode node = head;
    head = head.next;

    if (head != null)
      head.prev = null;
    else
      tail = null;

    count--;
    return node.value;
  }

  /**
   * Removes the element at the tail.
   * @return the removed element.
   */
  public Object popBack () {
    if (tail == null)
      return null;

    ListNode node = tail;
    tail = tail.prev;

    if (tail != null)
      tail.next = null;
    else
      head = null;

    count--;
    return node.value;
  }


  /**
   * @return true if the list is empty
   */
  public boolean isEmpty () {
    return head == null;
  }


  /**
   * @return number of elements in the list.
   */
  public int length () {
    return count;
  }

  /**
   * Adds all elements of the given list.
   * @param other list that is to be appended.
   */
  public void append (List other) {
    ListNode node = other.head;
    
    while (node != null) {
      pushBack (node.value);
      node = node.next;
    }
  }
  

  /**
   * Transforms the list into an empty one.
   */
  public void clear () {
    head = tail = null;
  }


  /**
   * @return  the element currently located at the head, without removing it.
   */
  public Object peekHead () {
    if (head != null)
      return head.value;
    else
      return null;
  }

  /**
   * @return  the element currently located at the tail, without removing it.
   */
  public Object peekTail () {
    if (tail != null)
      return tail.value;
    else
      return null;
  }
  
    
  /**
   * @return true if the element is present in the list.
   */
  public boolean has (Object elem) {
    ListNode node = head;

    while (node != null && !node.value.equals (elem))
      node = node.next;

    return node != null;
  }

  /**
   * * @return a shallow copy of the list.
   */
  public Object clone () {
    List temp = new List ();
    ListNode node = head;

    while (node != null) {
      temp.pushBack (node.value);
      node = node.next;
    }

    return temp;
  }

  /**
   * @return a string representation of the list with its contents.
   */
  public String toString () {
    String temp = "[";
    ListNode node = head;

    while (node != null) {
      temp += node.value.toString ();
      node = node.next;
      if (node != null)
        temp += ", ";
    }
    temp += "]";

    return temp;
  }

  /**
   * Classe para fazer enumeracoes para a frente
   */
  class EnumForward implements Enumeration {
    /**
     * Elemento corrente
     */
    private ListNode node;

    EnumForward (ListNode start) {
      node = start;
    }

    /**
     * Indica se ainda existem elementos
     */
    public boolean hasMoreElements () {
      return node != null;
    }

    /**
     * Devolve o proximo elemento
     */
    public Object nextElement () throws NoSuchElementException {
      Object temp;

      if (node == null)
        throw new NoSuchElementException ();

      temp = node.value;
      node = node.next;

      return temp;
    }
  }

  /**
   * Classe para fazer enumeracoes para a tras
   */
  class EnumBackward implements Enumeration {
    /**
     * Elemento corrente
     */
    private ListNode node;

    EnumBackward (ListNode start) {
      node = start;
    }

    /**
     * Indica se ainda existem elementos
     */
    public boolean hasMoreElements () {
      return node != null;
    }

    /**
     * Devolve o proximo elemento
     */
    public Object nextElement () throws NoSuchElementException {
      Object temp;

      if (node == null)
        throw new NoSuchElementException ();

      temp = node.value;
      node = node.prev;

      return temp;
    }
  }
  
  /**
   * Devolve uma enumeracao dos elementos da lista
   */
  public Enumeration elements () {
    return new EnumForward (head);
  }

  /**
   * Devolve uma enumeracao dos elementos da lista
   */
  public Enumeration elementsBackward () {
    return new EnumBackward (tail);
  }
  
  
}

  
