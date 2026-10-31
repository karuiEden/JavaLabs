package ru.mephi.lab6.compatible_classes;

import java.lang.reflect.*;
import java.util.*;

/** Conservative checked generic assignment, with no unchecked/raw conversions. */
public final class GenericAssign {
 private GenericAssign() {}
 public static boolean matches(Class<?> candidate, Type target) {
  // Generic declarations and non-static member classes require instantiation context.
  if (!(target instanceof ParameterizedType) || candidate.getTypeParameters().length != 0 ||
      (candidate.isMemberClass() && !Modifier.isStatic(candidate.getModifiers())) || !closed(target)) return false;
  return subtype(candidate, target, new HashSet<>());
 }
 private static boolean closed(Type t) {
  if (t instanceof TypeVariable<?>) return false;
  if (t instanceof Class<?>) {
   Class<?> c=(Class<?>)t;
   return c.isArray() ? closed(c.getComponentType()) : c.getTypeParameters().length==0;
  }
  if (t instanceof ParameterizedType) {
   ParameterizedType p=(ParameterizedType)t;
   if (p.getOwnerType()!=null && !closed(p.getOwnerType())) return false;
   for(Type a:p.getActualTypeArguments()) if(!closed(a)) return false;
  } else if (t instanceof WildcardType) {
   WildcardType w=(WildcardType)t;
   for(Type a:w.getUpperBounds()) if(!closed(a)) return false;
   for(Type a:w.getLowerBounds()) if(!closed(a)) return false;
  } else if (t instanceof GenericArrayType) return closed(((GenericArrayType)t).getGenericComponentType());
  else if (!(t instanceof Class<?>)) return false;
  return true;
 }
 private static Class<?> raw(Type t) {
  if(t instanceof Class<?>) return (Class<?>)t;
  if(t instanceof ParameterizedType && ((ParameterizedType)t).getRawType() instanceof Class<?>) return (Class<?>)((ParameterizedType)t).getRawType();
  return null;
 }
 private static Type component(Type t) {
  if(t instanceof Class<?> && ((Class<?>)t).isArray()) return ((Class<?>)t).getComponentType();
  if(t instanceof GenericArrayType) return ((GenericArrayType)t).getGenericComponentType();
  return null;
 }
 private static boolean subtype(Type from, Type to, Set<String> visiting) {
  if (same(from,to)) return true;
  Type fc=component(from), tc=component(to);
  if(tc!=null) return fc!=null && subtype(fc,tc,visiting);
  Class<?> f=raw(from), t=raw(to);
  if(f==null || t==null || !t.isAssignableFrom(f)) return false;
  if(to instanceof Class<?>) return true;
  if(!(to instanceof ParameterizedType)) return false;
  String key=from.getTypeName()+" -> "+to.getTypeName();
  if(!visiting.add(key)) return false;
  try {
   if(f==t) {
    if(!(from instanceof ParameterizedType)) return false;
    ParameterizedType a=(ParameterizedType)from,b=(ParameterizedType)to;
    if(!sameOwners(a.getOwnerType(),b.getOwnerType())) return false;
    Type[] x=a.getActualTypeArguments(), y=b.getActualTypeArguments();
    for(int i=0;i<x.length;i++) if(!closed(x[i]) || !contained(x[i],y[i],visiting)) return false;
    return true;
   }
   Map<TypeVariable<?>,Type> env=new HashMap<>();
   if(from instanceof ParameterizedType) {
    TypeVariable<?>[] vars=f.getTypeParameters(); Type[] args=((ParameterizedType)from).getActualTypeArguments();
    for(int i=0;i<vars.length;i++) env.put(vars[i],args[i]);
   }
   Type sup=f.getGenericSuperclass();
   if(sup!=null && subtype(subst(sup,env),to,visiting)) return true;
   for(Type i:f.getGenericInterfaces()) if(subtype(subst(i,env),to,visiting)) return true;
   return false;
  } finally { visiting.remove(key); }
 }
 private static boolean sameOwners(Type a,Type b) { return a==null ? b==null : b!=null && same(a,b); }
 private static boolean contained(Type from,Type to,Set<String> visiting) {
  if(same(from,to)) return true;
  if(!(to instanceof WildcardType)) return false;
  WildcardType w=(WildcardType)to;
  Type[] fl=from instanceof WildcardType ? ((WildcardType)from).getLowerBounds() : new Type[]{from};
  Type[] fu=from instanceof WildcardType ? ((WildcardType)from).getUpperBounds() : new Type[]{from};
  for(Type upper:w.getUpperBounds()) {
   boolean ok=false; for(Type u:fu) if(subtype(u,upper,visiting)) {ok=true;break;}
   if(!ok) return false;
  }
  for(Type lower:w.getLowerBounds()) {
   boolean ok=false; for(Type l:fl) if(subtype(lower,l,visiting)) {ok=true;break;}
   if(!ok) return false;
  }
  return true;
 }
 private static boolean same(Type a,Type b) {
  if(a instanceof Class<?> && b instanceof Class<?>) return a.equals(b);
  Type ac=component(a),bc=component(b); if(ac!=null || bc!=null) return ac!=null && bc!=null && same(ac,bc);
  if(a instanceof ParameterizedType && b instanceof ParameterizedType) {
   ParameterizedType x=(ParameterizedType)a,y=(ParameterizedType)b;
   if(!x.getRawType().equals(y.getRawType()) || !sameOwners(x.getOwnerType(),y.getOwnerType())) return false;
   return sameAll(x.getActualTypeArguments(),y.getActualTypeArguments());
  }
  if(a instanceof WildcardType && b instanceof WildcardType) {
   WildcardType x=(WildcardType)a,y=(WildcardType)b;
   return sameAll(x.getUpperBounds(),y.getUpperBounds()) && sameAll(x.getLowerBounds(),y.getLowerBounds());
  }
  return a.equals(b);
 }
 private static boolean sameAll(Type[] a,Type[] b) {
  if(a.length!=b.length) return false; for(int i=0;i<a.length;i++) if(!same(a[i],b[i])) return false; return true;
 }
 private static Type subst(Type t,Map<TypeVariable<?>,Type> env) {
  if(t instanceof TypeVariable<?>) return env.getOrDefault(t,t);
  if(t instanceof ParameterizedType) {
   ParameterizedType p=(ParameterizedType)t; Type[] a=p.getActualTypeArguments().clone();
   for(int i=0;i<a.length;i++) a[i]=subst(a[i],env);
   Type o=p.getOwnerType(); return new P(p.getRawType(),o==null?null:subst(o,env),a);
  }
  if(t instanceof GenericArrayType) return new A(subst(((GenericArrayType)t).getGenericComponentType(),env));
  if(t instanceof WildcardType) {
   WildcardType w=(WildcardType)t; Type[] u=w.getUpperBounds().clone(),l=w.getLowerBounds().clone();
   for(int i=0;i<u.length;i++) u[i]=subst(u[i],env); for(int i=0;i<l.length;i++) l[i]=subst(l[i],env);
   return new W(u,l);
  }
  return t;
 }
 private record P(Type raw,Type owner,Type[] args) implements ParameterizedType {
  public Type getRawType(){return raw;} public Type getOwnerType(){return owner;}
  public Type[] getActualTypeArguments(){return args.clone();}
 }
 private record A(Type component) implements GenericArrayType { public Type getGenericComponentType(){return component;} }
 private record W(Type[] upper,Type[] lower) implements WildcardType {
  public Type[] getUpperBounds(){return upper.clone();} public Type[] getLowerBounds(){return lower.clone();}
 }
}
