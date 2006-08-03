package com.baulsupp.kolja.log.line.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TypeList implements Iterable<Type>, Serializable {
  private static final long serialVersionUID = 7929443304823632589L;

  private List<Type> types = new ArrayList<Type>();
  
  public TypeList() {
  }

  public Iterator<Type> iterator() {
    return types.iterator();
  }
  
  public void setTypes(List<Type> types) {
    this.types = types;
  }
  
  public List<Type> getTypes() {
    return types;
  }

  public List<String> getNames() {
    List<String> result = new ArrayList<String>(types.size());

    for (Type s : types) {
      result.add(s.getName());
    }
    
    return result;
  }

  public int size() {
    return types.size();
  }

  public static TypeList build(Type... types) {
    TypeList result = new TypeList();
    
    result.setTypes(Arrays.asList(types));
    
    return result;
  }

  public void add(Type t) {
    types.add(t);
  }
}
